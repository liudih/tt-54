package context;

import java.io.Serializable;

public class WebCookie implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private  String name;

	private  String value;

	private  Integer maxAge;

	private  String path;

	private  String domain;

	/**
	 * @author lijun
	 */
	public WebCookie() {
		
	}

	public WebCookie(String name, String value, Integer maxAge, String path,
			String domain) {
		super();
		this.name = name;
		this.value = value;
		this.maxAge = maxAge;
		this.path = path;
		this.domain = domain;
	}

	public String name() {
		return name;
	}

	public String value() {
		return value;
	}

	public Integer maxAge() {
		return maxAge;
	}

	public String path() {
		return path;
	}

	public String domain() {
		return domain;
	}

}
