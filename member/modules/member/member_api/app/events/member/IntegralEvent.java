package events.member;

import context.WebContext;

public class IntegralEvent {
	// 积分
	final Integer integral;
	// 用户email
	final String email;

	final WebContext webContext;

	public IntegralEvent(Integer integral, String email, WebContext webContext) {
		super();
		this.integral = integral;
		this.email = email;
		this.webContext = webContext;
	}

	public Integer getIntegral() {
		return integral;
	}

	public String getEmail() {
		return email;
	}

	public WebContext getWebContext() {
		return webContext;
	}

}
