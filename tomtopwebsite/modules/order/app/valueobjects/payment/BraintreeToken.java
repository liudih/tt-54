package valueobjects.payment;

import java.io.Serializable;

public class BraintreeToken implements Serializable{
	private String token;
	
	private int re; 
	
	private String msg;

	public String getToken() {
		token = token==null ? "" : token;
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMsg() {
		msg = msg==null ? "" : msg;
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getRe() {
		return re;
	}

	public void setRe(int re) {
		this.re = re;
	}
}
