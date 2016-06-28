package entity.loyalty;

import java.util.Date;

public class BulkRate {
    private Integer iid;

    private Integer ibulkid;

    private Integer iqty;

    private Double fdiscount;

    private Date dcreatedate;

    private Double fgrossprofit;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIbulkid() {
        return ibulkid;
    }

    public void setIbulkid(Integer ibulkid) {
        this.ibulkid = ibulkid;
    }

    public Integer getIqty() {
        return iqty;
    }

    public void setIqty(Integer iqty) {
        this.iqty = iqty;
    }

    public Double getFdiscount() {
        return fdiscount;
    }

    public void setFdiscount(Double fdiscount) {
        this.fdiscount = fdiscount;
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }

    public Double getFgrossprofit() {
        return fgrossprofit;
    }

    public void setFgrossprofit(Double fgrossprofit) {
        this.fgrossprofit = fgrossprofit;
    }
}