package services.loyalty.theme.impl;

import java.util.List;

import org.apache.commons.io.filefilter.FalseFileFilter;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeGroupNameDao;
import entity.loyalty.ThemeGroupName;
import services.loyalty.theme.IThemeGroupNameService;

/**
 * 专题分组名称接口实现类
 * 
 * @author Guozy
 *
 */
public class ThemeGroupNameService implements IThemeGroupNameService {

	@Inject
	private IThemeGroupNameDao iThemeGroupNameDao;

	@Override
	public List<ThemeGroupName> getThemeGroupNamesByThemeGroupId(
			int themeGroupId) {
		return iThemeGroupNameDao
				.getThemeGroupNamesByThemeGroupId(themeGroupId);
	}

	@Override
	public boolean addThemeGroupNmae(ThemeGroupName themeGroupName) {
		return iThemeGroupNameDao.addThemeGroupName(themeGroupName) > 0 ? true
				: false;
	}

	@Override
	public boolean updateThemeGroupNameByThemeGroupId(
			ThemeGroupName themeGroupName) {
		return iThemeGroupNameDao
				.updateThemeGroupNameByThemeGroupId(themeGroupName) > 0 ? true
				: false;
	}

}
