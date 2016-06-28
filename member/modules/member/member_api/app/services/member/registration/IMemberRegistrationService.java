package services.member.registration;

import java.util.Map;

import context.WebContext;
import play.libs.F.Promise;
import valueobjects.member.MemberOtherIdentity;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;

public interface IMemberRegistrationService {

	public abstract MemberRegistrationResult register(MemberRegistration reg,
			boolean activated,WebContext context);

	public abstract MemberRegistrationResult register(MemberRegistration reg,
			MemberOtherIdentity otherId,WebContext context);

	public abstract boolean resendActivationEmail(String email);

	/**
	 * Activate and Login if activation is done
	 * 
	 * @param activationCode
	 * @return
	 */
	public abstract boolean activate(String activationCode);

	/**
	 * 注册检测邮箱是否已经注册
	 * 
	 * @param email
	 * @return
	 */
	public abstract boolean getEmail(String email,WebContext context);

	/**
	 * 注册成功激活成功发送邮件
	 * 
	 * @param toemail
	 * @return
	 */

	public abstract Promise<Map<String, Object>> asyncRegSuccess(String toemail);

}