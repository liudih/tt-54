package services.shipping;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import services.base.CurrencyService;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import extensions.order.shipping.IFreightPlugin;

/**
 * 如果存在免费，且订单中商品全为免邮，总价超过 30 USD，则免邮的物流费用为0
 * 
 * @author luoJH
 *
 */
public class PTFreeShipping implements IFreightPlugin {
	@Inject
	CurrencyService currencyService;
	@Inject
	ShippingMethodService shippingMethodService;

	@Override
	public List<ShippingMethodInformation> processing(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst) {
		double usdTotal = currencyService.exchange(requst.getGrandTotal(),
				requst.getCurrency(), "USD");
		
		boolean isAllfree = shippingMethodService.checkIsAllfree(requst.getListingIds());
		List<String> pcodelist = shippingMethodService.getPcodeList();
		
		for (ShippingMethodInformation sm : list) {
			
//			System.out.println("isAllfree2=="+isAllfree+",freeShippingCode2.contains(e.getCcode())"+pcodelist.contains(sm.getCode()));
//			System.out.println("code=="+sm.getCode()+",e.getBistracking()===+e.getBistracking()");
			if((isAllfree && pcodelist.contains(sm.getCode()) && requst.getShippingWeight()==0 ) && 
					(!sm.isBistracking() || (sm.isBistracking() && usdTotal>=30)) ){
				sm.setFreight(0.0);
			}
		}
		return list;
	}

	@Override
	public int order() {
		return 100;
	}

}
