package dto.mobile;

import java.util.Date;

public class ProductBaseInfo {
	// 广告编号
	private String clistingid;
	// 站点编号
	private Integer iwebsiteid;
	// 语言编号
	private Integer ilanguageid;
	// SKU
	private String csku;
	// 状态(在售、停售、下架)
	private Integer istatus;
	// 新品期开始时间
	private Date dnewformdate;
	// 新品期结束时间
	private Date dnewtodate;
	// 特殊商品
	private Boolean bspecial;
	// 特殊商品
	private String cvideoaddress;
	// 数量
	private Integer iqty;
	// 网站基础价格
	private Double fprice;
	// 商品成本价格
	private Double fcostprice;
	// 重量
	private Double fweight;
	// 标题
	private String ctitle;
	// 描述
	private String cdescription;
	// 短描述
	private String cshortdescription;
	// 多语言关键字
	private String ckeyword;
	// meta 标题
	private String cmetatitle;
	// meta 多语言关键字
	private String cmetakeyword;
	// meta 描述
	private String cmetadescription;
	//
	private String cpaymentexplain;
	//
	private String creturnexplain;
	//
	private String cwarrantyexplain;
	//
	private String ctitle_default;
	//
	private String cdescription_default;
	//
	private String cshortdescription_default;
	//
	private String ckeyword_default;
	//
	private String cmetatitle_default;
	//
	private String cmetakeyword_default;
	//
	private String cmetadescription_default;
	//
	private Boolean bmultiattribute;
	// 父 SKU
	private String cparentsku;
	// 是否可见
	private Boolean bvisible;
	//
	private Boolean bpulish;
	// 创建用户
	private String ccreateuser;
	//
	private Date dcreatedate;
	//
	private Double ffreight;
	//
	private Boolean bmain;
	//
	private Boolean bactivity;

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		if (null == clistingid) {
			clistingid = "";
		}
		this.clistingid = clistingid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		if (null == iwebsiteid) {
			iwebsiteid = 0;
		}
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		if (null == ilanguageid) {
			ilanguageid = 0;
		}
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

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		if (null == istatus) {
			istatus = 0;
		}
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

	public Boolean getBspecial() {
		return bspecial;
	}

	public void setBspecial(Boolean bspecial) {
		if (null == bspecial) {
			bspecial = false;
		}
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

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		if (null == iqty) {
			iqty = 0;
		}
		this.iqty = iqty;
	}

	public Double getFprice() {
		return fprice;
	}

	public void setFprice(Double fprice) {
		if (null == fprice) {
			fprice = 0d;
		}
		this.fprice = fprice;
	}

	public Double getFcostprice() {
		return fcostprice;
	}

	public void setFcostprice(Double fcostprice) {
		if (null == fcostprice) {
			fcostprice = 0d;
		}
		this.fcostprice = fcostprice;
	}

	public Double getFweight() {
		return fweight;
	}

	public void setFweight(Double fweight) {
		if (null == fweight) {
			fweight = 0d;
		}
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

	public Boolean getBmultiattribute() {
		return bmultiattribute;
	}

	public void setBmultiattribute(Boolean bmultiattribute) {
		if (null == bmultiattribute) {
			bmultiattribute = false;
		}
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

	public Boolean getBvisible() {
		return bvisible;
	}

	public void setBvisible(Boolean bvisible) {
		if (null == bvisible) {
			bvisible = false;
		}
		this.bvisible = bvisible;
	}

	public Boolean getBpulish() {
		return bpulish;
	}

	public void setBpulish(Boolean bpulish) {
		if (null == bpulish) {
			bpulish = false;
		}
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

	public Double getFfreight() {
		return ffreight;
	}

	public void setFfreight(Double ffreight) {
		if (null == ffreight) {
			ffreight = 0d;
		}
		this.ffreight = ffreight;
	}

	public Boolean getBmain() {
		return bmain;
	}

	public void setBmain(Boolean bmain) {
		if (null == bmain) {
			bmain = false;
		}
		this.bmain = bmain;
	}

	public Boolean getBactivity() {
		return bactivity;
	}

	public void setBactivity(Boolean bactivity) {
		if (null == bactivity) {
			bactivity = false;
		}
		this.bactivity = bactivity;
	}
}
