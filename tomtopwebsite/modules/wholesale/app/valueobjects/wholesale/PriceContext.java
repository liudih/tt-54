package valueobjects.wholesale;

public class PriceContext {

	String sku;
	Double price;
	Integer qty;

	public PriceContext(String sku, Double price) {
		super();
		this.sku = sku;
		this.price = price;
	}

	public PriceContext(String sku, Double price, Integer qty) {
		super();
		this.sku = sku;
		this.price = price;
		this.qty = qty;
	}

	public PriceContext(String sku, Integer qty) {
		this(sku, null, qty);
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

}
