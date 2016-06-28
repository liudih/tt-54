package dto.product;

import java.io.Serializable;

public class CategoryBase implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private Integer iparentid;

    private String cpath;

    private Integer ilevel;

    private Integer iposition;

    private Integer ichildrencount;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIparentid() {
        return iparentid;
    }

    public void setIparentid(Integer iparentid) {
        this.iparentid = iparentid;
    }

    public String getCpath() {
        return cpath;
    }

    public void setCpath(String cpath) {
        this.cpath = cpath == null ? null : cpath.trim();
    }

    public Integer getIlevel() {
        return ilevel;
    }

    public void setIlevel(Integer ilevel) {
        this.ilevel = ilevel;
    }

    public Integer getIposition() {
        return iposition;
    }

    public void setIposition(Integer iposition) {
        this.iposition = iposition;
    }

    public Integer getIchildrencount() {
        return ichildrencount;
    }

    public void setIchildrencount(Integer ichildrencount) {
        this.ichildrencount = ichildrencount;
    }
}