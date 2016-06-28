package dto.mobile;

public class OrderInfo extends OrderBaseInfo {

	/**
	 * 国家
	 */
	private String country;
	/**
	 * 国家SN
	 */
	private String countrysn;
	/**
	 * 省份，州
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 街道
	 */
	private String address;
	/**
	 * 邮编
	 */
	private String postalcode;
	/**
	 * 电话
	 */
	private String telephone;
	/**
	 * 名字
	 */
	private String firstname;
	/**
	 * 中名
	 */
	private String middlename;
	/**
	 * 姓
	 */
	private String lastname;
	/**
	 * 邮费
	 */
	private Double shippingprice;
	/**
	 * 订单基本费用
	 */
	private double ordersubtotal;
	/**
	 * 额外费用
	 */
	private double extra;
	/**
	 * 结算费用
	 */
	private double grandtotal;

}
