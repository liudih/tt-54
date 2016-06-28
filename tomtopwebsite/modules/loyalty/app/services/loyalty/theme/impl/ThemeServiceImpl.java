package services.loyalty.theme.impl;

import java.util.Date;
import java.util.List;






import org.apache.ibatis.session.ExecutorType;
import org.mybatis.guice.transactional.Transactional;

import services.loyalty.theme.IThemeService;
import valueobjects.base.Page;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeDao;
import dao.loyalty.theme.IThemeTitleDao;
import entity.loyalty.Theme;
import entity.loyalty.ThemeTitle;
import forms.loyalty.theme.template.ThemeForm;

public class ThemeServiceImpl implements IThemeService {

	@Inject
	private IThemeDao themeDao;

	@Inject
	private IThemeTitleDao themeTitleDao;

	@Override

	public Page<ThemeForm> getPage(int page, int pageSize, ThemeForm theme) {
		// TODO Auto-generated method stub
		List<ThemeForm> themes = themeDao.getPage(page, pageSize,theme.getIid(),theme.getIenable(),theme.getCurl());
		int total = themeDao.getCount(theme.getIid(),theme.getIenable(),theme.getCurl());
		return new Page<ThemeForm>(themes, total, page, pageSize);
	}

	@Transactional(executorType = ExecutorType.BATCH)
	@Override
	public int insertThemeInfo(ThemeForm themeForm) {
		// TODO Auto-generated method stub
		Theme theme = getThemeFromThemeForm(themeForm);
		int rownum = themeDao.insert(theme);
		if (rownum > 0) {
			for (ThemeTitle title : themeForm.getLangs()) {
				title.setIthemeid(theme.getIid());
				themeTitleDao.insert(title);
			}
		}
		return rownum;

	}
	/**
	 * 把themeForm对象转成theme对象
	 * @param themeForm
	 * @return
	 */
	public Theme getThemeFromThemeForm(ThemeForm themeForm) {
		Theme theme = new Theme();
		theme.setIid(themeForm.getIid());
		theme.setCbannerurl(themeForm.getCbannerurl());
		theme.setCurl(themeForm.getCurl());
		theme.setIcssid(themeForm.getIcssid());
		theme.setIenable(themeForm.getIenable());
		theme.setDenableenddate(themeForm.getDenableenddate());
		theme.setDenablestartdate(themeForm.getDenablestartdate());
		theme.setCcreateuser(themeForm.getCcreateuser());
		theme.setCupdateuser(themeForm.getCupdateuser());
		theme.setDupdatedate(themeForm.getDupdatedate());
		theme.setIwebsiteid(themeForm.getIwebsiteid());
		return theme;
	}
		
	@Transactional(executorType = ExecutorType.BATCH)
	@Override
	public int updateThemeInfo(ThemeForm themeForm) {
		// TODO Auto-generated method stub
		Theme theme = getThemeFromThemeForm(themeForm);
		int rownum = themeDao.update(theme);
		if (rownum > 0) {
			for (ThemeTitle title : themeForm.getLangs()) {
				title.setIthemeid(theme.getIid());
				if (title.getIid() == null) {
					themeTitleDao.insert(title);
				}
				else{
					themeTitleDao.update(title);
				}				
			}
		}
		return rownum;
	}

	@Override
	public int deleteByID(int id) {
		return themeDao.deleteByID(id);
	}

	@Override
	public boolean getThemesCountByIcssId(int icssid) {
		return themeDao.getThemesCountByIcssId(icssid)>0?true:false;
	}

	@Override
	public Theme getThemeByThemeid(int Themeid) {
		return themeDao.getThemeByThemeid(Themeid);
		
	}
		
	public Theme getById(int id) {
		// TODO Auto-generated method stub
		return themeDao.getById(id);
	}

	@Override
	public Theme getThemeIidByCurl(String curl) {
		return themeDao.getThemeIidByCurl(curl);
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: getThemeByName</p>
	 * <p>Description: 通过名称获取专题</p>
	 * @param themeName
	 * @return
	 * @see services.loyalty.theme.IThemeService#getThemeByName(java.lang.String)
	 */
	public Theme getThemeByName(String themeName, int websiteId) {
		return themeDao.getThemeByUrl(themeName,new Date(),websiteId);
	}

	@Override
	public List<Theme> getAll() {
		// TODO Auto-generated method stub
		return themeDao.getAll();
	}

	@Override
	public int validateUrl(String url) {
		// TODO Auto-generated method stub
		return themeDao.validateUrl(url);
	}
}
