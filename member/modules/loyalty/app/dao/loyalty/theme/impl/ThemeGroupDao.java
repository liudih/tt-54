package dao.loyalty.theme.impl;

import java.util.List;

import mapper.loyalty.ThemeGroupMapper;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeGroupDao;
import entity.loyalty.ThemeGroup;
import forms.loyalty.theme.template.ThemeGroupForm;

/**
 * 专题样式Dao接口类的实现类
 * 
 * @author Guozy
 *
 */
public class ThemeGroupDao implements IThemeGroupDao {

	@Inject
	private ThemeGroupMapper themeGroupMapper;

	@Override
	public int addThemeGroup(ThemeGroupForm themeGroup) {
		return themeGroupMapper.insert(themeGroup);
	}

	@Override
	public int deleteThemeGroupByIid(int iid) {
		return themeGroupMapper.deleteByPrimaryKey(iid);
	}

	@Override
	public int updateThemeGroupByIid(ThemeGroup themeGroup) {
		return themeGroupMapper.updateByPrimaryKey(themeGroup);
	}

	@Override
	public Integer getCount(ThemeGroupForm themeGroup) {
		return themeGroupMapper.getCount(themeGroup.getIid(),
				themeGroup.getIthemeid());
	}

	@Override
	public List<ThemeGroup> getThemeGroupes(ThemeGroupForm themeGroup) {
		return themeGroupMapper.getThemeGroups(themeGroup.getIid(),
				themeGroup.getIthemeid(), 
				themeGroup.getPageSize(), themeGroup.getPageNum());
	}

	@Override
	public ThemeGroup getGroupByIid(int iid) {
		return themeGroupMapper.getThemeGroupByIid(iid);
	}

	@Override
	public List<ThemeGroupForm> getGroupByThemeid(Integer themeid) {
		// TODO Auto-generated method stub
		return themeGroupMapper.getGroupByThemeid(themeid);
	}
}
