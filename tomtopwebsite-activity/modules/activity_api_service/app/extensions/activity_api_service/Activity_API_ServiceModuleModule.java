package extensions.activity_api_service;

import java.util.List;

import services.activity.page.IClassService;
import services.activity.page.IPageItemNameService;
import services.activity.page.IPageItemService;
import services.activity.page.IPageJoinService;
import services.activity.page.IPagePrizeService;
import services.activity.page.IPagePrizeResultService;
import services.activity.page.IPageQualificationService;
import services.activity.page.IPageRuleService;
import services.activity.page.IPageService;
import services.activity.page.IPageTitleService;
import services.activity.page.IVoteRecordService;
import services.activity.page.impl.ClassServiceImp;
import services.activity.page.impl.PageItemNameServiceImpl;
import services.activity.page.impl.PageItemServiceImpl;
import services.activity.page.impl.PageJoinServiceImpl;
import services.activity.page.impl.PagePrizeServiceImpl;
import services.activity.page.impl.PagePrizeResultServiceImpl;
import services.activity.page.impl.PageQualificationServiceImpl;
import services.activity.page.impl.PageRuleServiceImpl;
import services.activity.page.impl.PageServiceImpl;
import services.activity.page.impl.PageTitleServiceImpl;
import services.activity.page.impl.VoteRecordServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.runtime.IApplication;

public class Activity_API_ServiceModuleModule extends ModuleSupport implements
		HessianServiceExtension {

	@Override
	public Module getModule(IApplication application) {
		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(IPageService.class).to(PageServiceImpl.class);
				bind(IPageItemService.class).to(PageItemServiceImpl.class);
				bind(IPageItemNameService.class).to(
						PageItemNameServiceImpl.class);
				bind(IPageTitleService.class).to(PageTitleServiceImpl.class);
				bind(IVoteRecordService.class).to(VoteRecordServiceImpl.class);
				bind(IPageQualificationService.class).to(
						PageQualificationServiceImpl.class);
				bind(IClassService.class).to(ClassServiceImp.class);
				bind(IPageRuleService.class).to(PageRuleServiceImpl.class);
				bind(IPagePrizeService.class).to(PagePrizeServiceImpl.class);
				bind(IPagePrizeResultService.class).to(PagePrizeResultServiceImpl.class);
				bind(IPageJoinService.class).to(PageJoinServiceImpl.class);
			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {

	}

	@Override
	public void registerRemoteService(HessianRegistrar hession) {
		hession.publishService("activity-page", IPageService.class,
				PageServiceImpl.class);
		hession.publishService("activity-pageitem", IPageItemService.class,
				PageItemServiceImpl.class);
		hession.publishService("activity-pagetitle", IPageTitleService.class,
				PageTitleServiceImpl.class);
		hession.publishService("activity-pageitemname",
				IPageItemNameService.class, PageItemNameServiceImpl.class);
		hession.publishService("activity-voterecord", IVoteRecordService.class,
				VoteRecordServiceImpl.class);
		hession.publishService("activity-pagequalification",
				IPageQualificationService.class,
				PageQualificationServiceImpl.class);
		hession.publishService("activity-class", IClassService.class,
				ClassServiceImp.class);
		hession.publishService("activity-pagerule", IPageRuleService.class,
				PageRuleServiceImpl.class);
		hession.publishService("activity-pageprize", IPagePrizeService.class,
				PagePrizeServiceImpl.class);
		hession.publishService("activity-pageprizeresult", IPagePrizeResultService.class,
				PagePrizeResultServiceImpl.class);
		hession.publishService("activity-pageJoin", IPageJoinService.class,
				PageJoinServiceImpl.class);
	}
}
