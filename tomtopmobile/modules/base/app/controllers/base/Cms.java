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
import services.ICmsContentService;
import services.ICmsMenuService;
import services.base.FoundationService;
import cms.model.CmsType;
import dto.CmsContent;
import dto.CmsMenu;
import dto.CmsMenuLanguage;

public class Cms extends Controller {

	@Inject
	ICmsContentService cmsContentService;

	@Inject
	FoundationService foundation;

	public Result getCmsContentByMenuId(Integer imenuid) {

		List<CmsContent> cmsContetList = this.cmsContentService
				.getCmsContentByMenuId(imenuid, foundation.getWebContext());

		return ok(views.html.base.mobile.cms.cms_detail.render(cmsContetList));
	}
}
