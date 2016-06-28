package session.redis;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.core.RMap;

import play.Logger;
import play.mvc.Http.Context;
import session.SessionServiceSupport;

public class SessionService extends SessionServiceSupport {

	final static String SESSION_ID_NAME = "SESSION_ID";

	final Redisson redisson;
	final long sessionTimeout;

	public SessionService(Redisson redisson, long sessionTimeout) {
		this.redisson = redisson;
		this.sessionTimeout = sessionTimeout;
	}

	@Override
	protected Serializable doGet(String name, String sessionID) {
		if (sessionID == null) {
			Logger.error("TT_LTC cookie not found, is the CookieTrackingFilter active?");
			return null;
		}
		RMap<String, Serializable> sessionMap = redisson.getMap(sessionID);
		sessionMap.expire(sessionTimeout, TimeUnit.SECONDS);
		Serializable s = sessionMap.get(name);
		Logger.trace("Getting {} from session: {}", name, s);
		return s;
	}

	@Override
	protected void doSet(String name, Serializable obj, String sessionID) {
		if (sessionID == null) {
			Logger.error("TT_LTC cookie not found, is the CookieTrackingFilter active?");
			return;
		}
		RMap<String, Serializable> sessionMap = redisson.getMap(sessionID);
		sessionMap.expire(sessionTimeout, TimeUnit.SECONDS);
		sessionMap.put(name, obj);
	}

	@Override
	protected void doRemove(String name, String sessionID) {
		if (sessionID == null) {
			Logger.error("TT_LTC cookie not found, is the CookieTrackingFilter active?");
			return;
		}
		RMap<String, Serializable> sessionMap = redisson.getMap(sessionID);
		sessionMap.remove(name);
	}

	@Override
	protected void doDestroy(String sessionID) {
		if (sessionID != null) {
			redisson.getMap(sessionID).delete();
		}
	}

}
