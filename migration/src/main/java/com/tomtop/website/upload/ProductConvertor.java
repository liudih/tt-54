package com.tomtop.website.upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tomtop.website.migration.product.ImageService;
import com.website.dto.product.AttributeItem;
import com.website.dto.product.ImageItem;
import com.website.dto.product.MultiProduct;
import com.website.dto.product.Product;

public class ProductConvertor {

	@Inject
	ImageService imageService;

	public Collection<JsonNode> getProduct(JsonNode jn) {
		Iterator<JsonNode> list = jn.iterator();
		Multimap<String, JsonNode> maps = Multimaps.index(list,
				obj -> obj.get("sku").asText());
		ObjectMapper jsonMapper = new ObjectMapper();

		Collection<JsonNode> lists = Collections2.transform(
				maps.keySet(),
				obj -> {
					ObjectNode onode = jsonMapper.createObjectNode();
					ArrayNode nodelist = onode.putArray("multiAttributes");
					for (JsonNode tobj : maps.get(obj)) {
						onode.put("parentSku", tobj.get("spu").asText());
						onode.put("websiteId", tobj.get("websiteId").asInt());
						onode.put("sku", tobj.get("sku").asText());

						if (tobj.get("key").asText() != null) {
							ObjectNode attnode = jsonMapper.createObjectNode();
							attnode.put("key", tobj.get("key").asText());
							attnode.put("value", tobj.get("value").asText());
							attnode.put("languangeId", 1);
							attnode.put("showImg", tobj.get("key").asText()
									.equalsIgnoreCase("color"));
							nodelist.add(attnode);
						}

					}
					return onode;
				});
		/*
		 * if (maps.containsKey("G0223B-L")) { try { jsonMapper.writeValue(new
		 * File("E:\\G0223.json"), lists); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 */
		return lists;
	}

	public Collection<JsonNode> getSellpoints(JsonNode jnode) {
		ObjectMapper jsonMapper = new ObjectMapper();
		Iterator<JsonNode> list = Iterators.transform(jnode.iterator(),
				obj -> {
					ObjectNode onode = jsonMapper.createObjectNode();
					ArrayNode an = onode.putArray("sellingPoints");
					an.addAll(Lists.newArrayList(obj
							.get("languageSellPointSet").iterator()));
					onode.put("sku", obj.get("sku").asText());
					onode.put("languageId",
							this.getLangId(obj.get("language").asText()));
					onode.put("websiteId", 1);
					System.out.println(onode + "");
					return onode;
				});
		return Lists.newArrayList(list);
	}

	private Integer getLangId(String iso) {
		switch (iso) {
		case "EN":
			return 1;
		case "ES":
			return 2;
		case "RU":
			return 3;
		case "DE":
			return 4;
		case "FR":
			return 5;
		case "IT":
			return 6;
		case "JP":
			return 7;
		}
		return null;
	}

	public Collection<JsonNode> getProductClearLabel(JsonNode jnode) {
		ObjectMapper jsonMapper = new ObjectMapper();
		Iterator<JsonNode> list = Iterators.transform(jnode.iterator(),
				obj -> {
					ObjectNode onode = jsonMapper.createObjectNode();
					ArrayNode an = onode.putArray("labelTypes");
					an.add("clearstocks");
					onode.put("sku", obj.get("sku").asText());
					onode.put("websiteId", 1);
					return onode;
				});
		return Lists.newArrayList(list);
	}

	public Collection<JsonNode> getMutilProductImages(String sourcePath,
			JsonNode jnode) {
		ObjectMapper jsonMapper = new ObjectMapper();
		File f = new File(sourcePath);
		File f1 = new File(f.getParentFile().getParent(),
				"/mproducts/skuimges.json");
		Map m = null;
		if (f1.exists()) {
			try {
				m = jsonMapper.readValue(f1, HashMap.class);
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
		Iterator<JsonNode> list = jnode.iterator();
		List<JsonNode> jlist = Lists.newArrayList();
		while (list.hasNext()) {
			JsonNode obj = list.next();
			Product p = new Product();
			try {
				p.setSku(obj.get("sku").asText());
				p.setWebsiteId(1);
				List<ImageItem> imglist = Lists.newArrayList();
				if (obj.get("images") != null) {
					Iterator<ImageItem> imgit = Iterators.transform(
							obj.get("images").iterator(), imobj -> {
								try {
									return jsonMapper.readValue(jsonMapper
											.writeValueAsString(imobj),
											ImageItem.class);
								} catch (Exception e) {
									// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					});
					imglist.addAll(Lists.newArrayList(Iterators.filter(imgit,
							im -> im != null)));
				}
				if (imglist.size() == 0) {
					// ~ get imge form img service
					List<String> imgservicelist = imageService.getImages(p
							.getSku());
					Collections.sort(imgservicelist);
					for (int i = 0; i < imgservicelist.size(); i++) {
						ImageItem ii = new ImageItem();
						ii.setBaseImage(true);
						ii.setImageUrl(imgservicelist.get(i));
						ii.setLabel("");
						ii.setOrder(i + 1);
						ii.setSmallImage(true);
						ii.setThumbnail(true);
						imglist.add(ii);
					}
				}
				System.out.println(p.getSku() + "---" + imglist.size());
				if (imglist.size() == 0)
					continue;
				p.setImages(imglist);
				String sku = p.getSku();
				if (m != null && m.containsKey(sku) && p.getImages().size() > 0) {
					String imgname = m.get(sku).toString();
					String timgname = imgname
							.substring(imgname.lastIndexOf("/") + 1)
							.toLowerCase().trim();
					System.out.println(timgname);
					Collection<ImageItem> imgs = Collections2.filter(
							p.getImages(), ti -> ti.getImageUrl().toLowerCase()
									.contains(timgname));
					if (imgs != null && imgs.size() > 0) {
						p.setImages(Lists.transform(p.getImages(), img -> {
							if (img.getImageUrl().trim().toLowerCase()
									.contains(timgname)) {
								img.setSmallImage(true);
								img.setBaseImage(true);
								img.setThumbnail(true);
							} else {
								img.setSmallImage(false);
								img.setBaseImage(false);
								img.setThumbnail(false);
							}
							return img;
						}));
					}
				}
				String jsons = jsonMapper.writeValueAsString(p);
				// System.out.println(jsons);
				jlist.add(jsonMapper.readValue(jsons, JsonNode.class));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			jsonMapper.writeValue(new File(f.getParentFile().getParent(),
					"/serviceimges.json"), jlist);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jlist;
	}

	public Collection<JsonNode> getProductNoSellStatus(JsonNode jn) {
		// 7,71,78,80,81,83
		System.out.println("----");
		List<Integer> stolist = Arrays.asList(new Integer[] { 7});//, 71, 78, 80,81, 83 });
		List<Integer> gzolist = Arrays.asList(new Integer[] { 77 });
		Iterator<JsonNode> jnlist = Iterators.transform(
				jn.iterator(),
				obj -> {
					System.out.println(obj.get("sku").asText() + ",");
					if (obj.get("productStockMaps") == null) {
						ObjectMapper jsonMapper = new ObjectMapper();
						ObjectNode on = jsonMapper.createObjectNode();
						on.put("websiteId", 1);
						on.put("sku", obj.get("sku").asText());
						on.put("status", 2);
						return on;
					}
					if (obj.get("sku").asText().startsWith("G")) {
						Iterator<JsonNode> nosellnodes = Iterators.filter(obj
								.get("productStockMaps").iterator(), tobj -> {
							return gzolist
									.contains(tobj.get("stockId").asInt());
						});
						if (nosellnodes != null && nosellnodes.hasNext()) {
							JsonNode jngz = nosellnodes.next();
							ObjectMapper jsonMapper = new ObjectMapper();
							ObjectNode on = jsonMapper.createObjectNode();
							on.put("websiteId", 1);
							on.put("sku", obj.get("sku").asText());
							on.put("status",
									((jngz.get("saleStatus").asInt() == 10) ? 2
											: 1));
							return on;
						}
					} else {
						Iterator<JsonNode> psmnodes = Iterators.filter(
								obj.get("productStockMaps").iterator(),
								tobj -> {
									if (stolist.contains(tobj.get("stockId")
											.asInt())
											&& 10 == tobj.get("saleStatus")
													.asInt()) {
										return true;
									}
									return false;
								});
						if (psmnodes != null && psmnodes.hasNext()) {
							ObjectMapper jsonMapper = new ObjectMapper();
							ObjectNode on = jsonMapper.createObjectNode();
							on.put("websiteId", 1);
							on.put("sku", obj.get("sku").asText());
							on.put("status", 2);
							return on;
						}
					}
					return null;
				});
		return Lists.newArrayList(Iterators.filter(jnlist, obj -> obj != null));
	}
	
	public JsonNode getMutilProductUrl(
			JsonNode jnode) {
		ObjectMapper jsonMapper = new ObjectMapper();
		List<JsonNode> relist = Lists.newArrayList();
		JsonNode nnode = jsonMapper.createObjectNode();
		
		
		return nnode;
	}
	
}
