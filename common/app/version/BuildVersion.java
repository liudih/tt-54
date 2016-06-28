package version;

import interceptors.CacheResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Singleton;

import play.Logger;
import play.Play;
import extensions.InjectorInstance;

@Singleton
public class BuildVersion {

	public static String build() {
		return InjectorInstance.getInstance(BuildVersion.class).buildNumber();
	}

	@CacheResult(expiration = Integer.MAX_VALUE)
	public String buildNumber() {
		InputStream in = Play.application().resourceAsStream(
				"app_version.properties");
		if (in != null) {
			Properties p = new Properties();
			try {
				p.load(in);
				String buildNumber = p.getProperty("BUILD_NUMBER");
				return buildNumber;
			} catch (IOException e) {
				Logger.warn("Cannot load app_version.properties", e);
			}
		}
		return "UNKNOWN";
	}
}
