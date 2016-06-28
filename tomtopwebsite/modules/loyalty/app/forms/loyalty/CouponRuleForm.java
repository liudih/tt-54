package forms.loyalty;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import entity.loyalty.CouponRule;
import play.Logger;
import services.base.utils.StringUtils;

public class CouponRuleForm extends CouponRule {

	// 页面需要的额外字段
	// 创建人名称
	private String creatorName;

	// 币种名称
	private String currencyName;

	// 购物券类型名称
	private String typeName;
	
	// 有效期最短
	private Integer validityMini;
	// 有效期最长
	private Integer validityMax;
	
	public Integer getValidityMini() {
		return validityMini;
	}

	public void setValidityMini(Integer validityMini) {
		this.validityMini = validityMini;
	}

	public Integer getValidityMax() {
		return validityMax;
	}

	public void setValidityMax(Integer validityMax) {
		this.validityMax = validityMax;
	}

	// add by lijun
	private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	public void setStartdateStr(String startdateStr) {
		if (StringUtils.notEmpty(startdateStr)) {
			try {
				setStartdate(formater.parse(startdateStr));
			} catch (ParseException e) {
				if (Logger.isInfoEnabled()) {
					Logger.info("-----------startdate {} formate failed",
							startdateStr, e);
				}
				setStartdate(null);
			}
		}
	}

	public String getStartdateStr() {
		if (getStartdate() != null) {
			return formater.format(getStartdate());
		} else {
			return "";
		}
	}

	public void setEnddateStr(String enddateStr) {
		if (StringUtils.notEmpty(enddateStr)) {
			try {
				setEnddate(formater.parse(enddateStr));
			} catch (ParseException e) {
				if (Logger.isInfoEnabled()) {
					Logger.info("-----------enddate {} formate failed",
							enddateStr, e);
				}
				setEnddate(null);
			}
		}
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getEnddateStr() {
		if (getEnddate() != null) {
			return formater.format(getEnddate());
		} else {
			return "";
		}
	}

}
