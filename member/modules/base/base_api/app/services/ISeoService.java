package services;

import java.util.List;

import dto.Seo;
import forms.base.homebasicSearch.SeoBaseForm;

/**
 * 首页基本信息管理服务类
 * 
 * @author guozy
 *
 */
public interface ISeoService {

	/**
	 * 添加首页基本信息管理信息
	 * 
	 * @return
	 */
	public boolean insertSeo(Seo seo);

	/**
	 * 删除首页基本信息管理信息
	 * 
	 * @return
	 */
	public boolean deleteSeo(int iid);

	/**
	 * 修改首页基本信息管理信息
	 * 
	 * @return
	 */
	public boolean updateSeo(Seo seo);

	/**
	 * 获取首页基本信息集合
	 * 
	 * @param homeBasicBaseSearchForm
	 * @return
	 */
	public List<Seo> getLists(SeoBaseForm seoForm);

	/**
	 * 获取首页基本信息的数量
	 * 
	 * @param baseSearchForm
	 * @return
	 */
	public Integer getCount(SeoBaseForm seoForm);

	/**
	 * 获取语言、站点、类型
	 * 
	 * @param Seo
	 * @return
	 */
	public Seo getSeoBylanguageIdAndSiteIdAndType(int siteId, int languageId,
			String type);
	
	
}
