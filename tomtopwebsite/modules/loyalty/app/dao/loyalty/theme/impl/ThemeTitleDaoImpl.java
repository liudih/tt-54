package dao.loyalty.theme.impl;

import java.util.List;

import mapper.loyalty.ThemeTitleMapper;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeTitleDao;
import entity.loyalty.ThemeTitle;

public class ThemeTitleDaoImpl implements IThemeTitleDao {

	@Inject
	private ThemeTitleMapper mapper;

	@Override
	public int insert(ThemeTitle themeTitle) {
		// TODO Auto-generated method stub
		return mapper.insert(themeTitle);
	}

	@Override
	public int update(ThemeTitle themeTitle) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(themeTitle);
	}

	@Override
	public int deleteByID(int id) {
		// TODO Auto-generated method stubs
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public ThemeTitle getById(int id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public List<ThemeTitle> getListByThemeId(int themeid) {
		// TODO Auto-generated method stub
		return mapper.getListByThemeId(themeid);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getThemeIdByTitle</p>
	 * <p>Description: 通过专题名称获取专题id</p>
	 * @param themeName
	 * @return
	 * @see dao.loyalty.theme.IThemeTitleDao#getThemeIdByTitle(java.lang.String)
	 */
	@Override
	public Integer getThemeIdByTitle(String themeName) {
		return mapper.getThemeIdByTitle(themeName);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getTTByThemeIdAndLanguageId</p>
	 * <p>Description: 通过主题id和语言id查询主题标题</p>
	 * @param iid
	 * @param languageId
	 * @return
	 * @see dao.loyalty.theme.IThemeTitleDao#getTTByThemeIdAndLanguageId(java.lang.Integer, int)
	 */
	@Override
	public ThemeTitle getTTByThemeIdAndLanguageId(Integer iid, int languageId) {
		return mapper.getTTByThemeIdAndLanguageId(iid,languageId);
	}
}
