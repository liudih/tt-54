package com.tomtop.website.migration.category;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.website.dto.Attribute;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class CategoryAttrConverter {

	@Inject
	StringUtils stringUtils;

	/**
	 * 
	 * @param attrPath
	 * @param pathRelation
	 *            速卖通品类，网站品类
	 */
	public Map<String, CategoryAttribute> getAttr(String attrPath,String savePath,
			Map<String, String> pathRelation) {
		WorkbookSettings workbookSettings = new WorkbookSettings();
		workbookSettings.setEncoding("UTF-8"); // 解决中文乱码，或GBK
		Workbook wb;
		String cpath = "";
		try {
			wb = Workbook.getWorkbook(new FileInputStream(attrPath),
					workbookSettings);
			ObjectMapper om = new ObjectMapper();
			Sheet[] sheetlist = wb.getSheets();
			Map<String, CategoryAttribute> caAttrmap = Maps.newHashMap();
			File f = new File(savePath,"/attr");
			f.mkdirs();
			// List<String> genalist = Lists.newArrayList();
			for (Sheet st : sheetlist) {
				int rows = st.getRows();
				for (int i = 1; i < rows; i++) {
					Cell[] cells = st.getRow(i);
					if (cells.length > 2) {
						String name = cells[0].getContents();
						cpath = name;
						name = stringUtils.generationKey(name, "").toLowerCase();
						if (pathRelation.containsKey(name) == false) {
							continue;
						}
						String jsonstr = cells[2].getContents();
						if (jsonstr.length() == 0)
							continue;
						JsonNode anode = om.readValue(jsonstr, JsonNode.class);
						JsonNode atrrnode = anode.get("attributes");

						if (atrrnode != null) {
							CategoryAttribute caAttr = new CategoryAttribute();

							caAttr.setCategoryPath(pathRelation.get(name));
							pathRelation.remove(name);
							Iterator<JsonNode> list = atrrnode.iterator();
							List<Attribute> attrlist = Lists.newArrayList();
							while (list.hasNext()) {
								JsonNode tn = list.next();
								// System.out.println(tn+"");
								String namestr = tn.get("names").get("en")
										.asText();
								JsonNode valueno = tn.get("values");
								if (null == valueno)
									continue;
								Iterator<JsonNode> valuelist = valueno
										.iterator();
								while (valuelist.hasNext()) {
									JsonNode vanode = valuelist.next();
									Attribute cattr = new Attribute();
									cattr.setKey(namestr);
									cattr.setValue(vanode.get("names")
											.get("en").asText());
									attrlist.add(cattr);
								}
							}
							caAttr.setAttributes(attrlist);
							om.writeValue(
									new File(f.getPath(), caAttr
											.getCategoryPath().replace("/", "-") + ".json"),
									caAttr);
							caAttrmap.put(caAttr.getCategoryPath(), caAttr);
						}
					}
				}
			}
			System.out.println(om.writeValueAsString(pathRelation));
			System.out.println(pathRelation.size());
			return caAttrmap;
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			System.out.println(cpath);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(cpath);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(cpath);
			e.printStackTrace();
		}
		return null;
	}
}
