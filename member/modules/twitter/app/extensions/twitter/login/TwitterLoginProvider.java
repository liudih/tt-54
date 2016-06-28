package extensions.twitter.login;

import javax.inject.Singleton;

import play.Logger;
import play.Play;
import play.twirl.api.Html;
import extensions.member.login.ILoginProvider;

@Singleton
public class TwitterLoginProvider implements ILoginProvider {

	String consumerKey;
	String consumerSecret;
	

	public TwitterLoginProvider() {
		this.consumerKey = Play.application().configuration()
				.getString("twitter.consumerKey");
		this.consumerSecret = Play.application().configuration()
				.getString("twitter.consumerSecret");
		if(Logger.isDebugEnabled()){
			Logger.debug("twitter consumerKey {}", this.consumerKey);
		}
		
	}

	
	public String getConsumerKey() {
		return consumerKey;
	}


	public String getConsumerSecret() {
		return consumerSecret;
	}


	@Override
	public boolean isPureJS() {
		return true;
	}

	@Override
	public Html getLoginButton() {
		return views.html.twitter.login.login_button.render();
	}

	@Override
	public int getDisplayOrder() {
		return 11;
	}

	public String getAppID() {
		return consumerKey;
	}
}
