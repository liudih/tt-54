package services.manager;

import play.Logger;
import mapper.manager.AdminUserMapper;
import session.ISessionService;

import com.google.inject.Inject;

import entity.manager.AdminUser;

public class LoginService {
	
	final static String ADMIN_SESSION_NAME = "ADMIN_LOGIN_CONTEXT";
	@Inject
	AdminUserMapper adminUserMapper;
	
	@Inject
	ISessionService sessionService;
	
	public boolean login(String username,String passwd)
	{
		AdminUser user = adminUserMapper.getAdminUser(username, passwd);
		if(user!=null)
		{
			sessionService.set(ADMIN_SESSION_NAME, user);
			return true;
		}
		return false;
	}


	public boolean publicLogin(String cjobnumber)
	{
		AdminUser user=(AdminUser) sessionService.get(ADMIN_SESSION_NAME);
		if(user!=null){
			Logger.debug("publicLogin exist cjobnumber:"+cjobnumber);
			return true;
		}else{
			
			AdminUser adminByJobnumber = adminUserMapper.getAdminByJobnumber(cjobnumber);
			if(adminByJobnumber!=null)
			{
				sessionService.set(ADMIN_SESSION_NAME, adminByJobnumber);
				return true;
			}
		}
		return false;
	}
	
}
