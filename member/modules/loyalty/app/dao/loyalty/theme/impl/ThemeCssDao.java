package dao.loyalty.theme.impl;

import java.util.List;

import mapper.loyalty.ThemeCssMapper;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeCssDao;
import entity.loyalty.ThemeCss;
import forms.loyalty.theme.template.ThemeCssForm;

/**
 * 专题样式Dao接口类的实现类
 * 
 * @author Guozy
 *
 */
public class ThemeCssDao implements IThemeCssDao {

	@Inject
	private ThemeCssMapper themeCssMapper;

	@Override
	public int addThemeCss(ThemeCss themeCss) {
		return themeCssMapper.insert(themeCss);
	}

	@Override
	public int deleteThemeCssByIid(int iid) {
		return themeCssMapper.deleteByPrimaryKey(iid);
	}

	@Override
	public int updateThemeCssByIid(ThemeCssForm themeCss) {
		return themeCssMapper.updateByPrimaryKey(themeCss);
	}

	@Override
	public Integer getCount(ThemeCssForm themeCssForm) {
		return themeCssMapper.getCount(themeCssForm.getIid(), themeCssForm.getCname(), themeCssForm.getCcreateuser());

	}

	@Override
	public List<ThemeCss> getThemeCsses(ThemeCssForm themeCssForm) {
		return themeCssMapper.getList(themeCssForm.getIid(), themeCssForm.getCname(), themeCssForm.getCcreateuser(), themeCssForm.getPageSize(), themeCssForm.getPageNum());

	}

	@Override
	public int getThemeCssCountByCanme(String cname) {
		return themeCssMapper.getThemeCssCountByCname(cname);
	}

	@Override
	public List<ThemeCss> getAll() {
		// TODO Auto-generated method stub
		return themeCssMapper.getAll();
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getThemeCssById</p>
	 * <p>Description: 通过id查询专题样式</p>
	 * @param icssid
	 * @return
	 * @see dao.loyalty.theme.IThemeCssDao#getThemeCssById(java.lang.Integer)
	 */
	@Override
	public ThemeCss getThemeCssById(Integer icssid) {
		return themeCssMapper.selectByPrimaryKey(icssid);
	}

}
