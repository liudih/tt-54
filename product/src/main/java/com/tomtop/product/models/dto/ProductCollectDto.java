package com.tomtop.product.models.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.tomtop.product.utils.DateFormatUtils;

public class ProductCollectDto implements Serializable  {
	private static final long serialVersionUID = 1L;
    private Integer iid;

    private String cemail;

    private String clistingid;

    private Date dcreatedate;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail == null ? null : cemail.trim();
    }

    public String getClistingid() {
        return clistingid;
    }

    public void setClistingid(String clistingid) {
        this.clistingid = clistingid == null ? null : clistingid.trim();
    }

	public Date getDcreatedate() {
		if(dcreatedate == null){
			try {
				dcreatedate = DateFormatUtils.getCurrentUtcTimeDate();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dcreatedate;
	}

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }
}