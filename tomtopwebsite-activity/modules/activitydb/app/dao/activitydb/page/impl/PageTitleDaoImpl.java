package dao.activitydb.page.impl;

import java.util.List;

import mapper.activitydb.page.PageTitleMapper;

import com.google.inject.Inject;

import dao.activitydb.page.IPageTitleDao;
import entity.activity.page.PageTitle;

/**
 *  页面page  dao 实现类
 * @author liulj
 *
 */
public class PageTitleDaoImpl implements IPageTitleDao{
	
	@Inject
	PageTitleMapper mapper;

	@Override
	public List<PageTitle> getAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public PageTitle getById(int id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public List<PageTitle> getListByPageid(Integer pageid) {
		// TODO Auto-generated method stub
		return mapper.getListByPageid(pageid);
	}

	@Override
	public int insert(PageTitle pageTitle) {
		// TODO Auto-generated method stub
		return mapper.insert(pageTitle);
	}

	@Override
	public int update(PageTitle pageTitle) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(pageTitle);
	}

	@Override
	public int deleteByID(int id) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(id);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPTByPageIdAndLId</p>
	 * <p>Description: 通过活动id和语言id查询活动标题</p>
	 * @param iid
	 * @param languageId
	 * @return
	 * @see dao.activitydb.page.IPageTitleDao#getPTByPageIdAndLId(java.lang.Integer, int)
	 */
	@Override
	public PageTitle getPTByPageIdAndLId(Integer iid, int languageId) {
		return mapper.getPTByPageIdAndLId(iid,languageId);
	}
}
