package extensions.interaction;

import handlers.interaction.CommentAutoAuditHandler;
import handlers.interaction.DailyDealsHandler;
import handlers.interaction.InteractionHandler;
import handlers.interaction.MemberProductViewHandler;
import handlers.interaction.ReviewCountIndexHandler;
import handlers.interaction.TidyCommentIsHelpHandler;

import java.util.List;
import java.util.Set;

import mapper.interaction.DropshipProductMapper;
import mapper.interaction.InteractionCommentMapper;
import mapper.interaction.InteractionProductMemberPhotosMapper;
import mapper.interaction.InteractionProductMemberVideoMapper;
import mapper.interaction.PriceMatchMapper;
import mapper.interaction.ProductBrowseMapper;
import mapper.interaction.ProductCollectMapper;
import mapper.interaction.ProductPostEvaluateMapper;
import mapper.interaction.ProductPostHelpQtyMapper;
import mapper.interaction.ProductPostMapper;
import mapper.interaction.ProductPostTypeMapper;
import mapper.interaction.ReportErrorMapper;
import mapper.interaction.SuperDealMapper;
import mapper.interaction.WholesaleInquiryMapper;
import mapper.interaction.review.InteractionCommentHelpEvaluateMapper;
import mapper.interaction.review.InteractionCommentHelpQtyMapper;
import mapper.label.RecommendLabelMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import org.apache.camel.builder.RouteBuilder;

import services.base.template.ITemplateFragmentProvider;
import services.cart.ICartFragmentPlugin;
import services.cart.SimpleCartFragmentPlugin;
import services.home.fragment.SuperDealsProvider;
import services.interaciton.review.MemberReviewsServiece;
import services.interaciton.review.ProductReviewsService;
import services.interaction.ICollectService;
import services.interaction.IMemberBrowseHistoryService;
import services.interaction.IProductPhotosService;
import services.interaction.ISuperDealService;
import services.interaction.MemberBrowseHistoryService;
import services.interaction.ProductPhotosService;
import services.interaction.collect.CollectService;
import services.interaction.collect.InteractionProductCollectFragmentRenderer;
import services.interaction.dropship.DropshipCollectFragmentRenderer;
import services.interaction.dropship.DropshipService;
import services.interaction.product.post.IProductPostService;
import services.interaction.product.post.ProductPostService;
import services.interaction.review.IMemberReviewsServiece;
import services.interaction.review.IProductReviewsService;
import services.interaction.superdeal.SuperDealService;
import services.label.IRecommendLabelService;
import services.label.RecommendLabelService;
import services.order.dropShipping.IDropshipService;
import services.product.IProductAttrIconProvider;
import services.product.IProductAttrPartProvider;
import services.product.IProductBadgePartProvider;
import services.product.IProductFragmentPlugin;
import services.product.SimpleProductFragmentPlugin;
import services.product.fragment.InteractionCommentFragmentProvider;
import services.product.fragment.InteractionCopyCommentFragmentProvider;
import services.product.fragment.InteractionProductMemberPhotoProvider;
import services.product.fragment.InteractionProductMemberVideoFragmentProvider;
import services.product.fragment.ProductPostFragmentProvider;
import services.product.fragment.renderer.InteractionCommentFragmentRenderer;
import services.product.fragment.renderer.InteractionCommentReviewCountFragmentRenderer;
import services.product.fragment.renderer.InteractionPriceMatchFragmentRenderer;
import services.product.fragment.renderer.InteractionProductMemberPhotoRenderer;
import services.product.fragment.renderer.InteractionProductMemberVideoFragmentRenderer;
import services.product.fragment.renderer.InteractionReportErrorFragmentRenderer;
import services.product.fragment.renderer.InteractionWholesaleInquiryFragmentRenderer;
import services.product.fragment.renderer.ProductPostFragmentRenderer;
import valueobjects.search.ISearchQueryParser;
import valueobjects.search.ISearchUIProvider;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import dao.interaction.IBrowseEnquiryDao;
import dao.interaction.IDropshipProductDao;
import dao.interaction.IReviewDao;
import dao.interaction.ISuperDealDao;
import dao.interaction.impl.BrowseEnquiryDao;
import dao.interaction.impl.DropshipProductDao;
import dao.interaction.impl.ReviewDao;
import dao.interaction.impl.SuperDealDao;
import dao.product.IProductRecommendEnquiryDao;
import dao.product.impl.ProductRecommendEnquiryDao;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.template.ITemplateExtension;
import extensions.camel.ICamelExtension;
import extensions.cart.ICartFragmentExtension;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.interaction.camel.CommentAutoAuditTimerTrigger;
import extensions.interaction.camel.DailyDealTimerTriggerRouteBuilder;
import extensions.interaction.camel.TidyIsHelpTimerTrigger;
import extensions.interaction.collect.CollectBoxProvider;
import extensions.interaction.livechat.MemberEnquiryRoleProvider;
import extensions.interaction.member.DropshipProductBatchAddMenuProvider;
import extensions.interaction.member.DropshipProductListMenuProvider;
import extensions.interaction.member.DropshipProductWishlistMenuProvider;
import extensions.interaction.member.MemberInteractionStatistics;
import extensions.interaction.member.MemberProductPostMenuProvider;
import extensions.interaction.member.MemberReviewsFragment;
import extensions.interaction.member.MemberReviewsMenuProvider;
import extensions.interaction.member.MemberWishlistFragment;
import extensions.interaction.member.MemberWishlistMenuProvider;
import extensions.interaction.member.PriceMatchMenuProvider;
import extensions.interaction.member.ReviewsQuickMenuProvider;
import extensions.interaction.member.WishListQuickMenuProvider;
import extensions.interaction.search.InteractionIndexProvider;
import extensions.interaction.search.InteractionSearchQueryParser;
import extensions.interaction.search.ReviewCountUI;
import extensions.interaction.share.IShareExtension;
import extensions.interaction.share.IShareProvider;
import extensions.interaction.share.PinterestShareProvider;
import extensions.interaction.share.TwitterShareProvider;
import extensions.interaction.template.MemberBrowseHistoryTemplateProvider;
import extensions.livechat.role.EnquiryRoleProvider;
import extensions.livechat.role.LiveChatRoleExtension;
import extensions.livechat.role.SupportRoleProvider;
import extensions.member.MemberModule;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.IMemberAccountMenuProvider;
import extensions.member.account.IMemberQuickMenuProvider;
import extensions.order.collect.ICollectExtension;
import extensions.order.collect.ICollectProvider;
import extensions.product.IProductAttrIconFragmentExtension;
import extensions.product.IProductAttrPartFragmentExtension;
import extensions.product.IProductBadgeFragmentExtension;
import extensions.product.IProductCommentExtension;
import extensions.product.IProductCommentProvider;
import extensions.product.IProductFragmentExtension;
import extensions.product.ProductModule;
import extensions.runtime.IApplication;
import extensions.search.ISearchExtension;
import extensions.search.ISearchIndexProvider;

public class InteractionModule extends ModuleSupport implements
		MyBatisExtension, IProductFragmentExtension, IEventExtension,
		ITemplateExtension, IProductBadgeFragmentExtension,
		ICartFragmentExtension, ISearchExtension, IMemberAccountExtension,
		IShareExtension, ICollectExtension, IProductAttrIconFragmentExtension,
		IProductCommentExtension, ICamelExtension, LiveChatRoleExtension,
		HessianServiceExtension, IProductAttrPartFragmentExtension {
	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(ProductModule.class, MemberModule.class);
	}

	@Override
	public void registerShareProvider(Multibinder<IShareProvider> binder) {
		binder.addBinding().to(TwitterShareProvider.class);
		binder.addBinding().to(PinterestShareProvider.class);
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<IShareProvider> shareProviders = Multibinder
				.newSetBinder(binder, IShareProvider.class);
		for (IShareExtension e : filterModules(modules, IShareExtension.class)) {
			e.registerShareProvider(shareProviders);
		}
	}

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(IBrowseEnquiryDao.class).to(BrowseEnquiryDao.class);
				bind(ISuperDealDao.class).to(SuperDealDao.class);
				bind(IReviewDao.class).to(ReviewDao.class);
				bind(IDropshipProductDao.class).to(DropshipProductDao.class);
				bind(IDropshipService.class).to(DropshipService.class);
				bind(ICollectService.class).to(CollectService.class);
				bind(ISuperDealService.class).to(SuperDealService.class);
				bind(IMemberBrowseHistoryService.class).to(
						MemberBrowseHistoryService.class);
				bind(IProductRecommendEnquiryDao.class).to(
						ProductRecommendEnquiryDao.class);
				bind(IProductReviewsService.class).to(
						ProductReviewsService.class);
				bind(IProductPhotosService.class)
						.to(ProductPhotosService.class);
				bind(IRecommendLabelService.class).to(
						RecommendLabelService.class);
			}
		};
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("interaction", ProductPostMapper.class);
		service.addMapperClass("interaction", InteractionCommentMapper.class);
		service.addMapperClass("interaction", ProductPostEvaluateMapper.class);
		service.addMapperClass("interaction", ProductBrowseMapper.class);
		service.addMapperClass("interaction",
				InteractionProductMemberPhotosMapper.class);
		service.addMapperClass("interaction",
				InteractionProductMemberVideoMapper.class);
		service.addMapperClass("interaction", PriceMatchMapper.class);
		service.addMapperClass("interaction", WholesaleInquiryMapper.class);
		service.addMapperClass("interaction", ProductCollectMapper.class);
		service.addMapperClass("interaction", ProductPostTypeMapper.class);
		service.addMapperClass("interaction", ReportErrorMapper.class);
		service.addMapperClass("interaction", ProductPostHelpQtyMapper.class);
		service.addMapperClass("interaction",
				InteractionCommentHelpEvaluateMapper.class);
		service.addMapperClass("interaction",
				InteractionCommentHelpQtyMapper.class);
		service.addMapperClass("interaction", SuperDealMapper.class);
		service.addMapperClass("interaction", DropshipProductMapper.class);
		service.addMapperClass("interaction", RecommendLabelMapper.class);
	}

	@Override
	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("post",
						ProductPostFragmentProvider.class,
						ProductPostFragmentRenderer.class));

		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("comment",
						InteractionCommentFragmentProvider.class,
						InteractionCommentFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("review-count-and-score", null,
						InteractionCommentReviewCountFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("member-photos",
						InteractionProductMemberPhotoProvider.class,
						InteractionProductMemberPhotoRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("member-video",
						InteractionProductMemberVideoFragmentProvider.class,
						InteractionProductMemberVideoFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("wholesale_inquiry", null,
						InteractionWholesaleInquiryFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("price_match", null,
						InteractionPriceMatchFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("report_error", null,
						InteractionReportErrorFragmentRenderer.class));

		/*
		 * plugins.addBinding().toInstance( new
		 * SimpleProductFragmentPlugin("reviews", null,
		 * ProductReviewsFragmentRender.class));
		 */
	}

	@Override
	public void registerCartFragment(Multibinder<ICartFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleCartFragmentPlugin("product_collect", null,
						InteractionProductCollectFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleCartFragmentPlugin("dropship_collect", null,
						DropshipCollectFragmentRenderer.class));
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(MemberBrowseHistoryTemplateProvider.class);
		binder.addBinding()
				.to(services.member.fragment.MemberBrowseHistoryTemplateProvider.class);
		binder.addBinding().to(SuperDealsProvider.class);
		binder.addBinding().to(
				services.member.fragment.SuperDealsProvider.class);

	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(MemberProductViewHandler.class));
		eventBus.register(injector.getInstance(ReviewCountIndexHandler.class));
		eventBus.register(injector.getInstance(InteractionHandler.class));
		eventBus.register(injector.getInstance(DailyDealsHandler.class));
		eventBus.register(injector.getInstance(TidyCommentIsHelpHandler.class));
		eventBus.register(injector.getInstance(CommentAutoAuditHandler.class));
		// eventBus.register(injector.getInstance(SuperDealHandler.class));
	}

	@Override
	public void registerProductBadgePartProvider(
			Multibinder<IProductBadgePartProvider> provider) {
		provider.addBinding().to(RatingReviewProvider.class);
	}

	@Override
	public void registerSearchBehaviours(
			Multibinder<ISearchIndexProvider> indexProviders,
			Multibinder<ISearchQueryParser> queryParsers,
			Multibinder<ISearchUIProvider> uiProviders) {
		indexProviders.addBinding().to(InteractionIndexProvider.class);
		queryParsers.addBinding().to(InteractionSearchQueryParser.class);
		uiProviders.addBinding().to(ReviewCountUI.class);
	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountMenuProvider> menuProviders,
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders,
			Multibinder<IMemberQuickMenuProvider> quickMenuProvider) {
		menuProviders.addBinding().to(MemberWishlistMenuProvider.class);
		menuProviders.addBinding().to(PriceMatchMenuProvider.class);
		menuProviders.addBinding().to(MemberReviewsMenuProvider.class);
		menuProviders.addBinding().to(MemberProductPostMenuProvider.class);
		menuProviders.addBinding()
				.to(DropshipProductBatchAddMenuProvider.class);
		menuProviders.addBinding()
				.to(DropshipProductWishlistMenuProvider.class);
		menuProviders.addBinding().to(DropshipProductListMenuProvider.class);
		fragmentProviders.addBinding().to(MemberWishlistFragment.class);
		fragmentProviders.addBinding().to(MemberInteractionStatistics.class);
		fragmentProviders.addBinding().to(MemberReviewsFragment.class);
		quickMenuProvider.addBinding().to(WishListQuickMenuProvider.class);
		quickMenuProvider.addBinding().to(ReviewsQuickMenuProvider.class);
	}

	@Override
	public void registerCollectProvider(Multibinder<ICollectProvider> binder) {
		binder.addBinding().to(CollectBoxProvider.class);
	}

	@Override
	public void registerProductAttrIconProvider(
			Multibinder<IProductAttrIconProvider> binder) {
		binder.addBinding().to(AttrIconProvider.class);
	}

	@Override
	public void registerProductAttrPartProvider(
			Multibinder<IProductAttrPartProvider> provider) {
		provider.addBinding().to(AttrPartProvider.class);
	}

	@Override
	public void registerProductCommentFragment(
			Multibinder<IProductCommentProvider> plugins) {
		plugins.addBinding().to(InteractionCopyCommentFragmentProvider.class);
	}

	@Override
	public List<RouteBuilder> getRouteBuilders() {
		return Lists.newArrayList(new DailyDealTimerTriggerRouteBuilder(),
				new TidyIsHelpTimerTrigger(),
				new CommentAutoAuditTimerTrigger()
		// new SuperDealTimerTriggerRouteBuilder() //定时任务获取super deal
				);
	}

	@Override
	public void registerRoles(
			Multibinder<EnquiryRoleProvider> enquiryRoleProviders,
			Multibinder<SupportRoleProvider> supportRoleProviders) {
		enquiryRoleProviders.addBinding().to(MemberEnquiryRoleProvider.class);
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("member_reviews", IMemberReviewsServiece.class,
				MemberReviewsServiece.class);
		reg.publishService("prodcut_reviews", IProductReviewsService.class,
				ProductReviewsService.class);
		reg.publishService("collect", ICollectService.class,
				CollectService.class);
		reg.publishService("superdeals", ISuperDealService.class,
				SuperDealService.class);
		reg.publishService("browseHistory_service",
				IMemberBrowseHistoryService.class,
				MemberBrowseHistoryService.class);
		reg.publishService("product_post", IProductPostService.class,
				ProductPostService.class);
		reg.publishService("productPhotosService", IProductPhotosService.class,
				ProductPhotosService.class);
		reg.publishService("recooendLabelService",
				IRecommendLabelService.class, RecommendLabelService.class);
	}

}
