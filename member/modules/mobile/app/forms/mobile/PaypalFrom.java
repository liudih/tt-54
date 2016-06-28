package forms.mobile;

import java.io.Serializable;

public class PaypalFrom implements Serializable {

	private static final long serialVersionUID = 1L;

	private Exception exception;
	private String res;
	private String oid;

	public PaypalFrom(Exception exception) {
		this.exception = exception;
	}

	public PaypalFrom(String oid) {
		this.oid = oid;
	}

	public PaypalFrom(String res, String oid) {
		this.oid = oid;
		this.res = res;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

}
