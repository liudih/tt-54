package services.activity.page;

import java.util.List;

import valueobject.activity.page.PageRule;
import valueobjects.base.Page;
import forms.activity.page.PageRuleForm;

/**
 * 页面抽奖规则service
 * 
 * @author liulj
 */
public interface IPageRuleService {
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
	Page<PageRuleForm> getPage(int page, int pagesize, PageRuleForm quForm);

	public int add(PageRule rule);

	public int deleteByid(int iid);

	public int updateById(PageRule rule);

	public PageRule getById(Integer id);

	int getCountByPageid(Integer pageid);

	List<PageRule> getListByPageid(int pageid);
}
