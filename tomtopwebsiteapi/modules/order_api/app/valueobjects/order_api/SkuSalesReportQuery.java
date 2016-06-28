package valueobjects.order_api;

import java.util.Date;
import java.util.List;

/**
 * 产品销售报表页面的查询用实体
 * 
 * @author liulj
 *
 */
public class SkuSalesReportQuery {
	/**
	 * 页索引,等于-1时表示查所有
	 */
	private Integer p;
	/**
	 * 记录数
	 */
	private Integer count;

	private String csku;
	/**
	 * 一级类目
	 */
	private String acategory;
	/**
	 * 二级类目
	 */
	private String bcategory;
	/**
	 * 三级类目
	 */
	private String ccategory;
	/**
	 * 产品title
	 */
	private String ctitle;
	/**
	 * 产品单价
	 */
	private Double fprice;
	/**
	 * 产品销售金额
	 */
	private Double salesamount;
	/**
	 * 产品销售数量
	 */
	private Integer iqty;
	/**
	 * 产品上架时间
	 */
	private Date dcreatedate;
	/**
	 * 产品付款时间区间-开始
	 */
	private Date dpaymentstartdate;
	/**
	 * 产品付款时间区间-结束
	 */
	private Date dpaymentenddate;
	/**
	 * 产品上架时间区间-开始
	 */
	private Date dshelvesstartdate;
	/**
	 * 产品上架时间区间-结束
	 */
	private Date dshelvesenddate;
	/**
	 * 产品状态
	 */
	private List<Integer> istatus;
	/**
	 * 产品查询的类目
	 */
	private String category;
	/**
	 * 产品来源
	 */
	private List<String> cvhosts;
	
	/**
	 * 下单开始时间
	 */
	private Date createStartDate;
	/**
	 * 下单结束时间
	 */
	private Date createEndDate;

	public Date getCreateStartDate() {
		return createStartDate;
	}

	public void setCreateStartDate(Date createStartDate) {
		this.createStartDate = createStartDate;
	}

	public Date getCreateEndDate() {
		return createEndDate;
	}

	public void setCreateEndDate(Date createEndDate) {
		this.createEndDate = createEndDate;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getAcategory() {
		return acategory;
	}

	public void setAcategory(String acategory) {
		this.acategory = acategory;
	}

	public String getBcategory() {
		return bcategory;
	}

	public void setBcategory(String bcategory) {
		this.bcategory = bcategory;
	}

	public String getCcategory() {
		return ccategory;
	}

	public void setCcategory(String ccategory) {
		this.ccategory = ccategory;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public Double getFprice() {
		return fprice;
	}

	public void setFprice(Double fprice) {
		this.fprice = fprice;
	}

	public Double getSalesamount() {
		return salesamount;
	}

	public void setSalesamount(Double salesamount) {
		this.salesamount = salesamount;
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date createdate) {
		this.dcreatedate = createdate;
	}

	public Date getDpaymentstartdate() {
		return dpaymentstartdate;
	}

	public void setDpaymentstartdate(Date dpaymentstartdate) {
		this.dpaymentstartdate = dpaymentstartdate;
	}

	public Date getDpaymentenddate() {
		return dpaymentenddate;
	}

	public void setDpaymentenddate(Date dpaymentenddate) {
		this.dpaymentenddate = dpaymentenddate;
	}

	public Date getDshelvesstartdate() {
		return dshelvesstartdate;
	}

	public void setDshelvesstartdate(Date dshelvesstartdate) {
		this.dshelvesstartdate = dshelvesstartdate;
	}

	public Date getDshelvesenddate() {
		return dshelvesenddate;
	}

	public void setDshelvesenddate(Date dshelvesenddate) {
		this.dshelvesenddate = dshelvesenddate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Integer> getIstatus() {
		return istatus;
	}

	public void setIstatus(List<Integer> istatus) {
		this.istatus = istatus;
	}

	public List<String> getCvhosts() {
		return cvhosts;
	}

	public void setCvhosts(List<String> cvhosts) {
		this.cvhosts = cvhosts;
	}

	public Integer getP() {
		return p;
	}

	public void setP(Integer p) {
		this.p = p;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
