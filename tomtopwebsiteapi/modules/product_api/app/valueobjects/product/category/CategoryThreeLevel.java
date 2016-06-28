package valueobjects.product.category;

import java.util.Date;

/**
 * 产品三级类别实体，配置sku销售报表时查询三级类别时使用
 * 
 * @author liulj
 *
 */
public class CategoryThreeLevel {
	/**
	 * 产品sku
	 */
	private String csku;
	/**
	 * 产品标题
	 */
	private String ctitle;
	/**
	 * 产品上架时间
	 */
	private Date dcreatedate;
	/**
	 * 类目名称
	 */
	private String cname;
	/**
	 * 类目等级
	 */
	private Integer ilevel;

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIlevel() {
		return ilevel;
	}

	public void setIlevel(Integer ilevel) {
		this.ilevel = ilevel;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
}
