package extensions.google.googlemerchantback;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;

import java.util.Arrays;
import java.util.Collections;

/**
 * Thread-safe Google JSON client.
 *
 * @since 1.12
 * @author Yaniv Inbar
 */
public abstract class AbstractGoogleJsonClientBack extends AbstractGoogleClientBack {

  /**
   * @param builder builder
   *
   * @since 1.14
   */
  protected AbstractGoogleJsonClientBack(ShoppingContentBack.Builder builder) {
    super(builder);
  }

  @Override
  public JsonObjectParser getObjectParser() {
    return (JsonObjectParser) super.getObjectParser();
  }

  /** Returns the JSON Factory. */
  public final JsonFactory getJsonFactory() {
    return getObjectParser().getJsonFactory();
  }

  /**
   * Builder for {@link AbstractGoogleJsonClient}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   */
  public abstract static class Builder extends AbstractGoogleClientBack.Builder {

    /**
     * @param transport HTTP transport
     * @param jsonFactory JSON factory
     * @param rootUrl root URL of the service
     * @param servicePath service path
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @param legacyDataWrapper whether using the legacy data wrapper in responses
     */
    protected Builder(HttpTransport transport, JsonFactory jsonFactory, String rootUrl,
        String servicePath, HttpRequestInitializer httpRequestInitializer,
        boolean legacyDataWrapper) {
      super(transport, rootUrl, servicePath, new JsonObjectParser.Builder(
          jsonFactory).setWrapperKeys(
          legacyDataWrapper ? Arrays.asList("data", "error") : Collections.<String>emptySet())
          .build(), httpRequestInitializer);
    }

    @Override
    public final JsonObjectParser getObjectParser() {
      return (JsonObjectParser) super.getObjectParser();
    }

    /** Returns the JSON Factory. */
    public final JsonFactory getJsonFactory() {
      return getObjectParser().getJsonFactory();
    }

    @Override
    public abstract AbstractGoogleJsonClientBack build();

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        GoogleClientRequestInitializerBack googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }

    @Override
    public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }
  }

}