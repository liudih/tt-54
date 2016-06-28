package services.loyalty.theme.impl;

import java.util.List;

import services.loyalty.theme.IThemeCssService;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeCssDao;
import entity.loyalty.ThemeCss;
import forms.loyalty.theme.template.ThemeCssForm;

/**
 * 主题样式模板服务类
 * 
 * @author Guozy
 *
 */
public class ThemeCssService implements IThemeCssService{


	@Inject
	private IThemeCssDao iThemeCssDao;

	/**
	 * 添加主题样式信息
	 * 
	 * @param themeCss
	 * @return
	 */
	@Override
	public boolean addThemeCss(ThemeCss themeCss) {
		int result = iThemeCssDao.addThemeCss(themeCss);
		return result > 0 ? true : false;
	};

	/**
	 * 根据专题编号，删除主题样式信息
	 * 
	 * @param themeCss
	 * @return
	 */
	@Override
	public boolean deleteThemeCssByIid(int iid) {
		int result = iThemeCssDao.deleteThemeCssByIid(iid);
		return result > 0 ? true : false;
	};

	/**
	 * 根据专题编号，修改专题样式信息
	 * 
	 * @param themeCss
	 * @return
	 */
	@Override
	public boolean updateThemeCssByIid(ThemeCssForm themeCss) {
		int result = iThemeCssDao.updateThemeCssByIid(themeCss);
		return result > 0 ? true : false;
	}

	/**
	 * 查询主题模板的页面数量
	 * 
	 * @param themeCssForm
	 * @return
	 */
	@Override
	public Integer getCount(ThemeCssForm themeCssForm) {
		return iThemeCssDao.getCount(themeCssForm);
	};

	/**
	 * 查询主题模板页的所有数据信息
	 * 
	 * @param themeCssForm
	 * @return
	 */
	@Override
	public List<ThemeCss> getThemeCsses(ThemeCssForm themeCssForm) {
		return iThemeCssDao.getThemeCsses(themeCssForm);
	};

	/**
	 * 通过样式名，获取主体样式信息
	 * 
	 * @return
	 */
	@Override
	public boolean getThemeCssCountByCanme(String cname) {
		return iThemeCssDao.getThemeCssCountByCanme(cname) > 0 ? true : false;

	}

	@Override
	public List<ThemeCss> getAll() {
		// TODO Auto-generated method stub
		return iThemeCssDao.getAll();
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getThemeCssById</p>
	 * <p>Description: 通过id获取专题样式</p>
	 * @param icssid
	 * @return
	 * @see services.loyalty.theme.IThemeCssService#getThemeCssById(java.lang.Integer)
	 */
	@Override
	public ThemeCss getThemeCssById(Integer icssid) {
		return iThemeCssDao.getThemeCssById(icssid);
	}
	
	
}
