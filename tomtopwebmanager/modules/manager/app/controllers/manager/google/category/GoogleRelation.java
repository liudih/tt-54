package controllers.manager.google.category;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.google.category.GoogleCategoryRelationMapper;
import mapper.product.CategoryNameMapper;
import mapper.product.CategoryWebsiteMapper;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.product.google.feeds.IGoogleCategoryBaseService;
import valueobjects.base.Page;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;

import controllers.InterceptActon;
import dto.product.CategoryName;
import dto.product.CategoryWebsiteWithName;
import dto.product.google.category.GoogleCategory;
import dto.product.google.category.GoogleCategoryRelation;

@With(InterceptActon.class)
public class GoogleRelation extends Controller {

	@Inject
	IGoogleCategoryBaseService categoryBaseService;

	@Inject
	GoogleCategoryRelationMapper relationMapper;

	@Inject
	CategoryNameMapper enquiryService;

	@Inject
	IGoogleCategoryBaseService googleCategoryBaseService;

	@Inject
	CategoryWebsiteMapper platformMapper;

	public Result getAll(int p, String cpath) {

		List<CategoryWebsiteWithName> rootCategories = platformMapper
				.getFirstCategory();

		dto.product.google.category.GoogleCategory googleBase = new GoogleCategory();
		if (cpath == null || cpath.equals("")) {
			String path = request().getQueryString("cpath");
			googleBase.setCpath(path);
		} else {
			googleBase.setCpath(cpath);
		}

		Page<dto.product.google.category.GoogleCategory> categoryList = categoryBaseService
				.getAll(p, 15, googleBase);
		Map<Integer, List<Integer>> idMaps = Maps.newHashMap();
		Map<List<Integer>, List<CategoryName>> cateMap = Maps.newHashMap();
		List<CategoryName> names = Lists.newArrayList();
		if (categoryList.getList() != null
				&& categoryList.getList().size() != 0) {
			for (GoogleCategory g : categoryList.getList()) {
				List<Integer> cid = relationMapper
						.getCidByGid(g.getIcategory());
				if (cid != null && cid.size() != 0) {
					idMaps.put(g.getIcategory(), cid);
					names = enquiryService.getAllByCid(cid);
					cateMap.put(cid, names);
					cateMap.get(idMaps.get(g.getIcategory())).get(0)
							.getIcategoryid();
				}

			}
		}
		return ok(views.html.manager.google.category.google_relation.render(
				categoryList, idMaps, cateMap, cpath, rootCategories));
	}

	public Result getWebChild(int cid) {
		List<CategoryWebsiteWithName> rootlists = platformMapper
				.getChildByParentId(cid);
		return ok(Json.toJson(rootlists));
	}

	public Result addRelation(int gid, int wid) {
		String r = "";
		int result = relationMapper.add(gid, wid);
		if (result > 0) {
			r = "Add successful!";
		}
		return ok(Json.toJson(r));
	}

	public Result checkRelation(int gid, int wid) {
		String r = "";
		GoogleCategoryRelation relation = relationMapper
				.getRelationByGidAndWid(gid, wid);
		if (null != relation) {
			r = "关联关系已存在！";
		}

		return ok(Json.toJson(r));
	}

	public Result getDetail(int gid) {
		List<GoogleCategoryRelation> relations = relationMapper
				.getRelationByGid(gid);
		List<CategoryName> detailMaps = Lists.newArrayList();
		if (null != relations && relations.size() != 0) {
			for (int i = 0; i < relations.size(); i++) {
				GoogleCategoryRelation relation = relations.get(i);
				CategoryName name = enquiryService
						.getCategoryNameByCategoryIdAndLanguage(
								relation.getIcategory(), 1);
				detailMaps.add(name);
			}
		}
		return ok(Json.toJson(detailMaps));
	}

	public Result delRelation(int cid, int gid) {
		String result = "";
		int r = relationMapper.deleteRelationByGidAndWid(gid, cid);
		if (r > 0) {
			result = "successful!";
		}
		return ok(Json.toJson(result));
	}
}
