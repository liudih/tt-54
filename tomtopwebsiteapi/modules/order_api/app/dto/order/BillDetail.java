package dto.order;

import java.io.Serializable;
import java.util.Date;

public class BillDetail implements Serializable {
	private Integer iid;
	private Integer iorderid;
	private String ctype;
	private Integer iqty;
	private String cmsg;
	private Double foriginalprice;
	private Double fpresentprice;
	private Double ftotalprice;
	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public String getCmsg() {
		return cmsg;
	}

	public void setCmsg(String cmsg) {
		this.cmsg = cmsg;
	}

	public Double getForiginalprice() {
		return foriginalprice;
	}

	public void setForiginalprice(Double foriginalprice) {
		this.foriginalprice = foriginalprice;
	}

	public Double getFpresentprice() {
		return fpresentprice;
	}

	public void setFpresentprice(Double fpresentprice) {
		this.fpresentprice = fpresentprice;
	}

	public Double getFtotalprice() {
		return ftotalprice;
	}

	public void setFtotalprice(Double ftotalprice) {
		this.ftotalprice = ftotalprice;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
