package enums.loyalty.coupon.manager;

import org.apache.commons.lang3.StringUtils;



/**
 * 为coupon rule多选提供枚举
 * 
 * @author xiaoch
 *
 */
public class CouponRuleSelect {
	public static String getTypeName(String typeid) {
		if (!StringUtils.isEmpty(typeid)) {
			for (TimeType b : TimeType.values()) {
				if (b.getTypeid() == typeid) {
					return b.getTypename();
				}
			}
		}
		return null;

	}

	/**
	 * 日期类型选择
	 *
	 */

	public enum TimeType {
		
		// validity:有效时长,date:开始日期，结束日期
		VALIDITY("Validity", "Validity"), DATE("Date", "Date");

		private String typeid;

		private String typename;

		public String getTypeid() {
			return typeid;
		}

		public String getTypename() {
			return typename;
		}

		TimeType(String typeid, String typename) {
			this.typeid = typeid;
			this.typename = typename;
		}
		/**
		 * 通过typeId 获取枚举
		 * @author lijun
		 * @param typeId
		 * @return maybe return null
		 */
		public static TimeType get(String typeId){
			TimeType[] values = TimeType.values();
			for(TimeType t : values){
				if(t.getTypeid().equals(typeId)){
					return t;
				}
			}
			return null;
		}
	}

}
