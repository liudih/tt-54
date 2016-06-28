package services.loyalty.theme;

import java.util.List;

import entity.loyalty.ThemeTitle;
/**
 * 主题的标题服务接口
 * @author Administrator
 *
 */
public interface IThemeTitleService {

	/**
	 * 根具主题id获取标题
	 * @param themeid
	 * @return
	 */
	List<ThemeTitle> getListByThemeId(int themeid);

	/**
	 * 插入标题
	 * @param themeTitle
	 * @return
	 */
	int insert(ThemeTitle themeTitle);
	
	/**
	 * 更新标题
	 * @param themeTitle
	 * @return
	 */
	int update(ThemeTitle themeTitle);

	/**
	 * 删除标题
	 * @param id 标题id
	 * @return
	 */
	int deleteByID(int id);

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
