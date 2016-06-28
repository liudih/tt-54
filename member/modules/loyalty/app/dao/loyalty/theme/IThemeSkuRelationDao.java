package dao.loyalty.theme;

import java.util.List;

import entity.loyalty.ThemeSkuRelation;
import forms.loyalty.theme.template.ThemeSkuRelationForm;

/**
 * 主题和sku关系表dao
 * 
 * @author liulj
 *
 */
public interface IThemeSkuRelationDao {
	ThemeSkuRelation getById(int id);

	/**
	 * 获取总数，配合getPage用的
	 * 
	 * @param sku
	 *            查询的条件
	 * @return
	 */
	int getCount(String sku);

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 *            页码
	 * @param pagesize
	 *            页面大小
	 * @param sku
	 *            查询条件
	 * @return
	 */
	List<ThemeSkuRelationForm> getPage(int page, int pagesize, String sku);

	int insert(ThemeSkuRelation themeSku);

	int update(ThemeSkuRelation themeSku);

	int deleteByID(int id);
}
