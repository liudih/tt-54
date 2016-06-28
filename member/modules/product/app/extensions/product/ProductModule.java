package extensions.product;

import handlers.product.CategoryRecommendHandler;
import handlers.product.HomePageProductShowHandler;
import handlers.product.InternalizeImageHandler;
import handlers.product.InventoryChangeHandler;
import handlers.product.ProductViewCountHandler;

import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import mapper.attribute.AttributeItemMapper;
import mapper.category.CategoryBackgroundImagesMapper;
import mapper.category.CategoryFilterAttributeMapper;
import mapper.category.CategoryProductRecommendMapper;
import mapper.category.FilterAttributeKeyMapper;
import mapper.category.FilterAttributeValueMapper;
import mapper.google.category.AttributeKeyMapper;
import mapper.google.category.AttributeValueMapper;
import mapper.google.category.GoogleAttributeMapper;
import mapper.google.category.GoogleCategoryBaseMapper;
import mapper.google.category.GoogleCategoryDetailMapper;
import mapper.google.category.GoogleCategoryMapMapper;
import mapper.google.category.GoogleCategoryRelationMapper;
import mapper.google.category.GoogleFeedsMapper;
import mapper.product.AttachmentDescMapper;
import mapper.product.AttachmentMapper;
import mapper.product.AttributeMapper;
import mapper.product.CategoryBaseMapper;
import mapper.product.CategoryLabelMapper;
import mapper.product.CategoryLabelNameMapper;
import mapper.product.CategoryLabelTypeMapper;
import mapper.product.CategoryNameMapper;
import mapper.product.CategoryWebsiteMapper;
import mapper.product.DailyDealMapper;
import mapper.product.HomePageShowHistoryMapper;
import mapper.product.MerchantsProductMapper;
import mapper.product.ProductActivityRelationDetailMapper;
import mapper.product.ProductActivityRelationMapper;
import mapper.product.ProductAttachmentMapperMapper;
import mapper.product.ProductBaseMapper;
import mapper.product.ProductBaseTranslateMapper;
import mapper.product.ProductBundleSaleMapper;
import mapper.product.ProductCategoryMapperMapper;
import mapper.product.ProductCategoryRankMapper;
import mapper.product.ProductEntityMapMapper;
import mapper.product.ProductExplainMapper;
import mapper.product.ProductExplainTypeMapper;
import mapper.product.ProductGroupPriceMapper;
import mapper.product.ProductImageMapper;
import mapper.product.ProductInterceptUrlMapper;
import mapper.product.ProductLabelMapper;
import mapper.product.ProductLabelTypeMapper;
import mapper.product.ProductMultiattributeAttributeMapper;
import mapper.product.ProductMultiattributeBaseMapper;
import mapper.product.ProductMultiattributeSkuMapper;
import mapper.product.ProductParentUrlMapper;
import mapper.product.ProductRecommendMapper;
import mapper.product.ProductSalePriceMapper;
import mapper.product.ProductSellingPointsMapper;
import mapper.product.ProductStorageMapMapper;
import mapper.product.ProductTranslateMapper;
import mapper.product.ProductUrlMapper;
import mapper.product.ProductVideoMapper;
import mapper.product.ProductViewCountMapper;
import mapper.product.ThirdPlatformDataMapper;
import mapper.product.inventory.InventoryHistoryMapper;
import mapper.topic.TopicPageMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import org.apache.camel.builder.RouteBuilder;

import services.IStorageService;
import services.base.StorageService;
import services.base.template.ITemplateFragmentProvider;
import services.home.HomePageDataEnquiry;
import services.home.HomePageProductShowHistoryService;
import services.home.fragment.Category;
import services.home.fragment.ClearanceSalesLinkProvider;
import services.home.fragment.ClearanceSalesProvider;
import services.home.fragment.FeaturedItemsProvider;
import services.home.fragment.GadgetDropDownProvider;
import services.home.fragment.HomeAdHeadProvider;
import services.home.fragment.HomeAdRightProvider;
import services.home.fragment.HomeAdTopProvider;
import services.home.fragment.HomeBanner;
import services.home.fragment.HomeDailyDeals;
import services.home.fragment.HotCategoriesProvider;
import services.home.fragment.HotColumnsProvider;
import services.home.fragment.HotEventsProvider;
import services.home.fragment.HotSalesMoreLinkProvider;
import services.home.fragment.HotSalesProvider;
import services.home.fragment.NewArrivalsProvider;
import services.image.IImageEnquiryService;
import services.image.IImageUpdateService;
import services.image.ImageEnquiryService;
import services.image.ImageUpdateService;
import services.price.BasicPriceProvider;
import services.price.BundlePriceProvider;
import services.price.BundleSaleDiscountProvider;
import services.price.IDiscountProvider;
import services.price.IPriceCalculationContextProvider;
import services.price.IPriceProvider;
import services.price.IPriceService;
import services.price.PriceService;
import services.price.SaleDiscountProvider;
import services.product.AttachmentDescService;
import services.product.AttachmentService;
import services.product.CategoryEnquiryService;
import services.product.DailyDealEnquiryService;
import services.product.EntityMapService;
import services.product.IAttachmentDescService;
import services.product.IAttachmentService;
import services.product.ICategoryEnquiryService;
import services.product.IEntityMapService;
import services.product.IHomePageDataEnquiry;
import services.product.IHomePageProductShowHistoryService;
import services.product.IMerchantsService;
import services.product.IProductAttachmentMapperService;
import services.product.IProductAttrIconProvider;
import services.product.IProductAttrPartProvider;
import services.product.IProductBadgePartProvider;
import services.product.IProductBadgeService;
import services.product.IProductBaseEnquiryService;
import services.product.IProductCategoryEnquiryService;
import services.product.IProductEnquiryService;
import services.product.IProductExplainService;
import services.product.IProductFragmentPlugin;
import services.product.IProductInterceptUrlService;
import services.product.IProductLabelServices;
import services.product.IProductLabelTypeService;
import services.product.IProductMessageService;
import services.product.IProductMultiatributeService;
import services.product.IProductRecommendService;
import services.product.IProductSellingPointsService;
import services.product.IProductUpdateService;
import services.product.IProductUrlService;
import services.product.IProductVideoEnquiryService;
import services.product.IThirdPlatformDataEnquiryService;
import services.product.ProductAttachmentMapperService;
import services.product.ProductBadgeService;
import services.product.ProductBaseEnquiryService;
import services.product.ProductCategoryEnquiryService;
import services.product.ProductEnquiryService;
import services.product.ProductExplainService;
import services.product.ProductInterceptUrlService;
import services.product.ProductLabelService;
import services.product.ProductMessageService;
import services.product.ProductMultiatributeService;
import services.product.ProductRecommendService;
import services.product.ProductSellingPointsService;
import services.product.ProductUpdateService;
import services.product.ProductUrlService;
import services.product.ProductVideoEnquiryService;
import services.product.SimpleProductFragmentPlugin;
import services.product.ThirdPlatformDataEnquiryService;
import services.product.fragment.AdvertisingProductDetailFragmentProvider;
import services.product.fragment.HotProductFragment;
import services.product.fragment.ProductBaseFragmentProvider;
import services.product.fragment.ProductBreadcrumbFragmentProvider;
import services.product.fragment.ProductCategoryRankFragmentProvider;
import services.product.fragment.ProductCustomAttributesFragmentProvider;
import services.product.fragment.ProductGroupPriceFragmentProvider;
import services.product.fragment.ProductImagesFragmentProvider;
import services.product.fragment.ProductVideoFragmentProvider;
import services.product.fragment.RecommendationFragmentProvider;
import services.product.fragment.renderer.AdvertisingProductDetailFragmentRenderer;
import services.product.fragment.renderer.ProductBaseFragmentRenderer;
import services.product.fragment.renderer.ProductBreadcrumbFragmentRenderer;
import services.product.fragment.renderer.ProductCategoryRankFragmentRenderer;
import services.product.fragment.renderer.ProductCustomAttributesFragmentRenderer;
import services.product.fragment.renderer.ProductDescriptionRenderer;
import services.product.fragment.renderer.ProductGroupPriceFragmentRenderer;
import services.product.fragment.renderer.ProductImagesFragmentRenderer;
import services.product.fragment.renderer.ProductKeywordsRenderer;
import services.product.fragment.renderer.ProductPolicyFragmentRender;
import services.product.fragment.renderer.ProductRecommendFragmentRenderer;
import services.product.fragment.renderer.ProductShareFragmentRenderer;
import services.product.fragment.renderer.ProductVideoFragmentRenderer;
import services.product.google.feeds.IGoogleCategoryBaseService;
import services.product.google.feeds.IGoogleFeedsBaseService;
import services.product.google.feeds.impl.GoogleCategoryBaseService;
import services.product.google.feeds.impl.GoogleFeedsBaseService;
import services.product.impl.MerchantsService;
import services.product.impl.ProductLabelTypeService;
import services.product.inventory.IInventoryEnquiryDao;
import services.product.inventory.IInventoryEnquiryService;
import services.product.inventory.IInventoryUpdateDao;
import services.product.inventory.InventoryEnquiryDao;
import services.product.inventory.InventoryEnquiryService;
import services.product.inventory.InventoryUpdateDao;
import services.product.variables.ClothingSizeReferenceVariableProvider;
import services.product.variables.ProductAttributeVariableProvider;
import services.product.variables.ProductImagesVariableProvider;
import services.search.IAsyncSearchService;
import services.search.IDailyDealEnquiryService;
import services.search.IKeyWordSuggestService;
import services.search.IOperatingIndex;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.IndexingService;
import services.search.KeywordSuggestService;
import services.search.ProductSearchQueryParser;
import services.search.SearchContextFactory;
import services.search.SearchService;
import services.search.criteria.KeywordSearchCriteria;
import services.shipping.IShippingServices;
import services.shipping.ShippingServices;
import valueobjects.search.ISearchKeyWords;
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

import dao.product.IAttachmentDescEnquiryDao;
import dao.product.IAttachmentDescUpdateDao;
import dao.product.IAttachmentEnquiryDao;
import dao.product.IAttachmentUpdateDao;
import dao.product.IAttributeEnquiryDao;
import dao.product.IDailyDealEnquiryDao;
import dao.product.IDailyDealUpdateDao;
import dao.product.IGoogleCategoryBaseDao;
import dao.product.IHomePageShowHistoryDao;
import dao.product.IInventoryHistoryEnquiryDao;
import dao.product.IInventoryHistoryUpdateDao;
import dao.product.IProductActivityDetailEnquiryDao;
import dao.product.IProductActivityDetailUpdateDao;
import dao.product.IProductActivityRelationEnquiryDao;
import dao.product.IProductActivityRelationUpdateDao;
import dao.product.IProductAttachmentMapperEnquiryDao;
import dao.product.IProductAttachmentMapperUpdateDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductBaseUpdateDao;
import dao.product.IProductBundleSaleEnquiryDao;
import dao.product.IProductBundleSaleUpdateDao;
import dao.product.IProductCategoryEnquiryDao;
import dao.product.IProductExplainEnquiryDao;
import dao.product.IProductExplainTypeEnquiryDao;
import dao.product.IProductExplainUpdateDao;
import dao.product.IProductImageDao;
import dao.product.IProductInterceptUrlEnquiryDao;
import dao.product.IProductInterceptUrlUpdateDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductLabelTypeDao;
import dao.product.IProductLabelUpdateDao;
import dao.product.IProductParentUrlEnquiryDao;
import dao.product.IProductParentUrlUpdateDao;
import dao.product.IProductRecommendEnquiryDao;
import dao.product.IProductRecommendUpdateDao;
import dao.product.IProductSalePriceEnquiryDao;
import dao.product.IProductSalePriceUpdateDao;
import dao.product.IProductSellingPointsEnquiryDao;
import dao.product.IProductSellingPointsUpdateDao;
import dao.product.IProductStorageMapEnquiryDao;
import dao.product.IProductStorageMapUpdateDao;
import dao.product.IProductTranslateEnquiryDao;
import dao.product.IProductTranslateUpdateDao;
import dao.product.IProductUrlEnquiryDao;
import dao.product.IProductUrlUpdateDao;
import dao.product.IProductVideoEnquiryDao;
import dao.product.IProductVideoUpdateDao;
import dao.product.IProductViewCountEnquiryDao;
import dao.product.IProductViewCountUpdateDao;
import dao.product.IThirdPlatformDataEnquiryDao;
import dao.product.IThirdPlatformDataUpdateDao;
import dao.product.impl.AttachmentDescEnquiryDao;
import dao.product.impl.AttachmentDescUpdateDao;
import dao.product.impl.AttachmentEnquiryDao;
import dao.product.impl.AttachmentUpdateDao;
import dao.product.impl.AttributeEnquiryDao;
import dao.product.impl.DailyDealEnquiryDao;
import dao.product.impl.DailyDealUpdateDao;
import dao.product.impl.GoogleCategoryBaseDao;
import dao.product.impl.HomePageShowHistoryDao;
import dao.product.impl.InventoryHistoryEnquiryDao;
import dao.product.impl.InventoryHistoryUpdateDao;
import dao.product.impl.ProductActivityDetailEnquiryDao;
import dao.product.impl.ProductActivityDetailUpdateDao;
import dao.product.impl.ProductActivityRelationEnquiryDao;
import dao.product.impl.ProductActivityRelationUpdateDao;
import dao.product.impl.ProductAttachmentMapperEnquiryDao;
import dao.product.impl.ProductAttachmentMapperUpdateDao;
import dao.product.impl.ProductBaseEnquiryDao;
import dao.product.impl.ProductBaseUpdateDao;
import dao.product.impl.ProductBundleSaleEnquiryDao;
import dao.product.impl.ProductBundleSaleUpdateDao;
import dao.product.impl.ProductCategoryEnquiryDao;
import dao.product.impl.ProductExplainEnquiryDao;
import dao.product.impl.ProductExplainTypeEnquiryDao;
import dao.product.impl.ProductExplainUpdateDao;
import dao.product.impl.ProductImageDao;
import dao.product.impl.ProductInterceptUrlEnquiryDao;
import dao.product.impl.ProductInterceptUrlUpdateDao;
import dao.product.impl.ProductLabelEnquiryDao;
import dao.product.impl.ProductLabelTypeDao;
import dao.product.impl.ProductLabelUpateDao;
import dao.product.impl.ProductParentUrlEnquiryDao;
import dao.product.impl.ProductParentUrlUpdateDao;
import dao.product.impl.ProductRecommendEnquiryDao;
import dao.product.impl.ProductRecommendUpdateDao;
import dao.product.impl.ProductSalePriceEnquiryDao;
import dao.product.impl.ProductSalePriceUpdateDao;
import dao.product.impl.ProductSellingPointsEnquiryDao;
import dao.product.impl.ProductSellingPointsUpdateDao;
import dao.product.impl.ProductStorageMapEnquiryDao;
import dao.product.impl.ProductStorageMapUpdateDao;
import dao.product.impl.ProductTranslateEnquiryDao;
import dao.product.impl.ProductTranslateUpdateDao;
import dao.product.impl.ProductUrlEnquiryDao;
import dao.product.impl.ProductUrlUpdateDao;
import dao.product.impl.ProductVideoEnquiryDao;
import dao.product.impl.ProductVideoUpdateDao;
import dao.product.impl.ProductViewCountEnquiryDao;
import dao.product.impl.ProductViewCountUpdateDao;
import dao.product.impl.ThirdPlatformDataEnquiryDao;
import dao.product.impl.ThirdPlatformDataUpdateDao;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.BaseModule;
import extensions.base.template.ITemplateExtension;
import extensions.camel.ICamelExtension;
import extensions.common.CommonModule;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.image.ImageModule;
import extensions.product.camel.CategoryRecommendTimerTrigger;
import extensions.product.camel.HomePageShowTimerTriggerRouteBuilder;
import extensions.product.camel.NewArrivalTimerTriggerRouteBuilder;
import extensions.product.search.DiscountUI;
import extensions.product.search.FreeShippingUI;
import extensions.product.search.NewArrivalsUI;
import extensions.product.search.PagerUI;
import extensions.product.search.PopularityUI;
import extensions.product.search.PriceRangeUI;
import extensions.product.search.PriceUI;
import extensions.product.search.ProductSaleIndexProvider;
import extensions.product.search.SaleUI;
import extensions.product.subscribe.ISubscribeExtension;
import extensions.product.subscribe.ISubscribeProvider;
import extensions.product.template.SearchBar;
import extensions.runtime.IApplication;
import extensions.search.ISearchExtension;
import extensions.search.ISearchIndexProvider;
import extensions.search.SearchModule;
import extensions.shipping.ShippingModule;

public class ProductModule extends ModuleSupport implements MyBatisExtension,
		ITemplateExtension, IProductFragmentExtension,
		IProductDescriptionVariableExtension, IPriceExtension,
		ISearchExtension, ISubscribeExtension,
		IEventExtension, HessianServiceExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(CommonModule.class, BaseModule.class,
				ImageModule.class, SearchModule.class, ShippingModule.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(IProductBaseEnquiryDao.class).to(
						ProductBaseEnquiryDao.class);
				bind(IProductBaseUpdateDao.class)
						.to(ProductBaseUpdateDao.class);
				bind(IProductLabelUpdateDao.class).to(
						ProductLabelUpateDao.class);
				bind(IProductLabelEnquiryDao.class).to(
						ProductLabelEnquiryDao.class);
				bind(IProductUrlEnquiryDao.class)
						.to(ProductUrlEnquiryDao.class);
				bind(IProductUrlUpdateDao.class).to(ProductUrlUpdateDao.class);
				bind(IProductVideoEnquiryDao.class).to(
						ProductVideoEnquiryDao.class);
				bind(IProductVideoUpdateDao.class).to(
						ProductVideoUpdateDao.class);
				bind(IAttributeEnquiryDao.class).to(AttributeEnquiryDao.class);
				bind(IProductExplainEnquiryDao.class).to(
						ProductExplainEnquiryDao.class);
				bind(IProductViewCountEnquiryDao.class).to(
						ProductViewCountEnquiryDao.class);
				bind(IProductViewCountUpdateDao.class).to(
						ProductViewCountUpdateDao.class);
				bind(IProductTranslateEnquiryDao.class).to(
						ProductTranslateEnquiryDao.class);
				bind(IProductTranslateUpdateDao.class).to(
						ProductTranslateUpdateDao.class);
				bind(IProductBundleSaleUpdateDao.class).to(
						ProductBundleSaleUpdateDao.class);
				bind(IProductStorageMapEnquiryDao.class).to(
						ProductStorageMapEnquiryDao.class);
				bind(IProductStorageMapUpdateDao.class).to(
						ProductStorageMapUpdateDao.class);
				bind(IProductSellingPointsEnquiryDao.class).to(
						ProductSellingPointsEnquiryDao.class);
				bind(IProductSellingPointsUpdateDao.class).to(
						ProductSellingPointsUpdateDao.class);
				bind(IProductSalePriceEnquiryDao.class).to(
						ProductSalePriceEnquiryDao.class);
				bind(IProductSalePriceUpdateDao.class).to(
						ProductSalePriceUpdateDao.class);
				bind(IInventoryEnquiryDao.class).to(InventoryEnquiryDao.class);
				bind(IInventoryUpdateDao.class).to(InventoryUpdateDao.class);
				bind(IProductParentUrlEnquiryDao.class).to(
						ProductParentUrlEnquiryDao.class);
				bind(IProductParentUrlUpdateDao.class).to(
						ProductParentUrlUpdateDao.class);
				bind(IProductExplainEnquiryDao.class).to(
						ProductExplainEnquiryDao.class);
				bind(IProductExplainUpdateDao.class).to(
						ProductExplainUpdateDao.class);
				bind(IProductExplainTypeEnquiryDao.class).to(
						ProductExplainTypeEnquiryDao.class);
				bind(IProductRecommendUpdateDao.class).to(
						ProductRecommendUpdateDao.class);
				bind(IProductRecommendEnquiryDao.class).to(
						ProductRecommendEnquiryDao.class);
				bind(IProductInterceptUrlEnquiryDao.class).to(
						ProductInterceptUrlEnquiryDao.class);
				bind(IProductInterceptUrlUpdateDao.class).to(
						ProductInterceptUrlUpdateDao.class);
				bind(IProductCategoryEnquiryDao.class).to(
						ProductCategoryEnquiryDao.class);
				bind(IDailyDealUpdateDao.class).to(DailyDealUpdateDao.class);
				bind(IDailyDealEnquiryDao.class).to(DailyDealEnquiryDao.class);
				bind(IProductBundleSaleEnquiryDao.class).to(
						ProductBundleSaleEnquiryDao.class);
				bind(IProductActivityRelationUpdateDao.class).to(
						ProductActivityRelationUpdateDao.class);
				bind(IProductActivityRelationEnquiryDao.class).to(
						ProductActivityRelationEnquiryDao.class);
				bind(IProductImageDao.class).to(ProductImageDao.class);
				bind(IProductBadgeService.class).to(ProductBadgeService.class);
				bind(IProductActivityDetailUpdateDao.class).to(
						ProductActivityDetailUpdateDao.class);
				bind(IProductExplainService.class).to(
						ProductExplainService.class);
				bind(IProductActivityDetailEnquiryDao.class).to(
						ProductActivityDetailEnquiryDao.class);
				bind(IInventoryHistoryEnquiryDao.class).to(
						InventoryHistoryEnquiryDao.class);
				bind(IInventoryHistoryUpdateDao.class).to(
						InventoryHistoryUpdateDao.class);
				bind(IThirdPlatformDataEnquiryDao.class).to(
						ThirdPlatformDataEnquiryDao.class);
				bind(IThirdPlatformDataUpdateDao.class).to(
						ThirdPlatformDataUpdateDao.class);
				bind(IHomePageShowHistoryDao.class).to(
						HomePageShowHistoryDao.class);

				// bind service
				bind(ICategoryEnquiryService.class).to(
						CategoryEnquiryService.class);
				bind(ISearchKeyWords.class).to(KeywordSearchCriteria.class);
				bind(ISearchService.class).to(SearchService.class);
				bind(IAsyncSearchService.class).to(SearchService.class);
				bind(ISearchContextFactory.class)
						.to(SearchContextFactory.class);
				bind(IProductBadgeService.class).to(ProductBadgeService.class);
				bind(ISearchContextFactory.class)
						.to(SearchContextFactory.class);
				bind(IEntityMapService.class).to(EntityMapService.class);
				bind(IProductEnquiryService.class).to(
						ProductEnquiryService.class);
				bind(IImageEnquiryService.class).to(ImageEnquiryService.class);
				bind(IProductCategoryEnquiryService.class).to(
						ProductCategoryEnquiryService.class);
				bind(IDailyDealEnquiryService.class).to(
						DailyDealEnquiryService.class);
				bind(IImageUpdateService.class).to(ImageUpdateService.class);
				bind(IKeyWordSuggestService.class).to(
						KeywordSuggestService.class);
				bind(IOperatingIndex.class).to(IndexingService.class);
				bind(IHomePageDataEnquiry.class).to(HomePageDataEnquiry.class);
				bind(IProductBaseEnquiryService.class).to(
						ProductBaseEnquiryService.class);
				bind(IProductMessageService.class).to(
						ProductMessageService.class);
				bind(IProductInterceptUrlService.class).to(
						ProductInterceptUrlService.class);
				bind(IProductSellingPointsService.class).to(
						ProductSellingPointsService.class);
				bind(IPriceService.class).to(PriceService.class);
				bind(IProductUpdateService.class)
						.to(ProductUpdateService.class);
				bind(IStorageService.class).to(StorageService.class);
				bind(IHomePageProductShowHistoryService.class).to(
						HomePageProductShowHistoryService.class);
				bind(IProductRecommendService.class).to(
						ProductRecommendService.class);
				bind(IAttachmentEnquiryDao.class)
						.to(AttachmentEnquiryDao.class);
				bind(IAttachmentUpdateDao.class).to(AttachmentUpdateDao.class);
				bind(IAttachmentService.class).to(AttachmentService.class);
				bind(IAttachmentDescEnquiryDao.class).to(
						AttachmentDescEnquiryDao.class);
				bind(IAttachmentDescUpdateDao.class).to(
						AttachmentDescUpdateDao.class);
				bind(IAttachmentDescService.class).to(
						AttachmentDescService.class);
				bind(IProductAttachmentMapperEnquiryDao.class).to(
						ProductAttachmentMapperEnquiryDao.class);
				bind(IProductAttachmentMapperUpdateDao.class).to(
						ProductAttachmentMapperUpdateDao.class);
				bind(IProductAttachmentMapperService.class).to(
						ProductAttachmentMapperService.class);
				bind(IProductUrlService.class).to(ProductUrlService.class);
				bind(IProductLabelServices.class).to(ProductLabelService.class);
				bind(IProductVideoEnquiryService.class).to(
						ProductVideoEnquiryService.class);
				bind(IInventoryEnquiryService.class).to(
						InventoryEnquiryService.class);
				bind(IGoogleCategoryBaseDao.class).to(
						GoogleCategoryBaseDao.class);
				bind(IGoogleCategoryBaseService.class).to(
						GoogleCategoryBaseService.class);
				bind(IGoogleFeedsBaseService.class).to(
						GoogleFeedsBaseService.class);
			}
		};
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("product", CategoryBaseMapper.class);
		service.addMapperClass("product", CategoryNameMapper.class);
		service.addMapperClass("product", ProductBaseMapper.class);
		service.addMapperClass("product", ProductUrlMapper.class);
		service.addMapperClass("product", ProductImageMapper.class);
		service.addMapperClass("product", ProductGroupPriceMapper.class);
		service.addMapperClass("product", ProductRecommendMapper.class);
		service.addMapperClass("product", ProductEntityMapMapper.class);
		service.addMapperClass("product", ProductCategoryMapperMapper.class);
		service.addMapperClass("product", ProductStorageMapMapper.class);
		service.addMapperClass("product", ProductSalePriceMapper.class);
		service.addMapperClass("product", ProductSellingPointsMapper.class);
		service.addMapperClass("product", ProductCategoryRankMapper.class);
		service.addMapperClass("product", ProductBundleSaleMapper.class);
		service.addMapperClass("product", CategoryWebsiteMapper.class);
		service.addMapperClass("product", ProductVideoMapper.class);
		service.addMapperClass("product", CategoryFilterAttributeMapper.class);
		service.addMapperClass("product", FilterAttributeKeyMapper.class);
		service.addMapperClass("product", FilterAttributeValueMapper.class);
		service.addMapperClass("product", ProductMultiattributeSkuMapper.class);
		service.addMapperClass("product", ProductTranslateMapper.class);
		service.addMapperClass("product",
				ProductMultiattributeAttributeMapper.class);
		service.addMapperClass("product", ProductMultiattributeBaseMapper.class);
		service.addMapperClass("product", ProductBaseTranslateMapper.class);
		service.addMapperClass("product", AttributeMapper.class);
		service.addMapperClass("product", TopicPageMapper.class);
		service.addMapperClass("product", CategoryLabelTypeMapper.class);
		service.addMapperClass("product", CategoryLabelMapper.class);
		service.addMapperClass("product", CategoryLabelNameMapper.class);
		service.addMapperClass("product", ProductLabelMapper.class);
		service.addMapperClass("product", CategoryBackgroundImagesMapper.class);
		service.addMapperClass("product", ProductViewCountMapper.class);
		service.addMapperClass("product", ProductExplainMapper.class);
		service.addMapperClass("product", ProductExplainTypeMapper.class);
		service.addMapperClass("product", ProductActivityRelationMapper.class);
		service.addMapperClass("product", ProductParentUrlMapper.class);
		service.addMapperClass("product", ProductInterceptUrlMapper.class);
		service.addMapperClass("product", DailyDealMapper.class);
		service.addMapperClass("product", InventoryHistoryMapper.class);
		service.addMapperClass("product",
				ProductActivityRelationDetailMapper.class);
		service.addMapperClass("product", ThirdPlatformDataMapper.class);
		// add by lijun
		service.addMapperClass("product", ProductLabelTypeMapper.class);
		service.addMapperClass("product", HomePageShowHistoryMapper.class);
		service.addMapperClass("product", AttachmentMapper.class);
		service.addMapperClass("product", AttachmentDescMapper.class);
		service.addMapperClass("product", ProductAttachmentMapperMapper.class);
		service.addMapperClass("product", CategoryProductRecommendMapper.class);
		service.addMapperClass("product", GoogleCategoryBaseMapper.class);
		service.addMapperClass("product", GoogleCategoryDetailMapper.class);
		service.addMapperClass("product", GoogleCategoryMapMapper.class);
		service.addMapperClass("product", MerchantsProductMapper.class);
		service.addMapperClass("product", GoogleFeedsMapper.class);
		service.addMapperClass("product", GoogleCategoryRelationMapper.class);
		service.addMapperClass("product", GoogleAttributeMapper.class);
		service.addMapperClass("product", AttributeKeyMapper.class);
		service.addMapperClass("product", AttributeValueMapper.class);
		service.addMapperClass("product", AttributeItemMapper.class);
		service.addMapperClass("product", mapper.attribute.AttributeKeyMapper.class);
		service.addMapperClass("product", mapper.attribute.AttributeKeyNameMapper.class);
		service.addMapperClass("product", mapper.attribute.AttributeValueMapper.class);
		service.addMapperClass("product", mapper.attribute.AttributeValueNameMapper.class);
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		binder.addBinding().to(SearchBar.class);
		binder.addBinding().to(Category.class);
		binder.addBinding().to(HomeBanner.class);
		binder.addBinding().to(HomeDailyDeals.class);
		binder.addBinding().to(GadgetDropDownProvider.class);
		binder.addBinding().to(HotSalesProvider.class);
		binder.addBinding().to(ClearanceSalesProvider.class);
		binder.addBinding().to(FeaturedItemsProvider.class);
		binder.addBinding().to(NewArrivalsProvider.class);
		binder.addBinding().to(HotColumnsProvider.class);
		binder.addBinding().to(HotCategoriesProvider.class);
		binder.addBinding().to(HomeAdRightProvider.class);
		binder.addBinding().to(HotEventsProvider.class);
		binder.addBinding().to(HotProductFragment.class);
		binder.addBinding().to(HotSalesMoreLinkProvider.class);
		binder.addBinding().to(ClearanceSalesLinkProvider.class);
		binder.addBinding().to(HomeAdTopProvider.class);
		binder.addBinding().to(HomeAdHeadProvider.class);
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {

		final Multibinder<IProductFragmentPlugin> miProviders = Multibinder
				.newSetBinder(binder, IProductFragmentPlugin.class);

		final Multibinder<IProductDescriptionVariableProvider> descProviders = Multibinder
				.newSetBinder(binder, IProductDescriptionVariableProvider.class);

		final Multibinder<IProductBadgePartProvider> MassPartProviders = Multibinder
				.newSetBinder(binder, IProductBadgePartProvider.class);

		for (IProductFragmentExtension e : filterModules(modules,
				IProductFragmentExtension.class)) {
			e.registerProductFragment(miProviders);
		}

		for (IProductDescriptionVariableExtension e : filterModules(modules,
				IProductDescriptionVariableExtension.class)) {
			e.registerVariableProvider(descProviders);
		}

		for (IProductBadgeFragmentExtension e : filterModules(modules,
				IProductBadgeFragmentExtension.class)) {
			e.registerProductBadgePartProvider(MassPartProviders);
		}

		// Price Related Extensions
		final Multibinder<IPriceProvider> priceProviders = Multibinder
				.newSetBinder(binder, IPriceProvider.class);
		final Multibinder<IDiscountProvider> discountProviders = Multibinder
				.newSetBinder(binder, IDiscountProvider.class);
		final Multibinder<IPriceCalculationContextProvider> contextProviders = Multibinder
				.newSetBinder(binder, IPriceCalculationContextProvider.class);
		for (IPriceExtension e : filterModules(modules, IPriceExtension.class)) {
			e.registerPriceDiscountProviders(priceProviders, discountProviders,
					contextProviders);
		}

		final Multibinder<IProductAdvertisingProvider> advertisingProviders = Multibinder
				.newSetBinder(binder, IProductAdvertisingProvider.class);
		for (IProductAdvertisingExtension e : filterModules(modules,
				IProductAdvertisingExtension.class)) {
			e.registerProductAdvertisingFragment(advertisingProviders);
		}

		final Multibinder<ISubscribeProvider> shareProviders = Multibinder
				.newSetBinder(binder, ISubscribeProvider.class);
		for (ISubscribeExtension e : filterModules(modules,
				ISubscribeExtension.class)) {
			e.registerSubscribeProvider(shareProviders);
		}
		final Multibinder<IProductAttrIconProvider> attriconProviders = Multibinder
				.newSetBinder(binder, IProductAttrIconProvider.class);
		for (IProductAttrIconFragmentExtension e : filterModules(modules,
				IProductAttrIconFragmentExtension.class)) {
			e.registerProductAttrIconProvider(attriconProviders);
		}
		// 品类页badge的属性
		final Multibinder<IProductAttrPartProvider> attrPartProviders = Multibinder
				.newSetBinder(binder, IProductAttrPartProvider.class);
		for (IProductAttrPartFragmentExtension e : filterModules(modules,
				IProductAttrPartFragmentExtension.class)) {
			e.registerProductAttrPartProvider(attrPartProviders);
		}

		final Multibinder<IProductCommentProvider> commentProviders = Multibinder
				.newSetBinder(binder, IProductCommentProvider.class);
		for (IProductCommentExtension e : filterModules(modules,
				IProductCommentExtension.class)) {
			e.registerProductCommentFragment(commentProviders);
		}

		// add by lijun
		binder.bind(IProductLabelTypeDao.class).to(ProductLabelTypeDao.class);
		binder.bind(IProductLabelTypeService.class).to(
				ProductLabelTypeService.class);
		binder.bind(IShippingServices.class).to(ShippingServices.class);
		binder.bind(IMerchantsService.class).to(MerchantsService.class);
	}

	@Override
	public void registerProductFragment(
			Multibinder<IProductFragmentPlugin> plugins) {
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("base",
						ProductBaseFragmentProvider.class,
						ProductBaseFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("images",
						ProductImagesFragmentProvider.class,
						ProductImagesFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("recommendation",
						RecommendationFragmentProvider.class,
						ProductRecommendFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("group-price",
						ProductGroupPriceFragmentProvider.class,
						ProductGroupPriceFragmentRenderer.class));
		// plugins.addBinding().toInstance(
		// new SimpleProductFragmentPlugin("selling-points",
		// ProductSellingPointsFragmentProvider.class,
		// ProductSellingPointsFragmentRender.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("entity-map",
						ProductCustomAttributesFragmentProvider.class,
						ProductCustomAttributesFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("rank",
						ProductCategoryRankFragmentProvider.class,
						ProductCategoryRankFragmentRenderer.class));
		// plugins.addBinding().toInstance(
		// new SimpleProductFragmentPlugin("bundle-sale",
		// ProductBundleSaleFragmentProvider.class,
		// ProductBundleSaleFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("description", null,
						ProductDescriptionRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("breadcrumb",
						ProductBreadcrumbFragmentProvider.class,
						ProductBreadcrumbFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-policy", null,
						ProductPolicyFragmentRender.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("video",
						ProductVideoFragmentProvider.class,
						ProductVideoFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("keyword", null,
						ProductKeywordsRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-detail-advertising",
						AdvertisingProductDetailFragmentProvider.class,
						AdvertisingProductDetailFragmentRenderer.class));
		plugins.addBinding().toInstance(
				new SimpleProductFragmentPlugin("product-share", null,
						ProductShareFragmentRenderer.class));
	}

	@Override
	public void registerVariableProvider(
			Multibinder<IProductDescriptionVariableProvider> binder) {
		binder.addBinding().to(ProductAttributeVariableProvider.class);
		binder.addBinding().to(ClothingSizeReferenceVariableProvider.class);
		binder.addBinding().to(ProductImagesVariableProvider.class);
	}

	@Override
	public void registerPriceDiscountProviders(
			Multibinder<IPriceProvider> priceProviders,
			Multibinder<IDiscountProvider> discountProviders,
			Multibinder<IPriceCalculationContextProvider> contextProviders) {
		priceProviders.addBinding().to(BasicPriceProvider.class)
				.in(Singleton.class);
		priceProviders.addBinding().to(BundlePriceProvider.class)
				.in(Singleton.class);
		discountProviders.addBinding().to(SaleDiscountProvider.class)
				.in(Singleton.class);
		discountProviders.addBinding().to(BundleSaleDiscountProvider.class)
				.in(Singleton.class);
	}

	@Override
	public void registerSearchBehaviours(
			Multibinder<ISearchIndexProvider> indexProviders,
			Multibinder<ISearchQueryParser> queryParsers,
			Multibinder<ISearchUIProvider> uiProviders) {
		indexProviders.addBinding().to(ProductSaleIndexProvider.class);
		queryParsers.addBinding().to(ProductSearchQueryParser.class);
		uiProviders.addBinding().to(PopularityUI.class);
		uiProviders.addBinding().to(NewArrivalsUI.class);
		uiProviders.addBinding().to(PagerUI.class);
		uiProviders.addBinding().to(DiscountUI.class);
		uiProviders.addBinding().to(FreeShippingUI.class);
		uiProviders.addBinding().to(PriceUI.class);
		uiProviders.addBinding().to(PriceRangeUI.class);
		uiProviders.addBinding().to(SaleUI.class);
	}

	@Override
	public void registerSubscribeProvider(Multibinder<ISubscribeProvider> binder) {
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(InternalizeImageHandler.class));
		eventBus.register(injector.getInstance(ProductViewCountHandler.class));
		eventBus.register(injector.getInstance(InventoryChangeHandler.class));
		eventBus.register(injector
				.getInstance(HomePageProductShowHandler.class));
		eventBus.register(injector.getInstance(CategoryRecommendHandler.class));
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("explain", IProductExplainService.class,
				ProductExplainService.class);
		reg.publishService("search_keywords", ISearchKeyWords.class,
				KeywordSearchCriteria.class);
		reg.publishService("product_search", ISearchService.class,
				SearchService.class);
		reg.publishService("search_serviceIasy", IAsyncSearchService.class,
				SearchService.class);
		reg.publishService("category", ICategoryEnquiryService.class,
				CategoryEnquiryService.class);
		reg.publishService("product_searchcontext_factory",
				ISearchContextFactory.class, SearchContextFactory.class);
		reg.publishService("product_dailydeal_badge",
				IProductBadgeService.class, ProductBadgeService.class);
		reg.publishService("entitymap", IEntityMapService.class,
				EntityMapService.class);
		reg.publishService("product", IProductEnquiryService.class,
				ProductEnquiryService.class);
		reg.publishService("product_images_enquiry",
				IImageEnquiryService.class, ImageEnquiryService.class);
		reg.publishService("productcategory",
				IProductCategoryEnquiryService.class,
				ProductCategoryEnquiryService.class);
		reg.publishService("product_dailydeal_enquiry",
				IDailyDealEnquiryService.class, DailyDealEnquiryService.class);
		reg.publishService("product_images_update", IImageUpdateService.class,
				ImageUpdateService.class);
		reg.publishService("search_suggest_keyword",
				IKeyWordSuggestService.class, KeywordSuggestService.class);
		reg.publishService("product_data_homepage", IHomePageDataEnquiry.class,
				HomePageDataEnquiry.class);
		reg.publishService("third_platform_data",
				IThirdPlatformDataEnquiryService.class,
				ThirdPlatformDataEnquiryService.class);
		reg.publishService("product_operating_index", IOperatingIndex.class,
				IndexingService.class);
		reg.publishService("productbase", IProductBaseEnquiryService.class,
				ProductBaseEnquiryService.class);
		reg.publishService("productMessage", IProductMessageService.class,
				ProductMessageService.class);
		reg.publishService("product_sellingpoint",
				IProductSellingPointsService.class,
				ProductSellingPointsService.class);
		reg.publishService("product_Intercept_url",
				IProductInterceptUrlService.class,
				ProductInterceptUrlService.class);
		reg.publishService("product_url", IProductUrlService.class,
				ProductUrlService.class);
		reg.publishService("product_price", IPriceService.class,
				PriceService.class);
		reg.publishService("product_multiattribute",
				IProductMultiatributeService.class,
				ProductMultiatributeService.class);
		reg.publishService("shippingServices", IShippingServices.class,
				ShippingServices.class);
		reg.publishService("product_update_service",
				IProductUpdateService.class, ProductUpdateService.class);
		reg.publishService("product_recommend", IProductRecommendService.class,
				ProductRecommendService.class);
		reg.publishService("product_label", IProductLabelServices.class,
				ProductLabelService.class);
		reg.publishService("product_video", IProductVideoEnquiryService.class,
				ProductVideoEnquiryService.class);
		reg.publishService("storageService", IStorageService.class,
				StorageService.class);
		reg.publishService("inventoryEnquiryService",
				IInventoryEnquiryService.class, InventoryEnquiryService.class);
	}
}
