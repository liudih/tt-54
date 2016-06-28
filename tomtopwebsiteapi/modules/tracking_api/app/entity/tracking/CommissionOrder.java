package entity.tracking;

import java.util.Date;

public class CommissionOrder {
    private Integer iid;

    private Integer icommissionid;

    private Integer iorderid;

    private Date dcreatedate;

    private Integer istatus;

    private Double famount;
    
    private String cremark;

	public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIcommissionid() {
        return icommissionid;
    }

    public void setIcommissionid(Integer icommissionid) {
        this.icommissionid = icommissionid;
    }

    public Integer getIorderid() {
        return iorderid;
    }

    public void setIorderid(Integer iorderid) {
        this.iorderid = iorderid;
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }

    public Integer getIstatus() {
        return istatus;
    }

    public void setIstatus(Integer istatus) {
        this.istatus = istatus;
    }

    public Double getFamount() {
        return famount;
    }

    public void setFamount(Double famount) {
        this.famount = famount;
    }
    
    public String getCremark() {
		return cremark;
	}

	public void setCremark(String cremark) {
		this.cremark = cremark;
	}

	
	@Override
	public String toString() {
		return "CommissionOrder [iid=" + iid + ", icommissionid="
				+ icommissionid + ", iorderid=" + iorderid + ", dcreatedate="
				+ dcreatedate + ", istatus=" + istatus + ", famount=" + famount
				+ ", cremark=" + cremark + "]";
	}
	
	
}