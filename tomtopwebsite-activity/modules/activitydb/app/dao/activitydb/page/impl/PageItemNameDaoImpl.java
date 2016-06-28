package dao.activitydb.page.impl;

import java.util.List;

import mapper.activitydb.page.PageItemNameMapper;

import com.google.inject.Inject;

import dao.activitydb.page.IPageItemNameDao;
import entity.activity.page.PageItemName;

/**
 *  页面page  dao 实现类
 * @author liulj
 *
 */
public class PageItemNameDaoImpl implements IPageItemNameDao{
	
	@Inject
	PageItemNameMapper mapper;

	@Override
	public List<PageItemName> getAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public PageItemName getById(int id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int insert(PageItemName pageTitle) {
		// TODO Auto-generated method stub
		return mapper.insert(pageTitle);
	}

	@Override
	public int update(PageItemName pageTitle) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(pageTitle);
	}

	@Override
	public int deleteByID(int id) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<PageItemName> getListByPageItemid(Integer pageid) {
		// TODO Auto-generated method stub
		return mapper.getListByPageItemid(pageid);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPINameByPIIdAndLId</p>
	 * <p>Description: 通过活动项目id和语言id查询活动项目名称</p>
	 * @param iid
	 * @param languageId
	 * @return
	 * @see dao.activitydb.page.IPageItemNameDao#getPINameByPIIdAndLId(java.lang.Integer, int)
	 */
	@Override
	public String getPINameByPIIdAndLId(Integer iid, int languageId) {
		return mapper.getPINameByPIIdAndLId(iid,languageId);
	}
}
