package entity.tracking;

import java.util.Date;

public class CommissionStatusLog {
    private Integer iid;

    private Integer ibeforestatus;

    private Integer iafterstatus;

    private Date dcreatedate;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIbeforestatus() {
        return ibeforestatus;
    }

    public void setIbeforestatus(Integer ibeforestatus) {
        this.ibeforestatus = ibeforestatus;
    }

    public Integer getIafterstatus() {
        return iafterstatus;
    }

    public void setIafterstatus(Integer iafterstatus) {
        this.iafterstatus = iafterstatus;
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }
}