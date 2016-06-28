package services.order.fragment.renderer;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;

import dto.Currency;
import dto.Storage;
import play.twirl.api.Html;
import services.base.StorageService;
import services.order.ExistingOrderRenderContext;
import services.order.IOrderFragmentRenderer;
import services.order.OrderRenderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.PreparatoryOrderListVO;
import valueobjects.order_api.cart.CartItem;

public class PreparatoryOrderRenderer implements IOrderFragmentRenderer {
	@Inject
	private StorageService storageService;

	@Override
	public Html render(IOrderFragment fragment, OrderRenderContext context) {
		PreparatoryOrderListVO vo = (PreparatoryOrderListVO) fragment;
		Currency cy = context.getComposite().getCurrency();
		List<Storage> storages = storageService.getAllStorages();
		Map<Integer, Storage> storageMap = Maps.uniqueIndex(storages,
				s -> s.getIid());
		List<CartItem> items = context.getComposite().getOrderContext()
				.getCart().getAllItems();
		Map<String, CartItem> itemMap = Maps
				.uniqueIndex(items, i -> i.getCid());
		return views.html.order.preparatory_product.render(vo, cy, storageMap,
				itemMap);
	}

	@Override
	public Html renderExisting(IOrderFragment fragment,
			ExistingOrderRenderContext context) {
		return null;
	}

}
