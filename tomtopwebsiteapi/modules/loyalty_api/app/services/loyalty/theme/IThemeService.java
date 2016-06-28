package services.loyalty.theme;

import java.util.List;

import valueobjects.base.Page;
import entity.loyalty.Theme;
import forms.loyalty.theme.template.ThemeForm;
/**
 * 主题服务接口
 * @author liulj
 *
 */
public interface IThemeService {
	List<Theme> getAll();
	Theme getById(int id);
	/**
	 * 获取分页数据
	 * @param page  页码
	 * @param pageSize 页面大小
	 * @param themeQuery 查询条件
	 * @return
	 */
	Page<ThemeForm> getPage(int page, int pageSize, ThemeForm themeQuery);

	/**
	 * 插入主题的信息
	 * @param themeForm
	 * @return
	 */
	int insertThemeInfo(ThemeForm themeForm);
	/**
	 * 更新主题的信息
	 * @param themeForm
	 * @return
	 */
	int updateThemeInfo(ThemeForm themeForm);

	int deleteByID(int id);
	/**
	 * 通过专题样式编号，获取专题的数量
	 * 
	 * @param icssid
	 * @return
	 */
	public boolean getThemesCountByIcssId(int icssid);
	
	/**
	 * 通过专题编号，获取专题的数据信息
	 * @return
	 */
	public Theme getThemeByThemeid(int Themeid);
	
	/**
	 * 通过专题URL地址，获取专题信息
	 * @param curl
	 * @return
	 */
	public  Theme getThemeIidByCurl(String curl);;
	
	/**
	 * 
	 * @param context 
	 * @Title: getThemeByName
	 * @Description: TODO(通过名称获取专题)
	 * @param @param themeName
	 * @param @return
	 * @return Theme
	 * @throws 
	 * @author yinfei
	 */
	Theme getThemeByName(String themeName, int websiteId);
	/**
	 * 验证主题url是存在
	 * @param url
	 * @return  0 表示不存，大于1表示存在
	 */
	public int validateUrl(String url);

}
