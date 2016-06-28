package controllers.member;

import com.fasterxml.jackson.databind.ObjectMapper;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;

public class GoogleLogin extends Controller{

	String appId;
	String appSecret;
	ObjectMapper objectMapper;
	
	public Result googleLogin(){
		this.appId = Play.application().configuration()
				.getString("google.clientId");
		this.appSecret = Play.application().configuration()
				.getString("google.clientSecret");
		this.objectMapper = new ObjectMapper();
		return ok();
	}
}
