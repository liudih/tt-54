package entity.loyalty.promo;

import java.util.Date;

public class PromotionCode {

	String ccode;

	String crules;

	String cactions;

	String cruleparams;

	String cactionparams;

	Date dbegindate;

	Date denddate;

	Date dcreatedate;

	String ccreateuser;

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getCrules() {
		return crules;
	}

	public void setCrules(String crules) {
		this.crules = crules;
	}

	public String getCactions() {
		return cactions;
	}

	public void setCactions(String cactions) {
		this.cactions = cactions;
	}

	public String getCruleparams() {
		return cruleparams;
	}

	public void setCruleparams(String cruleparams) {
		this.cruleparams = cruleparams;
	}

	public String getCactionparams() {
		return cactionparams;
	}

	public void setCactionparams(String cactionparams) {
		this.cactionparams = cactionparams;
	}

	public Date getDbegindate() {
		return dbegindate;
	}

	public void setDbegindate(Date dbegindate) {
		this.dbegindate = dbegindate;
	}

	public Date getDenddate() {
		return denddate;
	}

	public void setDenddate(Date denddate) {
		this.denddate = denddate;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

}
