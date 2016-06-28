package com.tomtop.website.translation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class TranslatorUtils {
	@Inject
	MicroTranslator microTranslator;

	public String Trans(String text, Language fromlanguage,
			Language tolanguage) throws Exception {
		if (null == text || text.trim().length() == 0)
			return text;
		text = ">" + text + "<";
		System.out.println(fromlanguage.name() + "-->" + tolanguage.name());
		String pattern = ">[^<>]+<";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(text);
		String newStr = text;
		while (m.find()) {
			String mastr = m.group();
			String tranStr = microTranslator.translation(
					mastr.replaceAll("[><]", ""), fromlanguage.name(),
					tolanguage.name());
			newStr = newStr.replace(mastr, ">" + tranStr + "<");
			// System.out.println(mastr.replaceAll("[><]", "") + " ---> "
			// + tranStr.replaceAll("[><]", ""));
		}
		if (text.startsWith(">")) {
			newStr = newStr.substring(1);
		}
		if (text.endsWith("<")) {
			newStr = newStr.substring(0, newStr.length() - 1);
		}
		// System.out.println("end");
		// System.out.println(newStr);
		return newStr;
	}

	public String[] Trans(String[] text, Language fromlanguage,
			Language tolanguage) throws Exception {
		return microTranslator.translation(text, fromlanguage.name(),
				tolanguage.name());
	}

}
