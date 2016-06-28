package services.mobile;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.core.RMap;

import play.Logger;
import play.mvc.Http.Context;
import valueobjects.base.LoginContext;
import valuesobject.mobile.member.MobileContext;
import valuesobject.mobile.member.MobileLoginContext;
import valuesobject.mobile.member.Session;
import base.util.md5.MD5;

public class MobileSessionService implements IMobileSessionService {

	final Redisson redisson;

	final long timeout;

	public MobileSessionService(Redisson redisson, long timeout) {
		this.redisson = redisson;
		this.timeout = timeout;
	}

	@Override
	public void set(String name, Serializable value) {
		this.doSet(name, value, getSessionID());
	}

	@Override
	public void setWithPersist(String name, Serializable value) {
		this.doSetWithPersist(name, value, getSessionID());
	}

	@Override
	public Serializable get(String name) {
		return this.doGet(name, getSessionID());
	}

	@Override
	public void remove(String name) {
		this.doRemove(name, getSessionID());
	}

	protected void doRemove(String name, String sessionID) {
		if (sessionID == null) {
			Logger.warn("Session ID not found !");
			return;
		}
		redisson.getMap(sessionID).removeAsync(name);
	}

	protected void doSet(String name, Serializable value, String sessionID) {
		if (sessionID == null) {
			Logger.warn("Session ID not found !");
			return;
		}
		RMap<String, Serializable> map = redisson.getMap(sessionID);
		map.expire(timeout, TimeUnit.MINUTES);
		map.put(name, value);
	}

	protected void doSetWithPersist(String name, Serializable value,
			String sessionID) {
		if (sessionID == null) {
			Logger.warn("Session ID not found !");
			return;
		}
		RMap<String, Serializable> map = redisson.getMap(sessionID);
		map.expire(30, TimeUnit.DAYS);
		map.put(name, value);
	}

	protected Serializable doGet(String name, String sessionID) {
		if (sessionID == null) {
			Logger.warn("Session ID not found !");
			return null;
		}
		RMap<String, Serializable> map = redisson.getMap(sessionID);
		map.expire(timeout, TimeUnit.MINUTES);
		Serializable s = map.get(name);
		return s;
	}

	public String getSessionID() {
		return getSessionID(Context.current());
	}

	public String getSessionID(Context context) {
		String sessionID = context.request().getHeader("token");
		if (StringUtils.isBlank(sessionID)) {
			sessionID = context.request().getQueryString("token");
			if (StringUtils.isBlank(sessionID)) {
				sessionID = context.request().getQueryString("state");
				if (StringUtils.isBlank(sessionID)) {
					Logger.warn("mobile client token not found.");
				}
			}
		}
		return sessionID;
	}

	@Override
	public Serializable get(String name, String sessionID) {
		return doGet(name, sessionID);
	}

	@Override
	public Session getSession() {
		Session session = (Session) this.get(Session.M_SESSION_NAME);
		return session;
	}

	@Override
	public void setSesssion(Session session) {
		this.doSet(Session.M_SESSION_NAME, session, session.getSessionID());
	}

	@Override
	public void destroy(Context context) {
		String sessionID = getSessionID(context);
		if (sessionID != null) {
			redisson.getMap(sessionID).delete();
		}
	}

	@Override
	public Session getSession(Context context) {
		String sessionID = getSessionID(context);
		return (Session) this.get(Session.M_SESSION_NAME, sessionID);
	}

	@Override
	public LoginContext getLoginContext() {
		return (LoginContext) this.get(Session.LOGINT_CONTEXT, getAuthKey());
	}

	@Override
	public boolean setLoginContext(LoginContext lc) {
		String authKey = getAuthKey();
		if (StringUtils.isNotBlank(authKey)) {
			this.doSetWithPersist(Session.LOGINT_CONTEXT, lc, authKey);
			return true;
		}
		return false;
	}

	@Override
	public String getAuthKey() {
		Session session = (Session) this.get(Session.M_SESSION_NAME);
		if (session == null) {
			return "";
		}
		MobileContext mcontext = session.getMobileContext();
		if (mcontext != null && StringUtils.isNotBlank(mcontext.getCimei())
				&& StringUtils.isNotBlank(mcontext.getUuid())) {
			String imei = mcontext.getCimei().substring(0, 4);
			String uuid = mcontext.getUuid().substring(
					mcontext.getUuid().length() - 4,
					mcontext.getUuid().length());
			StringBuffer reuuid = new StringBuffer(uuid);
			String authStr = MD5.md5(MD5.md5(imei + "$TOMTOP_APP$")
					+ reuuid.reverse());
			return authStr;
		}
		return "";
	}

	@Override
	public MobileLoginContext getMLoginContext() {
		return (MobileLoginContext) this.get(Session.M_LOGIN_CONTEXT,
				getAuthKey());
	}

	@Override
	public boolean setMLoginContext(MobileLoginContext lc) {
		String authKey = getAuthKey();
		if (StringUtils.isNotBlank(authKey)) {
			this.doSet(Session.M_LOGIN_CONTEXT, lc, authKey);
			return true;
		}
		return false;
	}
}
