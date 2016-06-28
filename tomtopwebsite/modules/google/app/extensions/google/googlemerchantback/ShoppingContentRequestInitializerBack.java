package extensions.google.googlemerchantback;

public class ShoppingContentRequestInitializerBack extends CommonGoogleJsonClientRequestInitializerBack {

  public ShoppingContentRequestInitializerBack() {
    super();
  }

  /**
   * @param key API key or {@code null} to leave it unchanged
   */
  public ShoppingContentRequestInitializerBack(String key) {
    super(key);
  }

  /**
   * @param key API key or {@code null} to leave it unchanged
   * @param userIp user IP or {@code null} to leave it unchanged
   */
  public ShoppingContentRequestInitializerBack(String key, String userIp) {
    super(key, userIp);
  }

  
  public final void initializeJsonRequest(AbstractGoogleJsonClientRequestBack<?> request) throws java.io.IOException {
    super.initializeJsonRequest(request);
    initializeShoppingContentRequest((ShoppingContentRequestBack<?>) request);
  }

  /**
   * Initializes ShoppingContent request.
   *
   * <p>
   * Default implementation does nothing. Called from
   * {@link #initializeJsonRequest(com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest)}.
   * </p>
   *
   * @throws java.io.IOException I/O exception
   */
  protected void initializeShoppingContentRequest(ShoppingContentRequestBack<?> request) throws java.io.IOException {
  }
}
