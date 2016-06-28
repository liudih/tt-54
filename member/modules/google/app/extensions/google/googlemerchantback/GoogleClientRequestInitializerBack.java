package extensions.google.googlemerchantback;

import java.io.IOException;

/**
 * Google client request initializer.
 *
 * <p>
 * For example, this might be used to set a key URL query parameter on all requests:
 * </p>
 *
 * <pre>
  public class KeyRequestInitializer implements GoogleClientRequestInitializer {
    public void initialize(GoogleClientRequest<?> request) {
      request.put("key", KEY);
    }
  }
 * </pre>
 *
 * <p>
 * Implementations should be thread-safe.
 * </p>
 *
 * @since 1.12
 * @author Yaniv Inbar
 */
public interface GoogleClientRequestInitializerBack {

  /** Initializes a Google client request. */
  void initialize(AbstractGoogleClientRequestBack<?> request) throws IOException;
}
