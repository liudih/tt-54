package session;

import java.io.Serializable;

import play.Logger;
import play.mvc.Http.Context;
import context.WebContext;
import filters.common.CookieTrackingFilter;

public abstract class SessionServiceSupport implements ISessionService {

	@Override
	public Serializable get(String name) {
		return doGet(name, getSessionID(Context.current()));
	}

	@Override
	public Serializable get(String name, Context context) {
		return doGet(name, getSessionID(context));
	}

	@Override
	public Serializable get(String name, WebContext context) {
		return doGet(name, getSessionID(context));
	}

	@Override
	public void set(String name, Serializable obj) {
		doSet(name, obj, getSessionID(Context.current()));
	}

	@Override
	public void set(String name, Serializable obj, Context context) {
		doSet(name, obj, getSessionID(context));
	}

	@Override
	public void set(String name, Serializable obj, WebContext context) {
		doSet(name, obj, getSessionID(context));
	}

	@Override
	public void remove(String name) {
		doRemove(name, getSessionID(Context.current()));
	}

	@Override
	public void remove(String name, Context context) {
		doRemove(name, getSessionID(context));
	}

	@Override
	public void remove(String name, WebContext context) {
		doRemove(name, getSessionID(context));
	}

	@Override
	public void destroy() {
		doDestroy(getSessionID(Context.current()));
	}

	@Override
	public void destroy(Context context) {
		doDestroy(getSessionID(context));
	}

	@Override
	public void destroy(WebContext context) {
		doDestroy(getSessionID(context));
	}

	@Override
	public String getSessionID() {
		return getSessionID(Context.current());
	}

	@Override
	public String getSessionID(Context context) {
		String sessionID = CookieTrackingFilter.getLongTermCookie(context);
		if (sessionID == null) {
			Logger.error("TT_LTC cookie not found, is the CookieTrackingFilter active?");
		}
		return sessionID;
	}

	@Override
	public String getSessionID(WebContext context) {
		String sessionID = context.getLtc();
		if (sessionID == null) {
			Logger.error("TT_LTC cookie not found, is the CookieTrackingFilter active?");
		}
		return sessionID;
	}

	// to be implemented by implementor
	protected abstract Serializable doGet(String name, String sessionID);

	// to be implemented by implementor
	protected abstract void doSet(String name, Serializable obj,
			String sessionID);

	// to be implemented by implementor
	protected abstract void doRemove(String name, String sessionID);

	// to be implemented by implementor
	protected abstract void doDestroy(String sessionID);

}
