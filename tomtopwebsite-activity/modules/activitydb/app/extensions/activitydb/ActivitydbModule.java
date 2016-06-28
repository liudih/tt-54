package extensions.activitydb;

import java.util.List;

import mapper.activitydb.page.PageItemMapper;
import mapper.activitydb.page.PageItemNameMapper;
import mapper.activitydb.page.PageJoinMapper;
import mapper.activitydb.page.PageMapper;
import mapper.activitydb.page.PagePrizeMapper;
import mapper.activitydb.page.PagePrizeResultMapper;
import mapper.activitydb.page.PageQualificationMapper;
import mapper.activitydb.page.PageRuleMapper;
import mapper.activitydb.page.PageTitleMapper;
import mapper.activitydb.page.VoteRecordMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;

import dao.activitydb.page.IPageDao;
import dao.activitydb.page.IPageItemDao;
import dao.activitydb.page.IPageItemNameDao;
import dao.activitydb.page.IPageJoinDao;
import dao.activitydb.page.IPagePrizeDao;
import dao.activitydb.page.IPagePrizeResultDao;
import dao.activitydb.page.IPageQualificationDao;
import dao.activitydb.page.IPageRuleDao;
import dao.activitydb.page.IPageTitleDao;
import dao.activitydb.page.IVoteRecordDao;
import dao.activitydb.page.impl.PageDaoImpl;
import dao.activitydb.page.impl.PageItemDaoImpl;
import dao.activitydb.page.impl.PageItemNameDaoImpl;
import dao.activitydb.page.impl.PageJoinDaoImpl;
import dao.activitydb.page.impl.PagePrizeDaoImpl;
import dao.activitydb.page.impl.PagePrizeResultDaoImpl;
import dao.activitydb.page.impl.PageQualificationDaoImpl;
import dao.activitydb.page.impl.PageRuleDaoImpl;
import dao.activitydb.page.impl.PageTitleDaoImpl;
import dao.activitydb.page.impl.VoteRecordDaoImpl;
import entity.activity.page.PageJoin;
import extensions.IModule;
import extensions.ModuleSupport;
import extensions.runtime.IApplication;

public class ActivitydbModule extends ModuleSupport implements MyBatisExtension {

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
		binder.bind(IPageDao.class).to(PageDaoImpl.class);
		binder.bind(IPageTitleDao.class).to(PageTitleDaoImpl.class);
		binder.bind(IPageItemDao.class).to(PageItemDaoImpl.class);
		binder.bind(IPageItemNameDao.class).to(PageItemNameDaoImpl.class);
		binder.bind(IVoteRecordDao.class).to(VoteRecordDaoImpl.class);
		binder.bind(IPageQualificationDao.class).to(
				PageQualificationDaoImpl.class);
		binder.bind(IPageRuleDao.class).to(PageRuleDaoImpl.class);
		binder.bind(IPagePrizeDao.class).to(PagePrizeDaoImpl.class);
		binder.bind(IPageQualificationDao.class).to(PageQualificationDaoImpl.class);
		binder.bind(IPagePrizeResultDao.class).to(PagePrizeResultDaoImpl.class);
		binder.bind(IPageJoinDao.class).to(PageJoinDaoImpl.class);

	}

	@Override
	public void processConfiguration(MyBatisService myBatis) {
		myBatis.addMapperClass("activity", PageMapper.class);
		myBatis.addMapperClass("activity", PageTitleMapper.class);
		myBatis.addMapperClass("activity", PageItemMapper.class);
		myBatis.addMapperClass("activity", PageItemNameMapper.class);
		myBatis.addMapperClass("activity", VoteRecordMapper.class);
		myBatis.addMapperClass("activity", PageQualificationMapper.class);
		myBatis.addMapperClass("activity", PageRuleMapper.class);
		myBatis.addMapperClass("activity", PagePrizeMapper.class);
		myBatis.addMapperClass("activity", PagePrizeResultMapper.class);
		myBatis.addMapperClass("activity", PageJoinMapper.class);
	}

}
