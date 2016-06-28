package events.mobile;

import valuesobject.mobile.member.result.MobileMember;

public class LoginEvent {

	private int status;
	
	private MobileMember member;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public MobileMember getMember() {
		return member;
	}

	public void setMember(MobileMember member) {
		this.member = member;
	}
	
}
