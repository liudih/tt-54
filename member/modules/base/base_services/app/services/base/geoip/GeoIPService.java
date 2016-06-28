package services.base.geoip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

import play.Logger;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

public class GeoIPService {

	final DatabaseReader reader;

	public GeoIPService() throws IOException {
		InputStream in = getClass().getResourceAsStream(
				"/geoip/GeoLite2-Country.mmdb");
		this.reader = new DatabaseReader.Builder(in).build();
	}

	public GeoIPService(String dbpath) throws IOException {
		File dbfile = new File(dbpath);
		this.reader = new DatabaseReader.Builder(dbfile).build();
	}

	public String getCountryCode(String ip) {
		try {
			InetAddress ipAddress = InetAddress.getByName(ip);
			CountryResponse response = reader.country(ipAddress);

			Country country = response.getCountry();
			return country.getIsoCode();
		} catch (Exception e) {
			Logger.trace("Resolve GeoIP error", e);
			return null;
		}
	}
}
