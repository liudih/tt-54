package controllers.mobile.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import services.mobile.MobileService;
import services.mobile.product.CategoryService;
import services.mobile.product.ProductService;
import utils.MsgUtils;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseListJson;
import valuesobject.mobile.BasePageJson;
import valuesobject.mobile.BaseResultType;
import controllers.mobile.TokenController;
import dto.mobile.CategoryCompositeInfo;
import dto.mobile.CategoryInfo;
import dto.mobile.ProductLiteInfo;

public class CategoryController extends TokenController {

	@Inject
	CategoryService categoryService;

	@Inject
	ProductService productService;

	@Inject
	MobileService mobileService;

	/**
	 * 查询所有大类别
	 * 
	 * @param websiteid
	 * @param languageid
	 * @return
	 */
	public Result getRootCategorys(int max) {
		try {
			List<CategoryInfo> resultList = categoryService
					.getRootCategorys(max);
			if (resultList != null && resultList.size() > 0) {
				BaseListJson<CategoryInfo> result = new BaseListJson<CategoryInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setList(resultList);
				return ok(Json.toJson(result));
			}
		} catch (Exception p) {
			Logger.error("Exception", p);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
		return ok(Json.toJson(result));
	}

	/**
	 * 查询子分类
	 * 
	 * @param categoryId
	 * @return
	 */
	public Result getCategorysByParentId(int cid, int max, int depth) {
		try {
			List<CategoryCompositeInfo> resultList = categoryService
					.getCategorysByParentId(cid, max, depth);
			if (resultList != null && resultList.size() > 0) {
				BaseListJson<CategoryCompositeInfo> result = new BaseListJson<CategoryCompositeInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setList(resultList);
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Exception", e);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
		return ok(Json.toJson(result));
	}

	/**
	 * 查询某一类别的商品列表
	 * 
	 * @return
	 */
	public Result showCategoryProduct(Integer cid, Integer p, Integer size) {
		try {
			if(size==null || size.intValue()==0) size = 12; //初始值12
			if (cid != null && cid == 151225) {
				List<ProductLiteInfo> products = productService
						.getCustomProducts(getGids(), 1);
				BaseListJson<ProductLiteInfo> result = new BaseListJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setList(products);
				return ok(Json.toJson(result));
			}
			Map<String, String[]> queryStrings = request().queryString();
			utils.Page<ProductLiteInfo> resultList = productService
					.getCategoryProducts(cid, p, size, queryStrings);
			if (resultList != null && resultList.getList().size() > 0) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setTotal(resultList.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultList.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
		return ok(Json.toJson(result));
	}

	private List<String> getGids() {
		List<String> gids = new ArrayList<String>();
		gids.add("9ba351d4-ef0d-42d6-a351-d4ef0d82d6fe");
		gids.add("85c3a43b-44c4-4903-83a4-3b44c469037e");
		gids.add("986acf04-d941-1004-8d00-f7eee594cf91");
		gids.add("04600221-d914-1004-874c-d371c9ab96c0");
		gids.add("006f6f35-d914-1004-874c-d371c9ab96c0");
		gids.add("983a9753-7304-45e3-ba97-53730475e343");
		gids.add("5bdf373b-0912-4804-9f37-3b0912080413");
		gids.add("d578b4f0-d929-1004-835b-90389054983d");
		gids.add("ca54e13a-1834-41c8-94e1-3a183451c8eb");
		gids.add("09ae10b4-6a1d-46b6-ae10-b46a1df6b6b0");
		gids.add("006db768-d914-1004-874c-d371c9ab96c0");
		gids.add("0067e915-d914-1004-874c-d371c9ab96c0");
		gids.add("4f3ecdd6-3cb9-47eb-becd-d63cb967ebce");
		gids.add("00716f59-d914-1004-874c-d371c9ab96c0");
		gids.add("00685e43-d914-1004-874c-d371c9ab96c0");
		gids.add("c9e874f0-f3f1-46b4-a874-f0f3f176b4d0");
		gids.add("d57a3bf4-d929-1004-835b-90389054983d");
		gids.add("006330d9-d914-1004-874c-d371c9ab96c0");
		gids.add("1aba93e3-94ae-4397-ba93-e394ae339771");
		gids.add("c2197134-c663-42c3-9971-34c66342c3e0");
		gids.add("045fba45-d914-1004-874c-d371c9ab96c0");
		gids.add("006d4233-d914-1004-874c-d371c9ab96c0");
		gids.add("dc5f0d3a-857a-4c6f-9f0d-3a857a7c6ff4");
		gids.add("4d7f6ece-820c-4504-bf6e-ce820c7504d0");
		gids.add("04649794-d914-1004-874c-d371c9ab96c0");
		gids.add("0066b02e-d914-1004-874c-d371c9ab96c0");
		gids.add("6311f8cd-ed31-4cb3-91f8-cded316cb3cc");
		gids.add("695ce7de-0102-419a-9ce7-de0102d19a0b");
		gids.add("3b27ebe7-64b1-462f-a7eb-e764b1f62f1d");
		gids.add("30df0e2e-bdb0-4a3d-9f0e-2ebdb0fa3d47");
		gids.add("b2f9f0ad-b0f9-4746-b9f0-adb0f9e74673");
		gids.add("3ed9bdcb-5111-4575-99bd-cb511155753b");
		gids.add("422ac0e8-a565-4b4d-aac0-e8a5657b4df0");
		gids.add("44723032-d318-48f8-b230-32d318d8f8bb");
		gids.add("5e8e29ca-0ce8-4b3d-8e29-ca0ce8fb3d16");
		gids.add("00cc5a02-c368-4b28-8c5a-02c368bb2881");
		gids.add("bc61dc80-a3f6-4227-a1dc-80a3f6f2277b");
		gids.add("e54730ee-2374-4dd1-8730-ee23743dd1d0");
		gids.add("254f93ad-a7d3-42a1-8f93-ada7d322a187");
		return gids;
	}
}
