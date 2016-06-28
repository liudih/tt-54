package valueobjects.product;

/**
 * copy评论需要传入的参数对象
 * 
 * @author xcf
 *
 */
public class ProductCommentContext {

	/**
	 * 父级产品唯一ID
	 */
	String cparentlistingid;

	/**
	 * 父级产品SKU
	 */
	String cparentcku;
	/**
	 * 子产品唯一ID
	 */
	String csublistingid;
	/**
	 * 子级产品SKU
	 */
	String csubcku;
	
	public ProductCommentContext(String cparentlistingid, String cparentcku,
			String csublistingid, String csubcku) {
		
		this.cparentlistingid = cparentlistingid;
		this.cparentcku = cparentcku;
		this.csublistingid = csublistingid;
		this.csubcku = csubcku;
	}

	public String getCparentlistingid() {
		return cparentlistingid;
	}

	public String getCparentcku() {
		return cparentcku;
	}

	public String getCsublistingid() {
		return csublistingid;
	}

	public String getCsubcku() {
		return csubcku;
	}
	
}
