package entity.member;

public class MemberComment {
    private Integer iid;

    private String clistingid;

    private String csku;

    private Integer imemberid;

    private String ccomment;

    private Boolean bshow;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getClistingid() {
        return clistingid;
    }

    public void setClistingid(String clistingid) {
        this.clistingid = clistingid == null ? null : clistingid.trim();
    }

    public String getCsku() {
        return csku;
    }

    public void setCsku(String csku) {
        this.csku = csku == null ? null : csku.trim();
    }

    public Integer getImemberid() {
        return imemberid;
    }

    public Boolean getBshow() {
		return bshow;
	}

	public void setBshow(Boolean bshow) {
		this.bshow = bshow;
	}

	public void setImemberid(Integer imemberid) {
        this.imemberid = imemberid;
    }

    public String getCcomment() {
        return ccomment;
    }

    public void setCcomment(String ccomment) {
        this.ccomment = ccomment == null ? null : ccomment.trim();
    }


}