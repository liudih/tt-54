package services.member.login;

import play.libs.F.Promise;

public interface ILoginOther {

	Promise<IUserInfo> getUserInfo(String token);
	
	
	Promise<String> getAccessToken(String code, String returnUrl,
			String state,String appid,String appSecret);
}
