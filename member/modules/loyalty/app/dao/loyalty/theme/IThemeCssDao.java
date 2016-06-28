package dao.loyalty.theme;

import java.util.List;

import entity.loyalty.ThemeCss;
import forms.loyalty.theme.template.ThemeCssForm;

/**
 * 专题样式Dao接口类
 * 
 * @author Guozy
 */
public interface IThemeCssDao {

	/**
	 * 添加主题样式信息
	 * 
	 * @param themeCss
	 * @return
	 */
	public int addThemeCss(ThemeCss themeCss);

	/**
	 * 根据专题编号，删除主题样式信息
	 * 
	 * @param themeCss
	 * @return
	 */
	public int deleteThemeCssByIid(int iid);

	/**
	 * 根据专题编号，修改专题样式信息
	 * 
	 * @param themeCss
	 * @return
	 */
	public int updateThemeCssByIid(ThemeCssForm themeCss);

	/**
	 * 查询专题模板的页面数量
	 * 
	 * @param themeCssForm
	 * @return
	 */
	public Integer getCount(ThemeCssForm themeCssForm);

	/**
	 * 查询专题模板页的所有数据信息
	 * 
	 * @param themeCssForm
	 * @return
	 */
	public List<ThemeCss> getThemeCsses(ThemeCssForm themeCssForm);

	/**
	 * 通过样式名，获取专题样式信息
	 * 
	 * @return
	 */
	public int getThemeCssCountByCanme(String cname);

	/**
	 * 获取所有的数据
	 * 
	 * @return
	 */
	public List<ThemeCss> getAll();

	/**
	 * 
	 * @Title: getThemeCssById
	 * @Description: TODO(通过id获取专题样式)
	 * @param @param icssid
	 * @param @return
	 * @return ThemeCss
	 * @throws 
	 * @author yinfei
	 */
	public ThemeCss getThemeCssById(Integer icssid);

}
