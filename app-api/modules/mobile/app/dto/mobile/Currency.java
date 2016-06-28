package dto.mobile;

import java.io.Serializable;

public class Currency  implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private Integer iid;//币种ID
	private String ccode;//币种简称
	private String csymbol;//币种符号
	
	public Integer getIid() {
		
		return iid;
	}
	
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	
	public String getCcode() {
		
		if(null == ccode){
			return "";
		}
		
		return ccode;
	}
	
	public void setCcode(String ccode) {
		this.ccode = ccode;
	}
	
	public String getCsymbol() {
		
		if(null == csymbol){
			return "";
		}
		
		return csymbol;
	}
	
	public void setCsymbol(String csymbol) {
		this.csymbol = csymbol;
	}
	
	
	

}
