package com.tomtop.product.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.BaseBo;
import com.tomtop.product.models.bo.CollectCountBo;
import com.tomtop.product.models.bo.PrdouctDescBo;
import com.tomtop.product.models.bo.ProductDetailsBo;
import com.tomtop.product.models.bo.ProductHotBo;
import com.tomtop.product.models.bo.ProductPriceBo;
import com.tomtop.product.models.bo.ProductReviewBo;
import com.tomtop.product.models.bo.ProductSeoBo;
import com.tomtop.product.models.bo.ProductStorageBo;
import com.tomtop.product.models.bo.ReviewStartNumBo;
import com.tomtop.product.models.bo.ShippingMethodBo;
import com.tomtop.product.models.bo.TopicPageBo;
import com.tomtop.product.models.vo.ProductBaseDtlVo;
import com.tomtop.product.models.vo.ProductCollectVo;
import com.tomtop.product.models.vo.ReportErrorVo;
import com.tomtop.product.models.vo.WholesaleInquiryVo;
import com.tomtop.product.models.vo.WholesaleProductVo;
import com.tomtop.product.services.IEsProductDetailService;
import com.tomtop.product.services.IFeedbackService;
import com.tomtop.product.services.IProductCollectService;
import com.tomtop.product.services.IProductDetailService;
import com.tomtop.product.services.IProductExplainService;
import com.tomtop.product.services.IProductHotService;
import com.tomtop.product.services.dropship.IDropshipProductService;
import com.tomtop.product.services.review.IInteractionCommentService;
import com.tomtop.product.services.storage.IProductStorageService;
import com.tomtop.product.services.topic.ITopicPageService;
import com.tomtop.product.services.wholesale.IWholeSaleProductService;
import com.tomtop.product.utils.ProductBaseUtils;

/**
 * 产品详情控制类
 * 
 * @author renyy
 *
 */
@RestController
@RequestMapping(value = "/ic")
public class ProductDetailsController {

	private static final Logger logger = LoggerFactory
			.getLogger(ProductDetailsController.class);
	@Autowired
	IProductDetailService productDetailService;
	@Autowired
	IInteractionCommentService interactionCommentService;
	@Autowired
	IProductCollectService productCollectService;
	@Autowired
	ProductBaseUtils productBaseUtil;
	@Autowired
	IProductStorageService productStorageService;
	@Autowired
	IProductExplainService productExplainService;
	@Autowired
	ITopicPageService topicPageService;
	@Autowired
	IProductHotService productHotService;
	@Autowired
	IFeedbackService feedbackService;
	@Autowired
	IDropshipProductService dropshipProductService;
	@Autowired
	IWholeSaleProductService wholeSaleProductService;
	@Autowired
	IEsProductDetailService esPrdocutDetailService;
	
	private final static String paymentExplain = "paymentexplain";
	
	private final static String warrantyExplain = "warrantyexplain";
	
	/**
	 * 获取商品详情基本信息
	 * 
	 * @param sku
	 *            分别可以为 商品sku or listingId or url
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param client
	 *            币种
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v2/product/base")
	public Result getProductDtlSearch(
			@RequestParam("key") String key,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer lang,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer client,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		Result res = new Result();
		if(key == null || "".equals(key)){
			res.setRet(Result.FAIL);
			res.setErrCode("404");
			res.setErrMsg("key is null");
			return res;
		}
		logger.info("================================getProductBaseDtlVo be called==========================");
		ProductBaseDtlVo pbdvo = esPrdocutDetailService.getProductBaseDtlVo(key, lang, client, currency);

		if(pbdvo.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
			res.setData(pbdvo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(res.getErrCode());
			res.setErrMsg(res.getErrMsg());
		}
		return res;
	}
	
	/**
	 * 获取商品详情基本信息
	 * 
	 * @param sku
	 *            分别可以为 商品sku or listingId or url
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param client
	 *            币种
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/base/{sku}")
	public Result getProductDtl(
			@PathVariable("sku") String sku,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		logger.info("================================getProductDtl be called==========================");
		List<ProductDetailsBo> pdbList = esPrdocutDetailService.getProductDetailsBoList(sku, languageid, websiteid, currency);
		Result res = new Result();
		if(pdbList != null && pdbList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(pdbList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33001");
			res.setErrMsg("details not find");
		}
		return res;
	}
	/**
	 * 获取商品的desc
	 * 
	 * @param sku
	 *             分别可以为 商品sku or listingId or url
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/desc/{sku}")
	public Result getProductDescription(
			@PathVariable("sku") String sku,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid) {
		logger.info("================================getProductDescription be called==========================");
		PrdouctDescBo pdb = esPrdocutDetailService.getPrdouctDescBo(sku, languageid, websiteid);
		Result res = new Result();
		if(pdb != null && pdb.getRes() == 1){
			String payEx = productExplainService.getProductExplainByType(paymentExplain, websiteid, languageid);
			String warEx = productExplainService.getProductExplainByType(warrantyExplain, websiteid, languageid);
			pdb.setShippingPayment(payEx);
			pdb.setWarranty(warEx);
			res.setRet(Result.SUCCESS);
			pdb.setRes(null);
			res.setData(pdb);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33002");
			res.setErrMsg("desc not find");
		}
		return res;
	}
	
	/**
	 * 获取商品的SEO
	 * 
	 * @param sku
	 *             分别可以为 商品sku or listingId or url
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/seo/{sku}")
	public Result getProductSeo(
			@PathVariable("sku") String sku,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid) {
		logger.info("================================getProductSeo be called==========================");
		ProductSeoBo psb = esPrdocutDetailService.getProductSeoBo(sku, languageid, websiteid);
		Result res = new Result();
		if(psb != null && psb.getRes() == 1){
			res.setRet(Result.SUCCESS);
			psb.setRes(null);
			res.setData(psb);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33003");
			res.setErrMsg("seo not find");
		}
		return res;
	}
	
	/**
	 * 获取商品评论星级和数量
	 * 
	 * @param listingId
	 *            商品listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/review/start/{listingId}")
	public Result getProductStartNum(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid) {
		//ReviewStartNumBo rsnbo = interactionCommentService.getReviewStartNumBoById(listingId);
		logger.info("================================getProductStartNum be called==========================");
		ReviewStartNumBo rsnbo = esPrdocutDetailService.getReviewStartNumBoById(listingId,languageid,websiteid);
		Result res = new Result();
		if(rsnbo != null && rsnbo.getRes() == 1){
			res.setRet(Result.SUCCESS);
			rsnbo.setRes(null);
			res.setData(rsnbo);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33004");
			res.setErrMsg("product start num not find");
		}
		return res;
	}
	
	/**
	 * 获取商品收藏数
	 * 
	 * @param listingId
	 *            商品listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/collect/{listingId}")
	public Result getProductCollectNum(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid) {
		//CollectCountBo ccb = productCollectService.getCollectCountByListingId(listingId);
		logger.info("================================getProductCollectNum be called==========================");
		CollectCountBo ccb = esPrdocutDetailService.getCollectCountByListingId(listingId,languageid,websiteid);
		Result res = new Result();
		if(ccb != null && ccb.getRes() == 1){
			res.setRet(Result.SUCCESS);
			ccb.setRes(null);
			res.setData(ccb);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33005");
			res.setErrMsg("product collect num not find");
		}
		return res;
	}
	
	/**
	 * 获取商品价格接口
	 * 
	 * @param listingId
	 *            商品listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param client
	 *            币种
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/price/{listingId}")
	public Result getProductPrice(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		logger.info("================================getProductPrice be called==========================");
		Result res = new Result();
		if(listingId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("33006");
			res.setErrMsg("listingid is null");
			return res;
		}
		//ProductPriceBo ppb = productDetailService.getProductBasePriceBo(listingId, languageid, websiteid, currency);
		ProductPriceBo ppb = esPrdocutDetailService.getProductBasePriceBo(listingId, languageid, websiteid, currency);
		if(ppb != null){
			res.setRet(Result.SUCCESS);
			res.setData(ppb);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33007");
			res.setErrMsg("product price not find");
		}
		return res;
	}
	
	/**
	 * 获取仓库及邮寄方式接口
	 * 
	 * @param listingId
	 *            商品listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param client
	 *            币种
	 * @param country
	 *            国家
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/storage/{listingId}")
	public Result getProductStorageShipping(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "country", required = false, defaultValue = "US") String country,
			@RequestParam(value = "qty", required = false, defaultValue = "1") Integer qty) {
		Result res = new Result();
		if(listingId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("33008");
			res.setErrMsg("listingid is null");
			return res;
		}
		logger.info("================================getProductStorageShipping be called==========================");
		//List<ProductStorageShippingBo> pssbList = productStorageService.getProductStorage(listingId, 1, languageid, websiteid, currency, country);
		List<ProductStorageBo> pssbList = esPrdocutDetailService.getProductStorage(listingId, 1, languageid, websiteid, currency, country);
		//List<ProductStorageShippingBo> pssbList = productStorageService.getProductStorages(listingId, qty, languageid, websiteid, currency, country);
		if(pssbList != null && pssbList.size() >0){
			res.setRet(Result.SUCCESS);
			res.setData(pssbList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33009");
			res.setErrMsg("product storage  not find");
		}
		return res;
	}
	
	/**
	 * 获取商品详情explain
	 * 
	 * @param type
	 *            type
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/explain/{type}")
	public Result getProductExplainShippingPayment(
			@PathVariable("type") String type,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid) {
		logger.info("================================getProductExplainShippingPayment be called==========================");
		String explain = productExplainService.getProductExplainByType(type, websiteid, languageid);
		Result res = new Result();
		if(explain != null && !"".equals(explain)){
			res.setRet(Result.SUCCESS);
			res.setData(explain);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33010");
			res.setErrMsg("product explain not find");
		}
		return res;
	}
	
	/**
	 * 获取商品评论详情
	 * 
	 * @param listingId
	 *            listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/review/{listingId}")
	public Result getProductReviewList(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid) {
		logger.info("================================getProductReviewList be called==========================");
		List<ProductReviewBo> prboList = interactionCommentService.getProductReviewBoList(listingId, websiteid);
		Result res = new Result();
		if(prboList != null && prboList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(prboList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33011");
			res.setErrMsg("product review not find");
		}
		return res;
	}
	
	/**
	 * 获取商品Hot Events最新5个专题
	 * 
	 * @param type
	 *            type
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/topic")
	public Result getTopicPage(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid,
			@RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {
		
		logger.info("================================getTopicPage be called==========================");
		List<TopicPageBo> tpboList = topicPageService.filterTopicPage(type, websiteid, languageid, null, null, size);
		Result res = new Result();
		if(tpboList != null && tpboList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(tpboList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33012");
			res.setErrMsg("product topic page not find");
		}
		return res;
	}
	
	/**
	 * 获取商品Hot标签  top 5个
	 * 
	 * @param type
	 *            type
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/hot")
	public Result getProductHot(
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency) {
		logger.info("================================getProductHot be called==========================");
		List<ProductHotBo> phboList = esPrdocutDetailService.getProductHotBoList(languageid, websiteid, currency);
		//List<ProductHotBo> phboList = productHotService.getProductHotBoList(languageid, websiteid, currency);
		Result res = new Result();
		if(phboList != null && phboList.size() > 0){
			res.setRet(Result.SUCCESS);
			res.setData(phboList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33013");
			res.setErrMsg("product hot not find");
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 Wholesale Inquiry 
	 * 
	 * @param WholesaleInquiryVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/wholesaleInquiry")
	public Result putWholesaleInquiry(@RequestBody WholesaleInquiryVo wivo) {
		BaseBo bb = feedbackService.addWholesaleInquiry(wivo);
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 Report Error
	 * 
	 * @param ReportErrorVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/reportError")
	public Result putReportError(@RequestBody ReportErrorVo revo) {
		BaseBo bb = feedbackService.addReportError(revo);
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 收藏
	 * 
	 * @param ProductCollectVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/collect/add")
	public Result putProductCollect(@RequestBody ProductCollectVo pcvo) {
		BaseBo bb = productCollectService.addCollectCount(pcvo.getListingId(), pcvo.getEmail());
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 dropship 
	 * 
	 * @param ProductCollectVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/dropship/add")
	public Result putProductDropship(@RequestBody ProductCollectVo pcvo) {
		BaseBo bb = dropshipProductService.addProductDropshipBySku(pcvo.getEmail(), pcvo.getSku(), pcvo.getClient());
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	
	/**
	 * 详情页面中 添加 WholeSaleProduct 
	 * 
	 * @param ProductCollectVo
	 * 
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/v1/product/wholesale/add")
	public Result putWholeSaleProduct(@RequestBody WholesaleProductVo wpvo) {
		String email = wpvo.getEmail();
		String sku = wpvo.getSku();
		Integer qty = wpvo.getQty();
		Integer client = wpvo.getClient();
		BaseBo bb = wholeSaleProductService.addWholeSaleProduct(email, sku, qty, client);
		Result res = new Result();
		if(bb.getRes() == Result.SUCCESS){
			res.setRet(Result.SUCCESS);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode(bb.getRes().toString());
			res.setErrMsg(bb.getMsg());
		}
		return res;
	}
	/**
	 * 获取邮寄方式接口【停用】
	 * 
	 * @param listingId
	 *            商品listingId
	 * @param lang
	 *            语言ID
	 * @param client
	 *            站点ID
	 * @param client
	 *            币种
	 * @param country
	 *            国家
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/shippingMethod/{listingId}")
	public Result getStorageShippingMethod(
			@PathVariable("listingId") String listingId,
			@RequestParam(value = "storageId", required = false, defaultValue = "1") Integer storageId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid,
			@RequestParam(value = "currency", required = false, defaultValue = "USD") String currency,
			@RequestParam(value = "country", required = false, defaultValue = "US") String country,
			@RequestParam(value = "qty", required = false, defaultValue = "1") Integer qty) {
		Result res = new Result();
		if(listingId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("33014");
			res.setErrMsg("listingid is null");
			return res;
		}
		List<ShippingMethodBo> shippList = productStorageService.getStoragesShippingMethod(storageId, listingId, qty, languageid, websiteid, currency, country);
		if(shippList != null && shippList.size() >0){
			res.setRet(Result.SUCCESS);
			res.setData(shippList);
		}else{
			res.setRet(Result.FAIL);
			res.setErrCode("33009");
			res.setErrMsg("product storage shipping method not find");
		}
		return res;
	}
	
	/**
	 * 获取库存接口
	 * 
	 * @param listingId
	 *            商品listingId
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/v1/product/qty/{listingId}")
	public Result getProductQty(@PathVariable("listingId") String listingId) {
		Result res = new Result();
		if(listingId == null){
			res.setRet(Result.FAIL);
			res.setErrCode("33015");
			res.setErrMsg("listingid is null");
			return res;
		}
		Integer qty = productDetailService.getProductQty(listingId);
		if(qty == null){
			qty = 0;
		}
		res.setRet(Result.SUCCESS);
		res.setData(qty);
		return res;
	}
}
