package emailobjects;

import email.model.IEmailModel;

public class GetPrizeEmailModel extends IEmailModel {
	
	/**
	 * 用户获取活动奖品成功后发送邮件格式实体
	 * 
	 * @author xin
	 *
	 */

	private static final long serialVersionUID = 1L;

	private String title;

	private String firstName;

	private String context;

	public GetPrizeEmailModel(String emailType, Integer language, String title,
			String context, String firstName) {
		super(emailType, language);
		this.title = title;
		this.firstName = firstName;
		this.context = context;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

}
