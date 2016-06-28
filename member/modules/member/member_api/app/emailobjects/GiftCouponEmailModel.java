package emailobjects;

import email.model.IEmailModel;

/**
 * 用户订阅产品成功后发送邮件格式实体
 * 
 * @author xiaoch
 *
 */
public class GiftCouponEmailModel extends IEmailModel {

	private static final long serialVersionUID = 1L;
	private String toEmail;
	private String couponCode;

	public GiftCouponEmailModel(String emailType, Integer language) {
		super(emailType, language);
	}

	public GiftCouponEmailModel(String emailType, Integer language,
			String toEmail, String couponCode) {
		super(emailType, language);
		this.toEmail = toEmail;
		this.couponCode = couponCode;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

}
