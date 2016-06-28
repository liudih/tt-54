package enums.loyalty.coupon.manager;

public class CouponCodeBack {

	/**
	 * 
	 * code状态
	 * 
	 *
	 */
	public enum UseStatus {

		ALLOT(1, "allot", "已分配"), UNALLOT(0, "unallot", "未分配"), LOCK(-1,
				"lock", "锁定");

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
