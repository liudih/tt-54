package extensions.member.login;

import services.member.login.OtherLoginResult;
import context.WebContext;

public interface IThirdPartyLoginService {

	String getName();

	OtherLoginResult login(String code, String state, String reredirectUri,
			String appId, String appSecret, WebContext content);

}
