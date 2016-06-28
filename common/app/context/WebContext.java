package context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * WebContext for representing a call from remote browser similar to Play
 * Context. However Play Context is not serializable and therefore not safe to
 * be passed as a parameter of a remote function call (Hessian, for example)
 * 
 * @author kmtong
 * @see ContextUtils#getWebContext(play.mvc.Http.Context)
 */
public class WebContext implements Serializable {

	private static final long serialVersionUID = 7468989744793554282L;

	String remoteAddress;
	String vhost;
	String ltc;
	String stc;
	Map<String, Serializable> args;
	Map<String, WebCookie> cookies;
	// add by lijun
	String currencyCode;

	/**
	 * @author lijun
	 */
	public WebContext() {

	}

	public WebContext(String remoteAddress, String vhost, String ltc,
			String stc, List<WebCookie> cookies) {
		super();
		this.remoteAddress = remoteAddress;
		this.vhost = vhost;
		this.ltc = ltc;
		this.stc = stc;
		this.args = new HashMap<String, Serializable>();
		this.cookies = Maps.uniqueIndex(cookies, c -> c.name());
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public String getVhost() {
		return vhost;
	}

	public String getLtc() {
		return ltc;
	}

	public String getStc() {
		return stc;
	}

	public Serializable get(String key) {
		return args.get(key);
	}

	public void set(String key, Serializable value) {
		args.put(key, value);
	}

	public WebCookie cookie(String name) {
		return cookies.get(name);
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}
