package dao.loyalty.theme;

import java.util.List;

import entity.loyalty.ThemeTitle;

/**
 * 主题的标题的dao接口
 * @author liulj
 *
 */
public interface IThemeTitleDao {

	/**
	 * 根具主题id获取标题列表
	 * @param themeid  主题id
	 * @return
	 */
	List<ThemeTitle> getListByThemeId(int themeid);

	ThemeTitle getById(int id);

	int insert(ThemeTitle themeTitle);

	int update(ThemeTitle themeTitle);

	int deleteByID(int id);

	/**
	 * 
	 * @Title: getThemeIdByTitle
	 * @Description: TODO(通过专题名称获取专题id)
	 * @param @param themeName
	 * @param @return
	 * @return Integer
	 * @throws 
	 * @author yinfei
	 */
	Integer getThemeIdByTitle(String themeName);

	/**
	 * 
	 * @Title: getTTByThemeIdAndLanguageId
	 * @Description: TODO(通过主题id和语言id查询主题标题)
	 * @param @param iid
	 * @param @param languageId
	 * @param @return
	 * @return ThemeTitle
	 * @throws 
	 * @author yinfei
	 */
	ThemeTitle getTTByThemeIdAndLanguageId(Integer iid, int languageId);
}
