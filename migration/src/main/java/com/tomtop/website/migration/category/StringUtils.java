package com.tomtop.website.migration.category;

public class StringUtils {

	public String convertValue(String valstr) {
		if (null == valstr) {
			return "";
		}
		String val = valstr.replaceAll("[\\(（\\u4E00-\\u9FA5\\）)]*", "");
		val = val.replaceAll("/$", "").trim();
		if (val.equals("&")) {
			val = "";
		}
		return val;
	}

	public String generationKey(String name) {
		return this.generationKey(name, "-");
	}

	public String generationKey(String name, String reStr) {
		return name.trim().replaceAll("[^a-zA-Z0-9]+", reStr);
	}
}
