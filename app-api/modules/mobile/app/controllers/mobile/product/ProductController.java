package controllers.mobile.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.expr.NewArray;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import services.mobile.MobileService;
import services.mobile.interaction.InteractionCommentService;
import services.mobile.personal.SettingService;
import services.mobile.product.InterestTagService;
import services.mobile.product.ProductService;
import services.product.IProductBaseEnquiryService;
import utils.ImageUtils;
import utils.MsgUtils;
import utils.Page;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseListJson;
import valuesobject.mobile.BasePageJson;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.member.MobileContext;
import base.util.httpapi.ApiUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import controllers.mobile.TokenController;
import dto.mobile.ProductLiteInfo;
import dto.mobile.SuggestionInfo;

public class ProductController extends TokenController {

	@Inject
	ProductService productService;
	@Inject
	InteractionCommentService interactionCommentService;
	@Inject
	SettingService settingService;
	@Inject
	MobileService mobileService;
	@Inject
	InterestTagService interestTagService;
	@Inject
	IProductBaseEnquiryService productBaseEnquiryService;
	
	//推荐商品接口地址
	private final  String recommedListUrl = "http://product.api.tomtop.com/ic/v2/product/alsoBought";

	/**
	 * 商品详情
	 * 
	 * 
	 * 获取推荐商品，指定数量2
	 * 优先调用新接口，当新街口返回时报或者没有数据时，调用旧的推荐接口
	 * 
	 * @param gid
	 * @return
	 */
	public Result view( String gid) {
		try {
			if(StringUtils.isBlank(gid)) return null;
			if( !(gid.length()==36 && gid.split("\\-").length==5)){
				gid = productBaseEnquiryService.getListingsBySku(gid, mobileService.getWebSiteID());
				if(StringUtils.isBlank(gid)) return null;
			}
			Map<String, Object> vo = productService.showProductInfo(gid);
			if (vo != null) {
					String url = recommedListUrl+"?listingId="+gid+"&website="+mobileService.getWebSiteID()+
							"&lang="+mobileService.getLanguageID()+"&currency="+mobileService.getCurrency();
					Logger.debug("before send get date- url:"+url);
					String resultBody = new ApiUtil().get(url);
					Logger.debug("end send get date- resultBody:"+resultBody);
				if (StringUtils.isBlank(resultBody)|| !"1".equals(JSONObject.parseObject(resultBody).get("ret").toString())) { //交互成功
					@SuppressWarnings("unchecked")
					List<ProductLiteInfo> recommend = (List<ProductLiteInfo>) vo
							.get("recommend");
					if (recommend == null || recommend.size() <= 0) {
						Map<String, String[]> queryStrings = request()
								.queryString();
						utils.Page<ProductLiteInfo> featuredPage = productService
								.featured(queryStrings, 1, 20);
						if (featuredPage != null && featuredPage.getList() != null) {
							List<ProductLiteInfo> newRecom = Lists.newArrayList();
							newRecom.addAll(featuredPage.getList());
							Collections.shuffle(newRecom);
							vo.put("recommend",
									newRecom.size() > 6 ? newRecom.subList(0, 6)
											: newRecom);
						}
					}
				}else{
					List<ProductLiteInfo> InfoList =getResultPage(resultBody);
					if (InfoList != null && !InfoList.isEmpty()) {
						List<ProductLiteInfo> newRecom = Lists.newArrayList();
						newRecom.addAll(InfoList);
						Collections.shuffle(newRecom);
						vo.put("recommend",
								newRecom.size() > 6 ? newRecom.subList(0, 6)
										: newRecom);
					}
				}
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("re", BaseResultType.SUCCESS);
				result.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.putAll(vo);
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
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
				result.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.putAll(vo);
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
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
			if (comments != null && comments.getList() != null
					&& comments.getList().size() > 0) {
				map.put("list", comments.getList());
				map.put("p", comments.getP());
				map.put("size", comments.getSize());
				// map.put("total", comments.getTotal());
			}
			if (!map.isEmpty()) {
				map.put("re", BaseResultType.SUCCESS);
				map.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
				return ok(Json.toJson(map));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
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
			Logger.debug("getProductByKeword------key="+key+",p="+p+",size="+size);
			Page<ProductLiteInfo> resultPage = productService
					.getSearchProducts(key, queryStrings, p, size);
			Logger.debug("getProductByKeword------resultPage="+JSON.toJSONString(resultPage));
			if (resultPage != null && resultPage.getList() != null
					&& resultPage.getList().size() > 0) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			} else {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("re", BaseResultType.SUCCESS);
				result.put("msg", MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.put("total", 0);
				result.put("p", 1);
				result.put("size", 0);
				result.put("list", Lists.newArrayList());
				result.put("recommend",
						productService.getHotProducts(queryStrings, 1, 4)
								.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		// BaseJson result = new BaseJson();
		// result.setRe(BaseResultType.ERROR);
		// result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
		// return ok(Json.toJson(result));
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
			if (resultPage != null && resultPage.getList() != null
					&& resultPage.getList().size() > 0) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
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
	 * 超级特卖商品
	 * 
	 * @param p
	 * @param size
	 * @return
	 */
	public Result getSuperDealProducts(int p, int size) {
		try {
			Map<String, String[]> queryStrings = request().queryString();
			Page<ProductLiteInfo> resultPage = productService.superDeals(
					queryStrings, p, size);
			if (resultPage != null && resultPage.getList() != null
					&& resultPage.getList().size() > 0) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
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
	 * 特色商品列表 (替换部分推荐商品)
	 * 
	 * @param p
	 * @param size
	 * @return
	 */
	public Result getFeaturedProduct(int p, int size) {
		// try {
		Map<String, String[]> queryStrings = request().queryString();
		Page<ProductLiteInfo> resultPage = productService.featured(
				queryStrings, p, size);
		List<ProductLiteInfo> reusltList = null;
		if (resultPage != null) {
			reusltList = this.getInterestProduct(p, size, resultPage.getList());
		}
		if (reusltList != null && reusltList.size() > 0) {
			BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
			result.setRe(BaseResultType.SUCCESS);
			result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
			result.setTotal(resultPage.getTotal());
			result.setP(p);
			result.setSize(size);
			result.setList(reusltList);
			return ok(Json.toJson(result));
		}
		// } catch (Exception e) {
		// Logger.error("Product Exception", e);
		// BaseJson result = new BaseJson();
		// result.setRe(BaseResultType.EXCEPTION);
		// result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
		// return ok(Json.toJson(result));
		// }
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
		return ok(Json.toJson(result));
	}

	/**
	 * 在给出的的商品中，随机混合推荐商品 （比例为 3/10）
	 * 
	 * @param p
	 * @param size
	 * @param old
	 * @return
	 */
	public List<ProductLiteInfo> getInterestProduct(int p, int size,
			List<ProductLiteInfo> old) {
		if (old != null && old.size() > 0) {
			List<ProductLiteInfo> resultList = new ArrayList<ProductLiteInfo>();
			MobileContext mcontext = mobileService.getMobileContext();
			if (mcontext != null) {
				// 类型 1 代表 类别， 2 代表关键词
				List<String> cids = interestTagService.getInterestTags(
						mcontext.getCimei(), 1);
				if (cids != null && cids.size() > 0) {
					Map<String, String[]> queryStrings = request()
							.queryString();
					Page<ProductLiteInfo> cProduct = productService
							.getCategorysProducts(cids, p, size, queryStrings);
					if (cProduct != null && cProduct.getList() != null
							&& cProduct.getList().size() > 0) {
						List<ProductLiteInfo> cProductList = new ArrayList<ProductLiteInfo>();
						cProductList.addAll(cProduct.getList());
						// 随机排序
						Collections.shuffle(cProductList);
						int count = cProductList.size() > 3 / 10 * size ? 3 / 10 * size
								: cProductList.size();
						resultList.addAll(cProductList.subList(0,
								count > 1 ? count : 1));
						List<ProductLiteInfo> oldProducts = new ArrayList<ProductLiteInfo>();
						oldProducts.addAll(old);
						// 随机排序原有商品
						Collections.shuffle(oldProducts);
						if (oldProducts.size() > resultList.size()) {
							resultList.addAll(oldProducts.subList(0,
									oldProducts.size() - resultList.size()));
						} else {
							resultList.addAll(oldProducts);
						}
						Collections.shuffle(resultList);
						return resultList;
					}
				}
			}
		}
		return old;
	}

	/**
	 * 推荐商品展示
	 * 推荐商品展示页优先展示相关产品，
	 * 相干产品展示完毕后展示热门商品，分页
	 * @param gid
	 * @param p
	 * @param size
	 * @return
	 */
	public Result getSimilarProducts(String gid, int p, int size) {
		try {
			String resultBody ="";
			if(1==p){
				String url = recommedListUrl+"?listingId="+gid+"&website="+mobileService.getWebSiteID()+
						"&lang="+mobileService.getLanguageID()+"&currency="+mobileService.getCurrency();
				Logger.debug("before send get date- url:"+url);
				resultBody = new ApiUtil().get(url);
				Logger.debug("end send get date- resultBody:"+resultBody);
			}else if(p>1){
				p = p-1;
			}
			Page<ProductLiteInfo> resultPage = null;
			if (StringUtils.isBlank(resultBody)|| !"1".equals(JSONObject.parseObject(resultBody).get("ret").toString())) { //交互成功
				Map<String, String[]> queryStrings = request().queryString();
				resultPage = productService.featured(queryStrings, p, size);
			}else{
				List<ProductLiteInfo> InfoList =getResultPage(resultBody);
				resultPage = new Page<ProductLiteInfo>(InfoList, InfoList.size(), p, size);
			}
			Logger.debug("re change value  InfoList:"+JSON.toJSONString(resultPage));		
			if (resultPage != null && resultPage.getList() != null
					&& resultPage.getList().size() > 0) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
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
	 * 将数据转换封装
	 * @param resultBody
	 * @return
	 */
	private List<ProductLiteInfo> getResultPage(String resultBody) {
		JSONArray jsonArr = JSON.parseObject(resultBody).getJSONArray("data");
		List<ProductLiteInfo> pinfoList = new ArrayList<ProductLiteInfo>();
		for(int i=0;i<=jsonArr.size()-1;i++){
			if (jsonArr.size()%2 != 0 && i == jsonArr.size()-1) continue; //保持数量是
			JSONObject jsonO = (JSONObject) jsonArr.get(i);
			ProductLiteInfo pinfo = new ProductLiteInfo();
			pinfo.setTitle(jsonO.getString("title"));//标题
			pinfo.setSale(jsonO.getDoubleValue("nowprice"));//现价
			pinfo.setImgurl( ImageUtils.getWebPath(
					jsonO.getString("imageUrl"), 500, 500,
					mobileService.getMobileContext()));//图片地址
			pinfo.setGid(jsonO.getString("listingId"));//商品ID
			pinfo.setPcs(jsonO.getDoubleValue("origprice"));//原价
			pinfo.setSku(jsonO.getString("sku"));//sku
			pinfo.setStar(jsonO.getDoubleValue("avgScore"));//平均星级
			pinfo.setQty(jsonO.getInteger("reviewCount"));//评论数
			pinfoList.add(pinfo);
		}
		Collections.shuffle(pinfoList); //随机打乱list顺序
		return pinfoList;

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
			if (resultPage != null && resultPage.getList() != null
					&& resultPage.getList().size() > 0) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
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
			if (resultPage != null && resultPage.getList() != null
					&& resultPage.getList().size() > 0) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setTotal(resultPage.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultPage.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
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
				result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
				result.setList(resultList);
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Product Exception", e);
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
}
