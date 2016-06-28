package com.tomtop.website.upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class ImportCommand implements ICommand, IMyBatisConfig {

	@Inject
	HttpUtils httpUtils;

	@Inject
	OrderConverter orderConverter;

	@Inject
	MemberConvertor memberConvertor;

	@Inject
	ProductConvertor productConvertor;

	@Inject
	CommentConvertor commentConvertor;

	@Override
	public void config(MyBatisService myBatisService) {
	}

	@Override
	public String commandName() {
		return "import";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("P", true, "JSON source path for import");
		options.addOption("W", true,
				"import restful url; http://locahost:9000/api/category/push");
		options.addOption("S", true, "import size one times;");
		options.addOption(
				"T",
				true,
				"using convertor [order,member,product-update,product-multi,product-sellpoint,comment,product-clearlabel,product-multi-images] ");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		// TODO Auto-generated method stub
		String sourcePath = args.getOptionValue("P", "");
		String url = args.getOptionValue("W", "");
		String sizestr = args.getOptionValue("S", "1");
		String type = args.getOptionValue("T", "");
		int size = Integer.valueOf(sizestr);
		if (url.length() == 0) {
			System.out.println("must be input url");
			return;
		}

		File f1 = new File(sourcePath);
		File[] files = null;
		if (f1.isDirectory() == false) {
			files = new File[] { f1 };
		} else {
			files = f1.listFiles();
		}
		ObjectMapper om = new ObjectMapper();
		List<File> fileList = Arrays.asList(files);
		List<List<File>> partitions = new LinkedList<List<File>>();
		for (int i = 0; i < fileList.size(); i += size) {
			partitions.add(fileList.subList(i,
					i + Math.min(size, fileList.size() - i)));
		}

		Unirest.setTimeouts(600000, 600000);
		List<String> arrayString = Lists.newArrayList();
		// Map<String, JsonNode> weightlist = getSkuWeights(f1.getParent(), om);
		long count = partitions
				.parallelStream()
				.map((List<File> f) -> {
					try {

						ArrayNode uploadList = JsonNodeFactory.instance
								.arrayNode();
						for (File ff : f) {
							System.out.print(ff.getName() + ",");
							if (ff.isFile()) {
								if (!ff.getName().endsWith(".json")) {
									System.out
											.print("--not a json file skip! ");
									continue;
								}
								JsonNode jn = om.readValue(ff, JsonNode.class);
								List<JsonNode> jsons = new ArrayList<JsonNode>();
								if ("order".equals(type)) {
									jsons.add(orderConverter.getOrder(jn));
								} else if ("member".equals(type)) {
									jsons.add(memberConvertor.getMember(jn));
								} else if ("member-point".equals(type)) {
									JsonNode tnode = memberConvertor
											.getMemberPoint(jn);
									if (null != tnode) {
										jsons.add(tnode);
									}
								} else if ("product-update".equals(type)) {
									JsonNode[] jlist = Iterators.toArray(
											jn.iterator(), JsonNode.class);
									jsons = Arrays.asList(jlist);
								} else if ("product-multi".equals(type)) {
									uploadList.addAll(productConvertor
											.getProduct(jn));
								} else if ("product-sellpoint".equals(type)) {
									uploadList.addAll(productConvertor
											.getSellpoints(jn));
								} else if ("comment".equals(type)) {
									uploadList.add(commentConvertor
											.getCommnet(jn));
								} else if ("product-clearlabel".equals(type)) {
									uploadList.addAll(productConvertor
											.getProductClearLabel(jn));
								} else if ("product-multi-images".equals(type)) {
									uploadList.addAll(productConvertor
											.getMutilProductImages(sourcePath,
													jn));
								} else if ("product-n-status".equals(type)) {
									System.out.println("get--");
									uploadList.addAll(productConvertor
											.getProductNoSellStatus(jn));

								}else if ("product-multi-url".equals(type)) {
									uploadList.addAll(productConvertor
											.getMutilProductImages(sourcePath,
													jn));
								}  else {
									/*
									 * // ~fix not weight if (jn.get("weight")
									 * != null && jn.get("weight").asInt() == 0
									 * && weightlist.containsKey(jn.get(
									 * "sku").asText().toUpperCase())) {
									 * JsonNode wwjnode = weightlist.get(jn
									 * .get("sku").asText().toUpperCase());
									 * com.website.dto.product.Product tpro = om
									 * .readValue( ff,
									 * com.website.dto.product.Product.class);
									 * tpro.setWeight(wwjnode
									 * .get("pureWeight").asDouble());
									 * tpro.setSku(tpro.getSku().toUpperCase());
									 * arrayString.add(ff.getName() +
									 * "--invalid weight!"); jn = om.readValue(
									 * om.writeValueAsString(tpro),
									 * JsonNode.class); }
									 */
									jsons.add(jn);
								}
								uploadList.addAll(jsons);
							}
						}
						System.out.println("Batch Ends");
						// upload
						System.out.println("Uploading... ");
						// for single item, simply send the object, not array
						com.mashape.unirest.http.JsonNode node = (uploadList
								.size() == 1) ? new com.mashape.unirest.http.JsonNode(
								uploadList.get(0).toString())
								: new com.mashape.unirest.http.JsonNode(
										uploadList.toString());

						HttpResponse<String> response = Unirest.post(url)
								.header("Content-Type", "application/json")
								.header("Accept", "application/json")
								.header("token", "28b992af-836d-4051-b9cd-2545d08d69ec")
								.body(node).asString();
						System.out.println("Uploaded " + uploadList.size()
								+ " Result: " + response.getStatus() + ": "
								+ response.getBody());
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}).count();
		System.out.println("Source Path: " + sourcePath + " Processed " + count
				+ " batches");
		try {
			if (arrayString.size() > 0)
				om.writeValue(new File(f1.getParent(), "failsku.json"),
						arrayString);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Map<String, JsonNode> getSkuWeights(String path, ObjectMapper om) {
		File wf = new File(path, "weightskus.json");
		Map<String, JsonNode> weightlist = new HashMap<String, JsonNode>();
		if (wf.exists()) {
			JsonNode wjnode;
			try {
				wjnode = om.readValue(wf, JsonNode.class);
				weightlist = Maps.uniqueIndex(wjnode.iterator(),
						ww -> ww.get("sku").asText());
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return weightlist;
	}
}
