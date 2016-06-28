package valueobjects.order_api.cart;

import java.io.Serializable;

public class CartCreateRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	final String email;
	final String ltc;
	final String ccy;
	final Integer siteID;
	final Integer languageID;

	/**
	 * get the cart with the context's default currency
	 * 
	 * @param email
	 * @param ltc
	 */
	public CartCreateRequest(String email, String ltc) {
		this(email, ltc, null, null, null);
	}

	/**
	 * get the cart with the currency given for item price.
	 * 
	 * @param email
	 * @param ltc
	 * @param ccy
	 */
	public CartCreateRequest(String email, String ltc, Integer siteID,
			Integer languageID, String ccy) {
		super();
		this.email = email;
		this.ltc = ltc;
		this.siteID = siteID;
		this.languageID = languageID;
		this.ccy = ccy;
	}

	public String getEmail() {
		return email;
	}

	public String getLtc() {
		return ltc;
	}

	public String getCcy() {
		return ccy;
	}

	public Integer getSiteID() {
		return siteID;
	}

	public Integer getLanguageID() {
		return languageID;
	}

	public boolean isAnonymous() {
		return email == null;
	}
}
