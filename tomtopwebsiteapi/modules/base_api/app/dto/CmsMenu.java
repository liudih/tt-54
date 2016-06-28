package dto;

import java.io.Serializable;
import java.sql.Date;

public class CmsMenu implements Serializable {

	private static final long serialVersionUID = 1L;

	private int iid;
	
	private String cname;
	
	private Integer iparentid;
	
	private Integer ilevel;
	
	private String ciconurl;
	
	private String curl;
	
	private String cclass;
	
    private String ccreateuser;

    private Date dcreatedate;

    private String clastupdateduser;

    private Date dlastupdateddate;
    
    private Integer iisnominate;
    
    private String ctype;
    
    public CmsMenu(int iid,String cname) {
    	this.iid = iid;
    	this.cname = cname;
    }
    
    public CmsMenu() {
    	
    }

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public Integer getIisnominate() {
		return iisnominate;
	}

	public void setIisnominate(Integer iisnominate) {
		this.iisnominate = iisnominate;
	}

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getIparentid() {
		return iparentid;
	}

	public void setIparentid(Integer iparentid) {
		this.iparentid = iparentid;
	}

	public Integer getIlevel() {
		return ilevel;
	}

	public void setIlevel(Integer ilevel) {
		this.ilevel = ilevel;
	}

	public String getCiconurl() {
		return ciconurl;
	}

	public void setCiconurl(String ciconurl) {
		this.ciconurl = ciconurl;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public String getCclass() {
		return cclass;
	}

	public void setCclass(String cclass) {
		this.cclass = cclass;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getClastupdateduser() {
		return clastupdateduser;
	}

	public void setClastupdateduser(String clastupdateduser) {
		this.clastupdateduser = clastupdateduser;
	}

	public Date getDlastupdateddate() {
		return dlastupdateddate;
	}

	public void setDlastupdateddate(Date dlastupdateddate) {
		this.dlastupdateddate = dlastupdateddate;
	}

	 
 
}
