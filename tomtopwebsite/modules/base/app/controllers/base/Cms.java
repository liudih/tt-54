package controllers.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import context.ContextUtils;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Context;
import services.ICmsMenuService;
import services.base.FoundationService;
import services.base.cms.CmsContentService;
import cms.model.CmsType;
import dto.CmsContent;
import dto.CmsMenu;
import dto.CmsMenuLanguage;

public class Cms extends Controller {

	@Inject
	ICmsMenuService cmsMenuService;

	@Inject
	CmsContentService cmsContentService;

	@Inject
	FoundationService foundation;

	public Result getAllCms() {

		List<CmsMenu> oneCmsMenuList = this.cmsMenuService
				.getCmsMenuByLevelIdAndType(1, 0, CmsType.HELPCENTER.getType(),
						ContextUtils.getWebContext(Context.current()));

		Map<Integer, List<CmsMenu>> cmsMenuMap = this.getIMenuIdKeyMap();

		Map<Integer, List<CmsContent>> cmsContetMap = this
				.getIMenuIdContentMap(2);

		List<CmsMenu> nominatetMenuList = this.cmsMenuService
				.getNominatetCmsMenu(1, CmsType.HELPCENTER.getType(),
						ContextUtils.getWebContext(Context.current()));

		List<CmsContent> nominatetContentList = this.cmsContentService
				.getNominatetCmsContent(foundation.getLanguage());

		return ok(views.html.base.cms.render(oneCmsMenuList, cmsMenuMap,
				nominatetMenuList, nominatetContentList, cmsContetMap));
	}

	public Result cmsCustomer(Integer imenuid) {

		List<CmsMenu> oneCmsMenuList = this.cmsMenuService
				.getCmsMenuByLevelIdAndType(1, 0, CmsType.HELPCENTER.getType(),
						ContextUtils.getWebContext(Context.current()));

		Map<Integer, List<CmsMenu>> cmsMenuMap = this.getIMenuIdKeyMap();

		// 根据 imenuid找到文章 及目录
		List<CmsContent> cmsContentList = this.cmsContentService
				.getCmsContentByMenuId(imenuid, foundation.getWebContext());

		return ok(views.html.base.cms_customer.render(oneCmsMenuList,
				cmsMenuMap, cmsContentList));
	}

	private Map<Integer, List<CmsMenu>> getIMenuIdKeyMap() {
		List<CmsMenu> twoCmsMenuList = this.cmsMenuService
				.getCmsMenuByLevelIdAndType(2, 0, CmsType.HELPCENTER.getType(),
						foundation.getWebContext());

		Map<Integer, List<CmsMenu>> cmsMenuMap = new HashMap<Integer, List<CmsMenu>>();

		for (CmsMenu cm : twoCmsMenuList) {

			List<CmsMenu> cmsMenuList = null;
			if (cmsMenuMap.containsKey(cm.getIparentid())) {
				cmsMenuList = cmsMenuMap.get(cm.getIparentid());
				cmsMenuList.add(cm);

			} else {
				cmsMenuList = new ArrayList<CmsMenu>();
				cmsMenuList.add(cm);
			}

			cmsMenuMap.put(cm.getIparentid(), cmsMenuList);
		}
		return cmsMenuMap;
	}

	private Map<Integer, List<CmsContent>> getIMenuIdContentMap(Integer levelId) {

		List<Integer> cmsMenuIdList = this.cmsMenuService
				.getMenuIdByLevelIdAndType(levelId,
						CmsType.HELPCENTER.getType(),
						foundation.getWebContext());

		return getMenuIdKeyMap(cmsMenuIdList);
	}

	private Map<Integer, List<CmsContent>> getMenuIdKeyMap(
			List<Integer> cmsMenuIdList) {
		List<CmsContent> allContentList = this.cmsContentService
				.getCmsContentByMenuIds(cmsMenuIdList,
						foundation.getWebContext());

		Map<Integer, List<CmsContent>> cmsMenuMap = new HashMap<Integer, List<CmsContent>>();

		for (CmsContent cm : allContentList) {

			List<CmsContent> cmsContentList = null;
			if (cmsMenuMap.containsKey(cm.getImenuid())) {
				cmsContentList = cmsMenuMap.get(cm.getImenuid());
				cmsContentList.add(cm);

			} else {
				cmsContentList = new ArrayList<CmsContent>();
				cmsContentList.add(cm);
			}

			cmsMenuMap.put(cm.getImenuid(), cmsContentList);
		}
		return cmsMenuMap;
	}

	public Result getCmsContentByMenuId(Integer imenuid) {

		List<CmsMenu> oneCmsMenuList = this.cmsMenuService
				.getCmsMenuByLevelIdAndType(1, 0, CmsType.HELPCENTER.getType(),
						foundation.getWebContext());

		Map<Integer, List<CmsMenu>> cmsMenuMap = this.getIMenuIdKeyMap();

		CmsMenuLanguage cmsMenuLanguage = cmsMenuService
				.getCmsMenuMoreLanguage(imenuid, foundation.getLanguage());
		String menuName = null;
		if (null != cmsMenuLanguage) {
			menuName = cmsMenuLanguage.getCmenuname();
		}

		List<CmsContent> cmsContetList = this.cmsContentService
				.getCmsContentByMenuId(imenuid, foundation.getWebContext());

		return ok(views.html.base.cms_detail.render(oneCmsMenuList, cmsMenuMap,
				menuName, cmsContetList));
	}
}
