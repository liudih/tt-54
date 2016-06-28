package forms.loyalty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import entity.loyalty.CouponCode;

public class CouponCodeForm extends CouponCode {

	// 页面的其他属性
	// 创建人name
	private String creatorName;

	private String couponruleName;
	
	private Date startDate;

	private Date endDate;
	
	private Integer pageSize;
	
	private Integer pageNum;
	
	private String cordernumber;
	
	private Integer istatus;
	
	
	private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}
	private Date dusedate;

	public String getCordernumber() {
		return cordernumber;
	}

	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}

	public Date getDusedate() {
		return dusedate;
	}

	public void setDusedate(Date dusedate) {
		this.dusedate = dusedate;
	}

	
	public Integer getPageSize() {
		return pageSize != null ? pageSize :15;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum != null ? pageNum : 1;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(String startdateStr) {
		if (!StringUtils.isEmpty(startdateStr)) {
			try {
				this.startDate = formater.parse(startdateStr);
			} catch (ParseException e) {
				if (Logger.isInfoEnabled()) {
					Logger.info("-----------startdate {} formate failed",
							startdateStr, e);
				}
			}
		}
	}
	
	public String getStartdateStr() {
		if (getStartDate() != null) {
			return formater.format(getStartDate());
		} else {
			return null;
		}
	}
	
	public String getEnddateStr() {
		if (getEndDate() != null) {
			return formater.format(getEndDate());
		} else {
			return null;
		}
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(String enddateStr) {
		if (!StringUtils.isEmpty(enddateStr)) {
			try {
				this.endDate = formater.parse(enddateStr);
			} catch (ParseException e) {
				if (Logger.isInfoEnabled()) {
					Logger.info("-----------startdate {} formate failed",
							enddateStr, e);
				}
			}
		}
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCouponruleName() {
		return couponruleName;
	}

	public void setCouponruleName(String couponruleName) {
		this.couponruleName = couponruleName;
	}

}
