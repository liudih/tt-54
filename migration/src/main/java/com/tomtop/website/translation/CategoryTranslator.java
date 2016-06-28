package com.tomtop.website.translation;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class CategoryTranslator {

	@Inject
	FreeTranslation translatorUtils;
	@Inject
	MicroTranslator microTranslator;

	public void Translation1(String sourcePath, String targetPath,
			List<String> targetLanguageCodes) throws JsonParseException,
			JsonMappingException, IOException {

		File f1 = new File(sourcePath);
		File[] files = f1.listFiles();
		ObjectMapper om = new ObjectMapper();

		if (files.length <= 0) {
			System.out.print("not found files");
			return;
		}
		for (File f : files) {
			if (f.isFile() == false || f.exists() == false
					|| f.getName().endsWith(".json") == false) {
				System.out.print(f.getName() + " is not a effective file");
				continue;
			}
			com.website.dto.category.Category[] calist = om.readValue(f,
					com.website.dto.category.Category[].class);
			if (null == calist || calist.length == 0) {
				System.out.print(" not found json files");
				continue;
			}
			for (String lang : targetLanguageCodes) {
				Language targetlang = Language.valueOf(lang);
				int languageid = targetlang.id();
				System.out.println(languageid);
				Collection<com.website.dto.category.Category> rlist = Collections2
						.transform(
								Lists.newArrayList(calist),
								obj -> {
									if (obj.getLanguageId() != 1) {
										System.out
												.println(" source must be english language skip");
										return null;
									}
									com.website.dto.category.Category ca = new com.website.dto.category.Category();
									String name = obj.getName();
									System.out.println(name);
									try {
										name = translatorUtils.translate(name,
												Language.en, targetlang);
									} catch (Exception e) {
										e.printStackTrace();
										return null;
									}
									ca.setId(obj.getId());
									ca.setLanguageId(languageid);
									ca.setLevel(obj.getLevel());
									ca.setName(name);
									ca.setParentId(obj.getParentId());
									ca.setPath(obj.getPath());
									ca.setTitle(name);
									return ca;
								});
				om.writeValue(
						new File(targetPath, "category_" + lang + ".json"),
						rlist);
			}

		}
	}

	public void Translation(String sourcePathfile, String targetPath,
			List<String> targetLanguageCodes) throws JsonParseException,
			JsonMappingException, IOException {

		File f1 = new File(sourcePathfile);
		ObjectMapper om = new ObjectMapper();
		if (f1.exists() == false) {
			System.out.print(f1.getName()
					+ " is not a effective file;file must be end with en.json");
			return;
		}
		com.website.dto.category.Category[] salist = om.readValue(f1,
				com.website.dto.category.Category[].class);
		if (null == salist || salist.length == 0) {
			System.out.print(f1.getName() + " is not a effective json file");
			return;
		}
		String[] names = new String[salist.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = salist[i].getName();
		}

		for (String lang : targetLanguageCodes) {
			com.website.dto.category.Category[] resultlist = salist;
			int count = resultlist.length;
			try {
				Language targetlang = Language.valueOf(lang);
				int languageid = targetlang.id();
				System.out.println(targetlang.iso3() + "--" + languageid + "--"
						+ count);
				StringBuilder htmlBuilder = new StringBuilder();
				StringBuilder htmlTranBilder = new StringBuilder();
				for (int i = 0; i < count; i++) {
					htmlBuilder.append("<p id=" + i + ">" + names[i]
							+ "</p>");
					if (i % 150 == 0) {
						htmlTranBilder
								.append(translatorUtils.translateHtml(
										htmlBuilder.toString(), Language.en,
										targetlang));
						htmlBuilder.delete(0, htmlBuilder.length());
					}
				}
				if (htmlBuilder.length() > 0) {
					htmlTranBilder.append(translatorUtils.translateHtml(
							htmlBuilder.toString(), Language.en, targetlang));
				}
				Document doc = Jsoup.parseBodyFragment(htmlTranBilder
						.toString());
				for (int i = 0; i < count; i++) {
					String name = doc.getElementById("" + i).ownText();
					resultlist[i].setLanguageId(languageid);
					resultlist[i].setName(name);
					resultlist[i].setTitle(name);
				}
				File newpath = new File(targetPath, "/tran");
				if (newpath.exists() == false) {
					newpath.mkdir();
				}
				om.writeValue(new File(newpath.getPath(), "htmlcategory_"
						+ lang + ".html"), htmlTranBilder.toString());
				om.writeValue(new File(newpath.getPath(), "category_" + lang
						+ ".json"), resultlist);
			} catch (Exception e) {
				System.out.println(lang + "tran failes");
				e.printStackTrace();
			}
		}
	}
}
