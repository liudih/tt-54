package services.order.exception;

public class OrderException extends RuntimeException {

	private static final long serialVersionUID = -1872887687338667681L;

	final ExType exceptionType;

	public OrderException(ExType exceptionType) {
		super("Error Type: " + exceptionType);
		this.exceptionType = exceptionType;
	}

	public ExType getExceptionType() {
		return exceptionType;
	}

}
