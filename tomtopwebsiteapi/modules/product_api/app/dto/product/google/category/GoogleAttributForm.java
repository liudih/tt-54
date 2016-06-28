package dto.product.google.category;

import java.io.Serializable;

public class GoogleAttributForm implements Serializable {

	private static final long serialVersionUID = -4236123111780547125L;

	private Integer id;
	private Integer icategoryid;
	private String ckeyname;
	private String cvaluename;
	private String cname;
	private String webkeyname;
	private Integer wkeyId;
	private Integer iid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public String getCkeyname() {
		return ckeyname;
	}

	public void setCkeyname(String ckeyname) {
		this.ckeyname = ckeyname;
	}

	public String getCvaluename() {
		return cvaluename;
	}

	public void setCvaluename(String cvaluename) {
		this.cvaluename = cvaluename;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getWebkeyname() {
		return webkeyname;
	}

	public void setWebkeyname(String webkeyname) {
		this.webkeyname = webkeyname;
	}

	public Integer getWkeyId() {
		return wkeyId;
	}

	public void setWkeyId(Integer wkeyId) {
		this.wkeyId = wkeyId;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

}
