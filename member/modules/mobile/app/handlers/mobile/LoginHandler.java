package handlers.mobile;

import com.google.common.eventbus.Subscribe;

import events.mobile.LoginEvent;

public class LoginHandler {

	@Subscribe
	public void onLogin(LoginEvent event) {
	}
}
