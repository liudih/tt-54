package services.shipping;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;

import play.Logger;
import services.cart.ICartLifecycleService;
import services.order.IFreightService;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.cart.CartGetRequest;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.order_api.shipping.ShippingMethodRequst;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import common.test.ModuleTest;
import dto.ShippingMethodDetail;
import dto.shipping.ShippingMethod;
import extensions.IModule;
import extensions.order.OrderModule;
import facades.cart.Cart;

public class TestGetFreight extends ModuleTest {
	@Inject
	IShippingMethodService shippingMethodService;
	@Inject
	IFreightService freightService;
	@Inject
	ICartLifecycleService cartLifecycle;

	@Test
	public void validFreight() {
		run(() -> {
			{
				ShippingMethod sm = shippingMethodService
						.getShippingMethodById(613);
			}
			{ // free shipping sku and freeshippingmethod
				Logger.info("------------------------wrong sku and freeshippingmethod");
				validFreightWithListingIdAndShippingMethodId("H0A1_LISTING",
						613, 0.0, 0.001);
			}
			{ // free shipping sku and shippingmethod
				Logger.info("------------------------wrong sku and shippingmethod");
				validFreightWithListingIdAndShippingMethodId("H0A1_LISTING",
						14, 1.0, 0.01);
			}
			{ // shipping sku and freeshippingmethod
				Logger.info("------------------------shipping sku and freeshippingmethod");
				validFreightWithListingIdAndShippingMethodId(
						"98c3ee54-d8c9-1004-8a97-438cca05a146", 613, 0.72, 0.01);
			}
			{ // shipping sku and shippingmethod
				Logger.info("------------------------shipping sku and shippingmethod");
				validFreightWithListingIdAndShippingMethodId(
						"98c3ee54-d8c9-1004-8a97-438cca05a146", 14, 3.06, 0.01);
			}
			{ // validFreight
				Logger.info("validFreight Test Running");
				ShippingMethodDetail smd = shippingMethodService
						.getShippingMethodDetail(14, 1);
				Double freight = freightService.getFinalFreight(smd, 177.0,
						177.0, "USD", 4.3);
				Logger.debug("TestGetFreight validFreight freight: {}", freight);
				assertEquals(3.12, freight.doubleValue(), 0.005);
			}
		});
	}

	private void validFreightWithListingIdAndShippingMethodId(String listingId,
			Integer shippingId, double expected, double delta) {
		CartGetRequest req = new CartGetRequest("luojh@gmail.com",
				"foundation.getLoginContext.getLTC", 1, 1, "USD");
		Cart cart = cartLifecycle.getOrCreateCart(req);
		CartItem cartItem = new SingleCartItem();
		cartItem.setClistingid(listingId);
		cartItem.setIqty(1);
		cart.addItem(cartItem);
		Cart newCart = cartLifecycle.getOrCreateCart(req);
		Double totalWeight = freightService.getTotalWeight(newCart);
		Double shippingWeight = freightService.getTotalWeight(newCart, true);
		Double freight = freightService.getFinalFreight(shippingId,
				totalWeight, shippingWeight, "USD", 1, newCart.getGrandTotal());
		assertEquals(expected, freight.doubleValue(), delta);
	}

	@Test
	public void validShippingMethodSpecial() {
		run(() -> {
			{ // non-special
				Logger.info("valid ShippingMethod Non-Special Test Running");
				String country = "RU";
				Double weight = 177.0;
				Double shippingWeight = weight;
				List<String> listingIds = Lists.newArrayList();
				listingIds.add("cdf33010-74cd-43eb-92e5-9a6b0c5b6147");
				Boolean isSpecial = false;
				ShippingMethodRequst requst = new ShippingMethodRequst(1,
						country, weight, shippingWeight, 1, 4.3, listingIds,
						isSpecial, "USD");
				ShippingMethodInformations smis = shippingMethodService
						.getShippingMethodInformations(requst);
				Set<Integer> ids = Sets.newHashSet(14, 44, 447, 613);
				Set<Integer> results = FluentIterable.from(smis.getList())
						.transform(smi -> smi.getId()).toSet();
				Logger.info("Set<Integer> results ======= {}", results);
				assertEquals(ids, results);
			}
			{
				// special
				Logger.info("valid ShippingMethod Special Test Running");
				String country = "RU";
				Double weight = 177.0;
				Double shippingWeight = weight;
				List<String> listingIds = Lists.newArrayList();
				listingIds.add("cdf33010-74cd-43eb-92e5-9a6b0c5b6147");
				Boolean isSpecial = shippingMethodService.isSpecial(listingIds);
				Logger.info("valid ShippingMethod Special isSpecial: {}",
						isSpecial);
				ShippingMethodRequst requst = new ShippingMethodRequst(1,
						country, weight, shippingWeight, 1, 4.3, listingIds,
						isSpecial, "USD");
				ShippingMethodInformations smis = shippingMethodService
						.getShippingMethodInformations(requst);
				Logger.info("valid ShippingMethod Special results size: {}",
						smis.getList().size());
				assertEquals(0, smis.getList().size());
			}
		});
	}

	@Override
	public String[] mybatisNames() {
		return new String[] { "order", "cart", "product", "member", "base",
				"search", "image" };
	}

	@Override
	public Class<? extends IModule>[] moduleClasses() {
		return new Class[] { OrderModule.class };
	}

}
