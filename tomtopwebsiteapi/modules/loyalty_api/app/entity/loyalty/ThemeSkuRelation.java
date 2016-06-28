package entity.loyalty;

import javax.validation.constraints.NotNull;

public class ThemeSkuRelation {
    private Integer iid;

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
}