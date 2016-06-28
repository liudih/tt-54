package extensions.order;

import play.twirl.api.Html;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.ExtraSaveInfo;
import valueobjects.order_api.cart.ExtraLine;
import dto.order.Order;
import facades.cart.Cart;

public interface IOrderExtrasProvider {

	int getDisplayOrder();

	/**
	 * IOrderExtraProvider的ID用来标识此类
	 *
	 * @return
	 */
	String getId();

	/**
	 * 提供输入方式的HTML，插件实现方需要根据extraLine里面的payload决定如何显示内容
	 *
	 * @param extraLine
	 * @return
	 */
	Html renderInput(Cart cart, ExtraLine extraLine);

	/**
	 * 返回该ExtraLine的显示信息，包括费用（负数为扣除）
	 *
	 * @param cart
	 *            币种该从Cart得到
	 * @param line
	 * @return 显示用的ExtrasLineView，当中包括价格、显示内容等信息
	 */
	ExtraLineView extralineView(Cart cart, ExtraLine line);

	/**
	 * 当订单确认的时候所需要做的操作。在 Cart 构建 Order 的时候调用，这个阶段订单 Order 还没保存。
	 * 
	 * @param cart
	 * @param line
	 *
	 * @return
	 * @author luojiaheng
	 */
	ExtraSaveInfo prepareOrderInstance(Cart cart, ExtraLine line);

	/**
	 * 当订单最终付款的操作
	 *
	 * @param line
	 * @author luojiaheng
	 */
	void payOperation(ExtraLine line);

	/**
	 * 订单保存成功后，保存相应的额外优惠的记录
	 *
	 * @return
	 * @author luojiaheng
	 */
	boolean saveOrderExtras(Order order, ExtraSaveInfo info);

	/**
	 * 如果其他Extras保存失败，成功执行过的Extras需要取消上次操作
	 * 
	 * @param order
	 * @param info
	 */
	void undoSaveOrderExtras(Order order, ExtraSaveInfo info);

	/**
	 * 取消订单时的相关操作
	 * 
	 * @param line
	 */
	void cancelledOperation(ExtraLine line);

	/**
	 * 当优惠应用后,绘制订单小计
	 * 
	 * @author lijun
	 * @param cart
	 * @param line
	 * @return
	 */
	Html renderOrderSubtotal(Cart cart, ExtraLine line);

}
