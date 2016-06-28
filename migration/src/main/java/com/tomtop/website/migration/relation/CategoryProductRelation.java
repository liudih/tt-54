package com.tomtop.website.migration.relation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.Cell;

public class CategoryProductRelation {

	public String getProductCategory(String savepath, String excelpath,
			String categoryfile) throws BiffException, FileNotFoundException,
			IOException {

		WorkbookSettings workbookSettings = new WorkbookSettings();
		workbookSettings.setEncoding("UTF-8"); // 解决中文乱码，或GBK
		Workbook wb = Workbook.getWorkbook(new FileInputStream(excelpath),
				workbookSettings);
		Sheet[] sheetlist = wb.getSheets();

		File f = new File(categoryfile);
		if (!f.exists()) {
			System.out.println("category file not fount!");
		}

		ObjectMapper om = new ObjectMapper();
		com.website.dto.category.Category[] carr = om.readValue(f,
				com.website.dto.category.Category[].class);
		Map<String, com.website.dto.category.Category> catMap = Maps
				.uniqueIndex(Lists.newArrayList(carr), obj -> obj.getPath());
		Map<String, List<Integer>> values = Maps.newHashMap();
		String secondCategoryName = "";
		String errmsg = "not fount category: ";
		List<ObjectNode> nodelist = Lists.newArrayList();
		for (Sheet s : sheetlist) {
			System.out.println("sheet: " + s.getName());
			secondCategoryName = "";
			for (int i = 0; i < s.getColumns(); i++) {
				Cell[] ces = s.getColumn(i);
				if(ces.length<=1)
					continue;
				String key = "";
				if (ces[0].getContents().trim().length() != 0) {
					secondCategoryName = ces[0].getContents().trim();
					secondCategoryName = this.generationKey(secondCategoryName,
							"-");
				}
				if (secondCategoryName.length() == 0
						&& ces[1].getContents().trim().length() == 0) {
					continue;
				}
				key = secondCategoryName;
				if (ces[1].getContents().trim().length() != 0) {
					key = key + "/"
							+ this.generationKey(ces[1].getContents(), "-");

				}
				com.website.dto.category.Category cat = this.getCategory(
						catMap, key);
				if (cat == null) {
					errmsg = errmsg + s.getName() + " --> "
							+ ces[0].getContents() + "/"
							+ ces[1].getContents().trim() + " -- " + key
							+ System.lineSeparator();
					continue;
				}

				//System.out.println("category found " + key);
				for (int j = 2; j < ces.length; j++) {
					String sku = ces[j].getContents().trim();
					if (sku.length() == 0)
						break;
					if (sku.contains(",")) {
						for (String tsku : sku.split(",")) {
							this.setCategoryValue(values, catMap, tsku,
									cat.getPath());
							nodelist.add(buildNode(om, tsku, cat.getPath()));
						}
					} else {
						this.setCategoryValue(values, catMap, sku,
								cat.getPath());
						nodelist.add(buildNode(om, sku, cat.getPath()));
					}
				}
			}
		}

		File output;
		File output1;
		String filename = "skucategory.json";
		String nfname = "sku-category.json";
		if (savepath != null) {
			output = new File(savepath, filename);
			output1 = new File(savepath, nfname);
		} else {
			output = new File(filename);
			output1 = new File(savepath, nfname);
		}
		om.writeValue(output, values);
		om.writeValue(output1, nodelist);
		System.out.println(errmsg);
		return output.getPath();
	}

	private void setCategoryValue(Map<String, List<Integer>> values,
			Map<String, com.website.dto.category.Category> catMap, String sku,
			String categoryPath) {
		String catekey = "";
		List<Integer> list = null;
		sku = this.generationKey(sku, "");
		if (values.containsKey(sku.trim())) {
			list = values.get(sku);
		}
		if (null == list) {
			list = Lists.newArrayList();
		}
		for (String key : categoryPath.split("/")) {
			catekey = catekey + ((catekey.length() == 0) ? "" : "/") + key;
			com.website.dto.category.Category ca = catMap.get(catekey);
			System.out.println(sku + " - find category path : " + ca.getPath());
			if (!list.contains(ca.getId())) {
				list.add(ca.getId());
			}
		}
		values.put(sku, list);
	}

	private ObjectNode buildNode(ObjectMapper om, String sku, String path) {
		ObjectNode oNode = om.createObjectNode();
		oNode.put("sku", sku.trim());
		oNode.put("categoryPath", path);
		oNode.put("websiteId", 1);
		return oNode;
	}

	private String generationKey(String name, String spilter) {
		if (null == name) {
			return "";
		}
		name = convertValue(name);
		return name.trim().replaceAll("[^a-zA-Z0-9]+", spilter);
	}

	private String convertValue(String valstr) {
		String val = valstr.replaceAll("[\\(（\\u4E00-\\u9FA5\\）)]*", "");
		val = val.replaceAll("/$", "").trim();
		return val;
	}

	private com.website.dto.category.Category getCategory(
			Map<String, com.website.dto.category.Category> catMap,
			String endString) {
		Map<String, com.website.dto.category.Category> filtermap = null;
		if (endString.startsWith("-/")) {
			endString = endString.substring(1);
		}
		final String endStr = endString;
		filtermap = Maps.filterKeys(catMap, obj -> obj.trim().toLowerCase()
				.endsWith(endStr.trim().toLowerCase()));
		if (filtermap == null || filtermap.size() == 0) {
			filtermap = Maps.filterKeys(catMap, obj -> obj.trim().toLowerCase()
					.endsWith((endStr.trim() + "-Accessories").toLowerCase()));
		}
		if (filtermap == null || filtermap.size() == 0) {
			System.out.println("skip: the category not be found " + endStr);
			return null;
		}
		if (filtermap.size() > 1) {
			System.out.println("skip: over 2 item match this category "
					+ endStr);
			return null;
		}
		return (com.website.dto.category.Category) filtermap.values().toArray()[0];
	}
}
