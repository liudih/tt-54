package dao.loyalty.theme;

import java.util.Date;
import java.util.List;

import entity.loyalty.Theme;
import forms.loyalty.theme.template.ThemeForm;

/**
 * 主题dao接口
 * @author Administrator
 *
 */
public interface IThemeDao {
	List<Theme> getAll();
	Theme getById(int id);
	
	/**
	 * 获取总数，配合getPage用的
	 * @param iid
	 * @param ienable
	 * @param url
	 * @return
	 */
	int getCount(Integer iid,Integer ienable,String url);

	/**
	 *  * 获取分页数据
	 * @param page 页码
	 * @param pagesize 页面大小
	 * @param iid  查询条件id
	 * @param ienable  查询条件是否启用
	 * @param url 查询条件主题url
	 * @return
	 */
	List<ThemeForm> getPage(int page, int pagesize,Integer iid,Integer ienable,String url);

	int insert(Theme theme);

	int update(Theme theme);

	int deleteByID(int id);
	
	/**
	 * 通过专题样式编号，获取专题的数量
	 * 
	 * @param icssid
	 * @return
	 */
	public int getThemesCountByIcssId(int icssid);
	
	/**
	 * 通过专题编号，获取专题的数据信息
	 * @return
	 */
	public Theme getThemeByThemeid(int iid);
	
	
	/**
	 * 通过专题信息，获取专题编号
	 * @param curl
	 * @return
	 */
	public  Theme getThemeIidByCurl(String curl);

	/**
	 * 
	 * @param websiteId 
	 * @Title: getThemeByUrl
	 * @Description: TODO(通过url查询专题)
	 * @param @param themeName
	 * @param @param date
	 * @param @return
	 * @return Theme
	 * @throws 
	 * @author yinfei
	 */
	Theme getThemeByUrl(String themeName, Date date, int websiteId);
	/**
	 * 验证主题url是存在
	 * @param url
	 * @return  0 表示不存，大于1表示存在
	 */
	public int validateUrl(String url);

}
