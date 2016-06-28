package com.tomtop.website.translation;

public enum Language {
	// supported for now
	en("eng", 1), es("spa", 2), ru("rus", 3),
	// unsupported yet
	de("ger", 4), fr("fra", 5), it("ita", 6), jp("jpn", 7);

	private int id;
	private String iso3;

	Language(String iso3, int id) {
		this.iso3 = iso3;
		this.id = id;
	}

	int id() {
		return id;
	}

	String iso3() {
		return iso3;
	}

	String iso2() {
		return name();
	}
}