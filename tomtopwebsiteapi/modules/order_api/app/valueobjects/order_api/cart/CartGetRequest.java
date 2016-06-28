package valueobjects.order_api.cart;

import java.io.Serializable;

public class CartGetRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	final String email;
	final String ltc;
	final String ccy;
	final Integer siteID;
	final Integer languageID;

	/**
	 * Use context's default currency for getting the cart.
	 * 
	 * @param email
	 * @param ltc
	 */
	public CartGetRequest(String email, String ltc) {
		this(email, ltc, null, null, null);
	}

	/**
	 * Use the given currency for the cart item prices.
	 * 
	 * @param email
	 * @param ltc
	 * @param ccy
	 */
	public CartGetRequest(String email, String ltc, Integer siteID,
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

	public boolean isAnonymous() {
		return email == null;
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

}
