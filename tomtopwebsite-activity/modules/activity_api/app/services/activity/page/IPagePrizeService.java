package services.activity.page;

import java.util.List;

import valueobject.activity.page.PagePrize;
import valueobjects.base.Page;
import forms.activity.page.PagePrizeForm;

/**
 * 页面抽奖奖品dao
 * 
 * @author liulj
 */
public interface IPagePrizeService {

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 *            页码
	 * @param pagesize
	 *            页面大小 查询条件页面url
	 * @param quForm
	 *            查询条件
	 * @return
	 */
	Page<PagePrizeForm> getPage(int page, int pagesize, PagePrizeForm quForm);

	public int add(PagePrize prize);

	public int deleteById(int iid);

	public int updateById(PagePrize prize);

	public PagePrize getById(Integer id);
	
	/**
	 * 获取奖品的所有数据信息
	 * @return
	 */
	public List<PagePrize> getPagePrizes();
}
