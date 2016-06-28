package extensions.payment;

import play.data.DynamicForm;
import play.twirl.api.Html;
import valueobjects.order_api.payment.PaymentContext;

/**
 * 付款方式需要实现的接口
 *
 * @author kmtong
 *
 */
public interface IPaymentProvider {

	/**
	 * 这个付款方式的唯一标识：如 PayPal 这个 id 值可能是 “paypal”
	 *
	 * @return
	 */
	String id();

	/**
	 * 付款方式名字，用于显示
	 *
	 * @return
	 */
	String name();

	/**
	 * 显示顺序
	 *
	 * @return
	 */
	int getDisplayOrder();

	/**
	 * 图片URL
	 *
	 * @return
	 */
	String iconUrl();

	/**
	 * 付款方式的描述字段，用来显示。需要考虑多语种情况，必要时用FoundationService拿到当前语言。
	 * 
	 * @param context
	 *            TODO
	 *
	 * @return
	 */
	Html getDescription(PaymentContext context);

	/**
	 * 标识这个付款方式是否人工处理
	 *
	 * @return
	 * @see #getInstruction()
	 */
	boolean isManualProcess();

	/**
	 * 如果人工处理的付款方式，这里提供一些而外指示，如账号、地址等信息。
	 *
	 * 如果是在线支付的方式， 这个字段暂时不用（但也可以用于跳转前的文字提示）。
	 *
	 * @return
	 */
	Html getInstruction(PaymentContext context);

	/**
	 * 在线支付的方式：生成跳转到支付网站的HTML跳转
	 *
	 * @return
	 */
	Html getDoPaymentHtml(PaymentContext context);

	/**
	 * 是否需要额外信息
	 *
	 * @return
	 */
	boolean isNeedExtraInfo();

	/**
	 * 对表单内容进行校验
	 *
	 * @param df
	 * @return
	 */
	boolean validForm(DynamicForm df);

	/**
	 * 使用区域，通用为GLOBAL，专用其他为国家简写，如US、CN
	 * 
	 * @return
	 */
	String area();
}
