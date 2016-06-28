package valueobjects.livechat.status;

public enum RoleType {

	CUSTOMERSERVICE(1), // 客服
	CUSTOMER(2); // 客户

	private int type;

	private RoleType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
