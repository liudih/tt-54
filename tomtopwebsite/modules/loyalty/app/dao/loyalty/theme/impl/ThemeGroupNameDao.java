package dao.loyalty.theme.impl;

import java.util.List;

import mapper.loyalty.ThemeGroupNameMapper;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeGroupNameDao;
import entity.loyalty.ThemeGroupName;
import forms.loyalty.theme.template.ThemeGroupForm;

public class ThemeGroupNameDao implements IThemeGroupNameDao {

	@Inject
	private ThemeGroupNameMapper themeGroupNameMapper;

	@Override
	public List<ThemeGroupName> getThemeGroupNamesByThemeGroupId(
			int themeGroupId) {
		return themeGroupNameMapper
				.getThemeGroupNamesByThemeGroupId(themeGroupId);
	}

	@Override
	public int addThemeGroupName(ThemeGroupName themeGroupName) {
		return themeGroupNameMapper.insert(themeGroupName);
	}

	@Override
	public int updateThemeGroupNameByThemeGroupId(ThemeGroupName themeGroupName) {
		return themeGroupNameMapper.updateByPrimaryKey(themeGroupName);
	}

}
