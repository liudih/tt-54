package dao.loyalty.theme;

import java.util.List;

import entity.loyalty.ThemeGroupName;

/**
 * 专题分组名称Dao接口类
 * 
 * @author Guozy
 *
 */
public interface IThemeGroupNameDao {

	/**
	 * 根据专题分组编号，查询分组名称的所有信息
	 * 
	 * @param themeGroupId
	 * @return
	 */
	public List<ThemeGroupName> getThemeGroupNamesByThemeGroupId(
			int themeGroupId);

	/**
	 * 添加专题分组名称信息
	 * 
	 * @param themeGroupName
	 * @return
	 */
	public int addThemeGroupName(ThemeGroupName themeGroupName);

	/**
	 * 通过专题分组编号，修改专题分组的数据信息
	 * 
	 * @param themeGroupName
	 * @return
	 */
	public int updateThemeGroupNameByThemeGroupId(ThemeGroupName themeGroupName);

}
