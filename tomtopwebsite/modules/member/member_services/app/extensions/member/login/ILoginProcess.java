package extensions.member.login;

import events.member.LoginEvent;

public interface ILoginProcess {

	String getName();

	int getOrder();

	String execute(LoginEvent event);

}
