package extensions.google.merchants;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.content.ShoppingContent;

import dto.SystemParameter;
import extensions.google.googlemerchantback.ShoppingContentBack;

import java.io.IOException;
import java.math.BigInteger;

import javax.inject.Inject;

import mapper.base.SystemParameterMapper;
import play.Logger;
import services.ISystemParameterService;

/**
 * Base class for the API samples.
 */
public class BaseMerchantsProduct {
	protected BigInteger merchantId;
	protected ShoppingContent content;
	protected ShoppingContentBack contentBack;
	private final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
	private Credential credential;
	private final HttpTransport httpTransport;
	private Config config;

	public void setConfig(Config config) {
		this.config = config;
	}

	public final static String MERCHANTS_PRODUCT_TOKEN = "MERCHANTS_PRODUCT_TOKEN";

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public BaseMerchantsProduct() {
		config = loadConfig();
		merchantId = config.getMerchantId();
		httpTransport = createHttpTransport();
		credential = createCredential();
		credential.setRefreshToken(config.getRefreshToken());
		content = createContentService();
		contentBack = createContentBackService();
	}

	public Config getConfig() {
		return config;
	}

	protected Config loadConfig() {
		try {
			return Config.load();
		} catch (IOException e) {
			System.out
					.println("There was an error while loading configuration.");
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	protected HttpTransport createHttpTransport() {
		try {
			return GoogleNetHttpTransport.newTrustedTransport();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	protected ShoppingContent createContentService() {
		return new ShoppingContent.Builder(httpTransport, jsonFactory,
				credential).setApplicationName(config.getApplicationName())
				.build();
	}
	protected ShoppingContentBack createContentBackService() {
		return new ShoppingContentBack.Builder(httpTransport, jsonFactory,
				credential).setApplicationName(config.getApplicationName())
				.build();
	}
	protected Credential createCredential() {
		return new GoogleCredential.Builder()
				.setClientSecrets(config.getClientId(),
						config.getClientSecret()).setJsonFactory(jsonFactory)
				.setTransport(httpTransport).build();
	}

}
