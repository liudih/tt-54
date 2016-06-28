package events.member;

import java.io.Serializable;

import context.WebContext;

public class PrizeEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	final String toemail;

	final String title;

	final String context;

	final WebContext webContext;

	public PrizeEvent(String toemail, String title, String firstName,
			String context, WebContext webContext) {
		super();
		this.toemail = toemail;
		this.title = title;
		this.context = context;
		this.webContext = webContext;
	}

	public String getToemail() {
		return toemail;
	}

	public String getTitle() {
		return title;
	}

	public String getContext() {
		return context;
	}

	public WebContext getWebContext() {
		return webContext;
	}

}
