package services.price;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import play.Logger;
import services.product.ProductBaseTest;
import valueobjects.price.BundlePrice;
import valueobjects.price.Price;
import valueobjects.price.PriceCalculationContext;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.ProductSpecBuilder;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PriceServiceTest extends ProductBaseTest {

	private static final double DELTA = 0.005;

	@Inject
	PriceService priceService;

	@Test
	public void test() {
		run(() -> {
			// -- single test
			testSingle();
			// -- bundle test
			testBundle();
			// bundle test Mutil
			testBundleMutil();
			// -- discount test
			testDiscount();
			// Strange Discount With Higher Price
			testStrangeDiscountWithHigherPrice();
		});
	}

	private void testSingle() {
		List<IProductSpec> spec = Lists.newArrayList(ProductSpecBuilder.build(
				"5d5abc40-d986-1004-8e0c-b321d9e0c5c1").get());
		Map<IPriceProvider, List<Price>> testPrice = priceService
				.getPriceByProvider(spec, new PriceCalculationContext("USD"));
		assertNotNull(testPrice);
		Map<IPriceProvider, List<Price>> single = Maps.filterValues(testPrice,
				l -> l != null && l.size() > 0);
		List<Price> price = single.values().iterator().next();
		Price p = price.get(0);
		assertEquals(2.49, p.getPrice(), DELTA);
	}

	private void testStrangeDiscountWithHigherPrice() {
		IProductSpec spec = ProductSpecBuilder.build(
				"d4a3c59c-d929-1004-835b-90389054983d").get();

		// discount price is 120, original price is 100
		{ // GBP
			Price testPrice = priceService.getPrice(spec,
					new PriceCalculationContext("GBP"));
			assertNotNull(testPrice);
			assertEquals(100.00 * 0.6632, testPrice.getPrice(), DELTA);
		}

		{ // USD
			Price testPrice = priceService.getPrice(spec,
					new PriceCalculationContext("USD"));
			assertNotNull(testPrice);
			assertEquals(100.00, testPrice.getPrice(), DELTA);
		}

		{ // AUD
			Price testPrice = priceService.getPrice(spec,
					new PriceCalculationContext("AUD"));
			assertNotNull(testPrice);
			assertEquals(100.00 * 1.316, testPrice.getPrice(), DELTA);
		}

		{ // JPY
			Price testPrice = priceService.getPrice(spec,
					new PriceCalculationContext("JPY"));
			assertNotNull(testPrice);
			assertEquals(100.00 * 121.393997, testPrice.getPrice(), DELTA);
		}

		{ // CNY
			Price testPrice = priceService.getPrice(spec,
					new PriceCalculationContext("CNY"));
			assertNotNull(testPrice);
			assertEquals(100.00 * 6.2621, testPrice.getPrice(), DELTA);
		}

		{ // RUB
			Price testPrice = priceService.getPrice(spec,
					new PriceCalculationContext("RUB"));
			assertNotNull(testPrice);
			assertEquals(100.00 * 62.099499, testPrice.getPrice(), DELTA);
		}
	}

	private void testBundle() {
		List<IProductSpec> bundlespec = Lists.newArrayList(ProductSpecBuilder
				.build("ca4432c0-d92f-1004-8e79-5d7e63ca983b")
				.bundleWith("c5b95d98-d92f-1004-8e79-5d7e63ca983b").get());
		Map<IPriceProvider, List<Price>> testBundledPrice = priceService
				.getPriceByProvider(bundlespec, new PriceCalculationContext(
						"USD"));
		assertNotNull(testBundledPrice);
		Map<IPriceProvider, List<Price>> bundle = Maps.filterValues(
				testBundledPrice, l -> l != null && l.size() > 0);
		List<Price> price = bundle.values().iterator().next();
		System.out.println("testBundle ->" + price.get(0).getPrice());
		assertEquals(18.77 + (6.57 * 0.8), price.get(0).getPrice(), DELTA);
	}

	private void testBundleMutil() {
		List<IProductSpec> bundlespec = Lists.newArrayList(ProductSpecBuilder
				.build("ca4432c0-d92f-1004-8e79-5d7e63ca983b")
				.bundleWith("c5b95d98-d92f-1004-8e79-5d7e63ca983b").get());
		Map<IPriceProvider, List<Price>> testBundledPrice = priceService
				.getPriceByProvider(bundlespec, new PriceCalculationContext(
						"USD"));
		assertNotNull(testBundledPrice);
		Map<IPriceProvider, List<Price>> bundle = Maps.filterValues(
				testBundledPrice, l -> l != null && l.size() > 0);
		List<Price> price = bundle.values().iterator().next();
		System.out.println("testBundleMutil op ->" + price.size());
		BundlePrice bprice = (BundlePrice) price.get(0);
		System.out.println("testBundleMutil ->" + price.get(0).getPrice());
		System.out.println("testBundleMutil ->" + bprice.getBreakdown().size());
		assertEquals(18.77 + (6.57 * 0.8), price.get(0).getPrice(), DELTA);
	}

	private void testDiscount() {
		List<IProductSpec> discountspec = Lists.newArrayList(ProductSpecBuilder
				.build("008795a7-d914-1004-874c-d371c9ab96c0").get());
		Map<IPriceProvider, List<Price>> testBundledPrice = priceService
				.getPriceByProvider(discountspec, new PriceCalculationContext(
						"USD"));
		assertNotNull(testBundledPrice);
		Map<IPriceProvider, List<Price>> discount = Maps.filterValues(
				testBundledPrice, l -> l != null && l.size() > 0);
		List<Price> price = discount.values().iterator().next();
		Price p = price.get(0);
		// original price 4.69, discount price 4.00
		assertEquals(4.00, p.getPrice(), DELTA);
		Logger.debug("Valid {} - {}", p.getValidFrom(), p.getValidTo());
		assertNotNull(p.getValidFrom());
		assertNotNull(p.getValidTo());
	}

}
