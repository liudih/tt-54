package services;

import org.junit.Test;

import play.Logger;
import services.base.MicroTranslate;

public class MicroTranslateTest {
	@Test
	public void testTranslate() {
		try {
			MicroTranslate microTranslate = new MicroTranslate();
			String g = microTranslate.translate("hello","en","cn");
			Logger.debug(g);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
