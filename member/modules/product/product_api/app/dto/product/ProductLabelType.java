package dto.product;

/**
 * @see t_product_label_type
 * @author lijun
 *
 */
public class ProductLabelType {
	private Integer id;
	private String type;
	private String creater;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
}
