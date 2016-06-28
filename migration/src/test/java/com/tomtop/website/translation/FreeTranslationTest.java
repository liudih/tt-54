package com.tomtop.website.translation;

import junit.framework.TestCase;

public class FreeTranslationTest extends TestCase {

	public void testTranslate() throws Exception {
		FreeTranslation ft = new FreeTranslation();
		String txt = ft
				.translate("Test Body", Language.en, Language.ru);
		assertEquals("Проверки Органа", txt);
	}

	public void testTranslateHtml() throws Exception {
		FreeTranslation ft = new FreeTranslation();
		String txl = ft.translateHtml(
				"<h1>Test</h1><p class='func'>Test Body</p>", Language.en,
				Language.ru);
		System.out.println("Html: " + txl);
		String simple = ft.translateHtml("Simple string appears here.",
				Language.en, Language.ru);
		System.out.println("Simple Text: " + simple);
	}
}
