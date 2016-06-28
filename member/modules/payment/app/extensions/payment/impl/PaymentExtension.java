package extensions.payment.impl;

import java.util.Set;

import com.google.common.collect.Sets;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.order.OrderModule;
import extensions.payment.IPaymentExtension;
import extensions.payment.IPaymentProvider;
import extensions.payment.impl.oceanpayment.OceanPaymentBoletoPaymentProvider;
import extensions.payment.impl.oceanpayment.OceanPaymentCreditPaymentProvider;
import extensions.payment.impl.oceanpayment.OceanPaymentJCBPaymentProvider;
import extensions.payment.impl.oceanpayment.OceanPaymentQiwiPaymentProvider;
import extensions.payment.impl.oceanpayment.OceanPaymentYandexPaymentProvider;
import extensions.payment.impl.wiretransfer.WireTransferPaymentProvider;
import extensions.runtime.IApplication;

public class PaymentExtension extends ModuleSupport implements
		IPaymentExtension {

	@Override
	public void registerPaymentProvider(Multibinder<IPaymentProvider> providers) {
		providers.addBinding().to(WireTransferPaymentProvider.class);
		providers.addBinding().to(OceanPaymentYandexPaymentProvider.class);
		providers.addBinding().to(OceanPaymentCreditPaymentProvider.class);
		providers.addBinding().to(OceanPaymentJCBPaymentProvider.class);
		providers.addBinding().to(OceanPaymentQiwiPaymentProvider.class);
		providers.addBinding().to(OceanPaymentBoletoPaymentProvider.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(OrderModule.class);
	}

}