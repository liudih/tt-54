package forms.mobile.member;


import play.data.validation.Constraints.Required;

/**
 * 用户重置密码表单
 * @author xiaoch
 *
 */
public class NewPasswordForm {
	
	@Required
	String oldPwd;
	
	@Required
	String newPwd;

	public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	

}
