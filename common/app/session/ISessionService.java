package session;

import java.io.Serializable;

import play.mvc.Http.Context;
import context.WebContext;

public interface ISessionService {

	/**
	 * Set session variable for the given name.
	 *
	 * @param name
	 * @param obj
	 */
	public abstract void set(String name, Serializable obj);

	/**
	 * Set session variable for the given name and the given context
	 *
	 * @param name
	 * @param obj
	 */
	public abstract void set(String name, Serializable obj, Context context);

	/**
	 * Set session variable for the given name and the given web context
	 *
	 * @param name
	 * @param obj
	 */
	public abstract void set(String name, Serializable obj, WebContext context);

	/**
	 * Remove session variable for the given name
	 *
	 * @param name
	 */
	public abstract void remove(String name);

	/**
	 * Remove session variable for the given name and the given context
	 *
	 * @param name
	 * @param context
	 */
	public abstract void remove(String name, Context context);

	/**
	 * Remove session variable for the given name and the given web context
	 *
	 * @param name
	 * @param context
	 */
	public abstract void remove(String name, WebContext context);

	/**
	 * Get session variable for the given name.
	 *
	 * @param name
	 * @return
	 */
	public abstract Serializable get(String name);

	/**
	 * Get session variable for the given name and the given http context
	 *
	 * @param name
	 * @return
	 */
	public abstract Serializable get(String name, Context context);

	/**
	 * Get session variable for the given name and the given web context
	 *
	 * @param name
	 * @return
	 */
	public abstract Serializable get(String name, WebContext context);

	public abstract String getSessionID();

	public abstract String getSessionID(Context context);

	public abstract String getSessionID(WebContext context);

	/**
	 * Destroy the current session of the current Context.
	 */
	public abstract void destroy();

	public abstract void destroy(Context context);

	public abstract void destroy(WebContext context);
}