package valueobject.activity.page;

import java.io.Serializable;

public class PageTitle implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private Integer ipageid;

    private String ctitle;

    private Integer ilanguageid;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIpageid() {
        return ipageid;
    }

    public void setIpageid(Integer ipageid) {
        this.ipageid = ipageid;
    }

    public String getCtitle() {
        return ctitle;
    }

    public void setCtitle(String ctitle) {
        this.ctitle = ctitle == null ? null : ctitle.trim();
    }

    public Integer getIlanguageid() {
        return ilanguageid;
    }

    public void setIlanguageid(Integer ilanguageid) {
        this.ilanguageid = ilanguageid;
    }
}