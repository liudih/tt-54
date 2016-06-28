package extensions.base;

import handlers.base.CurrencyRateHandler;
import intercepters.IMoneyIntercepter;
import intercepters.JPYMoneyIntercepter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import mapper.base.BannerMapper;
import mapper.base.CmsContentMapper;
import mapper.base.CmsMenuMapper;
import mapper.base.CountryMapper;
import mapper.base.CurrencyMapper;
import mapper.base.CurrencyRateMapper;
import mapper.base.EmailAccountMapper;
import mapper.base.EmailTemplateMapper;
import mapper.base.EmailTypeMapper;
import mapper.base.EmailVariableMapper;
import mapper.base.JobLogMapper;
import mapper.base.LanguageMapper;
import mapper.base.LoginTerminalMapper;
import mapper.base.PlatformMapper;
import mapper.base.ProvinceMapper;
import mapper.base.ReceivedDataHistoryMapper;
import mapper.base.SeoMapper;
import mapper.base.StorageArrivalMapper;
import mapper.base.StorageDefaultMapper;
import mapper.base.StorageMapper;
import mapper.base.StorageNameMappingMapper;
import mapper.base.StorageParentMapper;
import mapper.base.SystemParameterMapper;
import mapper.base.VhostMapper;
import mapper.base.WebsiteMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import org.apache.camel.builder.RouteBuilder;

import play.Logger;
import play.Play;
import play.libs.Json;
import services.IBannerService;
import services.ICaptchaService;
import services.ICmsContentService;
import services.ICmsMenuService;
import services.IConfigService;
import services.ICountryService;
import services.ICurrencyService;
import services.IDefaultSettings;
import services.IEmailAccountService;
import services.IEmailTemplateService;
import services.IEventBusService;
import services.IFoundationService;
import services.ILanguageService;
import services.IMoneyService;
import services.IPlatformService;
import services.IProvinceService;
import services.ISeoService;
import services.IStorageParentService;
import services.ISystemParameterService;
import services.IVhostService;
import services.IWebsiteService;
import services.base.BannerService;
import services.base.ConfigService;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.DefaultSettings;
import services.base.EmailAccountService;
import services.base.EmailTemplateService;
import services.base.EventBusService;
import services.base.FoundationService;
import services.base.MoneyService;
import services.base.PlatformService;
import services.base.ProvinceService;
import services.base.SeoService;
import services.base.StorageParentService;
import services.base.SystemParameterService;
import services.base.VhostService;
import services.base.WebsiteService;
import services.base.captcha.CaptchaService;
import services.base.cms.CmsContentService;
import services.base.cms.CmsMenuService;
import services.base.geoip.GeoIPService;
import services.base.lang.LanguageService;
import services.base.template.ITemplateFragmentProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import dao.IReceivedDataHistoryDao;
import dao.IStorageDao;
import dao.IStorageNameMappingDao;
import dao.IVhostEnquiryDao;
import dao.impl.ReceivedDataHistoryDao;
import dao.impl.StorageDao;
import dao.impl.StorageNameMappingDao;
import dao.impl.VhostEnquiryDao;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.camel.TimerTriggerRouteBuilder;
import extensions.base.template.ITemplateExtension;
import extensions.camel.ICamelExtension;
import extensions.common.CommonModule;
import extensions.event.IEventExtension;
import extensions.filter.IFilter;
import extensions.filter.IFilterExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.runtime.IApplication;
import factory.common.HessianInvokeFactory;
import factory.common.ServiceInvokeFactory;
import filter.base.PageRenderTimingFilter;

public class BaseModule extends ModuleSupport implements MyBatisExtension,
		IFilterExtension, IEventExtension,
		HessianServiceExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(CommonModule.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(GeoIPService.class).in(Singleton.class);
				bind(LanguageService.class).in(Singleton.class);
				bind(IVhostEnquiryDao.class).to(VhostEnquiryDao.class);
				bind(IFoundationService.class).to(FoundationService.class);
				bind(ICountryService.class).to(CountryService.class);
				bind(IBannerService.class).to(BannerService.class);
				bind(ICurrencyService.class).to(CurrencyService.class);
				bind(IWebsiteService.class).to(WebsiteService.class);
				bind(IPlatformService.class).to(PlatformService.class);
				bind(IEmailAccountService.class).to(EmailAccountService.class);
				bind(ILanguageService.class).to(LanguageService.class);
				bind(ICmsMenuService.class).to(CmsMenuService.class);
				bind(IVhostService.class).to(VhostService.class);
				bind(ICmsContentService.class).to(CmsContentService.class);
				bind(ISystemParameterService.class).to(
						SystemParameterService.class);
				bind(IEmailTemplateService.class)
						.to(EmailTemplateService.class);
			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<ITemplateFragmentProvider> tfProviders = Multibinder
				.newSetBinder(binder, ITemplateFragmentProvider.class);

		for (ITemplateExtension e : filterModules(modules,
				ITemplateExtension.class)) {
			e.registerTemplateProviders(tfProviders);
		}

		final Multibinder<HtmlRenderHook> hooks = Multibinder.newSetBinder(
				binder, HtmlRenderHook.class);

		for (IHtmlHookExtension e : filterModules(modules,
				IHtmlHookExtension.class)) {
			e.registerHtmlHook(hooks);
		}

		binder.bind(IReceivedDataHistoryDao.class).to(
				ReceivedDataHistoryDao.class);
		binder.bind(IStorageNameMappingDao.class).to(
				StorageNameMappingDao.class);
		binder.bind(IStorageDao.class).to(StorageDao.class);
		binder.bind(ICaptchaService.class).to(CaptchaService.class);
		binder.bind(IDefaultSettings.class).to(DefaultSettings.class);
		binder.bind(ISystemParameterService.class).to(
				SystemParameterService.class);
		binder.bind(ISeoService.class).to(SeoService.class);
		binder.bind(IConfigService.class).to(ConfigService.class);

		final Multibinder<IMoneyIntercepter> moneyIntercepters = Multibinder
				.newSetBinder(binder, IMoneyIntercepter.class);
		moneyIntercepters.addBinding().to(JPYMoneyIntercepter.class);
		binder.bind(IMoneyService.class).to(MoneyService.class);

		this.binderHessianService(binder);
	}

	/**
	 * 绑定Hessian服务类
	 * 
	 * @author lijun
	 * @param binder
	 */
	private void binderHessianService(Binder binder) {
		try {
			ServiceInvokeFactory factory = new HessianInvokeFactory();
			JsonNode jsonConfig = Json.parse(Play.application().classloader()
					.getResourceAsStream("HessianService-json.properties"));
			if (jsonConfig.isArray()) {
				Iterator<JsonNode> iterator = jsonConfig.iterator();
				while (iterator.hasNext()) {
					JsonNode node = iterator.next();
					String url = node.findValue("url").asText();
					String serviceRemoteUrl = Play.application()
							.configuration().getString(url);
					JsonNode services = node.findValue("service");
					if (services.isArray()) {
						Iterator<JsonNode> si = services.iterator();
						while (si.hasNext()) {
							JsonNode service = si.next();
							String endPoint = service.findValue("endPoint")
									.asText();
							String cl = service.findValue("class").asText();

							Class c = this.loadClass(cl);
							String serviceUrl = serviceRemoteUrl
									+ endPoint.trim();
							Object servictObject = factory.getService(
									serviceUrl, c);
							if (servictObject != null) {
								binder.bind(c).toInstance(servictObject);
								Logger.debug("binder {} succeed,url : {}", cl,
										serviceUrl);
							} else {
								Logger.debug("binder {} failed", cl);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.debug("Loading file : HessianService-json.properties error",
					e);
		}
	}

	/**
	 * @author lijun
	 * @param clazz
	 * @return
	 */
	private Class loadClass(String clazz) {
		try {
			return Class.forName(clazz);
		} catch (Exception e) {
			Logger.error("hessian service class error: " + clazz, e);
			return null;
		}
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("base", PlatformMapper.class);
		service.addMapperClass("base", WebsiteMapper.class);
		service.addMapperClass("base", StorageMapper.class);
		service.addMapperClass("base", StorageArrivalMapper.class);
		service.addMapperClass("base", StorageDefaultMapper.class);
		service.addMapperClass("base", StorageParentMapper.class);
		service.addMapperClass("base", LanguageMapper.class);
		service.addMapperClass("base", CurrencyMapper.class);
		service.addMapperClass("base", CurrencyRateMapper.class);
		service.addMapperClass("base", CountryMapper.class);
		service.addMapperClass("base", ProvinceMapper.class);
		service.addMapperClass("base", BannerMapper.class);
		service.addMapperClass("base", SystemParameterMapper.class);
		service.addMapperClass("base", EmailAccountMapper.class);
		service.addMapperClass("base", EmailTemplateMapper.class);
		service.addMapperClass("base", CmsContentMapper.class);
		service.addMapperClass("base", CmsMenuMapper.class);
		service.addMapperClass("base", EmailTypeMapper.class);
		service.addMapperClass("base", EmailVariableMapper.class);
		service.addMapperClass("base", JobLogMapper.class);
		service.addMapperClass("base", ReceivedDataHistoryMapper.class);
		service.addMapperClass("base", StorageNameMappingMapper.class);
		service.addMapperClass("base", VhostMapper.class);
		service.addMapperClass("base", LoginTerminalMapper.class);
		service.addMapperClass("base", SeoMapper.class);

	}

	@Override
	public void registerFilter(Multibinder<IFilter> filters) {
		filters.addBinding().to(PageRenderTimingFilter.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(CurrencyRateHandler.class));
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("foundation", IFoundationService.class,
				FoundationService.class);
		reg.publishService("banner", IBannerService.class, BannerService.class);
		reg.publishService("country", ICountryService.class,
				CountryService.class);
		reg.publishService("currency", ICurrencyService.class,
				CurrencyService.class);
		reg.publishService("email", IEmailAccountService.class,
				EmailAccountService.class);
		reg.publishService("platform", IPlatformService.class,
				PlatformService.class);
		reg.publishService("vhost", IVhostService.class, VhostService.class);
		reg.publishService("website", IWebsiteService.class,
				WebsiteService.class);
		reg.publishService("language", ILanguageService.class,
				LanguageService.class);
		reg.publishService("cms", ICmsMenuService.class, CmsMenuService.class);
		reg.publishService("captcha", ICaptchaService.class,
				CaptchaService.class);
		reg.publishService("defaultSettings", IDefaultSettings.class,
				DefaultSettings.class);
		reg.publishService("cmscontent", ICmsContentService.class,
				CmsContentService.class);
		reg.publishService("systemParameter", ISystemParameterService.class,
				SystemParameterService.class);
		reg.publishService("base_EventBus", IEventBusService.class,
				EventBusService.class);
		reg.publishService("base_configService", IConfigService.class,
				ConfigService.class);
		reg.publishService("base_moneyService", IMoneyService.class,
				MoneyService.class);
		reg.publishService("emaiTemp", IEmailTemplateService.class,
				EmailTemplateService.class);
		reg.publishService("cmsMenu", ICmsMenuService.class,
				CmsMenuService.class);
		reg.publishService("cmsContent", ICmsContentService.class,
				CmsContentService.class);
		reg.publishService("province", IProvinceService.class,
				ProvinceService.class);
		reg.publishService("storageParentService", IStorageParentService.class,
				StorageParentService.class);
		

	}

}
