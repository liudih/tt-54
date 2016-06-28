package events.mobile;

import play.mvc.Http.Context;

public class VisitEvent {

	private int consumeTime;

	private String requestUri;

	private String remoteAddress;

	private String clientid;

	private int languageid;

	private Context ctx;

	public VisitEvent() {
		super();
	}

	public VisitEvent(int consumeTime, String requestUri, String remoteAddress,
			String clientid, int languageid, Context ctx) {
		super();
		this.consumeTime = consumeTime;
		this.requestUri = requestUri;
		this.remoteAddress = remoteAddress;
		this.clientid = clientid;
		this.languageid = languageid;
		this.ctx = ctx;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public int getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(int consumeTime) {
		this.consumeTime = consumeTime;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public int getLanguageid() {
		return languageid;
	}

	public void setLanguageid(int languageid) {
		this.languageid = languageid;
	}

	public Context getCtx() {
		return ctx;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}
}
