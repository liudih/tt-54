package extensions.member;

import handlers.member.IntegralHandler;
import handlers.member.JoinDropshipHandler;
import handlers.member.JoinWholeSaleHandler;
import handlers.member.LoginAuditTrailHandler;
import handlers.member.PrizeHandler;
import handlers.member.ReplyMemberFaqHandler;

import java.util.List;
import java.util.Set;

import mapper.member.BlackUserMapper;
import mapper.member.DropShipBaseMapper;
import mapper.member.DropShipLevelMapper;
import mapper.member.EmailSuffixMapper;
import mapper.member.ForgetPasswdBaseMapper;
import mapper.member.MemberAddressMapper;
import mapper.member.MemberBaseMapper;
import mapper.member.MemberByStatisticsMapper;
import mapper.member.MemberEmailVerifyMapper;
import mapper.member.MemberGroupCriterionMapper;
import mapper.member.MemberGroupMapper;
import mapper.member.MemberLoginHistoryMapper;
import mapper.member.MemberOtherIdMapper;
import mapper.member.MemberPhotoMapper;
import mapper.member.MemberRoleMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;
import service.member.fragement.LoginPopBoxFragement;
import service.member.fragement.PhotoFragement;
import services.MemberEnquiryService;
import services.MemberUpdateService;
import services.base.template.ITemplateFragmentProvider;
import services.member.EmailSuffixService;
import services.member.IEmailSuffixService;
import services.member.IMemberBlackUserService;
import services.member.IMemberEmailService;
import services.member.IMemberEnquiryService;
import services.member.IMemberPhotoService;
import services.member.IMemberRoleService;
import services.member.IMemberUpdateService;
import services.member.MemberBlackUserService;
import services.member.MemberEmailService;
import services.member.MemberPhotoService;
import services.member.MemberRoleService;
import services.member.address.AddressService;
import services.member.address.IAddressService;
import services.member.findPassword.FindPasswordService;
import services.member.findpassword.IFindPasswordService;
import services.member.forgetpasswd.ForgetPasswdBaseService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import services.member.login.ILoginOther;
import services.member.login.ILoginService;
import services.member.login.LoginService;
import services.member.login.LoginServiceV2;
import services.member.registration.IMemberRegistrationService;
import services.member.registration.MemberRegistrationService;
import session.ISessionService;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import dao.dropship.IDropShipBaseEnquiryDao;
import dao.dropship.IDropShipBaseUpdateDao;
import dao.dropship.IDropShipLevelEnquiryDao;
import dao.dropship.impl.DropShipBaseEnquiryDao;
import dao.dropship.impl.DropShipBaseUpdateDao;
import dao.dropship.impl.DropShipLevelEnquiryDao;
import extension.point.IExtensionSignin;
import extension.point.ISigninProvider;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.base.BaseModule;
import extensions.base.template.ITemplateExtension;
import extensions.common.CommonModule;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.member.account.IMemberAccountExtension;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.IMemberAccountMenuProvider;
import extensions.member.account.IMemberQuickMenuProvider;
import extensions.member.account.impl.MemberBillingAddressMenuProvider;
import extensions.member.account.impl.MemberSettingsMenuProvider;
import extensions.member.account.impl.MemberShippingAddressMenuProvider;
import extensions.member.dropship.DropShipMenuProvider;
import extensions.member.login.ILoginExtension;
import extensions.member.login.ILoginProcess;
import extensions.member.login.ILoginProcessExtension;
import extensions.member.login.ILoginProvider;
import extensions.member.login.IThirdPartyLoginService;
import extensions.member.login.ThirdPartyLoginExtensionPoint;
import extensions.runtime.IApplication;

public class MemberModule extends ModuleSupport implements MyBatisExtension,
		ITemplateExtension, IMemberAccountExtension, IEventExtension,
		HessianServiceExtension {

	@SuppressWarnings("unchecked")
	@Override
	public Set<Class<? extends IModule>> getDependentModules() {
		return Sets.newHashSet(CommonModule.class, BaseModule.class);
	}

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(IDropShipBaseEnquiryDao.class).to(
						DropShipBaseEnquiryDao.class);
				bind(IDropShipBaseUpdateDao.class).to(
						DropShipBaseUpdateDao.class);
				bind(IDropShipLevelEnquiryDao.class).to(
						DropShipLevelEnquiryDao.class);
				bind(ILoginService.class).to(LoginService.class);
				bind(IMemberRegistrationService.class).to(
						MemberRegistrationService.class);

				bind(IMemberPhotoService.class).to(MemberPhotoService.class);
				bind(IMemberUpdateService.class).to(MemberUpdateService.class);
				bind(IForgetPasswdBaseService.class).to(
						ForgetPasswdBaseService.class);
				bind(IFindPasswordService.class).to(FindPasswordService.class);
				bind(IMemberRoleService.class).to(MemberRoleService.class);
				// bind(IThirdPartyLoginService.class).to(GoogleThridPartyLoginService.class);
				// bind(ILoginOther.class).to(GoogleLogin.class);
			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		final Multibinder<ILoginProvider> loginProviders = Multibinder
				.newSetBinder(binder, ILoginProvider.class);
		for (ILoginExtension e : filterModules(modules, ILoginExtension.class)) {
			e.registerLoginProvider(loginProviders);
		}

		final Multibinder<IMemberAccountMenuProvider> menuProviders = Multibinder
				.newSetBinder(binder, IMemberAccountMenuProvider.class);
		final Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders = Multibinder
				.newSetBinder(binder, IMemberAccountHomeFragmentProvider.class);
		final Multibinder<IMemberQuickMenuProvider> quickMenuProviders = Multibinder
				.newSetBinder(binder, IMemberQuickMenuProvider.class);
		for (IMemberAccountExtension e : filterModules(modules,
				IMemberAccountExtension.class)) {
			e.registerMemberAccountRelatedProviders(menuProviders,
					fragmentProviders, quickMenuProviders);
		}
		// add by lijun
		Multibinder<IMyMessageProvider> multibinder = Multibinder.newSetBinder(
				binder, IMyMessageProvider.class);

		final Multibinder<ISigninProvider> signinProviders = Multibinder
				.newSetBinder(binder, ISigninProvider.class);
		for (IExtensionSignin e : filterModules(modules, IExtensionSignin.class)) {
			e.registerSignin(signinProviders);
		}
		binder.bind(IMemberEnquiryService.class).to(MemberEnquiryService.class);

		// login登陆触发事件
		final Multibinder<ILoginProcess> loginProcess = Multibinder
				.newSetBinder(binder, ILoginProcess.class);
		for (ILoginProcessExtension e : filterModules(modules,
				ILoginProcessExtension.class)) {
			e.registerLoginProcess(loginProcess);
		}

		final Multibinder<IThirdPartyLoginService> thirdPartyLogin = Multibinder
				.newSetBinder(binder, IThirdPartyLoginService.class);

		final Multibinder<ILoginOther> loginOtherBinder = Multibinder
				.newSetBinder(binder, ILoginOther.class);
		for (ThirdPartyLoginExtensionPoint e : filterModules(modules,
				ThirdPartyLoginExtensionPoint.class)) {
			e.registerThirdPartyLoginProvider(thirdPartyLogin);
			e.registerThirdPartyLoginOther(loginOtherBinder);
		}
		binder.bind(IAddressService.class).to(AddressService.class);
		binder.bind(IMemberEmailService.class).to(MemberEmailService.class);
		binder.bind(IMemberBlackUserService.class).to(
				MemberBlackUserService.class);
		binder.bind(IEmailSuffixService.class).to(EmailSuffixService.class);
		binder.bind(services.ILoginProvider.class).to(LoginServiceV2.class);
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("member", MemberBaseMapper.class);
		service.addMapperClass("member", MemberGroupMapper.class);
		service.addMapperClass("member", MemberAddressMapper.class);
		service.addMapperClass("member", MemberOtherIdMapper.class);
		service.addMapperClass("member", ForgetPasswdBaseMapper.class);
		service.addMapperClass("member", MemberByStatisticsMapper.class);
		service.addMapperClass("member", MemberGroupCriterionMapper.class);
		service.addMapperClass("member", MemberPhotoMapper.class);
		service.addMapperClass("member", MemberEmailVerifyMapper.class);
		service.addMapperClass("member", MemberLoginHistoryMapper.class);
		service.addMapperClass("member", DropShipBaseMapper.class);
		service.addMapperClass("member", DropShipLevelMapper.class);
		service.addMapperClass("member", EmailSuffixMapper.class);
		service.addMapperClass("member", BlackUserMapper.class);
		service.addMapperClass("member", MemberRoleMapper.class);
	}

	@Override
	public void registerTemplateProviders(
			Multibinder<ITemplateFragmentProvider> binder) {
		//binder.addBinding().to(MemberNavigationBarRegion.class);
		binder.addBinding().to(PhotoFragement.class);
		binder.addBinding().to(LoginPopBoxFragement.class);

	}

	@Override
	public void registerMemberAccountRelatedProviders(
			Multibinder<IMemberAccountMenuProvider> menuProviders,
			Multibinder<IMemberAccountHomeFragmentProvider> fragmentProviders,
			Multibinder<IMemberQuickMenuProvider> quickMenuProvider) {
		menuProviders.addBinding().to(MemberSettingsMenuProvider.class);
		menuProviders.addBinding().to(MemberShippingAddressMenuProvider.class);
		menuProviders.addBinding().to(MemberBillingAddressMenuProvider.class);
		menuProviders.addBinding().to(DropShipMenuProvider.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(LoginAuditTrailHandler.class));
		eventBus.register(injector.getInstance(JoinWholeSaleHandler.class));
		eventBus.register(injector.getInstance(JoinDropshipHandler.class));
		eventBus.register(injector.getInstance(ReplyMemberFaqHandler.class));
		eventBus.register(injector.getInstance(PrizeHandler.class));
		eventBus.register(injector.getInstance(IntegralHandler.class));
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("login", ILoginService.class, LoginService.class);
		reg.publishService("registrate", IMemberRegistrationService.class,
				MemberRegistrationService.class);

		reg.publishService("member", IMemberEnquiryService.class,
				MemberEnquiryService.class);
		reg.publishService("session", ISessionService.class,
				ISessionService.class);
		reg.publishService("memeber_photoservice", IMemberPhotoService.class,
				MemberPhotoService.class);
		reg.publishService("memeber_updateservice", IMemberUpdateService.class,
				MemberUpdateService.class);
		reg.publishService("addressService", IAddressService.class,
				AddressService.class);
		reg.publishService("forget_password", IForgetPasswdBaseService.class,
				ForgetPasswdBaseService.class);
		reg.publishService("find_password", IFindPasswordService.class,
				FindPasswordService.class);
		reg.publishService("member_email_service", IMemberEmailService.class,
				MemberEmailService.class);
		reg.publishService("email_suffix", IEmailSuffixService.class,
				EmailSuffixService.class);
		
		reg.publishService("loginCtx",  services.ILoginProvider.class,
				LoginServiceV2.class);
	}

}
