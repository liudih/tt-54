package events.loyalty;

import java.util.List;

import context.WebContext;
import entity.loyalty.Coupon;

/**
 * 赠送优惠券event
 * 
 * @author lijun
 *
 */
public class GiftCouponEvent {
	// 用户激活邮件发优惠券
	public static final String TYPE_SIGNIN = "signin";
	// 用户订阅发优惠券事件
	public static final String TYPE_RSS = "RSS";
	// 优惠券code
	final List<Coupon> codes;
	// 用户email
	final String email;
	// 事件类型(用户激活邮件发优惠券 or 用户订阅发优惠券事件)
	final String eventType;
	// 是否为网站会员
	final Boolean isExist;
	// 语言
	final Integer language;

	final Integer websiteId;

	/**
	 * 
	 * @param codes
	 * @param email
	 * @param eventType
	 *            GiftCouponEvent.TYPE_SIGNIN or GiftCouponEvent.TYPE_RSS
	 */
	public GiftCouponEvent(List<Coupon> codes, String email, String eventType,
			Boolean isExist, Integer language, Integer websiteId) {
		this.codes = codes;
		this.email = email;
		this.eventType = eventType;
		this.isExist = isExist;
		this.language = language;
		this.websiteId = websiteId;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public List<Coupon> getCodes() {
		return codes;
	}

	public String getEmail() {
		return email;
	}

	public Boolean getIsExist() {
		return isExist;
	}

	public Integer getLanguage() {
		return language;
	}

	/**
	 * 该事件是否是账号激活送优惠券事件
	 * 
	 * @return
	 */
	public boolean isSigninType() {
		return GiftCouponEvent.TYPE_SIGNIN.equals(this.eventType);
	}

	/**
	 * 该事件是否是订阅送优惠券事件
	 * 
	 * @return
	 */
	public boolean isRSSType() {
		return GiftCouponEvent.TYPE_RSS.equals(this.eventType);
	}
}
