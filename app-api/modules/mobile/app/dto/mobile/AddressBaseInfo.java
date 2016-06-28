package dto.mobile;

public class AddressBaseInfo {

	private String aid;

	private String name;

	private Integer isdef;

	private String addr;

	private String tel;

	private String vatno;

	public String getAid() {
		return aid == null ? "" : aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public Integer getIsdef() {
		return isdef == null ? 0 : isdef;
	}

	public void setIsdef(Integer isdef) {
		this.isdef = isdef;
	}

	public String getAddr() {
		return addr == null ? "" : addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getVatno() {
		return vatno == null ? "" : vatno;
	}

	public void setVatno(String vatno) {
		this.vatno = vatno;
	}

	public String getName() {
		return name == null ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel == null ? "" : tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
