package valueobjects.member;

import java.io.Serializable;

public class MemberRegistrationResult implements Serializable {

	private static final long serialVersionUID = 1L;

	final MemberRegistration registration;
	final boolean success;
	final ResultType type;
	final Exception error;

	public MemberRegistrationResult(MemberRegistration reg, boolean success,
			ResultType type, Exception error) {
		this.registration = reg;
		this.success = success;
		this.type = type;
		this.error = error;
	}

	public MemberRegistration getRegistration() {
		return registration;
	}

	public boolean isSuccess() {
		return success;
	}

	public ResultType getType() {
		return type;
	}

	public Exception getError() {
		return error;
	}

}
