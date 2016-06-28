package services.loyalty.theme;

import valueobjects.base.Page;
import entity.loyalty.ThemeSkuRelation;
import forms.loyalty.theme.template.ThemeSkuRelationForm;

/**
 * 专题sku关系服务接口类
 * 
 * @author Guozy
 *
 */
public interface IThemeSkuRelationService {
	ThemeSkuRelation getById(int id);

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            页面大小
	 * @param themeQuery
	 *            查询条件
	 * @return
	 */
	Page<ThemeSkuRelationForm> getPage(int page, int pageSize,
			ThemeSkuRelationForm queryForm);

	int insert(ThemeSkuRelation form);

	int update(ThemeSkuRelation form);

	int deleteByID(int id);
}
