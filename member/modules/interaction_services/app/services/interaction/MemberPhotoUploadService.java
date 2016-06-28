package services.interaction;

import java.io.File;

import javax.inject.Inject;

import services.base.utils.FileUtils;
import services.member.login.LoginService;
import session.ISessionService;
import mapper.interaction.InteractionProductMemberPhotosMapper;

public class MemberPhotoUploadService {
	@Inject
	InteractionProductMemberPhotosMapper memberPhotosMapper;
	
	@Inject
	LoginService loginService;
	
	@Inject
	ISessionService sessionService;
	
	final String guid = "TYPEIID";
	
	public void pushWithSession(File file,String contentType){
		String sessionId =loginService.getLoginData().getSessionId();
		String filedId =sessionId;
		String contentTypeId = sessionId + guid;
		sessionService.set(filedId, file);
		sessionService.set(contentTypeId, contentType);
	}
	
	public byte[] getWithSession(){
		String sessionId = loginService.getLoginData().getSessionId();
		File file = (File)sessionService.get(sessionId);
		return FileUtils.toByteArray(file);
	}
	
	public String getContentTypeWithSession(){
		String sessionid = loginService.getLoginData().getSessionId();
		return (String) sessionService.get(sessionid + guid);
	}
	

}
