package services.base;

import play.Configuration;
import play.Play;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class MicroTranslate {
	public String translate(String value, String languageform, String languageto)
			throws Exception {

		Configuration config = Play.application().configuration()
				.getConfig("translate");
		Translate.setClientId(config.getString("clientId"));
		Translate.setClientSecret(config.getString("Secret"));
		String translatedText = Translate.execute(value,
				getLanguageEnum(languageform), getLanguageEnum(languageto));
		return translatedText;
	}

	public Language getLanguageEnum(String language) {
		switch (language) {
		case "en":
			return Language.ENGLISH;
		case "es":
			return Language.ESTONIAN;
		case "cn":
			return Language.CHINESE_SIMPLIFIED;
		case "ru":
			return Language.RUSSIAN;
		default:
			return Language.ENGLISH;

		}
	}

}
