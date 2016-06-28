package services.loyalty.theme.impl;

import java.util.List;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeGroupDao;
import entity.loyalty.ThemeGroup;
import forms.loyalty.theme.template.ThemeGroupForm;
import services.loyalty.theme.IThemeGroupService;

/**
 * 专题分组服务类接口实现类
 * @author Guozy
 *
 */
public class ThemeGroupService implements IThemeGroupService {

	@Inject
	private IThemeGroupDao themeGroupDao;
	
	@Override
	public List<ThemeGroup> getGroups(ThemeGroupForm themeGroupForm) {
		return themeGroupDao.getThemeGroupes(themeGroupForm);
	}

	@Override
	public Integer getCount(ThemeGroupForm themeGroupForm) {
		return themeGroupDao.getCount(themeGroupForm);
	}

	@Override
	public boolean addThemeGroup(ThemeGroupForm themeGroup) {
		return themeGroupDao.addThemeGroup(themeGroup)>0?true:false;
	}

	@Override
	public boolean deleteThemeGroupByIid(int iid) {
		return themeGroupDao.deleteThemeGroupByIid(iid)>0?true:false;
	}

	@Override
	public boolean upodateThemeGroupByIid(ThemeGroup themeGroup) {
		return themeGroupDao.updateThemeGroupByIid(themeGroup)>0?true:false; 
	}

	@Override
	public ThemeGroup getGroupByIid(int iid) {
		return themeGroupDao.getGroupByIid(iid);
	}

	@Override
	public List<ThemeGroupForm> getGroupByThemeid(Integer themeid){
		// TODO Auto-generated method stub
		return themeGroupDao.getGroupByThemeid(themeid);
		
	}

}
