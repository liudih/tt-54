package forms;

import java.util.Date;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class CmsContentForm {
	
	private int iid;
	
	private Integer imenuid;
	
	private String ckey;
	
	private String ctitle;
	
	private String ccontent;
	
    private String ccreateuser;

    private Date dcreatedate;

    private String clastupdateduser;

    private Date dlastupdateddate;
    
    private Integer iisnominate;
    
    private Integer page = 1;
    

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
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

	public Integer getImenuid() {
		return imenuid;
	}

	public void setImenuid(Integer imenuid) {
		this.imenuid = imenuid;
	}

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
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
