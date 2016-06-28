package services.loyalty.theme;

import java.util.List;

import entity.loyalty.ThemeCss;
import forms.loyalty.theme.template.ThemeCssForm;

/**
 * 专题样式服务接口
 * @author Guozy
 *
 */
public interface IThemeCssService {

	/**
	 * 添加主题样式信息
	 * 
	 * @param themeCss
	 * @return
	 */
	public boolean addThemeCss(ThemeCss themeCss);

	/**
	 * 根据专题编号，删除主题样式信息
	 * 
	 * @param themeCss
	 * @return
	 */
	public boolean deleteThemeCssByIid(int iid);

	/**
	 * 根据专题编号，修改专题样式信息
	 * 
	 * @param themeCss
	 * @return
	 */
	public boolean updateThemeCssByIid(ThemeCssForm themeCss);

	/**
	 * 查询主题模板的页面数量
	 * 
	 * @param themeCssForm
	 * @return
	 */
	public Integer getCount(ThemeCssForm themeCssForm);

	/**
	 * 查询主题模板页的所有数据信息
	 * 
	 * @param themeCssForm
	 * @return
	 */
	public List<ThemeCss> getThemeCsses(ThemeCssForm themeCssForm);
	/**
	 * 通过样式名，获取主体样式信息
	 * 
	 * @return
	 */
	public boolean getThemeCssCountByCanme(String cname);
	
	/**
	 * 获取所有的数据
	 * 
	 * @return
	 */
	public List<ThemeCss> getAll();

	public ThemeCss getThemeCssById(Integer icssid);	
}
