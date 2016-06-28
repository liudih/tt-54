package services.order.fragment.renderer;

import java.util.List;

import org.elasticsearch.common.collect.Lists;

import play.twirl.api.Html;
import services.ICurrencyService;
import services.base.FoundationService;
import services.order.ExistingOrderRenderContext;
import services.order.IOrderFragmentRenderer;
import services.order.OrderRenderContext;
import services.product.EntityMapService;
import services.product.ProductEnquiryService;
import valueobjects.cart.CartItem;
import valueobjects.order_api.Carts;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.OrderItems;

import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;

import dto.Currency;
import dto.order.Order;

public class OrderCartProductRenderer implements IOrderFragmentRenderer {
	@Inject
	ICurrencyService cs;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	FoundationService foundation;

	@Inject
	EntityMapService entityMapService;

	@Override
	public Html render(IOrderFragment fragment, OrderRenderContext context) {
		Currency c = context.getComposite().getCurrency();
		Carts carts = (Carts) fragment;
		//modify by lijun
		List<CartItem> items = carts.getItems();
		if(items == null){
			return views.html.order.cart_product_view.render(carts, c);
		}else{
			return views.html.order.v2.product_list.render(items, c);
		}
		
	}

	@Override
	public Html renderExisting(IOrderFragment fragment,
			ExistingOrderRenderContext context) {
		Order order = context.getComposite().getExistingOrderContext()
				.getOrder();
		Currency c = context.getComposite().getCurrency();
		//modify by lijun
		if(context.isConfirmView()){
			List<OrderItem> itemList = Lists.newLinkedList();
			OrderItems items = (OrderItems) fragment;
			FluentIterable.from(items.getList()).forEach(i -> {
				if(i.getChildList() != null && i.getChildList().size() > 0){
					itemList.addAll(i.getChildList());
				}
				itemList.add(i);
			});
			OrderItems result = new OrderItems(itemList);
			return views.html.order.order_product_confirm_view.render(
					result, order, c);
		}
		return views.html.order.order_product_view.render(
				(OrderItems) fragment, order, c);
	}

}
