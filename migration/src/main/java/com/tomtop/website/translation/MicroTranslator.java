package com.tomtop.website.translation;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class MicroTranslator {

	// # Microsoft translate API
	String clientId = "oofly";//"50c54ccf-d909-4a79-b03a-5dbdb399e9bd";
	String Secret = "J47Ie6RiX0f2nCIW75V4HdktOIiQvQ5gEzE2cikT/M4=";//"684GRpQ1buWj87gSRUuqmlxO0JxngtzfLU8Dm5iMt6Q=";

	public String translation(String text, Language from, Language to)
			throws Exception {
		Translate.setClientId(clientId);
		Translate.setClientSecret(Secret);
		return Translate.execute(text, from, to);
	}

	public String translation(String text, String fromCountryCode,
			String toCountryCode) throws Exception {
		return Translate.execute(text, this.getLanguage(fromCountryCode),
				this.getLanguage(toCountryCode));
	}
	
	public String[] translation(String[] text, String fromCountryCode,
			String toCountryCode) throws Exception {
		return Translate.execute(text, this.getLanguage(fromCountryCode),
				this.getLanguage(toCountryCode));
	}

	/**
	 * 
	 * @param countryCode
	 *            cn ,de,fr,us
	 * @return
	 */
	private Language getLanguage(String countryCode) {
		Language.setClientId(clientId);
		Language.setClientSecret(Secret);
		return Language.fromString(countryCode.toLowerCase());
	}

}
