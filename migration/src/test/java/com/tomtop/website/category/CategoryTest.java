package com.tomtop.website.category;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import junit.framework.TestCase;

public class CategoryTest extends TestCase {

	public void testCategory() throws Exception {
		fixActiveSku();
		// fixMutiSku();
		// getMemberEmail() ;
		 //fixActiveSku();
/*		 this.diffSkus("E:\\export\\ttactiveskus.json",
		 "F:\\skunosell.json",
		 null,
		 "F:\\notsellskus.json");*/
		/*
		 * this.diffSkus("E:\\export\\mgmultiskus.json",
		 * "E:\\export\\ttmultiskus.json", null, "F:\\notinsertmultiskus.json");
		 */
		//getClearSku();
		assertEquals(true, true);
	}

	private void checkEmails() {
		ObjectMapper om = new ObjectMapper();
		File f = new File("E:\\emails.txt");
		File f1 = new File("E:\\emails.json");
		try {
			String[] emlist = om.readValue(f, String[].class);
			List<String> list = Arrays.asList(emlist);
			String[] emalllist = om.readValue(f1, String[].class);
			for (String em : emalllist) {
				if (list.contains(em.split("---")[0].toLowerCase()) == false) {
					System.out.println("get it "
							+ em.split("---")[0].toLowerCase());
					File cf = new File("E:\\export\\members",
							em.split("---")[1]);
					cf.renameTo(new File("E:\\tm", cf.getName()));
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

	private void getMemberEmail() {
		ObjectMapper om = new ObjectMapper();
		File f = new File("E:\\export\\members\\points");
		List<String> list = new ArrayList<String>();
		int count = f.list().length;
		for (File tf : f.listFiles()) {
			try {
				JsonNode jn = om.readValue(tf, JsonNode.class);
				list.add(jn.get("email").asText() + "---" + tf.getName());
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
			System.out.println(count--);
		}
		try {
			om.writeValue(new File("E:\\emailsii.json"), list);
			System.out.print("-----------end---------------");
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

	private void fixMutiSku() {
		try {
			ObjectMapper om = new ObjectMapper();
			File f = new File("F:\\skus.txt");
			List<String> skus = new ArrayList<String>();
			String[] existskus = om.readValue(f, String[].class);
			File f1 = new File("F:\\multiskus.txt");
			String[] allskus = om.readValue(f1, String[].class);
			List<String> existskulist = Lists.transform(
					Arrays.asList(existskus), obj -> obj.toUpperCase().trim());
			for (String sku : allskus) {
				if (existskulist.contains(sku.toUpperCase().trim()) == false) {
					skus.add(sku.toUpperCase().trim());
				}
			}
			om.writeValue(new File("F:\\nsmusku.json"), skus);
			System.out.println(skus.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void fixActiveSku() {
//		this.diffSkus("E:\\export\\ttactiveskus.json",
//				"E:\\export\\erpactiveskus.json", null, "F:\\notsellskus.json");
		
		this.diffSkus("F:\\nocostpricesku.txt",
		"F:\\skucategoryjson.txt", null, "F:\\notsellskus.json");

	}

	private void diffSkus(String sourceskupath, String targeskuspath,
			String activeskupath, String savepathName) {
		try {
			ObjectMapper om = new ObjectMapper();
			File f = new File(sourceskupath);
			List<String> skus = new ArrayList<String>();
			String[] ttskus = om.readValue(f, String[].class);
			File f1 = new File(targeskuspath);
			JsonNode erpskus = om.readValue(f1, JsonNode.class);
			List<String> existskulist = Lists.newArrayList(Iterators.transform(
					erpskus.iterator(), obj -> {
						if (obj.get("sku") == null) {
							// System.out.println(obj + "");
					return (obj + "").replace("\"", "");
				} else {
					return obj.get("sku").asText();
				}
			}));
			List<String> existactiveskulist = Lists.newArrayList();
			if (null != activeskupath) {
				File f2 = new File(targeskuspath);
				if (f2.exists()) {
					JsonNode erpactiveskus = om.readValue(f2, JsonNode.class);
					existactiveskulist = Lists.newArrayList(Iterators
							.transform(erpactiveskus.iterator(), obj -> {
								if (obj.get("sku") == null) {
									// System.out.println(obj + "");
									return (obj + "").replace("\"", "");
								} else {
									return obj.get("sku").asText();
								}
							}));
				}
			}
			for (String sku : ttskus) {
				if ((existskulist.contains(sku.toUpperCase().trim()) == false
						&& (existactiveskulist.size() == 0 || existactiveskulist
								.contains(sku.toUpperCase().trim())))) {
					skus.add(sku.toUpperCase().trim());
				}
			}
			om.writeValue(new File(savepathName), skus);
			System.out.println(skus.size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void getClearSku() {
		List<Integer> gzList = Arrays.asList(new Integer[] { 77 });
		List<Integer> szList = Arrays.asList(new Integer[] { 7});//, 71, 78, 80, 81,83 });
		ObjectMapper om = new ObjectMapper();
		File f1 = new File("E:\\export\\erpskuclear.txt");
		File erpcategoryFile = new File(
				"E:\\export\\relation\\newerpcategory.json");
		File erpskucategoryFile = new File("E:\\export\\relation\\SKUfile.txt");
		try {
			System.out.println("get categorys");
			JsonNode erpcategorys = om.readValue(erpcategoryFile,
					JsonNode.class);
			List<Integer> closhcategoryids = Lists.transform(Lists
					.newArrayList(Iterators.filter(erpcategorys.iterator(),
							obj -> obj.get("path").asText().startsWith("服饰"))),
					obj1 -> obj1.get("id").asInt());
			System.out.println("--get sku categorys");
			JsonNode erpskucategorys = om.readValue(erpskucategoryFile,
					JsonNode.class);
			Multimap<String, JsonNode> map = Multimaps.index(
					erpskucategorys.iterator(), obj -> obj.get("sku").asText());
			List<String> closhSkus = new ArrayList<String>();
			for (String cid : map.keySet()) {
				Collection<JsonNode> clist = map.get(cid);
				for (JsonNode jn1 : clist) {
					if (jn1.get("catagoryId") != null
							&& closhcategoryids.contains(jn1.get("catagoryId")
									.asInt())) {
						closhSkus.add(cid);
					}
				}
			}

			System.out.println("-- begin 1 --> ");
			JsonNode erpskus = om.readValue(f1, JsonNode.class);
			/*
			 * List<JsonNode> existskulist = Lists
			 * .newArrayList(Iterators.filter( erpskus.iterator(), obj -> {
			 * JsonNode stors = obj.get("productStockMaps"); Iterator<JsonNode>
			 * clearNodes = Iterators .filter(stors.iterator(), obj1 ->
			 * (obj1.get("saleStatus") .asInt() == 20)); return (clearNodes !=
			 * null && clearNodes .hasNext()); }));
			 */
			List<JsonNode> existskulist = Lists
					.newArrayList(erpskus.iterator());

			Collection<JsonNode> gzNodes = Collections2
					.filter(existskulist,
							obj -> {
								if (closhSkus.contains(obj.get("sku").asText())) {
									JsonNode stors = obj
											.get("productStockMaps");
									Iterator<JsonNode> clearNodes = Iterators.filter(
											stors.iterator(),
											obj1 -> ((gzList.contains(obj1.get(
													"stockId").asInt()) && (obj1
													.get("saleStatus").asInt() == 20))));
									return (clearNodes != null && clearNodes
											.hasNext());
								}
								return false;
							});
			List<JsonNode> clearList = Lists.newArrayList(this.convertClears(
					gzNodes, om, gzList));
			Collection<JsonNode> szNodes = Collections2
					.filter(existskulist,
							obj -> {
								if (closhSkus.contains(obj.get("sku").asText()) == false) {
									JsonNode stors = obj
											.get("productStockMaps");
									Iterator<JsonNode> clearNodes = Iterators.filter(
											stors.iterator(),
											obj1 -> ((szList.contains(obj1.get(
													"stockId").asInt()) && (obj1
													.get("saleStatus").asInt() == 20))));
									return (clearNodes != null && clearNodes
											.hasNext());
								}
								return false;
							});
			clearList.addAll(this.convertClears(szNodes, om, szList));
			om.writeValue(new File("F:\\clearSkus.json"), clearList);
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

	private Collection<JsonNode> convertClears(Collection<JsonNode> list,
			ObjectMapper om, List<Integer> storages) {
		return Collections2.transform(
				list,
				obj -> {
					ObjectNode on = om.createObjectNode();
					on.put("sku", obj.get("sku").asText());
					on.put("websiteId", 1);
					ArrayNode an = on.putArray("labelTypes");
					an.add("clearstocks");

					JsonNode stors = obj.get("productStockMaps");
					Iterator<JsonNode> clearNodes = Iterators.filter(stors
							.iterator(), obj1 -> ((storages.contains(obj1.get(
							"stockId").asInt())) && (obj1.get("saleStatus")
							.asInt() == 20)));
					if (clearNodes.hasNext()) {
						on.put("qty", clearNodes.next().get("stockCount")
								.asInt());
					}
					return on;
				});
	}
}
