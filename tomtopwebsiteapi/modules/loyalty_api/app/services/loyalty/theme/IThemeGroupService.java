package services.loyalty.theme;

import java.util.List;

import entity.loyalty.ThemeGroup;
import forms.loyalty.theme.template.ThemeGroupForm;

/**
 * 专题分组服务接口类
 * @author Guozy
 *
 */
public interface IThemeGroupService {
	
	/**
	 * 显示所有的专题分组数据信息
	 * @return
	 */
	public List<ThemeGroup> getGroups(ThemeGroupForm themeGroupForm);
	
	/**
	 * 根据相应条件，获取数据信息的数量
	 * @param themeGroupForm
	 * @return
	 */
	public Integer getCount(ThemeGroupForm themeGroupForm);
	
	/**
	 * 新增专题分组信息
	 * @param themeGroup
	 * @return
	 */
	public boolean addThemeGroup(ThemeGroup themeGroup);
	
	/**
	 * 根据专题分组编号，删除专题分组信息
	 * @param iid
	 * @return
	 */
	public boolean deleteThemeGroupByIid(int iid);
	
	/**
	 * 根据专题分组编号，修改专题分组的数据信息
	 * @param themeGroup
	 * @return
	 */
	public boolean upodateThemeGroupByIid(ThemeGroup themeGroup);
	
	/**
	 * 根具主题id获取分组
	 * @param ithemeid
	 * @return
	 */
	public List<ThemeGroupForm> getGroupByThemeid(Integer ithemeid);
	
}
