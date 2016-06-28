package com.tomtop.website.migration.product;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.migration.category.StringUtils;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;
import com.website.dto.product.AttributeItem;
import com.website.dto.product.ImageItem;
import com.website.dto.product.TranslateItem;

public class UnActiveProductExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	StringUtils stringUtils;
	@Inject
	SellingPointService sellingPointService;

	@Override
	public void config(MyBatisService myBatisService) {

	}

	@Override
	public String commandName() {
		return "product-unactive";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("D", true,
				"Directory that records being extracted to.  Default: current directory");
		options.addOption("P1", true,
				"Directory that erp unactive product Source.");
		options.addOption("P2", true,
				"Directory that erp category product Source.");
		options.addOption("P3", true, "Directory that erp category Source.");
		options.addOption("P4", true,
				"Directory that tomtop Active product Source.");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		String erpunactivepath = args.hasOption("P1") ? args
				.getOptionValue("P1") : null;
		String erpCategoryProductpath = args.hasOption("P2") ? args
				.getOptionValue("P2") : null;
		String erpCategorycepath = args.hasOption("P3") ? args
				.getOptionValue("P3") : null;
		String ttActiveSkupath = args.hasOption("P4") ? args
				.getOptionValue("P4") : null;
		ObjectMapper om = new ObjectMapper();
		try {
			JsonNode unactiveNode = om.readValue(new File(erpunactivepath),
					JsonNode.class);
			JsonNode erpCategoryProductNode = om.readValue(new File(
					erpCategoryProductpath), JsonNode.class);
			JsonNode erpCategoryNode = om.readValue(
					new File(erpCategorycepath), JsonNode.class);
			File af = new File(erpCategorycepath);
			// if (af.exists()) {
			List<String> activeskus = Arrays.asList(om.readValue(new File(
					ttActiveSkupath), String[].class));
			// }

			List<Integer> closhCateogrys = Lists.newArrayList(Iterators
					.transform(Iterators.filter(erpCategoryNode.iterator(),
							obj -> {
								return obj.get("path").asText()
										.startsWith("服饰");
							}), obj1 -> obj1.get("id").asInt()));

			List<String> erpCategoryProducts = Lists
					.newArrayList(Iterators.transform(
							Iterators.filter(
									erpCategoryProductNode.iterator(),
									obj -> (obj.get("catagoryId") != null && closhCateogrys
											.contains(obj.get("catagoryId")
													.asInt()))), obj1 -> obj1
									.get("sku").asText()));
			List<Integer> stolist = Arrays.asList(new Integer[] { 7 });
			List<Integer> gzolist = Arrays.asList(new Integer[] { 77 });
			Iterator<JsonNode> unactiveNodes = Iterators
					.filter(unactiveNode.iterator(),
							obj -> {
								if (activeskus
										.contains(obj.get("sku").asText()) == false) {
									return false;
								}
								Iterator<JsonNode> nosellnodes = null;
								if (erpCategoryProducts.contains(obj.get("sku")
										.asText())) {
									nosellnodes = Iterators.filter(
											obj.get("productStockMaps")
													.iterator(),
											tobj -> {
												return (gzolist
														.contains(tobj.get(
																"stockId")
																.asInt()) && tobj
														.get("saleStatus")
														.asInt() == 10);
											});
								} else {
									nosellnodes = Iterators.filter(
											obj.get("productStockMaps")
													.iterator(),
											tobj -> {
												return (stolist
														.contains(tobj.get(
																"stockId")
																.asInt()) && tobj
														.get("saleStatus")
														.asInt() == 10);
											});
								}
								return (null != nosellnodes && nosellnodes
										.hasNext());
							});

			while (unactiveNodes.hasNext()) {
				JsonNode obj = unactiveNodes.next();
				ObjectNode on = om.createObjectNode();
				on.put("websiteId", 1);
				on.put("sku", obj.get("sku").asText());
				on.put("status", 2);
				om.writeValue(
						new File(path, obj.get("sku").asText() + ".json"), on);
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
	/*
	 * public Collection<JsonNode> getProductNoSellStatus(JsonNode jn) { //
	 * 7,71,78,80,81,83 System.out.println("----"); List<Integer> stolist =
	 * Arrays.asList(new Integer[] { 7 });// , 71, 78, // 80,81, 83 // });
	 * List<Integer> gzolist = Arrays.asList(new Integer[] { 77 });
	 * Iterator<JsonNode> jnlist = Iterators.transform( jn.iterator(), obj -> {
	 * System.out.println(obj.get("sku").asText() + ","); if
	 * (obj.get("productStockMaps") == null) { ObjectMapper jsonMapper = new
	 * ObjectMapper(); ObjectNode on = jsonMapper.createObjectNode();
	 * on.put("websiteId", 1); on.put("sku", obj.get("sku").asText());
	 * on.put("status", 2); return on; } if
	 * (obj.get("sku").asText().startsWith("G")) { Iterator<JsonNode>
	 * nosellnodes = Iterators.filter(obj .get("productStockMaps").iterator(),
	 * tobj -> { return gzolist .contains(tobj.get("stockId").asInt()); }); if
	 * (nosellnodes != null && nosellnodes.hasNext()) { JsonNode jngz =
	 * nosellnodes.next(); ObjectMapper jsonMapper = new ObjectMapper();
	 * ObjectNode on = jsonMapper.createObjectNode(); on.put("websiteId", 1);
	 * on.put("sku", obj.get("sku").asText()); on.put("status",
	 * ((jngz.get("saleStatus").asInt() == 10) ? 2 : 1)); return on; } } else {
	 * Iterator<JsonNode> psmnodes = Iterators.filter(
	 * obj.get("productStockMaps").iterator(), tobj -> { if
	 * (stolist.contains(tobj.get("stockId") .asInt()) && 10 ==
	 * tobj.get("saleStatus") .asInt()) { return true; } return false; }); if
	 * (psmnodes != null && psmnodes.hasNext()) { ObjectMapper jsonMapper = new
	 * ObjectMapper(); ObjectNode on = jsonMapper.createObjectNode();
	 * on.put("websiteId", 1); on.put("sku", obj.get("sku").asText());
	 * on.put("status", 2); return on; } } return null; }); return
	 * Lists.newArrayList(Iterators.filter(jnlist, obj -> obj != null)); }
	 */

}
