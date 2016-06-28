package services.loyalty.theme.impl;

import java.util.List;

import services.loyalty.theme.IThemeTitleService;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeTitleDao;
import entity.loyalty.ThemeTitle;

/**
 * 主题的标题服务实现类
 * @author liulj
 *
 */
public class ThemeTitleServiceImpl implements IThemeTitleService {

	/**
	 * 标题的dao层
	 */
	@Inject
	private IThemeTitleDao themeTitleDao;

	@Override
	public List<ThemeTitle> getListByThemeId(int themeid) {
		// TODO Auto-generated method stub
		return themeTitleDao.getListByThemeId(themeid);
	}

	@Override
	public int insert(ThemeTitle themeTitle) {
		// TODO Auto-generated method stub
		return themeTitleDao.insert(themeTitle);
	}

	@Override
	public int update(ThemeTitle themeTitle) {
		// TODO Auto-generated method stub
		return themeTitleDao.insert(themeTitle);
	}

	@Override
	public int deleteByID(int id) {
		// TODO Auto-generated method stub
		return themeTitleDao.deleteByID(id);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getTTByThemeIdAndLanguageId</p>
	 * <p>Description: 通过主题id和语言id查询主题标题</p>
	 * @param iid
	 * @param languageId
	 * @return
	 * @see services.loyalty.theme.IThemeTitleService#getTTByThemeIdAndLanguageId(java.lang.Integer, int)
	 */
	@Override
	public ThemeTitle getTTByThemeIdAndLanguageId(Integer iid, int languageId) {
		return themeTitleDao.getTTByThemeIdAndLanguageId(iid,languageId);
	}
}
