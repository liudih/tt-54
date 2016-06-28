package enums.loyalty.coupon.manager;

/**
 * coupon type
 * 
 * @author lijun
 *
 */
public enum Type {
	ALL_USER(0, "All User"), EDM_MARKETING(1, " EDM Marketing"), AFFILIATE_MARKETING(
			2, " Affiliate Marketing"), SPECIFY_USER(3, "Specifies the user"), REGISTER_MEMBER(
			4, "Register Member"), RSS(5, "RSS"),PROMO_CODE(6,"promo code");

	private final int code;
	private final String describeEN;

	private Type(int code, String describeEN) {
		this.code = code;
		this.describeEN = describeEN;
	}

	public int getCode() {
		return code;
	}

	public String getDescribeEN() {
		return describeEN;
	}

	public static Type getType(int code) {
		Type[] Types = Type.values();
		for (Type cell : Types) {
			if (cell.getCode() == code) {
				return cell;
			}
		}
		return null;
	}

}
