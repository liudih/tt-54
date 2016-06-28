package emailobjects;

import email.model.IEmailModel;

public class IntegralEmailModel extends IEmailModel {

	/**
	 * 活动送积分成功后发送邮件格式实体
	 * 
	 * @author xin
	 *
	 */

	private static final long serialVersionUID = 1L;

	private String toEmail;
	private int integral;
	private String firstname;

	public IntegralEmailModel(String emailType, Integer language,
			String toEmail, int integral, String firstname) {
		super(emailType, language);
		this.toEmail = toEmail;
		this.integral = integral;
		this.firstname = firstname;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

}
