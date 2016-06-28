package valueobjects.interaction;

import java.io.Serializable;

public class SimpleComment implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String sku;	
	private String email;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
