package dto;

import java.io.Serializable;
import java.util.List;

public class AttributeKeyItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String ckeyname;

	private Integer ikeyid;

	private Integer ilanguageid;

	private boolean notReal;

	private List<dto.AttributeValueItem> attributeValue;

	public AttributeKeyItem() {
		super();
	}

	public AttributeKeyItem(Integer iid, String ckeyname, Integer ikeyid,
			Integer ilanguageid, List<AttributeValueItem> attributeValue) {
		super();
		this.iid = iid;
		this.ckeyname = ckeyname;
		this.ikeyid = ikeyid;
		this.ilanguageid = ilanguageid;
		this.attributeValue = attributeValue;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCkeyname() {
		return ckeyname;
	}

	public void setCkeyname(String ckeyname) {
		this.ckeyname = ckeyname;
	}

	public Integer getIkeyid() {
		return ikeyid;
	}

	public void setIkeyid(Integer ikeyid) {
		this.ikeyid = ikeyid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public List<dto.AttributeValueItem> getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(List<dto.AttributeValueItem> attributeValue) {
		this.attributeValue = attributeValue;
	}

	public boolean isNotReal() {
		return notReal;
	}

	public void setNotReal(boolean notReal) {
		this.notReal = notReal;
	}
}
