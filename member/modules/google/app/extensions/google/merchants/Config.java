package extensions.google.merchants;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import play.Play;

/**
 * Wrapper for the JSON configuration file used to keep user specific details
 * like authentication information and Merchant Center account ID.
 */
public class Config extends GenericJson {
	/*
	 * private static final String FILE_NAME = "shopping-content-samples.json";
	 * private static final File CONFIG_FILE = new
	 * File(System.getProperty("user.home"), FILE_NAME);
	 */

	@Key
	private String clientId = "";

	@Key
	private String clientSecret = "";

	@Key
	@JsonString
	private BigInteger merchantId;

	@Key
	private String applicationName = "";

	@Key
	private String refreshToken = "";

	private static Config config;

	private Config() {

	}

	public static Config load()  throws IOException{
		if (config == null) {
			config = new Config();
			String clientId = Play.application().configuration()
					.getString("google.merchants.clientId");
			String clientSecret = Play.application().configuration()
					.getString("google.merchants.clientSecret");
			String merchantId = Play.application().configuration()
					.getString("google.merchants.merchantId");
			String applicationName = Play.application().configuration()
					.getString("google.merchants.applicationName");
			config.setApplicationName(applicationName);
			config.setClientId(clientId);
			config.setClientSecret(clientSecret);
			config.setMerchantId(new BigInteger(merchantId));
		}
		return config;

	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public BigInteger getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(BigInteger merchantId) {
		this.merchantId = merchantId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "Config [clientId=" + clientId + ", clientSecret="
				+ clientSecret + ", merchantId=" + merchantId
				+ ", applicationName=" + applicationName + ", refreshToken="
				+ refreshToken + "]";
	}
}
