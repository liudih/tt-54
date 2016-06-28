package services.order;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import services.base.CurrencyService;
import services.base.FoundationService;
import valueobjects.order_api.ExistingOrderComposite;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.OrderComposite;
import valueobjects.order_api.OrderContext;
import dto.Currency;

public class OrderCompositeEnquiry {

	@Inject
	Set<IOrderFragmentPlugin> fragmentPlugins;

	@Inject
	CurrencyService currencyService;

	@Inject
	FoundationService foundationService;

	public OrderComposite getOrderComposite(OrderContext context) {
		Currency currency = currencyService.getCurrencyByCode(foundationService
				.getCurrency());
		OrderComposite composite = new OrderComposite(context, currency);
		for (IOrderFragmentPlugin fp : fragmentPlugins) {
			IOrderFragmentProvider provider = fp.getFragmentProvider();
			if (provider != null) {
				composite.put(fp.getName(), provider.getFragment(context));
			}
		}
		return composite;
	}
	
	/**
	 * @author lijun
	 * @param context
	 * @param pluginNames
	 * @return
	 */
	public OrderComposite getOrderComposite(OrderContext context,List<String> pluginNames) {
		Currency currency = currencyService.getCurrencyByCode(foundationService
				.getCurrency());
		OrderComposite composite = new OrderComposite(context, currency);
		for (IOrderFragmentPlugin fp : fragmentPlugins) {
			IOrderFragmentProvider provider = fp.getFragmentProvider();
			if (provider != null && pluginNames.contains(fp.getName())) {
				composite.put(fp.getName(), provider.getFragment(context));
			}
		}
		return composite;
	}

	public ExistingOrderComposite getOrderComposite(ExistingOrderContext context) {
		Currency currency = currencyService.getCurrencyByCode(foundationService
				.getCurrency());
		ExistingOrderComposite composite = new ExistingOrderComposite(context,
				currency);
		for (IOrderFragmentPlugin fp : fragmentPlugins) {
			IOrderFragmentProvider provider = fp.getFragmentProvider();
			if (provider != null) {
				composite.put(fp.getName(),
						provider.getExistingFragment(context));
			}
		}
		return composite;
	}

	/**
	 * 绘制具体的某一个插件
	 * 
	 * @author lijun
	 * @param context
	 * @param pluginName
	 *            插件名称
	 * @return
	 */
	public OrderComposite getOrderComposite(OrderContext context,
			String pluginName) {
		Currency currency = currencyService.getCurrencyByCode(foundationService
				.getCurrency());
		OrderComposite composite = new OrderComposite(context, currency);

		List<IOrderFragmentPlugin> plugin = FluentIterable.from(fragmentPlugins).filter(
				p -> p.getName().equals(pluginName)).toList();
		
		if(plugin.size() > 0){
			IOrderFragmentProvider provider = plugin.get(0).getFragmentProvider();
			if (provider != null) {
				composite.put(plugin.get(0).getName(), provider.getFragment(context));
			}
		}
		return composite;
	}

	/**
	 * @author lijun
	 * @param context
	 * @return
	 */
	public ExistingOrderComposite getOrderComposite(ExistingOrderContext context,List<String> pluginNames) {
		String currencyCode = context.getOrder().getCcurrency();
		Currency currency = currencyService.getCurrencyByCode(currencyCode);
		ExistingOrderComposite composite = new ExistingOrderComposite(context,
				currency);
		if(pluginNames == null || pluginNames.size() == 0){
			return composite;
		}
		
		for (IOrderFragmentPlugin fp : fragmentPlugins) {
			IOrderFragmentProvider provider = fp.getFragmentProvider();
			if (pluginNames.contains(fp.getName())) {
				composite.put(fp.getName(),
						provider.getExistingFragment(context));
			}
		}
		return composite;
	}
	
}
