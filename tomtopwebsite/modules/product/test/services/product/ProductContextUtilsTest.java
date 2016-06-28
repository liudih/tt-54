package services.product;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Test;

import valueobjects.product.ProductContext;

public class ProductContextUtilsTest extends ProductBaseTest {

	@Inject
	ProductContextUtils utils;

	@Test
	public void test() {
		run(() -> {
			try {
				ProductContext context = utils
						.createProductContext(
								"professional-phone-and-game-consoles-disassembly-tool-8-piece-set-pa1128",
								1, 1, "USD", true);
				assertNotNull(context);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

}
