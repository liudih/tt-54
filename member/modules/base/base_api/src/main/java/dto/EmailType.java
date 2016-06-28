package dto;

import java.io.Serializable;

public enum EmailType implements Serializable  {
                                                                                
	ACTIVATE("Activate"), 						             // 注册激活           
	WELCOME_REGIST("WelcomeRegist"), 			             // 注册欢迎           
	WELCOME_SUBSCIBE("WelcomeSubscibe"), 		             // 订阅欢迎           
	UPDATE_PASS("UpdatePass"), 					             // 密码修改           
	CHECKOUT_ORDER("CheckoutOrder"), 			             // 下单成功           
	PAYMENT_ORDER("PaymentOrder"),				             // 付款成功           
	NON_PAYMENT("Non-payment"), 				             // 未付款订单提醒        
	WESTERN_UNION("WesternUnion"), 				             // 西联支付           
	SHIPMENTS("Shipments"), 					             // 发货通知           
	CANCEL_ORDER("CancelOrder"),				             // 订单取消           
	SHIPPING_CARD_MARK_DOWN("ShippingCardMarkDown"), 		 // 购物车产品降价
	SHOPPING_CART_DID_NOT_BUY("ShoppingCartDidNotBuy"),      // 加购物车未买
	COMMENTS_INVITE("CommentsInvite"), 						 // 邀请评论
	COMMENTS_SUCCEED("CommentsSucceed"), 				 	 // 评论成功
	POINTS_EXPIRATION("PointsExpiration"),					 // 积分到期
	TICKET("Ticket"),                                        //尚未未定义,今后定义和实现
	Join_Drop_shipping("JoinDropshipping"),					 //注册加入tomtop drop-shipping program
	Activate_Pay_Pass("ActivatePayPass"),					 //支付密码激活	
	Activate_Success("ActivateSuccess"),					 //激活成功
	Join_Drop_shipping_Failure("DropshipFailure");				 //申请加入drop-shipping失败
	
	private String type;

	private EmailType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
