package dto.mobile;

import java.util.Date;

public class ProductDetailInfo {

	private String clistingid;

	private int iwebsiteid;

	private int ilanguageid;

	private String csku;

	private int istatus;

	private Date dnewformdate;

	private Date dnewtodate;

	private double bspecial;

	private String cvideoaddress;

	private int iqty;

	private double fprice;

	private double fcostprice;

	private double fweight;

	private String ctitle;

	private String cdescription;

	private String cshortdescription;

	private String ckeyword;

	private String cmetatitle;

	private String cmetakeyword;

	private String cmetadescription;

	private String cpaymentexplain;

	private String creturnexplain;

	private String cwarrantyexplain;

	private String ctitle_default;

	private String cdescription_default;

	private String cshortdescription_default;

	private String ckeyword_default;

	private String cmetatitle_default;

	private String cmetakeyword_default;

	private String cmetadescription_default;

	private boolean bmultiattribute;

	private String cparentsku;

	private boolean bvisible;

	private boolean bpulish;

	private String ccreateuser;

	private Date dcreatedate;

	private double ffreight;

	private boolean bmain;

	private boolean bactivity;

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		if (null == clistingid) {
			clistingid = "";
		}
		this.clistingid = clistingid;
	}

	public int getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(int iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public int getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(int ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		if (null == csku) {
			csku = "";
		}
		this.csku = csku;
	}

	public int getIstatus() {
		return istatus;
	}

	public void setIstatus(int istatus) {
		this.istatus = istatus;
	}

	public Date getDnewformdate() {
		return dnewformdate;
	}

	public void setDnewformdate(Date dnewformdate) {
		if (null == dnewformdate) {
			dnewformdate = new Date();
		}
		this.dnewformdate = dnewformdate;
	}

	public Date getDnewtodate() {
		return dnewtodate;
	}

	public void setDnewtodate(Date dnewtodate) {
		if (null == dnewtodate) {
			dnewtodate = new Date();
		}
		this.dnewtodate = dnewtodate;
	}

	public double getBspecial() {
		return bspecial;
	}

	public void setBspecial(double bspecial) {
		this.bspecial = bspecial;
	}

	public String getCvideoaddress() {
		return cvideoaddress;
	}

	public void setCvideoaddress(String cvideoaddress) {
		if (null == cvideoaddress) {
			cvideoaddress = "";
		}
		this.cvideoaddress = cvideoaddress;
	}

	public int getIqty() {
		return iqty;
	}

	public void setIqty(int iqty) {
		this.iqty = iqty;
	}

	public double getFprice() {
		return fprice;
	}

	public void setFprice(double fprice) {
		this.fprice = fprice;
	}

	public double getFcostprice() {
		return fcostprice;
	}

	public void setFcostprice(double fcostprice) {
		this.fcostprice = fcostprice;
	}

	public double getFweight() {
		return fweight;
	}

	public void setFweight(double fweight) {
		this.fweight = fweight;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		if (null == ctitle) {
			ctitle = "";
		}
		this.ctitle = ctitle;
	}

	public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		if (null == cdescription) {
			cdescription = "";
		}
		this.cdescription = cdescription;
	}

	public String getCshortdescription() {
		return cshortdescription;
	}

	public void setCshortdescription(String cshortdescription) {
		if (null == cshortdescription) {
			cshortdescription = "";
		}
		this.cshortdescription = cshortdescription;
	}

	public String getCkeyword() {
		return ckeyword;
	}

	public void setCkeyword(String ckeyword) {
		if (null == ckeyword) {
			ckeyword = "";
		}
		this.ckeyword = ckeyword;
	}

	public String getCmetatitle() {
		return cmetatitle;
	}

	public void setCmetatitle(String cmetatitle) {
		if (null == cmetatitle) {
			cmetatitle = "";
		}
		this.cmetatitle = cmetatitle;
	}

	public String getCmetakeyword() {
		return cmetakeyword;
	}

	public void setCmetakeyword(String cmetakeyword) {
		if (null == cmetakeyword) {
			cmetakeyword = "";
		}
		this.cmetakeyword = cmetakeyword;
	}

	public String getCmetadescription() {
		return cmetadescription;
	}

	public void setCmetadescription(String cmetadescription) {
		if (null == cmetadescription) {
			cmetadescription = "";
		}
		this.cmetadescription = cmetadescription;
	}

	public String getCpaymentexplain() {
		return cpaymentexplain;
	}

	public void setCpaymentexplain(String cpaymentexplain) {
		if (null == cpaymentexplain) {
			cpaymentexplain = "";
		}
		this.cpaymentexplain = cpaymentexplain;
	}

	public String getCreturnexplain() {
		return creturnexplain;
	}

	public void setCreturnexplain(String creturnexplain) {
		if (null == creturnexplain) {
			creturnexplain = "";
		}
		this.creturnexplain = creturnexplain;
	}

	public String getCwarrantyexplain() {
		return cwarrantyexplain;
	}

	public void setCwarrantyexplain(String cwarrantyexplain) {
		if (null == cwarrantyexplain) {
			cwarrantyexplain = "";
		}
		this.cwarrantyexplain = cwarrantyexplain;
	}

	public String getCtitle_default() {
		return ctitle_default;
	}

	public void setCtitle_default(String ctitle_default) {
		if (null == ctitle_default) {
			ctitle_default = "";
		}
		this.ctitle_default = ctitle_default;
	}

	public String getCdescription_default() {
		return cdescription_default;
	}

	public void setCdescription_default(String cdescription_default) {
		if (null == cdescription_default) {
			cdescription_default = "";
		}
		this.cdescription_default = cdescription_default;
	}

	public String getCshortdescription_default() {
		return cshortdescription_default;
	}

	public void setCshortdescription_default(String cshortdescription_default) {
		if (null == cshortdescription_default) {
			cshortdescription_default = "";
		}
		this.cshortdescription_default = cshortdescription_default;
	}

	public String getCkeyword_default() {
		return ckeyword_default;
	}

	public void setCkeyword_default(String ckeyword_default) {
		if (null == ckeyword_default) {
			ckeyword_default = "";
		}
		this.ckeyword_default = ckeyword_default;
	}

	public String getCmetatitle_default() {
		return cmetatitle_default;
	}

	public void setCmetatitle_default(String cmetatitle_default) {
		if (null == cmetatitle_default) {
			cmetatitle_default = "";
		}
		this.cmetatitle_default = cmetatitle_default;
	}

	public String getCmetakeyword_default() {
		return cmetakeyword_default;
	}

	public void setCmetakeyword_default(String cmetakeyword_default) {
		if (null == cmetakeyword_default) {
			cmetakeyword_default = "";
		}
		this.cmetakeyword_default = cmetakeyword_default;
	}

	public String getCmetadescription_default() {
		return cmetadescription_default;
	}

	public void setCmetadescription_default(String cmetadescription_default) {
		if (null == cmetadescription_default) {
			cmetadescription_default = "";
		}
		this.cmetadescription_default = cmetadescription_default;
	}

	public boolean isBmultiattribute() {
		return bmultiattribute;
	}

	public void setBmultiattribute(boolean bmultiattribute) {
		this.bmultiattribute = bmultiattribute;
	}

	public String getCparentsku() {
		return cparentsku;
	}

	public void setCparentsku(String cparentsku) {
		if (null == cparentsku) {
			cparentsku = "";
		}
		this.cparentsku = cparentsku;
	}

	public boolean isBvisible() {
		return bvisible;
	}

	public void setBvisible(boolean bvisible) {
		this.bvisible = bvisible;
	}

	public boolean isBpulish() {
		return bpulish;
	}

	public void setBpulish(boolean bpulish) {
		this.bpulish = bpulish;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		if (null == ccreateuser) {
			ccreateuser = "";
		}
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		if (null == dcreatedate) {
			dcreatedate = new Date();
		}
		this.dcreatedate = dcreatedate;
	}

	public double getFfreight() {
		return ffreight;
	}

	public void setFfreight(double ffreight) {
		this.ffreight = ffreight;
	}

	public boolean isBmain() {
		return bmain;
	}

	public void setBmain(boolean bmain) {
		this.bmain = bmain;
	}

	public boolean isBactivity() {
		return bactivity;
	}

	public void setBactivity(boolean bactivity) {
		this.bactivity = bactivity;
	}
}
