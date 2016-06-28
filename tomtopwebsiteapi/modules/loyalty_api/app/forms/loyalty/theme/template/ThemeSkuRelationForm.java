package forms.loyalty.theme.template;

import javax.validation.constraints.NotNull;

public class ThemeSkuRelationForm {
	
    private Integer iid;
    
    private String groupname;
    
    private Integer ienable;
    
    private String themetitle;
    
    private String themeurl;

    @NotNull
    private Integer ithemeid;

    @NotNull
    private Integer igroupid;

    @NotNull
    private String csku;

    @NotNull
    private Integer isort;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIthemeid() {
        return ithemeid;
    }

    public void setIthemeid(Integer ithemeid) {
        this.ithemeid = ithemeid;
    }

    public Integer getIgroupid() {
        return igroupid;
    }

    public void setIgroupid(Integer igroupid) {
        this.igroupid = igroupid;
    }

    public String getCsku() {
        return csku;
    }

    public void setCsku(String csku) {
        this.csku = csku == null ? null : csku.trim();
    }

    public Integer getIsort() {
        return isort;
    }

    public void setIsort(Integer isort) {
        this.isort = isort;
    }

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getThemeurl() {
		return themeurl;
	}

	public void setThemeurl(String themeurl) {
		this.themeurl = themeurl;
	}

	public String getThemetitle() {
		return themetitle;
	}

	public void setThemetitle(String themetitle) {
		this.themetitle = themetitle;
	}

	public Integer getIenable() {
		return ienable;
	}

	public void setIenable(Integer ienable) {
		this.ienable = ienable;
	}
}