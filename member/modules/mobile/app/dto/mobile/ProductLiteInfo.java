package dto.mobile;

import java.math.BigDecimal;

/**
 * 商品（精简版）
 * 
 * @author Administrator
 *
 */
public class ProductLiteInfo {
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 促销价格
	 */
	public Double sale;
	/**
	 * 图片地址
	 */
	private String imgurl;
	/**
	 * 商品ID
	 */
	private String gid;
	/**
	 * 降价百分比
	 */
	private Integer reduce = 0;
	/**
	 * 原价
	 */
	private Double pcs;
	/**
	 * 排名
	 */
	private Integer rank;
	/**
	 * 促销截止时间（倒计时用）
	 */
	private long enddate;

	/**
	 * SKU
	 */
	private String sku;
	/**
	 * 是否收藏（0 ：未收藏 1：已收藏）
	 */
	private Integer wish = 0;

	public String getTitle() {
		return title == null ? "" : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getSale() {
		return sale == null ? 0d : sale;
	}

	public void setSale(Double sale) {
		this.sale = sale;
	}

	public String getImgurl() {
		return imgurl == null ? "" : imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getGid() {
		return gid == null ? "" : gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public int getReduce() {
		if (this.getPcs() == 0) {
			return 0;
		}
		BigDecimal price = new BigDecimal(this.getPcs());
		BigDecimal discountprice = price
				.subtract(new BigDecimal(this.getSale()));
		return discountprice.divide(price, 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100)).intValue();
	}

	public void setReduce(Integer reduce) {
		if (reduce == null) {
			reduce = 0;
		}
		this.reduce = reduce;
	}

	public double getPcs() {
		return pcs == null ? 0d : pcs;
	}

	public void setPcs(Double pcs) {
		this.pcs = pcs;
	}

	public int getRank() {
		return rank == null ? 0 : rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public long getEnddate() {
		if (enddate == 0) {
			return 0;
		}
		return enddate - System.currentTimeMillis();
	}

	public void setEnddate(long enddate) {
		this.enddate = enddate;
	}

	public String getSku() {
		return sku == null ? "" : sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getWish() {
		return wish;
	}

	public void setWish(int wish) {
		this.wish = wish;
	}
}
