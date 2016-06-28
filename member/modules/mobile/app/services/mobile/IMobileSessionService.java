package services.mobile;

import java.io.Serializable;

import play.mvc.Http.Context;
import valuesobject.mobile.member.Session;

public interface IMobileSessionService {

	public void set(String name, Serializable value);

	public void setWithPersist(String name, Serializable value);

	public Serializable get(String name);

	public void remove(String name);

	public Serializable get(String name, String sessionID);

	public Session getSession();

	public Session getSession(Context context);

	public void setSesssion(Session session);

	public void destroy(Context context);

	public Session getAuthSession();

	public boolean setAuth(Session session);
	
	public String getAuthKey();

}
