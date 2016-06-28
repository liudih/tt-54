package services.base.cms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import mapper.base.CmsContentMapper;

import org.springframework.beans.BeanUtils;

import services.ICmsContentService;
import services.base.FoundationService;
import valueobjects.base.Page;

import com.google.common.collect.Collections2;

import context.WebContext;
import dto.CmsContent;
import dto.CmsContentLanguage;

public class CmsContentService implements ICmsContentService {

	final static int PAGE_SIZE = 10;

	@Inject
	CmsContentMapper cmsContentMapper;
	
	@Inject
	FoundationService foundation;

	public List<CmsContent> getCmsContentAll() {
		return this.cmsContentMapper.getCmsContentAll();
	}

	public List<CmsContent> getNominatetCmsContent(Integer languageId) {
		return this.cmsContentMapper.getNominatetCmsContent(languageId);
	}

	public Page<CmsContent> getCmsContentPage(Integer page) {

		List<CmsContent> list = this.cmsContentMapper.getCmsContentPage(page,
				PAGE_SIZE);

		int total = this.cmsContentMapper.getCmsContentCount();

		Collection<CmsContent> coll = Collections2.transform(list, cms -> {
			CmsContent cmsVo = new CmsContent();
			BeanUtils.copyProperties(cms, cmsVo);
			return cmsVo;
		});

		List<CmsContent> cmsContentList = new ArrayList<CmsContent>(coll);

		Page<CmsContent> p = new Page<CmsContent>(cmsContentList, total, page,
				PAGE_SIZE);

		return p;
	}

	public boolean addCmsContent(CmsContent cmsContent) {

		int result = this.cmsContentMapper.insert(cmsContent);
		return result > 0 ? true : false;
	}

	public CmsContent getCmsContent(Integer iid, Integer languageId) {

		CmsContent cmsContent = this.cmsContentMapper.selectByPrimaryKey(iid, languageId);

		return cmsContent;
	}

	public List<CmsContent> getCmsContentByMenuIds(List<Integer> imenuids, WebContext ctx) {

		List<CmsContent> cmsContent = this.cmsContentMapper
				.getCmsContentByMenuIds(imenuids, foundation.getLanguage(ctx),
						foundation.getSiteID(ctx), foundation.getDevice(ctx));

		return cmsContent;
	}

	/* (non-Javadoc)
	 * @see services.base.cms.ICmsContentService#getCmsContentByMenuId(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<CmsContent> getCmsContentByMenuId(Integer imenuid,
			WebContext ctx) {

		List<CmsContent> cmsContent = this.cmsContentMapper
				.getCmsContentByMenuId(imenuid, foundation.getLanguage(ctx),
						foundation.getSiteID(ctx), foundation.getDevice(ctx));

		return cmsContent;
	}

	public boolean deleteCmsContent(Integer iid) {
		int result = cmsContentMapper.deleteByPrimaryKey(iid);
		return result > 0 ? true : false;
	}

	public boolean updateCmsContent(CmsContent cmsContent) {
		int result = cmsContentMapper.updateByPrimaryKeySelective(cmsContent);
		return result > 0 ? true : false;
	}

	public Page<CmsContent> searchCmsContentPage(Integer page, String ckey) {

		List<CmsContent> list = this.cmsContentMapper.searchCmsContentPage(
				page, PAGE_SIZE, ckey);

		int total = this.cmsContentMapper.searchCmsContentCount(ckey);

		Collection<CmsContent> coll = Collections2.transform(list, cms -> {
			CmsContent cmsVo = new CmsContent();
			BeanUtils.copyProperties(cms, cmsVo);
			return cmsVo;
		});

		List<CmsContent> cmsContentList = new ArrayList<CmsContent>(coll);

		Page<CmsContent> p = new Page<CmsContent>(cmsContentList, total, page,
				PAGE_SIZE);

		return p;
	}

	public List<CmsContentLanguage> getAllLangContentByMenuId(Integer menuId) {
		return this.cmsContentMapper.getAllLangContentByMenuId(menuId);
	}

	public CmsContentLanguage addCmsContentMoreLanguage(
			CmsContentLanguage cmsContentLanguage) {

		this.cmsContentMapper.insertCmsContentLanguage(cmsContentLanguage);

		return cmsContentLanguage;
	}

	public boolean deleteCmsContentMoreLanguage(Integer iid) {
		int result = this.cmsContentMapper.deleteCmsContentMoreLanguage(iid);

		return result > 0 ? true : false;
	}

	public boolean validateCmsContentMoreLanguage(Integer icmscontentid,
			Integer ilanguageid) {
		CmsContentLanguage cmsContentLanguage = cmsContentMapper
				.getCmsContentByLangIdAndContentId(icmscontentid, ilanguageid);
		
		return cmsContentLanguage == null ? true : false;
	}
	
	
	public CmsContentLanguage getCmsContentByLangIdAndContentId(Integer icmscontentid,
			Integer ilanguageid) {
		CmsContentLanguage cmsContentLanguage = cmsContentMapper
				.getCmsContentByLangIdAndContentId(icmscontentid, ilanguageid);
		
		return cmsContentLanguage;
	}
	
	public List<CmsContent> getCmsContentByKey(Integer page, Integer pageSiz,
			String ckey) {
		return this.cmsContentMapper.searchCmsContentPage(page, pageSiz, ckey);
	}
}
