package extensions.tracking;

import java.util.Set;

import mappers.tracking.AffiliateBannerMapper;
import mappers.tracking.AffiliateBaseMapper;
import mappers.tracking.AffiliateIDMapper;
import mappers.tracking.AffiliateInfoMapper;
import mappers.tracking.AffiliateReferrerMapper;
import mappers.tracking.CommissionHistoryMapper;
import mappers.tracking.CommissionOrderMapper;
import mappers.tracking.CommissionStatusLogMapper;
import mappers.tracking.ManagerAffiliateBannerMapper;
import mappers.tracking.VisitLogMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import service.report.fragement.CommissionsFragment;
import service.report.fragement.SalesAmountFragment;
import service.report.fragement.TrafficFragment;
import service.report.fragement.TransactionsFragment;
import service.report.tag.fragement.ClickTotalTagFragment;
import service.report.tag.fragement.CommissionTotalTagFragment;
import service.report.tag.fragement.SalesAmountTotalTagFragment;
import service.report.tag.fragement.TrasactionTotalTagFragment;
import service.report.tag.fragement.UniqueClicksTotalTagFragment;
import service.tracking.AffiliateIDService;
import service.tracking.AffiliateService;
import service.tracking.IAffiliateIDService;
import service.tracking.IAffiliateService;
import service.tracking.IVisitLogService;
import service.tracking.VisitLogService;
import services.base.template.ITemplateFragmentProvider;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import dto.tracking.IAffiliateIDMapperEnquiryDao;
import dto.tracking.impl.AffiliateIDMapperEnquiryDao;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.affiliate.member.menu.CenterMenuProvider;
import extensions.affiliate.member.menu.HomeMenuProvider;
import extensions.affiliate.member.menu.PaymentMenuProvider;
import extensions.affiliate.member.menu.ReportMenuProvider;
import extensions.affiliate.member.menu.SettingMenuProvider;
import extensions.base.BaseModule;
import extensions.base.template.ITemplateExtension;
import extensions.event.IEventExtension;
import extensions.filter.IFilter;
import extensions.filter.IFilterExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.IMemberAccountMenuProvider;
import extensions.member.account.IMemberQuickMenuProvider;
import extensions.order.IOrderSourceExtension;
import extensions.order.IOrderSourceProvider;
import extensions.order.OrderModule;
import extensions.payment.IPaymentHTMLPlugIn;
import extensions.payment.IPaymentHtmlExtension;
import extensions.runtime.IApplication;
import filters.tracking.MarketingUserFilter;
import filters.tracking.VisitLogFilter;

public class TrackingModule extends ModuleSupport implements IFilterExtension,
		IOrderSourceExtension, IPaymentHtmlExtension, MyBatisExtension,
		IMemberAccountExtension, ITemplateExtension, HessianServiceExtension,
		IEventExtension {

	@Override
	public void registerFilter(Multibinder<IFilter> filters) {
		filters.addBinding().to(AffiliateIDTracking.class);
		// filters.addBinding().to(PiwikScriptAddition.class);
		filters.addBinding().to(VisitLogFilter.class);
		filters.addBinding().to(MarketingUserFilter.class);
		filters.addBinding().to(BingTagScriptAddition.class);
	}

	@Override
	public void registerOrderSourceProvider(
			Multibinder<IOrderSourceProvider> plugins) {
		plugins.addBinding().to(AffiliateIDTracking.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {

			@Override
			protected void configure() {
				bind(IVisitLogService.class).to(VisitLogService.class);
				bind(IAffiliateIDTracking.class).to(AffiliateIDTracking.class);
				bind(IAffiliateIDService.class).to(AffiliateIDService.class);
				bind(IAffiliateIDMapperEnquiryDao.class).to(
						AffiliateIDMapperEnquiryDao.class);
				bind(IAffiliateService.class).to(
						AffiliateService.class);
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(BaseModule.class, OrderModule.class);
	}

	@Override
	public void registerPaymentHtmlPlugin(
			Multibinder<IPaymentHTMLPlugIn> providers) {
		// providers.addBinding().to(PiwikScriptAddition.class);
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("tracking", AffiliateIDMapper.class);
		service.addMapperClass("tracking", VisitLogMapper.class);
		service.addMapperClass("tracking", AffiliateBaseMapper.class);
		service.addMapperClass("tracking", AffiliateInfoMapper.class);
		service.addMapperClass("tracking", AffiliateBannerMapper.class);
		service.addMapperClass("tracking", CommissionHistoryMapper.class);
		service.addMapperClass("tracking", CommissionOrderMapper.class);
		service.addMapperClass("tracking", CommissionStatusLogMapper.class);
		service.addMapperClass("tracking", ManagerAffiliateBannerMapper.class);
		service.addMapperClass("tracking", AffiliateReferrerMapper.class);
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountMenuProvider> menuProviders,
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders,
			Multibinder<IMemberQuickMenuProvider> quickMenuProvider) {

		menuProviders.addBinding().to(CenterMenuProvider.class);
		menuProviders.addBinding().to(HomeMenuProvider.class);
		menuProviders.addBinding().to(PaymentMenuProvider.class);
		menuProviders.addBinding().to(ReportMenuProvider.class);
		menuProviders.addBinding().to(SettingMenuProvider.class);

	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(ClickTotalTagFragment.class);
		binder.addBinding().to(CommissionTotalTagFragment.class);
		binder.addBinding().to(SalesAmountTotalTagFragment.class);
		binder.addBinding().to(TrasactionTotalTagFragment.class);
		binder.addBinding().to(UniqueClicksTotalTagFragment.class);
		binder.addBinding().to(SalesAmountFragment.class);
		binder.addBinding().to(TransactionsFragment.class);
		binder.addBinding().to(CommissionsFragment.class);
		binder.addBinding().to(TrafficFragment.class);

	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("tracking_visitlog", IVisitLogService.class,
				VisitLogService.class);
		reg.publishService("tracking_affiliateid", IAffiliateIDTracking.class,
				AffiliateIDTracking.class);
		reg.publishService("tracking_affiliateid_service",
				IAffiliateIDService.class, AffiliateIDService.class);
		reg.publishService("tracking_affiliate",
				IAffiliateService.class, AffiliateService.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		// TODO Auto-generated method stub
		eventBus.register(injector.getInstance(TrakingEventHandler.class));
	}

}
