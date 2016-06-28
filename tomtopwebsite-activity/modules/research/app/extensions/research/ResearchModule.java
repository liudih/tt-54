package extensions.research;

import handlers.research.VoteRecordEventHandler;

import java.util.List;

import service.activity.IPageService;
import service.activity.IVoteRecordService;
import service.activity.impl.PageService;
import service.activity.impl.VoteRecordService;
import services.research.activity.prize.provider.CouponActivityPrize;
import services.research.activity.prize.provider.OrderRefundActivityPrize;
import services.research.activity.prize.provider.PointActivityPrize;
import services.research.activity.prize.provider.VoteActivityPrize;
import services.research.activity.qualification.provider.EnoughOrderMoneyActivityQualificationProvider;
import services.research.activity.qualification.provider.ExpireActivityQualificationProvider;
import services.research.activity.qualification.provider.JoinTimesLimitActivityQualificationProvider;
import services.research.activity.qualification.provider.LoginActivityQualificationProvider;
import services.research.activity.qualification.provider.MaxJoinMemberQualification;
import services.research.activity.qualification.provider.NewMemberActivityQualificationProvider;
import services.research.activity.qualification.provider.OnceADayActivityQualificationProvider;
import services.research.activity.qualification.provider.SingleJoinActivityqualificationProvider;
import services.research.activity.qualification.provider.UnLimitActivityQualificationProvider;
import services.research.activity.qualification.provider.UsingPointActivityQualificationProvider;
import services.research.activity.rule.provider.LimitLotteryActivityRuleProvider;
import services.research.activity.rule.provider.ProbabilityActivityRuleProvider;
import services.research.activity.rule.provider.UnActivityRuleProvider;
import services.research.fragment.provider.ProductItemFragmentProvider;
import services.research.fragment.provider.RatingReviewProvider;
import services.research.fragment.provider.VoteItemFragmentProvider;
import services.research.fragment.renderer.ProductItemFragmentRenderer;
import services.research.fragment.renderer.VoteItemFragmentRenderer;
import services.research.vote.IVoteFragmentPlugin;
import services.research.vote.SimpleVoteFragmentPlugin;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.activity.IActivityPrizeProvider;
import extensions.activity.IActivityQualificationProvider;
import extensions.activity.IActivityRuleProvider;
import extensions.activity.IPrizeExtension;
import extensions.activity.IQualificationExtension;
import extensions.activity.IRuleExtension;
import extensions.event.IEventExtension;
import extensions.runtime.IApplication;

public class ResearchModule extends ModuleSupport implements IEventExtension,
		IVoteFragmentExtension, IProductBadgeFragmentExtension, IRuleExtension,
		IPrizeExtension, IQualificationExtension {

	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Set<Class<? extends IModule>> getDependentModules() {
	 * 
	 * }
	 */

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {

			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		binder.bind(IPageService.class).to(PageService.class);
		final Multibinder<IVoteFragmentPlugin> vfp = Multibinder.newSetBinder(
				binder, IVoteFragmentPlugin.class);
		for (IVoteFragmentExtension e : filterModules(modules,
				IVoteFragmentExtension.class)) {
			e.registerVoteFragment(vfp);
		}

		final Multibinder<IProductBadgePartProvider> productpart = Multibinder
				.newSetBinder(binder, IProductBadgePartProvider.class);
		for (IProductBadgeFragmentExtension e : filterModules(modules,
				IProductBadgeFragmentExtension.class)) {
			e.registerProductBadgePartProvider(productpart);
		}
		binder.bind(IVoteRecordService.class).to(VoteRecordService.class);
		binder.bind(HttpRequestFactory.class).toInstance(new NetHttpTransport().createRequestFactory());
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(VoteRecordEventHandler.class));
	}

	@Override
	public void registerActivityQualificationProviders(
			Multibinder<IActivityQualificationProvider> binder) {
		binder.addBinding().to(LoginActivityQualificationProvider.class);
		binder.addBinding().to(SingleJoinActivityqualificationProvider.class);
		binder.addBinding().to(ExpireActivityQualificationProvider.class);
		binder.addBinding().to(OnceADayActivityQualificationProvider.class);
		binder.addBinding().to(UnLimitActivityQualificationProvider.class);
		binder.addBinding().to(UsingPointActivityQualificationProvider.class);
		binder.addBinding().to(NewMemberActivityQualificationProvider.class);
		binder.addBinding().to(
				EnoughOrderMoneyActivityQualificationProvider.class);
		binder.addBinding().to(
				JoinTimesLimitActivityQualificationProvider.class);
		binder.addBinding().to(
				MaxJoinMemberQualification.class);
	}

	@Override
	public void registerVoteFragment(Multibinder<IVoteFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleVoteFragmentPlugin("vote-item",
						VoteItemFragmentProvider.class,
						VoteItemFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleVoteFragmentPlugin("product-item",
						ProductItemFragmentProvider.class,
						ProductItemFragmentRenderer.class));
	}

	@Override
	public void registerProductBadgePartProvider(
			Multibinder<IProductBadgePartProvider> provider) {
		provider.addBinding().to(RatingReviewProvider.class);
		// provider.addBinding().to(ShowStarReviewProvider.class);
	}

	@Override
	public void registerActivityPrizeProviders(
			Multibinder<IActivityPrizeProvider> binder) {
		binder.addBinding().to(VoteActivityPrize.class);
		binder.addBinding().to(PointActivityPrize.class);
		binder.addBinding().to(CouponActivityPrize.class);
		binder.addBinding().to(OrderRefundActivityPrize.class);
	}

	@Override
	public void registerActivityRuleProviders(
			Multibinder<IActivityRuleProvider> binder) {
		binder.addBinding().to(UnActivityRuleProvider.class);
		binder.addBinding().to(ProbabilityActivityRuleProvider.class);
		binder.addBinding().to(LimitLotteryActivityRuleProvider.class);
	}

}
