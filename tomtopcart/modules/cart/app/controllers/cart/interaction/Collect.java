package controllers.cart.interaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.collect.Collections2;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.member.login.ILoginService;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dto.interaction.ProductCollect;
import services.interaction.ICollectService;

public class Collect extends Controller {

	@Inject
	ICollectService collectService;

	@Inject
	FoundationService foundation;

	@Inject
	ILoginService loginService;

	public static final int START_PAGE = 1;

	public static final int PAGE_SIZE = 30;

	@BodyParser.Of(BodyParser.Json.class)
	public Result collect(String lid, String action) {
		boolean flag = false;
		Map<String,Object> mjson = new HashMap<String,Object>();
		String email = "";
		if (foundation.getLoginContext().isLogin()) {
			email = foundation.getLoginContext().getMemberID();
		} else {
			mjson.put("result", "nologin");
			return ok(Json.toJson(mjson));
		}
		// add all
		if ("addall".equals(action) && !"".equals(lid)) {
			String[] listingArr = lid.split(",");
			List<String> listingids = Lists.newArrayList(listingArr);
			listingids = ImmutableSet.copyOf(listingids).asList();
			List<String> existlist = collectService
					.getCollectListingIDByEmail(email);
			listingids = Lists.newArrayList(Collections2.filter(listingids,
					list -> !existlist.contains(list)));
			for (String ids : listingids) {
				collectService.addCollect(ids, email);
			}
			flag = true;
		}
		if ("add".equals(action) && lid != null && !"".equals(lid)) {
			List<ProductCollect> clist = collectService.getCollectByMember(lid,
					email);
			if (clist.size() > 0) {
				mjson.put("result", "This item is already on your wish list!");
				return ok(Json.toJson(mjson));
			}
			flag = collectService.addCollect(lid, email);
		} else if ("del".equals(action) && lid != null && !"".equals(lid)) {
			flag = collectService.delCollect(lid, email);
		}
		if (flag) {
			mjson.put("result", "success");
			return ok(Json.toJson(mjson));
		} else {
			mjson.put("result", "error");
			return ok(Json.toJson(mjson));
		}
	}

	public Result delCollect(String lids) {
		Map<String,Object> mjson = new HashMap<String,Object>();
		String email = foundation.getLoginContext().getMemberID();
		boolean islogin = foundation.getLoginContext().isLogin();
		if(!islogin){
			mjson.put("result", "no login");
			return ok(Json.toJson(mjson));
		}
		boolean flag = false;
		if (lids != null && !"".equals(lids)) {
			flag = collectService.delCollectByListingids(lids, email);
		}
		if (flag) {
			mjson.put("result", "success");
		} else {
			mjson.put("result", "error");
		}
		return ok(Json.toJson(mjson));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result getcollect(String listingid) {
		String email = "";
		Map<String,Object> mjson = new HashMap<String,Object>();
		if (foundation.getLoginContext().isLogin()) {
			email = foundation.getLoginContext().getMemberID();
		} else {
			mjson.put("result", "nologin");
			return ok(Json.toJson(mjson));
		}
		List<String> list = collectService.getCollectListingIDByEmail(email);
		// 计算商品收藏数量
		if (listingid != null && !"".equals(listingid)) {
			int count = collectService.getCountByListingID(listingid);
			Object[] o = new Object[] { list, count };
			return ok(Json.toJson(o));
		}

		return ok(Json.toJson(list));
	}


}