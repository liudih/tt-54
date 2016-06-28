package controllers.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.category.recommend.CategoryProductRecommendService;
import services.manager.CategoryManagerService;
import services.product.CategoryEnquiryService;
import session.ISessionService;
import util.AppsUtil;

import com.google.common.eventbus.EventBus;

import controllers.InterceptActon;
import dto.Vhost;
import dto.Website;
import dto.category.CategoryProductRecommend;
import dto.product.CategoryWebsiteWithName;
import entity.manager.AdminUser;
import events.product.CategoryRecommendEvent;
import forms.CategoryRecommendFrom;

@With(InterceptActon.class)
public class CategoryRecommend extends Controller {
	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	CategoryManagerService categoryManagerService;

	@Inject
	CategoryProductRecommendService categoryProductRecommendService;

	@Inject
	ISessionService sessionService;
	@Inject
	EventBus eventBus;

	/**
	 * 品类推荐产品设置管理
	 * 
	 * 
	 */
	public Result categoryRecommendManager(Integer parentid, Integer parentid2,
			Integer parentid3, String sku, String createdate, Integer siteid,
			String sdevice) {
		List<CategoryWebsiteWithName> rootCategories = categoryProductRecommendService
				.getRootCategory(1, 1);
		List<Website> website = categoryProductRecommendService.getWebsiteAll();
		List<Vhost> vhost = categoryProductRecommendService.getDeviceAll();
		List<CategoryProductRecommend> cprList = new ArrayList<CategoryProductRecommend>();
		if (parentid <= 0) {
			parentid = null;
		}
		if (siteid <= 0) {
			siteid = null;
		}
		if ("".equals(AppsUtil.trim(sku))) {
			sku = null;
		}
		if ("".equals(AppsUtil.trim(createdate))) {
			createdate = null;
		}
		if ("".equals(AppsUtil.trim(sdevice))) {
			sdevice = null;
		}
		Integer categoryid = 0;
		if (parentid != null) {
			if (parentid3 != 0) {
				categoryid = parentid3;
			} else if (parentid2 != 0) {
				categoryid = parentid2;
			} else {
				categoryid = parentid;
			}
		}
		if (parentid != null || sku != null || createdate != null
				|| siteid != null || sdevice != null) {
			cprList = categoryProductRecommendService.select(parentid,
					categoryid, sku, createdate, siteid, sdevice);
		}
		if (parentid == null) {
			parentid = -1;
		}
		if (siteid == null) {
			siteid = -1;
		}
		return ok(views.html.manager.categoryrecommend.recommendlist.render(
				rootCategories, cprList, vhost, website, parentid, parentid2,
				parentid3, sku, createdate, siteid, sdevice));
	}

	/**
	 * 查询父类
	 * 
	 * 
	 */
	public Result rootCategory(Integer siteid) {
		Map<Integer, Object> object = new HashMap<Integer, Object>();

		List<CategoryWebsiteWithName> rootCategories = categoryProductRecommendService
				.getRootCategory(1, siteid);

		if (rootCategories.size() > 0) {
			for (CategoryWebsiteWithName categoryWebsiteWithName : rootCategories) {
				object.put(categoryWebsiteWithName.getIcategoryid(),
						categoryWebsiteWithName.getCname());
			}
		} else {
			object.put(-1, "");
		}
		return ok(Json.toJson(object));
	}

	/**
	 * 根据父类查询子分类
	 * 
	 * 
	 */
	public Result childCategory(Integer categoryid, Integer siteid) {
		Map<Integer, Object> object = new HashMap<Integer, Object>();

		List<CategoryWebsiteWithName> cprList = categoryProductRecommendService
				.getChildCategory(categoryid, 1, siteid);

		if (cprList.size() > 0) {
			for (CategoryWebsiteWithName categoryWebsiteWithName : cprList) {
				object.put(categoryWebsiteWithName.getIcategoryid(),
						categoryWebsiteWithName.getCname());
			}
		} else {
			object.put(-1, "");
		}
		return ok(Json.toJson(object));
	}

	/**
	 * 添加品类推荐产品
	 * 
	 * 
	 */
	public Result addCategoryRecommend() {
		Form<CategoryRecommendFrom> form = Form.form(
				CategoryRecommendFrom.class).bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		CategoryRecommendFrom crfrom = form.get();
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		List<Integer> retint = categoryProductRecommendService.insert(crfrom,
				user.getCusername());
		int p1 = 0;
		int p2 = 0;
		int p3 = 0;
		if (retint.size() == 1)
			p1 = retint.get(0);
		if (retint.size() == 2)
			p2 = retint.get(1);
		if (retint.size() == 3)
			p3 = retint.get(2);
		return redirect(controllers.manager.routes.CategoryRecommend
				.categoryRecommendManager(p1, p2, p3, null,
						AppsUtil.getTodayStr(), crfrom.getWebsiteid(),
						crfrom.getDevice()));
	}

	/**
	 * 品类推荐产品历史记录
	 * 
	 * 
	 */
	public Result categoryRecommendHist(Integer parentid, Integer parentid2,
			Integer parentid3, String sku, String createdate, Integer siteid,
			String sdevice) {
		List<CategoryWebsiteWithName> rootCategories = categoryProductRecommendService
				.getRootCategory(1, 1);
		List<Website> website = categoryProductRecommendService.getWebsiteAll();
		List<Vhost> vhost = categoryProductRecommendService.getDeviceAll();
		List<CategoryProductRecommend> cprList = new ArrayList<CategoryProductRecommend>();
		if (parentid <= 0) {
			parentid = null;
		}
		if (siteid <= 0) {
			siteid = null;
		}
		if ("".equals(AppsUtil.trim(sku))) {
			sku = null;
		}
		if ("".equals(AppsUtil.trim(createdate))) {
			createdate = null;
		}
		if ("".equals(AppsUtil.trim(sdevice))) {
			sdevice = null;
		}
		Integer categoryid = 0;
		if (parentid != null) {
			if (parentid3 != 0) {
				categoryid = parentid3;
			} else if (parentid2 != 0) {
				categoryid = parentid2;
			} else {
				categoryid = parentid;
			}
		}
		if (parentid != null || sku != null || createdate != null
				|| siteid != null || sdevice != null) {
			cprList = categoryProductRecommendService.selectHistory(parentid,
					categoryid, sku, createdate, siteid, sdevice);
		}
		if (parentid == null) {
			parentid = -1;
		}
		if (siteid == null) {
			siteid = -1;
		}
		return ok(views.html.manager.categoryrecommend.recommendhist.render(
				rootCategories, cprList, vhost, website, parentid, parentid2,
				parentid3, sku, createdate, siteid, sdevice));
	}

	/**
	 * 删除品类推荐产品记录
	 * 
	 * 
	 */
	public Result delete(Integer id, Integer sequence, Integer parentid,
			Integer returnid, Integer returnid2, Integer returnid3, String sku,
			String createdate, Integer siteid, String sdevice) {

		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		categoryProductRecommendService.deleteCategoryRecommend(id, parentid,
				sequence, user.getCusername(), siteid, sdevice);
		return redirect(controllers.manager.routes.CategoryRecommend
				.categoryRecommendManager(returnid, returnid2, returnid3, sku,
						createdate, siteid, sdevice));
	}

	/**
	 * 品类推荐产品置顶排序
	 * 
	 * 
	 */
	public Result onTop(Integer id, Integer sequence, Integer parentid,
			Integer returnid, Integer returnid2, Integer returnid3, String sku,
			String createdate, Integer siteid, String sdevice) {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		categoryProductRecommendService.onTopCategoryRecommend(id, parentid,
				sequence, user.getCusername(), siteid, sdevice);
		return redirect(controllers.manager.routes.CategoryRecommend
				.categoryRecommendManager(returnid, returnid2, returnid3, sku,
						createdate, siteid, sdevice));
	}

	public Result checkSku(String sku, Integer categoryid, Integer siteid,
			String device) {
		Map<Integer, Object> object = new HashMap<Integer, Object>();
		if (sku == null) {
			object.put(0, "false");
			return ok(Json.toJson(object));
		}
		// check sku 是否在 分类下
		String listingid = categoryProductRecommendService
				.checkProductCategoryBySku(sku, categoryid);
		if (listingid == null) {
			object.put(-1, "sku not in category");
			return ok(Json.toJson(object));
		}
		Integer num = categoryProductRecommendService
				.checkCategoryRecommendBySku(sku, categoryid, siteid, device);
		if (num > 0) {
			object.put(-1, "category sku is existed");
			return ok(Json.toJson(object));
		}
		object.put(1, "success");
		return ok(Json.toJson(object));
	}

	public Result updateRecommendSearch() {
		eventBus.post(new CategoryRecommendEvent());

		return ok("success");
	}

	/**
	 * 对接搜索引擎
	 * 
	 * @return
	 */
	public Result importRecommend() {
		String result = categoryProductRecommendService.getRecommends();
		return ok(result);
	}
}
