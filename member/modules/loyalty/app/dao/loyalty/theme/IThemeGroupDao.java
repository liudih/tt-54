package dao.loyalty.theme;

import java.util.List;

import entity.loyalty.ThemeGroup;
import forms.loyalty.theme.template.ThemeGroupForm;

/**
 * 专题分组Dao接口类
 * @author Guozy
 *
 */
public interface IThemeGroupDao {

	/**
	 * 添加专题分组信息
	 * 
	 * @param ThemeGroup
	 * @return
	 */
	public int addThemeGroup(ThemeGroupForm themeGroup);

	/**
	 * 根据专题编号，删除专题分组信息
	 * 
	 * @param themeGroup
	 * @return
	 */
	public int deleteThemeGroupByIid(int iid);

	/**
	 * 根据专题编号，修改专题分组信息
	 * 
	 * @param themeGroup
	 * @return
	 */
	public int updateThemeGroupByIid(ThemeGroup themeGroup);

	/**
	 * 查询专题模板的页面数量
	 * 
	 * @param themeGroup
	 * @return
	 */
	public Integer getCount(ThemeGroupForm themeGroup);

	/**
	 * 查询专题模板页的所有数据信息
	 * 
	 * @param themeGroup
	 * @return
	 */
	public List<ThemeGroup> getThemeGroupes(ThemeGroupForm themeGroup);
	
	/**
	 * 通专题分组编号，获取数据信息
	 * @param iid
	 * @return
	 */
	public ThemeGroup getGroupByIid(int iid);
	
	/**
	 * 
	 * 根具主题url获取分组
	 * @param themeid
	 * @return
	 */
	public List<ThemeGroupForm> getGroupByThemeid(Integer themeid);

}
