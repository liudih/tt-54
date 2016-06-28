package enums.messaging;

/**
 * 我的消息状态枚举值
 * 
 * @author lijun
 *
 */
public enum Status {
	DELETE("delete", 0), READ("read", 1), EDIT("edit", 2), PUBLISH("publish", 3), unread(
			"unread", 4);

	private String describe;
	private int code;

	private Status(String describe, int code) {
		this.describe = describe;
		this.code = code;
	}

	public String getDescribe() {
		return describe;
	}

	public int getCode() {
		return code;
	}

}
