package services.base;


import java.util.List;

import javax.inject.Inject;

import dto.Seo;
import forms.base.homebasicSearch.SeoBaseForm;
import mapper.base.SeoMapper;
import services.ISeoService;

/**
 * 首页管理对象服务类
 * 
 * @author Administrator
 *
 */
public class SeoService implements ISeoService {

	@Inject
	private SeoMapper seoMapper;

	/**
	 * 添加首页基本信息
	 */
	@Override
	public boolean insertSeo(Seo seo) {
		int result = seoMapper.insertSeo(seo);
		return result > 0 ? true : false;
	}

	/**
	 * 删除首页管理信息
	 */
	@Override
	public boolean deleteSeo(int iid) {
		int result = seoMapper.deleteSeo(iid);
		return result > 0 ? true : false;
	}

	/**
	 * 修改首页基本信息
	 */
	@Override
	public boolean updateSeo(Seo homeManager) {
		int result = seoMapper.updateSeo(homeManager);
		return result > 0 ? true : false;
	}

	/**
	 * 获取首页所有的基本数据信息
	 */
	@Override
	public List<Seo> getLists(SeoBaseForm seoForm) {
		if (seoForm == null) {
			return null;
		}
		return seoMapper.getList(seoForm.getIwebsiteid(),
				seoForm.getIlanguageid(), seoForm.getCtype(),
				seoForm.getPageSize(), seoForm.getPageNum());
	}

	/**
	 * 获取基本信息的数量条数
	 */
	@Override
	public Integer getCount(SeoBaseForm seoForm) {
		return seoMapper.getCount(
				seoForm.getIwebsiteid(),
				seoForm.getIlanguageid(), seoForm.getCtype());
	}

	@Override
	public Seo getSeoBylanguageIdAndSiteIdAndType(int siteId, int languageId,
			String type) {
		return seoMapper.getSeoBylanguageIdAndSiteIdAndType(siteId, languageId,
				type);
	}

}
