package entity.loyalty;

import java.util.List;

import valueobjects.product.ProductBadge;



public class ThemeGroup {
    private Integer iid;

    private Integer ithemeid;

    private Integer isort;
    
	private String curl;

    public String getCurl() {
		return curl;
	}
    private List<ProductBadge> pbList;
    
    private String themeGroupName;

	public void setCurl(String curl) {
		this.curl = curl;
	}

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

    public Integer getIsort() {
        return isort;
    }

    public void setIsort(Integer isort) {
        this.isort = isort;
    }

	@Override
	public String toString() {
		return "ThemeGroup [iid=" + iid + ", ithemeid=" + ithemeid + ", isort="
				+ isort + ", curl=" + curl + "]";
	}
   
	public List<ProductBadge> getPbList() {
		return pbList;
	}

	public void setPbList(List<ProductBadge> pbList) {
		this.pbList = pbList;
	}

	public String getThemeGroupName() {
		return themeGroupName;
	}

	public void setThemeGroupName(String themeGroupName) {
		this.themeGroupName = themeGroupName;
	}
    
}