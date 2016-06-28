package services.activity.page;

import valueobject.activity.page.PageQualification;
import valueobjects.base.Page;
import forms.activity.page.PageQualificationForm;

/**
 * 页面访问规则service
 * 
 * @author liulj
 */
public interface IPageQualificationService {
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
	Page<PageQualificationForm> getPage(int page, int pagesize,
			PageQualificationForm quForm);

	public int add(PageQualification qualification);

	public int deleteById(int iid);

	public int updateById(PageQualification qualification);

	public PageQualification getById(Integer id);
}
