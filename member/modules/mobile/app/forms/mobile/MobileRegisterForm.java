package forms.mobile;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class MobileRegisterForm {

	@Required
	@Email
	private String email;

	@Required
	private String pwd;

	@Required
	private Integer post;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Integer getPost() {
		return post;
	}

	public void setPost(Integer post) {
		this.post = post;
	}

}
