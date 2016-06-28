package com.tomtop.es.entity;

/**
 * 查询过滤条件
 * @author ztiny
 *
 */
public class Filter {
	
	/**属性名称*/
	String propetyName;
	/**属性值*/
	Object propertyValue;
	/**过滤条件*/
	String express;
	/**是否需要聚合,默认不开启*/
	boolean isAgg = false;
	/**是否作为filter条件*/
	boolean isFilter = true;
	
	/**
	 * 构造函数
	 * @param propertyName 属性名称
	 * @param o	属性值
	 * @param express	属性匹配方式
	 */
	public Filter(String propertyName,Object o,String express){
		this.propetyName = propertyName;
		this.propertyValue = o;
		this.express = express;
	}
	
	/**
	 * 聚合构造函数
	 * @param propertyName
	 * @param o
	 * @param express
	 * @param isAgg
	 */
	public Filter(String propertyName,Object o,String express,boolean isAgg){
		this.propetyName = propertyName;
		this.propertyValue = o;
		this.express = express;
		this.isAgg = isAgg;
	}
	
	/**
	 * 聚合、过滤构造函数
	 * @param propertyName
	 * @param o
	 * @param express
	 * @param isAgg
	 * @param isFilter
	 */
	public Filter(String propertyName,Object o,String express,boolean isAgg,boolean isFilter){
		this.propetyName = propertyName;
		this.propertyValue = o;
		this.express = express;
		this.isAgg = isAgg;
		this.isFilter = isFilter;
	}
	
	
	public Filter(){}
	
	public enum character{
		/**must*/
		and {
			public String get() {
				return "&&";
			}
		},
		/**should*/
		or {
			public String get() {
				return "||";
			}
		},
		/**must_not*/
		not {
			public String get() {
				return "!=";
			}
		},
		/**大于*/
		gt {
			public String get() {
				return ">";
			}
		},
		/**小于*/
		lt {
			public String get() {
				return "<";
			}
		},
		/**大于等于*/
		ge {
			public String get() {
				return ">=";
			}
		},
		/**小于等于**/
		le {
			public String get() {
				return "<=";
			}
		},
		eq{
			public String get() {
				return "==";
			}	
		};
		 public abstract String get(); 
	}

	public String getPropetyName() {
		return propetyName;
	}


	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public Object getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(Object propertyValue) {
		this.propertyValue = propertyValue;
	}

	public void setPropetyName(String propetyName) {
		this.propetyName = propetyName;
	}



	public boolean isFilter() {
		return isFilter;
	}

	public void setFilter(boolean isFilter) {
		this.isFilter = isFilter;
	}

	public boolean isAgg() {
		return isAgg;
	}

	public void setAgg(boolean isAgg) {
		this.isAgg = isAgg;
	}
	
	
	
}
