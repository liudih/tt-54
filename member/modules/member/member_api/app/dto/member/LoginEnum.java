package dto.member;

public enum LoginEnum  {
	LoginNot("not login"), ActivitedNot("not activited"), Activited("activited"),Success("success"),RedirectAfterLogin("redirectAfterLogin");

	private String typeName;

	private LoginEnum(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return this.typeName;
	}
}
