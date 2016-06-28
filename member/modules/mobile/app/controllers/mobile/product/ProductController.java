package controllers.mobile.product;

import interceptor.ViewProduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.With;
import services.mobile.interaction.InteractionCommentService;
import services.mobile.product.ProductService;
import services.product.ProductMultiatributeService;
import utils.Page;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseListJson;
import valuesobject.mobile.BasePageJson;
import valuesobject.mobile.BaseResultType;
import controllers.mobile.TokenController;
import dto.mobile.ProductLiteInfo;
import dto.mobile.SuggestionInfo;

public class ProductController extends TokenController {

	@Inject
	private ProductService productService;

	@Inject
	InteractionCommentService interactionCommentService;

	@Inject
	ProductMultiatributeService productMultiatributeService;

	/**
	 * 商品详情
	 * 
	 * @param gid
	 * @return
	 */
	@With(ViewProduct.class)
	public Result view(final String gid) {
		try {
			Map<String, Object> vo = productService.showProductInfo(gid);
			if (vo != null) {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("re", BaseResultType.SUCCESS);
				result.put("msg", BaseResultType.SUCCESSMSG);
				result.putAll(vo);
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 获取商品简易信息
	 * 
	 * @param gid
	 * @return
	 */
	public Result simpleProduct(final String gid) {
		try {
			Map<String, Object> vo = productService.getSimpleProductAttr(gid);
			if (vo != null) {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("re", BaseResultType.SUCCESS);
				result.put("msg", BaseResultType.SUCCESSMSG);
				result.putAll(vo);
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 查询用户对商品的评论
	 * 
	 * @param gid
	 * @return
	 */
	public Result showInteractionComments(final String gid, int p, int size) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if (p == 1) {
				map.put("info",
						interactionCommentService.getProductComment(gid));
			}
			utils.Page<Map<String, Object>> comments = interactionCommentService
					.getProductCommentPage(gid, p, size);
			if (comments != null && comments.getList().size() > 0) {
				map.put("list", comments.getList());
				map.put("p", comments.getP());
				map.put("size", comments.getSize());
				// map.put("total", comments.getTotal());
			}
			if (!map.isEmpty()) {
				map.put("re", BaseResultType.SUCCESS);
				map.put("msg", BaseResultType.SUCCESSMSG);
				return ok(Json.toJson(map));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 关键词搜索 商品列表
	 * 
	 * @param key
	 * @param p
	 * @param size
	 * @return
	 */
	public Result getProductByKeword(String key, int p, int size) {
		try {
			Map<String, String[]> queryStrings = request().queryString();
			Page<ProductLiteInfo> resultPage = productService
					.getSearchProducts(key, queryStrings, p, size);
			if (resultPage != null) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 免邮商品列表
	 * 
	 * @param p
	 * @param size
	 * @return
	 */
	public Result getFreeProduct(int p, int size) {
		try {
			Map<String, String[]> queryStrings = request().queryString();
			Page<ProductLiteInfo> resultPage = productService.freeShipping(
					queryStrings, p, size);
			if (resultPage != null) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));

	}

	/**
	 * 特色商品列表
	 * 
	 * @param p
	 * @param size
	 * @return
	 */
	public Result getFeaturedProduct(int p, int size) {
		try {
			Map<String, String[]> queryStrings = request().queryString();
			Page<ProductLiteInfo> resultPage = productService.featured(
					queryStrings, p, size);
			if (resultPage != null) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));

	}

	/**
	 * 推荐商品展示
	 * 
	 * @param gid
	 * @param p
	 * @param size
	 * @return
	 */
	public Result getSimilarProducts(String gid, int p, int size) {
		try {
			Page<ProductLiteInfo> resultPage = productService.similarProducts(
					gid, p, size);
			if (resultPage != null) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 查询新上架的产品
	 * 
	 * @param p
	 * @param size
	 * @return
	 */
	public Result getNewProducts(int p, int size) {
		try {
			Map<String, String[]> queryStrings = request().queryString();
			Page<ProductLiteInfo> resultPage = productService
					.getNewArrivalProducts(queryStrings, p, size);
			if (resultPage != null) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 查询热门商品
	 * 
	 * @param p
	 * @param size
	 * @return
	 */
	public Result getHotProducts(int p, int size) {
		try {
			Map<String, String[]> queryStrings = request().queryString();
			Page<ProductLiteInfo> resultPage = productService.getHotProducts(
					queryStrings, p, size);
			if (resultPage != null) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 搜索 联想关键词列表
	 * 
	 * @param q
	 * @return
	 */
	public Result getSuggestion(String q) {
		try {
			List<SuggestionInfo> resultList = productService.getSuggestions(q);
			if (resultList != null && !resultList.isEmpty()) {
				BaseListJson<SuggestionInfo> result = new BaseListJson<SuggestionInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setList(resultList);
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e.fillInStackTrace());
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}
}
