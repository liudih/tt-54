package dao.loyalty.theme.impl;

import java.util.Date;
import java.util.List;

import mapper.loyalty.ThemeMapper;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeDao;
import entity.loyalty.Theme;
import forms.loyalty.theme.template.ThemeForm;

public class ThemeDaoImpl implements IThemeDao {

	@Inject
	private ThemeMapper themeMapper;

	@Override
	public List<ThemeForm> getPage(int page, int pageSize,Integer iid,Integer ienable,String url) {
		// TODO Auto-generated method stub
		return themeMapper.getPage(page, pageSize,iid,ienable,url);
	}

	@Override
	public int insert(Theme theme) {
		// TODO Auto-generated method stub
		return themeMapper.insert(theme);
	}

	@Override
	public int update(Theme theme) {
		// TODO Auto-generated method stub
		return themeMapper.updateByPrimaryKey(theme);
	}

	@Override
	public int deleteByID(int id) {
		// TODO Auto-generated method stub
		return themeMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int getCount(Integer iid,Integer ienable,String url) {
		// TODO Auto-generated method stub
		return themeMapper.getCount(iid,ienable,url);
	}

	@Override
	public Theme getById(int id) {
		// TODO Auto-generated method stub
		return themeMapper.selectByPrimaryKey(id);
	}

	@Override
	public int getThemesCountByIcssId(int icssid) {
		return themeMapper.getThemesCountByIcssId(icssid);
	}

	@Override
	public Theme getThemeByThemeid(int iid) {
		return themeMapper.getThemeByThemeId(iid);
	}

	@Override
	public Theme getThemeIidByCurl(String curl) {
		return themeMapper.getThemeIidByCurl(curl);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getThemeByUrl</p>
	 * <p>Description: 通过url查询专题</p>
	 * @param themeName
	 * @param date
	 * @return
	 * @see dao.loyalty.theme.IThemeDao#getThemeByUrl(java.lang.String, java.util.Date)
	 */
	@Override
	public Theme getThemeByUrl(String themeName, Date date,int websiteId) {
		return themeMapper.getThemeByUrl(themeName,date,websiteId);
	}

	@Override
	public List<Theme> getAll() {
		// TODO Auto-generated method stub
		return themeMapper.selectAll();
	}

	@Override
	public int validateUrl(String url) {
		// TODO Auto-generated method stub
		return themeMapper.validateUrl(url);
	}

}
