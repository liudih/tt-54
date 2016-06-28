package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.collect.FluentIterable;

import play.Logger;
import services.base.lang.LanguageService;
import services.mobile.MobileService;

import com.twelvemonkeys.lang.StringUtil;

import dto.Language;

public class LanguageUtil {
	static MobileService mobileService = MobileService.getInstance();

	static String DEFAULT_PRE = "message_";

	static String SUFFIX = ".properties";

	static String DEFAULT_LANGUAGE = "en";

	static Properties DEFAULT_PROPERTIES = new Properties();

	static Map<Integer, Properties> LANGUAGE = new HashMap<>();

	static {
		try {
			Logger.debug("------------------LanguageUtil---------------init ");
			InputStream defaultIn = LanguageUtil.class.getClassLoader()
					.getResourceAsStream(
							DEFAULT_PRE + DEFAULT_LANGUAGE + SUFFIX);
			if (defaultIn == null) {
				throw new IOException(" default language not find by : "
						+ DEFAULT_PRE + DEFAULT_LANGUAGE + SUFFIX);
			}
			DEFAULT_PROPERTIES.load(defaultIn);
			List<Language> languageList = LanguageService.getInstance()
					.getAllLanguage();
			if (CollectionUtils.isEmpty(languageList)) {
				throw new NullPointerException(
						"languageService.getAllLanguage() return null");
			}
			FluentIterable.from(languageList)
					.forEach(
							(l) -> {
								Properties message = new Properties();
								try {
									String name = DEFAULT_PRE + l.getCname()
											+ SUFFIX;
									InputStream in = LanguageUtil.class
											.getClassLoader()
											.getResourceAsStream(name);
									if (in != null) {
										message.load(in);
										LANGUAGE.put(l.getIid(), message);
									}
								} catch (IOException e) {
									Logger.error("load properties error", e);
								}
							});
		} catch (Exception e) {
			Logger.error("load LanguageUtil error", e);
		}
	}

	public static String getMessage(String key) {
		return getString(key);
	}

	private static String getString(String key) {
		String value = null;
		try {
			value = getProperties().getProperty(key);
			if (StringUtil.isEmpty(value)) {
				Logger.debug(key
						+ " value is not found by key,return default properties mseeage! ");
			}
		} catch (Exception e) {
			Logger.error("", e);
			value = getDufaultString(key);
		}
		return value;
	}

	private static String getDufaultString(String key) {
		return DEFAULT_PROPERTIES.getProperty(key);
	}

	private static Properties getProperties() {
		int lid = mobileService.getLanguageID();
		Properties properties = LANGUAGE.get(lid);
		return properties == null ? DEFAULT_PROPERTIES : properties;
	}
}
