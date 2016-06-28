package services;

import java.util.List;

import context.WebContext;
import dto.CmsContent;

public interface ICmsContentService {

	public abstract List<CmsContent> getCmsContentByMenuId(Integer imenuid,
			WebContext webContext);

	public List<CmsContent> getCmsContentByKey(Integer page, Integer pageSiz,
			String ckey);

	public List<CmsContent> getNominatetCmsContent(Integer languageId);

	public List<CmsContent> getCmsContentByMenuIds(List<Integer> imenuids,
			WebContext ctx);
}