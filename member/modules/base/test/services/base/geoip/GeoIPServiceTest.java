package services.base.geoip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GeoIPServiceTest {

	@Test
	public void testGeoIP() throws Exception {

		GeoIPService geoip = new GeoIPService();
		String us = geoip.getCountryCode("128.101.101.101");
		assertEquals("US", us);

		String cn = geoip.getCountryCode("14.20.48.20");
		assertEquals("CN", cn);
	}
}
