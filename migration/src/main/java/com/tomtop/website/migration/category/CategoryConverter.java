package com.tomtop.website.migration.category;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.website.dto.Attribute;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class CategoryConverter {

	@Inject
	StringUtils stringUtils;
	@Inject
	CategoryAttrConverter categoryAttrConverter;

	private int id = 0;

	private int getId() {
		id++;
		return id;
	}

	private Map<Integer, Integer> positionMap = Maps.newHashMap();

	private String currentpath;

	public List<com.website.dto.category.Category> getCategorys(
			String sourceFilePath, String savePath, String attrPath)
			throws BiffException, FileNotFoundException, IOException {
		this.id = 0;
		ObjectMapper om = new ObjectMapper();
		WorkbookSettings workbookSettings = new WorkbookSettings();
		workbookSettings.setEncoding("UTF-8"); // 解决中文乱码，或GBK
		Workbook wb = Workbook.getWorkbook(new FileInputStream(sourceFilePath),
				workbookSettings);
		Sheet[] sheetlist = wb.getSheets();
		List<com.website.dto.category.Category> categoryRoot = Lists
				.newArrayList();
		com.website.dto.category.Category catory1 = null;
		com.website.dto.category.Category catory2 = null;
		com.website.dto.category.Category catory3 = null;
		com.website.dto.category.Category catory4 = null;
		com.website.dto.category.Category catory5 = null;
		com.website.dto.category.Category catory6 = null;
		Map<String, String> caRelationMap = Maps.newHashMap();
		for (int sindex = 0; sindex < sheetlist.length; sindex++) {
			Sheet st = sheetlist[sindex];
			System.out.println(st.getName());
			Map<String, Integer> cellIdMap = Maps.newHashMap();
			for (Cell cl : st.getRow(0).clone()) {
				if (cl.getContents().contains("级")) {
					cellIdMap.put(cl.getContents().trim(), cl.getColumn());
				} else if (cl.getContents().contains("属性")) {
					cellIdMap.put("属性", cl.getColumn());
				} else if (cl.getContents().contains("速卖通")) {
					cellIdMap.put("速卖通", cl.getColumn());
				}

			}
			catory1 = null;
			for (int i = 1; i < st.getRows(); i++) {
				Cell[] cells = st.getRow(i);
				// ~ excel不规则，不是行都有相同数量的列
				// System.out.println(cells.length + "三级" + maxcolums);
				currentpath = "";
				if (cells.length <= 0) {
					continue;
				}
				if (cellIdMap.containsKey("一级")) {
					String val = stringUtils.convertValue(cells[cellIdMap
							.get("一级")].getContents());
					if (val.length() > 0 && null == catory1) {
						catory1 = buildCategory(val, 1, 1, this.getId(), null,
								stringUtils.generationKey(val),
								this.getPosition(1));
						currentpath = catory1.getPath();
						categoryRoot.add(catory1);
					}
				}
				if (cellIdMap.containsKey("二级")) {
					catory2 = this.getCategory(
							cells[cellIdMap.get("二级")].getContents(), catory1,
							catory2, 2, categoryRoot);
				}
				if (cellIdMap.containsKey("三级")) {
					// System.out.println(cellIdMap.get("三级"));
					catory3 = this.getCategory(
							cells[cellIdMap.get("三级")].getContents(), catory2,
							catory3, 3, categoryRoot);
				}
				if (cellIdMap.containsKey("四级")) {
					catory4 = this.getCategory(
							cells[cellIdMap.get("四级")].getContents(), catory3,
							catory4, 4, categoryRoot);
				}
				if (cellIdMap.containsKey("五级")) {
					catory5 = this.getCategory(
							cells[cellIdMap.get("五级")].getContents(), catory4,
							catory5, 5, categoryRoot);
				}
				if (cellIdMap.containsKey("六级")) {
					catory6 = this.getCategory(
							cells[cellIdMap.get("六级")].getContents(), catory5,
							catory6, 6, categoryRoot);
				}

				if (currentpath != null && currentpath.length() > 0
						&& cellIdMap.containsKey("属性")
						&& cellIdMap.get("属性") < cells.length) {
					String attrStr = cells[cellIdMap.get("属性")].getContents();
					if (attrStr != null && attrStr.trim().length() > 0) {
						this.getAttribute(savePath, currentpath, attrStr);
					}
				}

				if (cellIdMap.containsKey("速卖通")
						&& cellIdMap.get("速卖通") < cells.length) {
					String nameStr = cells[cellIdMap.get("速卖通")].getContents();
					if (nameStr.length() > 0)

						nameStr = stringUtils.generationKey(nameStr, "")
								.toLowerCase().replace("homeallcategories", "");
					caRelationMap.put(nameStr, currentpath);
				}
			}
		}
		if (null != attrPath)
			categoryAttrConverter.getAttr(attrPath, savePath, caRelationMap);
		return categoryRoot;
	}

	private int getPosition(int level) {
		int index = 1;
		if (positionMap.containsKey(level)) {
			index = positionMap.get(level);
			index++;
		}
		for (Integer key : positionMap.keySet()) {
			if (key > level) {
				positionMap.put(key, 0);
			}
		}
		positionMap.put(level, index);
		return index;
	}

	private com.website.dto.category.Category getCategory(String valstr,
			com.website.dto.category.Category pcategory,
			com.website.dto.category.Category category, int level,
			List<com.website.dto.category.Category> categoryRoot) {
		String val = stringUtils.convertValue(valstr);
		if (val.length() > 0) {
			if (null == category || !val.equals(category.getName())) {
				com.website.dto.category.Category tc = buildCategory(
						val,
						1,
						level,
						this.getId(),
						pcategory.getId(),
						pcategory.getPath() + "/"
								+ stringUtils.generationKey(val),
						this.getPosition(level));
				categoryRoot.add(tc);
				// pcategory.childs.add(tc);
				currentpath = tc.getPath();
				// System.out.println(currentpath);
				return tc;
			}
		}
		// com.website.dto.category.Category c;
		// if (null != category)
		// currentpath = category.getPath();
		return category;
	}

	private com.website.dto.category.Category buildCategory(String name,
			int lang, int level, int id, Integer pid, String path, int position) {
		com.website.dto.category.Category ca = new com.website.dto.category.Category();
		ca.setId(id);
		ca.setLanguageId(lang);
		ca.setLevel(level);
		ca.setName(name);
		ca.setParentId(pid);
		ca.setPath(path);
		ca.setTitle(name);
		ca.setPosition(position);
		return ca;
	}

	public void convertToWebSiteCategory(
			List<com.website.dto.category.Category> baseCategorys,
			String savePath, String vpath) throws BiffException,
			FileNotFoundException, IOException {
		List<com.website.dto.category.WebsiteCategory> webSiteList = Lists
				.newArrayList();
		List<String> vsList = this.getVisibleCategoryPath(vpath, savePath);
		com.website.dto.category.WebsiteCategory wc = null;
		for (com.website.dto.category.Category ca : baseCategorys) {
			wc = new com.website.dto.category.WebsiteCategory();
			wc.setCategoryId(ca.getId());
			wc.setDescription(ca.getDescription());
			wc.setId(ca.getId());
			wc.setKeywords(ca.getKeywords());
			wc.setLanguageId(ca.getLanguageId());
			wc.setLevel(ca.getLevel());
			wc.setName(ca.getName());
			wc.setParentId(ca.getParentId());
			wc.setPath(ca.getPath());
			wc.setTitle(ca.getTitle());
			wc.setWebsiteId(1);
			wc.setPosition(ca.getPosition());
			if (vsList.contains(ca.getPath())) {
				wc.setVisible(true);
			} else {
				wc.setVisible(false);
			}
			webSiteList.add(wc);
		}

		ObjectMapper om = new ObjectMapper();
		File output;
		String filename = "websitecategorys.json";
		if (savePath != null) {
			output = new File(savePath, filename);
		} else {
			output = new File(filename);
		}
		try {
			om.writeValue(output, webSiteList);
			System.out.println("save to web site !" + output.getPath());
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

	private void getAttribute(String savePath, String categorypath, String attr)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		String[] arr = attr.split(System.lineSeparator());
		List<List<Attribute>> attrList = Lists.transform(Arrays.asList(arr),
				str -> {
					String tstr = str.replace("\"", "");
					// System.out.println(tstr);
				tstr = tstr.replaceAll("\\(.*?\\)", "");
				// System.out.println(tstr);
				String[] narr = tstr.split(":");
				// cattr.setCategoryPath(path);
				String key = narr[0];
				if (narr.length == 1 || "N\\\\A".equals(key)) {
					Attribute cattr = new Attribute();
					cattr.setKey(key);
					cattr.setLanguageId(1);
					return Lists.newArrayList(cattr);
				} else {
					tstr = narr[1];
					tstr = tstr.replaceAll("[\\[\\]\\\\]*", "");
					if (tstr.length() > 0) {
						// System.out.println(tstr);
						String[] vaus = tstr.split(",");
						if (vaus.length > 0) {
							return Lists.transform(
									Arrays.asList(vaus),
									value -> {
										String tv = value.replace(
												"u003c", "<").trim();
										tv = tv.replace(
												"u003e", ">").trim();
										tv = tv.replace(
												"u0026", "&").trim();
										Attribute cattr = new Attribute();
										cattr.setKey(key);
										cattr.setLanguageId(1);
										cattr.setValue(tv);
										return cattr;
									});
						}
					}
					Attribute cattr1 = new Attribute();
					cattr1.setKey(key);
					cattr1.setLanguageId(1);
					return Lists.newArrayList(cattr1);
				}
			});

		List<Attribute> result = Lists.newArrayList();
		for (List<Attribute> rlist : attrList) {
			if (null != rlist)
				result.addAll(rlist);
		}
		if (result.size() == 0)
			return;
		CategoryAttribute cre = new CategoryAttribute();
		cre.setAttributes(result);
		cre.setCategoryPath(categorypath);
		File f = new File(savePath, "attribute");
		if (f.exists() == false)
			f.mkdirs();
		if (attrList.size() > 0)
			om.writeValue(new File(f.getPath(), categorypath.replace("/", "-")
					+ ".json"), cre);
		// return cre;
	}

	private List<String> getVisibleCategoryPath(String vpath, String spath)
			throws BiffException, FileNotFoundException, IOException {
		List<String> vsList = Lists.newArrayList();
		if (vpath != null) {
			List<com.website.dto.category.Category> vlist = this.getCategorys(
					vpath, spath, null);
			if (vlist != null && vlist.size() > 0) {
				vsList = Lists.transform(vlist, obj -> obj.getPath());
			}
		}
		return vsList;
	}

}
