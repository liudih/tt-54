package valueobjects.loyalty;

import java.io.Serializable;

import dto.order.Order;

public class LoyaltyPrefer implements Serializable {

	/**
	 * 远程调用应用优惠后的返回结果
	 */
	private static final long serialVersionUID = 1L;
	
	// 应用是否成功
	private Boolean isSuccess;
	
	// 返回值状态码
	private Integer statusCode;
	
	// 用户邮箱
	private String email;

	// 优惠的金额,应用成功返回的为负数
	private Double value;

	private String code;
	
	// 优惠类型：coupon,推广码,积分
	private String preferType;

	private String errorMessage;
	
	// 根据自己需要记录额外信息
	private String extra;
	
	// 订单信息
	private Order order;
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPreferType() {
		return preferType;
	}

	public void setPreferType(String preferType) {
		this.preferType = preferType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean isSuccess() {
		return null==isSuccess?false:isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
