package services.payment;

import interceptors.CacheResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import services.base.FoundationService;
import services.base.SystemParameterService;
import services.base.utils.StringUtils;
import services.payment.IPaymentService;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import extensions.InjectorInstance;
import extensions.payment.IPaymentHTMLPlugIn;
import extensions.payment.IPaymentProvider;

public class PaymentService implements IPaymentService {

	@Inject
	Set<IPaymentProvider> providers;
	@Inject
	Set<IPaymentHTMLPlugIn> plugIns;
	@Inject
	private SystemParameterService parameterService;
	@Inject
	private FoundationService foundation;

	/* (non-Javadoc)
	 * @see services.order.payment.IPaymentService#getPaymentById(java.lang.String)
	 */
	@Override
	@CacheResult("cache.payment")
	public IPaymentProvider getPaymentById(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		Map<String, IPaymentProvider> map = Maps.uniqueIndex(providers,
				e -> e.id());
		return map.get(id);
	}

	/* (non-Javadoc)
	 * @see services.order.payment.IPaymentService#getMap()
	 */
	@Override
	@CacheResult("cache.payment")
	public Map<String, IPaymentProvider> getMap() {
		return Maps.uniqueIndex(providers, e -> e.id());
	}

	/* (non-Javadoc)
	 * @see services.order.payment.IPaymentService#getHTMLPlugIns(int)
	 */
	@Override
	@CacheResult("cache.payment")
	public List<IPaymentHTMLPlugIn> getHTMLPlugIns(int type) {
		List<IPaymentHTMLPlugIn> list = Lists.newArrayList(Iterables.filter(
				plugIns, e -> e.getType() == type));
		Collections.sort(list,
				(a, b) -> a.getDisplayOrder() - b.getDisplayOrder());
		return list;
	}

	public static IPaymentService getInstance() {
		return InjectorInstance.getInstance(PaymentService.class);
	}

	/* (non-Javadoc)
	 * @see services.order.payment.IPaymentService#filterByOrderTag(java.util.List)
	 */
	@Override
	public List<String> filterByOrderTag(List<String> tags) {
		StringBuffer str = new StringBuffer();
		for (String string : tags) {
			String p = parameterService.getSystemParameter(
					foundation.getSiteID(), foundation.getLanguage(), string
							+ "_payment");
			if (StringUtils.notEmpty(p)) {
				str.append(p + ",");
			}
		}
		if (StringUtils.notEmpty(str.toString())) {
			List<String> payments = Lists.newArrayList(str.toString()
					.split(","));
			payments = Lists.newArrayList(Collections2.filter(payments,
					p -> StringUtils.notEmpty(p)));
			return payments;
		}
		return Lists.newArrayList();
	}
}
