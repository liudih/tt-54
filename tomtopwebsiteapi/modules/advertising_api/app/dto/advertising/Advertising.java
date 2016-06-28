package dto.advertising;

import java.io.Serializable;
import java.util.Date;

public class Advertising implements Serializable {
	private static final long serialVersionUID = 6162211800088509044L;
	private Long iid;

	private String ctitle;

	private String cimageurl;

	private String cdata; // 标识产品唯一，如：listingid，sku，根据data与type组装出image的url

	private Integer itype; // 此item广告属于哪个类型，如果:product，专场

	private Integer ilanguageid; // 语言ID

	private Integer iposition; // 方位，如：1代表right 2代表left

	private String ccreateuser;

	private Date dcreatedate;

	private String clastupdateduser;

	private Date dlastupdateddate;

	private Integer iwebsiteid;

	private String advertisingtypename;

	private String languagename;

	private String positionname;

	private String websitename;

	private String chrefurl;

	private String cbusinessid;

	private Integer iadvertisingid;

	private String cbgimages;
	private String cbgimageurl;
	private String cbgcolor;
	private int iindex;
	private boolean bstatus;
	private boolean bbgimgtile; // 背景图是否平铺

	public String getCbgimages() {
		return cbgimages;
	}

	public void setCbgimages(String cbgimages) {
		this.cbgimages = cbgimages;
	}

	public String getCbgimageurl() {
		return cbgimageurl;
	}

	public void setCbgimageurl(String cbgimageurl) {
		this.cbgimageurl = cbgimageurl;
	}

	public String getCbgcolor() {
		return cbgcolor;
	}

	public void setCbgcolor(String cbgcolor) {
		this.cbgcolor = cbgcolor;
	}

	public int getIindex() {
		return iindex;
	}

	public void setIindex(int iindex) {
		this.iindex = iindex;
	}

	public boolean isBstatus() {
		return bstatus;
	}

	public void setBstatus(boolean bstatus) {
		this.bstatus = bstatus;
	}

	public boolean isBbgimgtile() {
		return bbgimgtile;
	}

	public void setBbgimgtile(boolean bbgimgtile) {
		this.bbgimgtile = bbgimgtile;
	}

	public Integer getIadvertisingid() {
		return iadvertisingid;
	}

	public void setIadvertisingid(Integer iadvertisingid) {
		this.iadvertisingid = iadvertisingid;
	}

	public String getCbusinessid() {
		return cbusinessid;
	}

	public void setCbusinessid(String cbusinessid) {
		this.cbusinessid = cbusinessid;
	}

	public String getChrefurl() {
		return chrefurl;
	}

	public void setChrefurl(String chrefurl) {
		this.chrefurl = chrefurl;
	}

	public String getAdvertisingtypename() {
		return advertisingtypename;
	}

	public void setAdvertisingtypename(String advertisingtypename) {
		this.advertisingtypename = advertisingtypename;
	}

	public String getLanguagename() {
		return languagename;
	}

	public void setLanguagename(String languagename) {
		this.languagename = languagename;
	}

	public String getPositionname() {
		return positionname;
	}

	public void setPositionname(String positionname) {
		this.positionname = positionname;
	}

	public String getWebsitename() {
		return websitename;
	}

	public void setWebsitename(String websitename) {
		this.websitename = websitename;
	}

	public Long getIid() {
		return iid;
	}

	public void setIid(Long iid) {
		this.iid = iid;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCimageurl() {
		return cimageurl;
	}

	public void setCimageurl(String cimageurl) {
		this.cimageurl = cimageurl;
	}

	public String getCdata() {
		return cdata;
	}

	public void setCdata(String cdata) {
		this.cdata = cdata;
	}

	public Integer getItype() {
		return itype;
	}

	public void setItype(Integer itype) {
		this.itype = itype;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public Integer getIposition() {
		return iposition;
	}

	public void setIposition(Integer iposition) {
		this.iposition = iposition;
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

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

}