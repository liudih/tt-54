package extensions.manager;

import handlers.manager.CommentScoreHandler;
import handlers.manager.LeaveMessageHandler;
import handlers.manager.LiveChatMessageHandler;

import java.util.Set;

import mapper.manager.AdminMenuMapper;
import mapper.manager.AdminMenuRoleMapper;
import mapper.manager.AdminRoleMapper;
import mapper.manager.AdminUserMapper;
import mapper.manager.AdminUserWebsitMapMapper;
import mapper.manager.CustomerServiceScheduleMapper;
import mapper.manager.CustomerServiceScoreMapper;
import mapper.manager.CustomerServiceScoreTypeMapper;
import mapper.manager.LeaveMsgInfoMapper;
import mapper.manager.LivechatMsgInfoMapper;
import mapper.manager.ProfessionSkillMapper;
import mapper.manager.ProfessionSkillTopicMapper;
import mapper.manager.UserRoleMapMapper;
import mapper.manager.UserSkillMapMapper;
import mapper.manager.WelcomeSentenceMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import dao.manager.IAdminUserWebsiteMapEnquiryDao;
import dao.manager.IAdminUserWebsiteMapUpdateDao;
import dao.manager.ICategoryProductRecommendDao;
import dao.manager.ICustomerServiceScheduleEnquiryDao;
import dao.manager.ICustomerServiceScheduleUpdateDao;
import dao.manager.ICustomerServiceScoreEnquiryDao;
import dao.manager.ICustomerServiceScoreTypeEnquiryDao;
import dao.manager.ICustomerServiceScoreTypeUpdateDao;
import dao.manager.ILeaveMsgInfoEnquiryDao;
import dao.manager.ILeaveMsgInfoUpdateDao;
import dao.manager.ILivechatMsgInfoEnquiryDao;
import dao.manager.ILivechatMsgInfoUpdateDao;
import dao.manager.IProfessionSkillDao;
import dao.manager.IProfessionSkillTopicEnquiryDao;
import dao.manager.IProfessionSkillTopicUpdateDao;
import dao.manager.IUserSkillMapEnquiryDao;
import dao.manager.IUserSkillMapUpdateDao;
import dao.manager.IWelcomeSentenceEnquiryDao;
import dao.manager.impl.AdminUserWebsiteMapEnquiryDao;
import dao.manager.impl.AdminUserWebsiteMapUpdateDao;
import dao.manager.impl.CategoryProductRecommendDao;
import dao.manager.impl.CustomerServiceScheduleEnquiryDao;
import dao.manager.impl.CustomerServiceScheduleUpdateDao;
import dao.manager.impl.CustomerServiceScoreEnquiryDao;
import dao.manager.impl.CustomerServiceScoreTypeEnquiryDao;
import dao.manager.impl.CustomerServiceScoreTypeUpdateDao;
import dao.manager.impl.LeaveMsgInfoEnquiryDao;
import dao.manager.impl.LeaveMsgInfoUpdateDao;
import dao.manager.impl.LivechatMsgInfoEnquiryDao;
import dao.manager.impl.LivechatMsgInfoUpdateDao;
import dao.manager.impl.ProfessionSkillDao;
import dao.manager.impl.ProfessionSkillTopicEnquiryDao;
import dao.manager.impl.ProfessionSkillTopicUpdateDao;
import dao.manager.impl.UserSkillMapEnquiryDao;
import dao.manager.impl.UserSkillMapUpdateDao;
import dao.manager.impl.WelcomeSentenceEnquiryDao;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.common.CommonModule;
import extensions.event.IEventExtension;
import extensions.livechat.LiveChatAliasExtension;
import extensions.livechat.LiveChatAliasResolver;
import extensions.livechat.LiveChatOnDutyCustomerServiceProvider;
import extensions.livechat.role.EnquiryRoleProvider;
import extensions.livechat.role.ILiveChatOnDutyCustomerServiceProvider;
import extensions.livechat.role.LiveChatOnDutyCustomerServiceExtension;
import extensions.livechat.role.LiveChatRoleExtension;
import extensions.livechat.role.LiveChatRoleStatusExtension;
import extensions.livechat.role.LiveChatRoleStatusProvider;
import extensions.livechat.role.SupportRoleProvider;
import extensions.livechat.score.SessionScoreExtension;
import extensions.livechat.score.SessionScoreQuestionProvider;
import extensions.livechat.topic.ChatTopicExtension;
import extensions.livechat.topic.ChatTopicProvider;
import extensions.livechat.topic.IWelcomeSentenceProvider;
import extensions.livechat.topic.WelcomeSentenceExtension;
import extensions.runtime.IApplication;

public class ManagerModule extends ModuleSupport implements MyBatisExtension,
		LiveChatAliasExtension, LiveChatRoleExtension, ChatTopicExtension,
		LiveChatRoleStatusExtension, SessionScoreExtension, IEventExtension,
		WelcomeSentenceExtension, LiveChatOnDutyCustomerServiceExtension {
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
				bind(IAdminUserWebsiteMapEnquiryDao.class).to(
						AdminUserWebsiteMapEnquiryDao.class);
				bind(IAdminUserWebsiteMapUpdateDao.class).to(
						AdminUserWebsiteMapUpdateDao.class);
				bind(IUserSkillMapUpdateDao.class).to(
						UserSkillMapUpdateDao.class);
				bind(IUserSkillMapEnquiryDao.class).to(
						UserSkillMapEnquiryDao.class);
				bind(IProfessionSkillDao.class).to(ProfessionSkillDao.class);
				bind(ICustomerServiceScheduleUpdateDao.class).to(
						CustomerServiceScheduleUpdateDao.class);
				bind(ICustomerServiceScheduleEnquiryDao.class).to(
						CustomerServiceScheduleEnquiryDao.class);
				bind(IProfessionSkillTopicEnquiryDao.class).to(
						ProfessionSkillTopicEnquiryDao.class);
				bind(IProfessionSkillTopicUpdateDao.class).to(
						ProfessionSkillTopicUpdateDao.class);
				bind(ILivechatMsgInfoUpdateDao.class).to(
						LivechatMsgInfoUpdateDao.class);
				bind(ICustomerServiceScoreTypeEnquiryDao.class).to(
						CustomerServiceScoreTypeEnquiryDao.class);
				bind(ICustomerServiceScoreTypeUpdateDao.class).to(
						CustomerServiceScoreTypeUpdateDao.class);
				bind(ICustomerServiceScoreEnquiryDao.class).to(
						CustomerServiceScoreEnquiryDao.class);
				bind(ILeaveMsgInfoUpdateDao.class).to(
						LeaveMsgInfoUpdateDao.class);
				bind(ILeaveMsgInfoEnquiryDao.class).to(
						LeaveMsgInfoEnquiryDao.class);
				bind(ILivechatMsgInfoEnquiryDao.class).to(
						LivechatMsgInfoEnquiryDao.class);
				bind(IWelcomeSentenceEnquiryDao.class).to(
						WelcomeSentenceEnquiryDao.class);

				bind(ICategoryProductRecommendDao.class).to(
						CategoryProductRecommendDao.class);
			}
		};
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("manager", AdminMenuMapper.class);
		service.addMapperClass("manager", AdminUserMapper.class);
		service.addMapperClass("manager", AdminUserWebsitMapMapper.class);
		service.addMapperClass("manager", AdminMenuRoleMapper.class);
		service.addMapperClass("manager", AdminRoleMapper.class);
		service.addMapperClass("manager", UserRoleMapMapper.class);
		service.addMapperClass("manager", UserSkillMapMapper.class);
		service.addMapperClass("manager", ProfessionSkillMapper.class);
		service.addMapperClass("manager", CustomerServiceScheduleMapper.class);
		service.addMapperClass("manager", ProfessionSkillTopicMapper.class);
		service.addMapperClass("manager", LivechatMsgInfoMapper.class);
		service.addMapperClass("manager", CustomerServiceScoreTypeMapper.class);
		service.addMapperClass("manager", CustomerServiceScoreMapper.class);
		service.addMapperClass("manager", LeaveMsgInfoMapper.class);
		service.addMapperClass("manager", WelcomeSentenceMapper.class);

	}

	@Override
	public void registerRoles(
			Multibinder<EnquiryRoleProvider> enquiryRoleProviders,
			Multibinder<SupportRoleProvider> supportRoleProviders) {
		supportRoleProviders.addBinding().to(OperatorRoleProvider.class);
	}

	@Override
	public void registerChatTopics(Multibinder<ChatTopicProvider> topics) {
		topics.addBinding().to(TTChatTopicProvider.class);
	}

	@Override
	public void registerAliasResolver(Multibinder<LiveChatAliasResolver> binder) {
		binder.addBinding().to(TopicAliasResolver.class);
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(LiveChatMessageHandler.class));
		eventBus.register(injector.getInstance(LeaveMessageHandler.class));
		eventBus.register(injector.getInstance(CommentScoreHandler.class));
	}

	@Override
	public void registerRoleStatus(
			Multibinder<LiveChatRoleStatusProvider> binder) {
		binder.addBinding().to(CustomerServiceStatusProvider.class);
	}

	@Override
	public void registerSessionScores(
			Multibinder<SessionScoreQuestionProvider> questionBind) {
		questionBind.addBinding().to(CommentScoreQuestionProvider.class);
	}

	@Override
	public void registerWelcomeSentence(Multibinder<IWelcomeSentenceProvider> ws) {
		ws.addBinding().to(WelcomeSentenceProvider.class);

	}

	@Override
	public void registerDutyCustomerService(
			Multibinder<ILiveChatOnDutyCustomerServiceProvider> csBinder) {
		csBinder.addBinding().to(LiveChatOnDutyCustomerServiceProvider.class);
	}

}
