package com.tomtop.website.migration.product;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.migration.category.StringUtils;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;
import com.website.dto.product.AttributeItem;
import com.website.dto.product.ImageItem;
import com.website.dto.product.TranslateItem;

public class MultiProductExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	StringUtils stringUtils;
	@Inject
	SellingPointService sellingPointService;

	@Override
	public void config(MyBatisService myBatisService) {

	}

	@Override
	public String commandName() {
		return "multiproduct-export";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("D", true,
				"Directory that records being extracted to.  Default: current directory");
		options.addOption("P", true, "Directory that product Source.");
		options.addOption("SP", true,
				"Directory that product  sellpoints Source.");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		String sourcepath = args.hasOption("P") ? args.getOptionValue("P")
				: null;
		String sellpointpath = args.hasOption("SP") ? args.getOptionValue("SP")
				: null;
		this.getMuitlProduct(path, sourcepath, sellpointpath);
	}

	private void getMuitlProduct(String spath, String path, String sellpointpath) {
		ObjectMapper om = new ObjectMapper();
		JsonNode jnode;
		try {
			Map<String, List<AttributeItem>> attMap = this.getAttr(path);
			System.out.println(path);

			File f = new File(path);
			Map<String, String> skuImage = new HashMap<String, String>();
			for (File f1 : f.listFiles()) {
				if (f1.getPath().contains("spu") == false)
					continue;
				System.out.println("begin---" + f1.getPath());

				jnode = om.readValue(f1, JsonNode.class);
				Iterator<JsonNode> it = jnode.iterator();
				while (it.hasNext()) {
					JsonNode tnode = it.next();
					if (tnode.get("sku") == null)
						continue;
					String sku = tnode.get("sku").asText();
					if (tnode.get("imageUrl") != null) {
						String image = tnode.get("imageUrl").asText();
						skuImage.put(sku, image);
					}
					com.website.dto.product.MultiProduct mp = this
							.builtProduct(tnode, sellpointpath, attMap);
					// if (mp.getMultiAttributes() != null)
					om.writeValue(new File(spath, sku + ".json"), mp);
				}
			}
			om.writeValue(new File(spath, "skuimges.json"), skuImage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private com.website.dto.product.MultiProduct builtProduct(JsonNode node,
			String sellpointPath, Map<String, List<AttributeItem>> attrmap) {
		com.website.dto.product.MultiProduct mp = new com.website.dto.product.MultiProduct();
		// mp.setAttributes(this.getAttr(node));

		mp.setCategoryIds(null);
		if (node.get("saleStatus") != null) {
			mp.setCleanrstocks(node.get("saleStatus").asInt() == 20);
		} else if (node.get("productStockMaps") != null) {
			JsonNode pmnode = node.get("productStockMaps");
			if (pmnode.iterator().hasNext()) {
				JsonNode tpmnode = pmnode.iterator().next();
				mp.setCleanrstocks(tpmnode.get("saleStatus").asInt() == 20);
			}
		}

		mp.setCost(this.getPrice(node.get("price").asDouble()));
		mp.setFeatured(false);
		mp.setHot(false);
		mp.setIsNew(true);
		mp.setItems(this.getItems(node, sellpointPath));
		mp.setMultiAttributes(this.getAttr(node, attrmap));
		mp.setNewFromDate(new Date());
		mp.setNewToDate(null);
		if (node.get("spu") != null)
			mp.setParentSku(node.get("spu").asText());
		mp.setPrice(this.getPrice(node.get("price").asDouble() * 1.30));
		mp.setQty(1000);
		mp.setSku(node.get("sku").asText());
		mp.setImages(this.getImages(mp.getSku()));
		mp.setSpecial(false);
		mp.setStatus(1);
		int id = this.getStorageId(node);
		if (id != -1) {
			List<Integer> sids = new ArrayList<Integer>();
			sids.add(id);
			mp.setStorages(sids);
		} else {
			System.out.println("not stockid sku: " + mp.getSku());
		}
		mp.setVideos(null);
		mp.setVisible(true);
		mp.setWebsiteId(1);
		mp.setWeight(node.get("pureWeight").asDouble());

		return mp;
	}

	private int getStorageId(JsonNode jnode) {
		int id = -1;
		if (jnode.get("productStockMaps") != null) {
			JsonNode pmnode = jnode.get("productStockMaps");
			Iterator<JsonNode> itlist = pmnode.iterator();
			while (itlist.hasNext()) {
				JsonNode tpmnode = itlist.next();
				id = this.getStoreId(tpmnode.get("stockId").asInt());
				if (id != -1)
					return id;
			}
		} else if (jnode.get("stockId") != null) {
			id = jnode.get("stockId").asInt();
			return getStoreId(id);
		}
		return -1;
	}

	private int getStoreId(int id) {
		List<Integer> cnlist = Arrays.asList(new Integer[] { 7, 77, 71, 78, 80,
				81, 82, 83, 2001, 2002, 2003, 85, 1001, 1000, 2000 });
		if (cnlist.contains(id)) {
			return 1;
		}
		List<Integer> uslist = Arrays.asList(new Integer[] { 74, 3, 73, 79 });
		if (uslist.contains(id)) {
			return 2;
		}
		/*
		 * List<Integer> uklist = Arrays.asList(new Integer[]{2,6});
		 * if(uklist.contains(id)){ return 7; }
		 */
		// FBA 75,
		// HK 84,
		if (id == 70) { // au
			return 7;
		}
		if (id == 72) { // de
			return 5;
		}
		return -1;
	}

	private List<ImageItem> getImages(String sku) {
		String url = "http://74.86.127.115/images/" + sku.substring(0, 1) + "/"
				+ sku;
		// URI url1 = URI.create("74.86.127.115/images/B/B0062K/");
		// System.out.println(url +
		// java.nio.file.Paths.get(url1).getNameCount());
		File tf = new File(url);
		if (tf.exists() == false)
			return null;
		System.out.println(sku + "--get image");
		List<ImageItem> imgs = new ArrayList<ImageItem>();
		for (File f : tf.listFiles()) {
			ImageItem ii = new ImageItem();
			ii.setBaseImage(true);
			ii.setImageUrl(f.getPath());
			ii.setLabel(null);
			ii.setOrder(1);
			ii.setSmallImage(true);
			ii.setThumbnail(true);
			imgs.add(ii);
		}
		return imgs;
	}

	private List<AttributeItem> getAttr(JsonNode node,
			Map<String, List<AttributeItem>> attrmap) {
		String sku = node.get("sku").asText();
		if (attrmap.containsKey(sku)) {
			return attrmap.get(sku);
		}
		return null;
	}

	private Map<String, List<AttributeItem>> getAttr(String path) {
		System.out.println(path);
		File f = new File(path);
		File[] farr = f.listFiles();
		Map<String, List<AttributeItem>> map = Maps.newHashMap();
		ObjectMapper om = new ObjectMapper();
		for (File tf : farr) {
			if (tf.getPath().contains("attribute") == false)
				continue;
			try {
				JsonNode jnode = om.readValue(tf, JsonNode.class);
				Iterator<JsonNode> it = jnode.iterator();
				while (it.hasNext()) {
					JsonNode t2 = om.readValue(it.next().asText(),
							JsonNode.class);
					Iterator<JsonNode> it2 = t2.iterator();
					while (it2.hasNext()) {
						JsonNode t3 = it2.next();
						JsonNode specnode = t3.get("specificsValue");
						Iterator<String> ni = specnode.fieldNames();
						List<AttributeItem> attrlist = new ArrayList<AttributeItem>();
						while (ni.hasNext()) {
							String name = ni.next();
							if (name.contains("\\u0026")) {
								String[] names = name.split("\\u0026");
								String[] values = specnode.get(name).asText()
										.split("\\u0026");
								for (int i = 0; i < names.length; i++) {
									String key = this.getkey(names[i]);
									AttributeItem ai = new AttributeItem();
									ai.setKey(names[i]);
									ai.setValue(values[i]);
									ai.setLanguangeId(1);
									ai.setShowImg("Color".equals(key));
									attrlist.add(ai);
								}
							} else {
								String key = this.getkey(name);
								AttributeItem ai = new AttributeItem();
								ai.setKey(key);
								ai.setValue(specnode.get(name).asText()
										.replaceAll("\\(.*\\)", ""));
								ai.setLanguangeId(1);
								ai.setShowImg("Color".equals(key));
								attrlist.add(ai);
							}
						}
						// System.out.println(t3.get("sku"));
						map.put(t3.get("sku").asText(), attrlist);
					}
				}
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
		return map;
	}

	private String getkey(String key) {
		if (key.contains("Color") || key.contains("color")
				|| key.contains("Colour")) {
			key = "Color";
		} else if (key.toLowerCase().contains("size")) {
			key = "Size";
		}
		return key;
	}

	private List<TranslateItem> getItems(JsonNode node, String sellpointPath) {
		List<TranslateItem> tranlist = new ArrayList<TranslateItem>();
		TranslateItem titem = new TranslateItem();
		titem.setDescription(node.get("description").asText());
		titem.setKeyword(null);
		titem.setLanguageId(1);

		// titem.setMetaKeyword(metaKeyword);
		titem.setMetaTitle(node.get("nameEn").asText());
		List<String> spoints = sellingPointService.getSellPoint(node.get("sku")
				.asText(), "EN", sellpointPath);
		titem.setSellingPoints(spoints);
		if (node.get("shortDescription") != null) {
			titem.setShortDescription(node.get("shortDescription").asText());
			titem.setMetaDescription(node.get("shortDescription").asText());
		}
		titem.setTitle(node.get("nameEn").asText());
		tranlist.add(titem);
		return tranlist;
	}

	private Double getPrice(Double rmbprice) {
		DecimalFormat df = new DecimalFormat("0.00");
		return Double.parseDouble(df.format(rmbprice / 6.19));
	}

}
