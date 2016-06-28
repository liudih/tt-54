package enums.loyalty.coupon.manager;

/**
 * 
 * @author lijun
 *
 */
public enum Status {
	UNUSED(0, "unused", "未使用"), LOCKED(1, "locked", "锁定中"), USED(2, "used",
			"已使用"), SEND(3, "Send", "发布"), EDIT(4, "edit", "编辑"), DELETED(5,
			"deleted", "删除");

	private final int code;
	private final String describeEN;
	private final String describeCN;

	private Status(int code, String describeEN, String describeCN) {
		this.code = code;
		this.describeEN = describeEN;
		this.describeCN = describeCN;
	}

	public int getCode() {
		return code;
	}

	public String getDescribeEN() {
		return describeEN;
	}

	public String getDescribeCN() {
		return describeCN;
	}

	public static Status getStatus(int code) {
		Status[] status = Status.values();
		for (Status cell : status) {
			if (cell.getCode() == code) {
				return cell;
			}
		}
		return null;
	}
}
