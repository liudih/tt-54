package entity.loyalty;

import java.util.Date;
/**
 * 优惠券会员实体类对象
 * @author guozy
 *
 */
public class CouponMember {
	//会员优惠券编号
	private Integer iid;
	//站点
	private Integer iwebsiteid;
	//会员邮箱
	private String cemail;
	//规则编号
	private Integer iruleid;
	//优惠券号码
	private Integer icodeid;
	//类型
	private Integer itype;
	//状态
	private Integer istatus;
	//创建用户id
	private Integer icreator;
	//创建时间
	private Date dcreatedate;
	//修改编号
	private Integer cmodifierid;
	//修改时间
	private Date cmodifydate;
	//父节点id
	private Integer iparentid;
	//优惠券规则名称
	private String cname;
	//订单号
	private String cordernumber;
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public Integer getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	public String getCemail() {
		return cemail;
	}
	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	public Integer getIruleid() {
		return iruleid;
	}
	public void setIruleid(Integer iruleid) {
		this.iruleid = iruleid;
	}
	public Integer getIcodeid() {
		return icodeid;
	}
	public void setIcodeid(Integer icodeid) {
		this.icodeid = icodeid;
	}
	public Integer getItype() {
		return itype;
	}
	public void setItype(Integer itype) {
		this.itype = itype;
	}
	public Integer getIstatus() {
		return istatus;
	}
	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}
	public Integer getIcreator() {
		return icreator;
	}
	public void setIcreator(Integer icreator) {
		this.icreator = icreator;
	}
	public Date getDcreatedate() {
		return dcreatedate;
	}
	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
	public Integer getCmodifierid() {
		return cmodifierid;
	}
	public void setCmodifierid(Integer cmodifierid) {
		this.cmodifierid = cmodifierid;
	}
	public Date getCmodifydate() {
		return cmodifydate;
	}
	public void setCmodifydate(Date cmodifydate) {
		this.cmodifydate = cmodifydate;
	}
	public Integer getIparentid() {
		return iparentid;
	}
	public void setIparentid(Integer iparentid) {
		this.iparentid = iparentid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCordernumber() {
		return cordernumber;
	}
	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}
	
	
}
