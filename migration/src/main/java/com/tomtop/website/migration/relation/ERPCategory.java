package com.tomtop.website.migration.relation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

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
import com.tomtop.website.migration.category.StringUtils;

public class ERPCategory {

	@Inject
	StringUtils stringUtils;

	public void getSkuRelation(String erpCategoryPath,
			String categoryrelationpath, String skuCategoryPaht, String savepath) {
		String categoryPath = this.convert(erpCategoryPath, savepath);
		if (categoryPath.length() == 0)
			return;
		this.getSkucategoryRelation(categoryrelationpath, categoryPath,
				savepath, skuCategoryPaht);
	}

	private String convert(String filepath, String savePath) {
		try {

			/*
			 * String castr = new String(
			 * java.nio.file.Files.readAllBytes(java.nio.file.Path
			 * .get(filepath)));
			 */
			ObjectMapper om = new ObjectMapper();
			JsonNode oNode = om.readValue(new File(filepath), JsonNode.class);
			System.out.println(filepath);
			Iterator<JsonNode> nodes = oNode.iterator();
			System.out.println(oNode.isArray());
			List<JsonNode> jlist = Lists.newArrayList(nodes);
			Map<Integer, JsonNode> jNodeMaps = Maps.uniqueIndex(jlist, no -> no
					.get("id").asInt());
			String builder = "";
			ArrayNode renodes = om.createArrayNode();
			for (JsonNode obj : jlist) {
				ObjectNode onode = om.createObjectNode();
				int id = obj.get("id").asInt();
				onode.put("id", id);
				onode.put("name_cn", obj.get("name_cn").asText());
				onode.put("name_en", obj.get("name_en").asText());
				String path = this.getPath(id, jNodeMaps, "name_cn");
				onode.put("path", path);
				// System.out.println(path);
				renodes.add(onode);
			}
			try {
				File nf = new File(savePath, "newerpcategory.json");
				om.writeValue(nf, renodes);
				return nf.getPath();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				builder += e.getMessage();
				System.out.println(e.getMessage());
				// e.printStackTrace();

			}
			om.writeValue(new File(savePath, "error.json"), builder);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "";
	}

	private String getPath(int id, Map<Integer, JsonNode> mjson,
			String filedName) {
		if (id == -1)
			return "";
		System.out.println("id--"+id);
		JsonNode node = mjson.get(id);
		int pid = node.get("parent").asInt();
		String name = node.get(filedName).asText();

		if (pid != -1 && pid != id) {
			String pathname = this.getPath(pid, mjson, filedName);
			return pathname + "/" + name;
		}
		return name;
	}

	private void getSkucategoryRelation(String excelpath, String categoryPath,
			String savepath, String skuCategoryfilePath) {
		WorkbookSettings workbookSettings = new WorkbookSettings();
		workbookSettings.setEncoding("UTF-8"); // 解决中文乱码，或GBK
		Workbook wb;
		try {

			ObjectMapper om = new ObjectMapper();
			JsonNode node = om
					.readValue(new File(categoryPath), JsonNode.class);

			Iterator<JsonNode> nodes = node.iterator();
			Multimap<String, JsonNode> mapNodes = Multimaps.index(
					nodes,
					obj -> obj.get("path").asText()
							.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", ""));

			wb = Workbook.getWorkbook(new FileInputStream(excelpath),
					workbookSettings);
			Sheet[] sheetlist = wb.getSheets();
			Map<String, Integer> categoryrelations = Maps.newHashMap();
			List<Integer> ids = new ArrayList<Integer>();
			for (Sheet sh : sheetlist) {
				sh.getRows();
				System.out.println(sh.getName());
				for (int r = 1; r < sh.getRows(); r++) {
					Cell[] cells = sh.getRow(r);
					if (cells.length >= 2) {
						if (cells[0].getContents() != null
								&& cells[0].getContents().trim().length() > 0) {
							String caname = cells[1].getContents();
							caname = caname.replaceAll(
									"[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
							if (mapNodes.containsKey(caname)) {
								JsonNode tnode = (JsonNode) mapNodes
										.get(caname).toArray()[0];
								int id = tnode.get("id").asInt();
								categoryrelations.put(cells[0].getContents(),
										id);
								ids.add(id);
							}
						}
					}
				}
			}
			om.writeValue(new File(savepath, "caMap.json"), categoryrelations);
			// om.writeValue(new File(savepath, "ids.json"), ids);
			generationCategorySku(categoryrelations, skuCategoryfilePath,
					savepath);
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generationCategorySku(
			Map<String, Integer> categoryRelationMap,
			String skuCategoryfilePath, String savepath) {
		ObjectMapper om = new ObjectMapper();
		try {
			System.out.println(skuCategoryfilePath);
			JsonNode jnode = om.readValue(new File(skuCategoryfilePath),
					JsonNode.class);
			Iterator<JsonNode> jnodes = jnode.iterator();
			// System.out.println(jnode+""+jnode.isArray());
			Multimap<Integer, JsonNode> skumaps = Multimaps.index(jnodes,
					obj -> {
						// System.out.print(obj.get("sku")+"-->"+obj.get("catagoryId"));
					if (obj.get("catagoryId") == null)
						return -1;
					return obj.get("catagoryId").asInt();
				});

			Collection<List<ObjectNode>> jsonlist = Collections2.transform(
					categoryRelationMap.keySet(),
					obj -> {
						List<ObjectNode> list = Lists.newArrayList();
						/*
						 * String[] patharr = obj.split("/"); String capath ="";
						 * for(String tp:patharr){ capath +=
						 * stringUtils.generationKey(tp)+"/"; }
						 * name.trim().replaceAll("[^a-zA-Z0-9]+", reStr);
						 */
						String capath = obj.trim().replaceAll("[^a-zA-Z0-9/]+",
								"-");
						int caid = categoryRelationMap.get(obj);
						Collection<JsonNode> skulist = skumaps.get(caid);
						for (JsonNode skunode : skulist) {
							ObjectNode onode = om.createObjectNode();
							onode.put("categoryPath", capath);
							onode.put("websiteId", 1);
							onode.put("sku", skunode.get("sku").asText());
							list.add(onode);
						}
						return list;
					});
			List<ObjectNode> relist = Lists.newArrayList();
			for (List<ObjectNode> rl : jsonlist) {
				relist.addAll(rl);
			}
			om.writeValue(new File(savepath, "skuCategoryRelation.json"),
					relist);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getSellpointSkus() throws JsonParseException,
			JsonMappingException, IOException {

		File f = new File("E:\\export\\sellpoint");
		StringBuilder skus = new StringBuilder();
		ObjectMapper om = new ObjectMapper();
		List<String> skulist = new ArrayList<String>();
		for (File tf : f.listFiles()) {
			JsonNode jnode = om.readValue(tf, JsonNode.class);
			Iterator<JsonNode> jlist = jnode.iterator();
			Iterator<JsonNode> ids = Iterators.filter(jlist,
					obj -> "EN".equals(obj.get("language").asText()));
			if (ids != null && ids.hasNext()) {
				String sku = ids.next().get("sku").asText();
				skus.append("'" + sku + "',");
				skulist.add(sku);
			}
		}
		File sf = new File("E:\\nosellpointsku.txt");
		BufferedReader breader = new BufferedReader(new FileReader(sf));
		String line = "";
		List<String> reskus = new ArrayList<String>();
		int count = 1;
		int ncount = 1;
		while ((line = breader.readLine()) != null) {
			String[] arr = line.split("\\t");
			System.out.println(arr.length);
			String sku = arr[1].replace("\"", "");
			if (skulist.contains(sku) == false) {
				reskus.add(sku);
				ncount++;
			} else {
				count++;
			}
		}
		System.out.println(count);
		System.out.println(ncount);
		om.writeValue(new File("E:\\sellpointsku.txt"), skus.toString());
		om.writeValue(new File("E:\\nsellpointskus.txt"), reskus);
	}
}
