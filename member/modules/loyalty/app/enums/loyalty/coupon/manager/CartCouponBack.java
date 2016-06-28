package enums.loyalty.coupon.manager;

public class CartCouponBack {

	/**
	 * 
	 * cartcoupon表中usestatus状态
	 * 
	 *
	 */
	public enum UseStatus {

		UNUSED(0, "unused", "未使用"), LOCKED(1, "locked", "锁定中"), USED(2, "used",
				"已使用");

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

		UseStatus(Integer statusid, String statusEN, String statusCn) {
			this.statusid = statusid;
			this.statusEN = statusEN;
			this.statusCN = statusCn;
		}
	}
}
