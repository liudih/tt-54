package entity.tracking;

import java.util.Date;

public class CommissionHistory {
    private Integer iid;

    private String caid;

    private Double famount;

    private String ctransactionid;

    private Integer icommissionstatus;

    private String cremark;

    private Date dcreatedate;
    

    private String cwithdrawlid;

   	private int isDisplay;
   	
   	private Double realAmount;


    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCaid() {
        return caid;
    }

    public void setCaid(String caid) {
        this.caid = caid == null ? null : caid.trim();
    }

    public Double getFamount() {
        return famount;
    }

    public void setFamount(Double famount) {
        this.famount = famount;
    }

    public String getCtransactionid() {
        return ctransactionid;
    }

    public void setCtransactionid(String ctransactionid) {
        this.ctransactionid = ctransactionid == null ? null : ctransactionid.trim();
    }

    public Integer getIcommissionstatus() {
        return icommissionstatus;
    }

    public void setIcommissionstatus(Integer icommissionstatus) {
        this.icommissionstatus = icommissionstatus;
    }

    public String getCremark() {
        return cremark;
    }

    public void setCremark(String cremark) {
        this.cremark = cremark == null ? null : cremark.trim();
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }


	public String getCwithdrawlid() {
		return cwithdrawlid;
	}

	public void setCwithdrawlid(String cwithdrawlid) {
		this.cwithdrawlid = cwithdrawlid;
	}

	public int getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(int isDisplay) {
		this.isDisplay = isDisplay;
	}

	public Double getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Double realAmount) {
		this.realAmount = realAmount;
	}	
}