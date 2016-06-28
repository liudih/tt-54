package com.rabbit.dto.category;

import java.io.Serializable;

public class CategoryFilterAttribute implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private Integer icategoryid;

    private Integer iattributekeyid;

    private Integer iattributevalueid;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIcategoryid() {
        return icategoryid;
    }

    public void setIcategoryid(Integer icategoryid) {
        this.icategoryid = icategoryid;
    }

    public Integer getIattributekeyid() {
        return iattributekeyid;
    }

    public void setIattributekeyid(Integer iattributekeyid) {
        this.iattributekeyid = iattributekeyid;
    }

    public Integer getIattributevalueid() {
        return iattributevalueid;
    }

    public void setIattributevalueid(Integer iattributevalueid) {
        this.iattributevalueid = iattributevalueid;
    }
}