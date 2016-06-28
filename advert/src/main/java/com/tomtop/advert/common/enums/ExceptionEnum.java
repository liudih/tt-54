package com.tomtop.advert.common.enums;

import java.util.HashMap;
import java.util.Map;

/** 
 *************************************************************** 
 * <p>
 * @DESCRIPTION :   异常枚举值供商品系统统一调用 
 * @AUTHOR : 文龙       13715116671
 * @DATE :2015-11-7 上午11:15:33
 * </p>
 **************************************************************** 
 */
public class ExceptionEnum {
	
	/**
	 * PARAMETER_OBJECT = "入参对象为null"
	 */
	public static final String PARAMETER_OBJECT = "入参对象为null";
	
	/**
	 * PARAMETER_VALUE = "入参对象的属性为null"
	 */
	public static final String PARAMETER_VALUE = "入参对象的属性为null";
	
	/**
	 * PARA_VALUE = "入参不合法"
	 */
	public static final String PARA_VALUE = "入参不合法";
	
	/**
	 * ERROE_MSG_SYSTEM = "系统错误"
	 */
	public static final String ERROE_MSG_SYSTEM = "系统错误";
	
	/**
	 * 异常类型:参数异常
	 */
	public static final String ERROE_CODE_PARAMETER = "100";
	
	/**
	 * 异常类型:业务异常
	 */
	public static final String ERROE_CODE_BUSINESS = "200";
	
	/**
	 * 异常类型:系统异常
	 */
	public static final String ERROE_CODE_SYSTEM = "300";
	
	/**
	 * 业务异常 
     * 201：
	 */
    public enum BusExcep {
    	
        OrderNotExist("201","订单不存在"),
        OrderNotAllowCancel("202","当前状态不允许取消订单"),
        NoOrderStatusToInvalid("203","当前订单状态已经为无效状态，无需进行订单转无效操作"),
        NoOrderStatusToEffectively("204","当前订单状态不为无效状态，无需进行订单转有效操作"),
        FindOrderDeliverAddrFromUserFail("205","从用户中心获取收货地址信息失败"),
        GetUserInfoFromUserFail("206","从用户中心获取用户信息失败"),
        GetProductsListByProductsCodeFail("207","从商品中心获取商品信息详情列表失败"),
        ProductsAvailableStockLack("208","商品可定量库存不足"),
        ListOrderProductsDetailFail("209","获取商品信息详情列表失败");
        
        private final String key;
        private final String value;
        
        private BusExcep(String key, String value) {
            this.key = key;
            this.value = value;
        }
        
        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
         
        public static Map<String, String> getList() {
            Map<String, String> map = new HashMap<String, String>();
            for (BusExcep e : BusExcep.values()) {
                map.put(String.valueOf(e.getKey()), e.getValue());
            }
            return map;
        }
    }
	
}
