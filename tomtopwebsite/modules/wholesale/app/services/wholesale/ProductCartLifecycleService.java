package services.wholesale;

import extensions.InjectorInstance;
import facades.wholesale.WholeSaleProductCart;

public class ProductCartLifecycleService {
	public WholeSaleProductCart creatWholeSaleProductCart(String email,
			int siteID, int languageID, String ccy) {
		return createCartInstanceWithInjectedMembers(email, siteID, languageID,
				ccy);

	}

	private WholeSaleProductCart createCartInstanceWithInjectedMembers(
			String email, int siteID, int languageID, String ccy) {
		WholeSaleProductCart wholeSaleProductCart = new WholeSaleProductCart(
				email, siteID, languageID, ccy);
		InjectorInstance.getInjector().injectMembers(wholeSaleProductCart);
		return wholeSaleProductCart;
	}

}
