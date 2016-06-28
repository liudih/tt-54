package enums.loyalty.coupon.manager;

import java.util.ArrayList;
import java.util.List;

import play.Logger;

/**
 * 
 * 为coupon rule后台提供枚举
 * 
 * @author xiaoch
 *
 */
public class CouponRuleBack {
	/**
	 * 
	 * 规则状态
	 * 
	 *
	 */
	public enum Status {

		ON(1, "on", "启用"), OFF(0, "off", "未启用"), DELETE(-1, "delete", "已删除"), CLOSED(
				2, "closed", "已关闭");

		private Integer statusid;

		private String statusEN;

		private String statusCN;

		public Integer getStatusid() {

			return statusid;

		}

		public String getStatusEN() {

			return statusEN;

		}

		public String getStatusCN() {

			return statusCN;

		}

		public void setStatusCN(String statusCN) {

			this.statusCN = statusCN;

		}

		Status(Integer statusid, String statusEN, String statusCn) {

			this.statusid = statusid;

			this.statusEN = statusEN;

			this.statusCN = statusCn;

		}

	}

	/**
	 * 优惠券类型(现金券 or 折扣券)
	 * 
	 * @author lijun
	 *
	 */
	public enum CouponType {
		CASH(1, "cash", "现金"), DISCOUNT(2, "discount", "折扣");

		private Integer code;
		private String describeEN;
		private String describeCN;

		private CouponType(Integer code, String describeEN, String describeCN) {
			this.code = code;
			this.describeEN = describeEN;
			this.describeCN = describeCN;
		}

		/**
		 * 通过code获取枚举值
		 * 
		 * @param code
		 * @return mayba return null
		 */
		public static CouponType get(int code) {
			CouponType[] values = CouponType.values();
			for (CouponType t : values) {
				if (t.getCode() == code) {
					return t;
				}
			}
			return null;
		}

		public Integer getCode() {
			return code;
		}

		public String getDescribeEN() {
			return describeEN;
		}

		public String getDescribeCN() {
			return describeCN;
		}

	}

	/**
	 * 
	 * 使用终端类型
	 * 
	 * @author xiaoch
	 *
	 */
	public enum UseTerminal {

		PC("1", "web"), MOBILE("2", "mobile"), APP("3", "app");

		private String terminalId;
		private String terminalEN;

		public String getTerminalId() {
			return terminalId;
		}

		public String getTerminalEN() {
			return terminalEN;
		}

		UseTerminal(String terminalId, String terminalEN) {
			this.terminalId = terminalId;
			this.terminalEN = terminalEN;
		}

		/**
		 * 获取所有终端类型
		 * 
		 * @return
		 */
		public static List<UseTerminal> getAllTerminal() {
			UseTerminal[] values = UseTerminal.values();
			List<UseTerminal> list = new ArrayList<UseTerminal>(values.length);
			for (int i = 0; i < values.length; i++) {
				list.add(values[i]);
			}
			return list;
		}
		// 通过id获取终端类型
		public static UseTerminal getUseTerminal(String id) {
			Logger.debug("id========"+id);
			UseTerminal[] values = UseTerminal.values();
			for (UseTerminal t : values) {
				if (t.getTerminalId().equals(id)) {
					return t;
				}
			}
			return null;
		}

	}
}
