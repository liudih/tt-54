package forms.loyalty.theme.template;

import java.util.List;

import entity.loyalty.ThemeGroupName;
public class ThemeGroupForm{
	
	private List<ThemeGroupName> languages;
	
	private Integer iid;
	
	private Integer ithemeid;
	
	private Integer isort;
	
	private String curl;

	//
	private String tempcname;
    private Integer ithemegroupnameiid;

    private Integer igroupid;

    private String cname;

	
	private Integer pageSize;
	
	private Integer pageNum;

	//专题的URL地址
	private String themecurl;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public Integer getIthemeid() {
        return ithemeid;
    }

    public void setIthemeid(Integer ithemeid) {
        this.ithemeid = ithemeid;
    }

    public Integer getIsort() {
        return isort;
    }

    public void setIsort(Integer isort) {
        this.isort = isort;
    }
  
	public Integer getPageSize() {
		return pageSize != null ? pageSize : 20;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum != null ? pageNum : 1;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}


    public Integer getIgroupid() {
        return igroupid;
    }

    public void setIgroupid(Integer igroupid) {
        this.igroupid = igroupid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }


    

	public String getTempcname() {
		return tempcname;
	}

	public void setTempcname(String tempcname) {
		this.tempcname = tempcname;
	}

	public List<ThemeGroupName> getLanguages() {
		return languages;
	}

	public void setLanguages(List<ThemeGroupName> languages) {
		this.languages = languages;
	}

	public Integer getIthemegroupnameiid() {
		return ithemegroupnameiid;
	}

	public void setIthemegroupnameiid(Integer ithemegroupnameiid) {
		this.ithemegroupnameiid = ithemegroupnameiid;
	}

	
	public String getThemecurl() {
		return themecurl;
	}

	public void setThemecurl(String themecurl) {
		this.themecurl = themecurl;
	}

	@Override
	public String toString() {
		return "ThemeGroupForm [languages=" + languages + ", iid=" + iid
				+ ", ithemeid=" + ithemeid + ", isort=" + isort + ", curl="
				+ curl + ", tempcname=" + tempcname + ", ithemegroupnameiid="
				+ ithemegroupnameiid + ", igroupid=" + igroupid + ", cname="
				+ cname + ", pageSize=" + pageSize + ", pageNum=" + pageNum
				+ ", themecurl=" + themecurl + "]";
	}



}