package services.order;

import java.util.Set;

import javax.inject.Inject;

import services.base.utils.StringUtils;
import extensions.InjectorInstance;
import extensions.order.DefaultOrderDetailProvider;
import extensions.order.IOrderDetailProvider;

public class OrderDetailProviderService {
	@Inject
	private Set<IOrderDetailProvider> providers;

	public IOrderDetailProvider getProvider(String id) {
		IOrderDetailProvider provider = null;
		if (StringUtils.isEmpty(id)) {
			provider = InjectorInstance
					.getInstance(DefaultOrderDetailProvider.class);
		} else {
			for (IOrderDetailProvider iOrderDetailProvider : providers) {
				if (iOrderDetailProvider.getId().equals(id)) {
					provider = iOrderDetailProvider;
				}
			}
		}
		if (provider == null) {
			provider = InjectorInstance
					.getInstance(DefaultOrderDetailProvider.class);
		}
		return provider;
	}

	public Set<IOrderDetailProvider> getProviders() {
		return providers;
	}
}
