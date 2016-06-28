package entity.member;

public class DropShipLevel {
	private Integer iid;

	private String clevelname;

	private Double ftotal; // 该等级需消费金额的最小值(USD)

	private Double discount;

	private Integer iproductcount; // 上架商品最大数

	private Double fstartprice;

	private Double fendprice;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getClevelname() {
		return clevelname;
	}

	public void setClevelname(String clevelname) {
		this.clevelname = clevelname;
	}

	public Double getFtotal() {
		return ftotal;
	}

	public void setFtotal(Double ftotal) {
		this.ftotal = ftotal;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getIproductcount() {
		return iproductcount;
	}

	public void setIproductcount(Integer iproductcount) {
		this.iproductcount = iproductcount;
	}

	public Double getFstartprice() {
		return fstartprice;
	}

	public void setFstartprice(Double fstartprice) {
		this.fstartprice = fstartprice;
	}

	public Double getFendprice() {
		return fendprice;
	}

	public void setFendprice(Double fendprice) {
		this.fendprice = fendprice;
	}

}