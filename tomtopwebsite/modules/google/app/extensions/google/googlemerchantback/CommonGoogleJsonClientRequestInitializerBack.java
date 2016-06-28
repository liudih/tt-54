package extensions.google.googlemerchantback;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Google JSON client request initializer implementation for setting properties like key and userIp.
 *
 * <p>
 * The simplest usage is to use it to set the key parameter:
 * </p>
 *
 * <pre>
  public static final GoogleClientRequestInitializer KEY_INITIALIZER =
      new CommonGoogleJsonClientRequestInitializer(KEY);
 * </pre>
 *
 * <p>
 * There is also a constructor to set both the key and userIp parameters:
 * </p>
 *
 * <pre>
  public static final GoogleClientRequestInitializer INITIALIZER =
      new CommonGoogleJsonClientRequestInitializer(KEY, USER_IP);
 * </pre>
 *
 * <p>
 * If you want to implement custom logic, extend it like this:
 * </p>
 *
 * <pre>
  public static class MyRequestInitializer extends CommonGoogleJsonClientRequestInitializer {

    {@literal @}Override
    public void initialize(AbstractGoogleJsonClientRequest{@literal <}?{@literal >} request)
        throws IOException {
      // custom logic
    }
  }
 * </pre>
 *
 * <p>
 * Finally, to set the key and userIp parameters and insert custom logic, extend it like this:
 * </p>
 *
 * <pre>
  public static class MyKeyRequestInitializer extends CommonGoogleJsonClientRequestInitializer {

    public MyKeyRequestInitializer() {
      super(KEY, USER_IP);
    }

    {@literal @}Override
    public void initializeJsonRequest(
        AbstractGoogleJsonClientRequest{@literal <}?{@literal >} request) throws IOException {
      // custom logic
    }
  }
 * </pre>
 *
 * <p>
 * Subclasses should be thread-safe.
 * </p>
 *
 * @since 1.12
 * @author Yaniv Inbar
 */
public class CommonGoogleJsonClientRequestInitializerBack extends CommonGoogleClientRequestInitializerBack {

  public CommonGoogleJsonClientRequestInitializerBack() {
    super();
  }

  /**
   * @param key API key or {@code null} to leave it unchanged
   */
  public CommonGoogleJsonClientRequestInitializerBack(String key) {
    super(key);
  }

  /**
   * @param key API key or {@code null} to leave it unchanged
   * @param userIp user IP or {@code null} to leave it unchanged
   */
  public CommonGoogleJsonClientRequestInitializerBack(String key, String userIp) {
    super(key, userIp);
  }

  @Override
  public final void initialize(AbstractGoogleClientRequestBack<?> request) throws IOException {
    super.initialize(request);
    initializeJsonRequest((AbstractGoogleJsonClientRequestBack<?>) request);
  }

  /**
   * Initializes a Google JSON client request.
   *
   * <p>
   * Default implementation does nothing. Called from
   * {@link #initialize(AbstractGoogleClientRequest)}.
   * </p>
   *
   * @throws IOException I/O exception
   */
  protected void initializeJsonRequest(AbstractGoogleJsonClientRequestBack<?> request)
      throws IOException {
  }
}
