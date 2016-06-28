package valueobjects.order_api;

import java.io.Serializable;


public class ExtraLineView implements Serializable {
	private static final long serialVersionUID = 1L;
	final Object payLoad;
	final String type;
	final Double money;
	final String message;

	public ExtraLineView(String message, Object payLoad, String type,
			Double money) {
		this.message = message;
		this.payLoad = payLoad;
		this.type = type;
		this.money = money;
	}

	public Object getPayLoad() {
		return payLoad;
	}

	public String getType() {
		return type;
	}

	public Double getMoney() {
		return money;
	}

	public String getMessage() {
		return message;
	}

}
