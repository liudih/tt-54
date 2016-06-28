package dto.payment;

import java.io.Serializable;

public class WebPaymentResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private Exception exception;
	private String res;

	public WebPaymentResult(Exception exception) {
		this.exception = exception;
	}

	public WebPaymentResult(String res) {
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

}
