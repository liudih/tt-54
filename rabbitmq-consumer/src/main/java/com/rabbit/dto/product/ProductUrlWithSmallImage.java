package  com.rabbit.dto.product;

import java.io.Serializable;

public class ProductUrlWithSmallImage extends ProductUrl implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String cimageurl;

	public String getCimageurl() {
		return cimageurl;
	}

	public void setCimageurl(String cimageurl) {
		this.cimageurl = cimageurl;
	}

}
