package services.member.forgetpassword;

import java.util.Date;

import context.WebContext;
import dto.member.ForgetPasswdBase;

public interface IForgetPasswdBaseService {

	public abstract ForgetPasswdBase getForgetPasswdBase(String cmemberemail,
			Integer iwebsitid);

	public abstract boolean update(ForgetPasswdBase users);

	public abstract boolean insert(ForgetPasswdBase users);

	public abstract String getEmail(String cid);

	public abstract ForgetPasswdBase getForgetPwdByCode(String email,
			String code, boolean buse);

	public abstract boolean deleteByEmail(String email, WebContext webContext);

	public abstract Integer getCount(String email, Date startDate,
			Date endDate, WebContext webContext);

	public abstract boolean update(boolean buse, String cid);

	/**
	 * 检查token是否失效
	 * 
	 * @author lijun
	 * @param token
	 * @return
	 */
	public boolean isFail(String token);
}