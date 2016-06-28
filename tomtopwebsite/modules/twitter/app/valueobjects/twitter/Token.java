package valueobjects.twitter;

import java.io.Serializable;

/**
 * 由于RequestToken类的构造函数带有参数
 * 但Serializable的规定：不能有带参数的构造函数
 * 所以RequestToken不能存到session里
 * 所以才有这个类的存在
 * @author lijun
 *
 */
public class Token implements Cloneable, Serializable {
	private static final long serialVersionUID = 2584677658314615912L;
	
	private  String requestToken;
	private  String requestTokenSecret;
	
	
	public String getRequestToken() {
		return requestToken;
	}
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
	public String getRequestTokenSecret() {
		return requestTokenSecret;
	}
	public void setRequestTokenSecret(String requestTokenSecret) {
		this.requestTokenSecret = requestTokenSecret;
	}
}
