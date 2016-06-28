package extensions.google.merchants;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.rpc.ServiceException;

import mapper.google.category.GoogleFeedsMapper;
import mapper.product.ProductBaseMapper;
import mapper.product.ProductImageMapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Lists;

import play.Logger;
import play.Play;
import services.ICurrencyService;
import services.base.CountryService;
import services.base.FoundationService;
import services.price.PriceService;

import com.google.api.client.util.Maps;
import com.google.api.services.content.model.Price;
import com.google.api.services.content.model.Product;
import com.google.api.services.content.model.ProductShipping;

import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductUrlEnquiryDao;
import dto.product.ProductBase;
import dto.product.ProductImage;
import dto.product.ProductUrl;
import dto.product.google.category.MerchantsProductDto;

/**
 * Factory for creating Products to be inserted by the ProductInsert and
 * ProductBatchInsert samples.
 */
public class MerchantsProductFactory {
	private static final String HOST_IMG="http://www.tomtop.com/img/";
	
	private static final String GOOGLE_LANGE_EN="en";
	@Inject
	ProductBaseMapper productBaseMapper;
	@Inject
	FoundationService foundationService;
	@Inject
	PriceService priceService;
	@Inject
	CountryService countryService;
	@Inject
	ProductImageMapper productImagerMapper;
	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;
	@Inject
	IProductBaseEnquiryDao productBaseEnquityDao;
	@Inject
	ICurrencyService currencyService;
	@Inject
	GoogleFeedsMapper googleFeedsMapper;
	public Product create(MerchantsProductDto merchantsProductDto,
			ProductBase productbase,Map<String, Integer> feedMap) throws Exception {
		com.google.api.services.content.model.Product product = new Product();

		try {
			if (productbase == null || merchantsProductDto == null) {
				Logger.info("productbase OR merchantsProductDto productbase not exist!");
				return null;
			}
			String csku=productbase.getCsku();
			String googleTargetCountry=merchantsProductDto.getCtargetcountry();
			String googleLange=merchantsProductDto.getClanguage();
			String listingId = productbase.getClistingid();
			// "id": "online:en:CA:L0160W",
			String channel = productbase.getIstatus() == 1 ? "online" : "local";
			/*product.setId(channel + ":" + merchantsProductDto.getClanguage()
					+ ":" + merchantsProductDto.getCtargetcountry() + ":"
					+ productbase.getCsku());*/
			product.setId(merchantsProductDto.getCnodeid());
			product.setDescription(productbase.getCdescription());
			product.setChannel(channel);
			product.setContentLanguage(googleLange);
			product.setTargetCountry(googleTargetCountry);
			String avaliability=productbase.getIqty()>0?"in stock":"out of stock";
			product.setAvailability(avaliability);
			product.setGoogleProductCategory(merchantsProductDto.getGooglecategory());
			product.setOfferId(csku);

			product.setMpn(csku);
			product.setBrand("tomtop");
			product.setTitle(productbase.getCtitle());

			product.setCondition("new");// new/ refurbished/used
			product.setProductType(merchantsProductDto.getCpath());
			String feedMapKey=googleLange+"_"+googleTargetCountry;
			//过滤描述HTML
			fillHtmlTag(product);
			if(MapUtils.isNotEmpty(feedMap) && feedMap.containsKey(feedMapKey)){
				//Integer integer = feedMap.get(feedMapKey);
				ProductUrl purl = this.productUrlEnquityDao
						.getProductUrlsByListingId(listingId,1);
				// 设置产品详情链接
				if (purl != null) {
					String link ="";
					if(GOOGLE_LANGE_EN.equals(googleLange)){//if google product lange is en
						
						link = Play.application().configuration()
								.getString("host.link")
								+ purl.getCurl() + ".html?currency="+merchantsProductDto.getCcountrycurrency()+"&lang="+googleLange;
					}else{
						String limitHostLink = Play.application().configuration()
								.getString("host.link");
						String replace =limitHostLink.replace("www", googleLange);
						link=replace+purl.getCurl() + ".html?currency="+merchantsProductDto.getCcountrycurrency()+"&lang="+googleLange;
					}
					String mobileLink = Play.application().configuration()
							.getString("mobile.link")
							+ purl.getCurl() + ".html";
					// mobile_link
					product.setMobileLink(mobileLink);
					product.setLink(link);
				}
			}
			List<ProductImage> productImgsByListingId = productImagerMapper
					.getProductImgsByListingId(listingId);
			// 设置图片链接
			if (CollectionUtils.isNotEmpty(productImgsByListingId)) {
			//
				product.setImageLink(doWithImgPath(productImgsByListingId.get(0)
						.getCimageurl()));
				// 子图片链接数有限制
				if (productImgsByListingId.size() > 3) {
					productImgsByListingId = productImgsByListingId.subList(0,
							3);
				}
				List<String> addImgLinks = Lists.transform(
						productImgsByListingId, item -> doWithImgPath(item.getCimageurl()));
				product.setAdditionalImageLinks(addImgLinks);
			}
			
			double isCurrencyExist=currencyService.getRate(merchantsProductDto.getCcountrycurrency());
			//币种，
			if (isCurrencyExist>0 && StringUtils.isNotEmpty(merchantsProductDto.getCcountrycurrency())) {
				Logger.info("Ccurrency change opt listingId:" + listingId);
				//产品价格
				Logger.debug("---------------->:" + listingId+"  currency:"+merchantsProductDto.getCcountrycurrency());
				valueobjects.price.Price priceDto = priceService.getPrice(
						listingId, merchantsProductDto.getCcountrycurrency());
				if (priceDto != null) {
					
					Price price = new Price();
					price.setValue(priceDto.getPrice() + "");
					price.setCurrency(merchantsProductDto.getCcountrycurrency());
					product.setPrice(price);
					
					merchantsProductDto.setProductprice(priceDto.getPrice() + "");
				}else{
					Logger.debug("create product price fail :listingid:"+listingId+"   currency:"+merchantsProductDto.getCcountrycurrency());
					merchantsProductDto.setCfaultreason("create product price fail!listingid:"+listingId+"   currency:"+merchantsProductDto.getCcountrycurrency());
					throw new ServiceException("create product price fail");
				}
				//产品价格如果报错，将不能够创建产品，但汇率获取如果报错，不能够影响产品创建
				updateShippingPrice(product,merchantsProductDto);
			}else{
				
				Logger.info("create product fail isCurrencyExist:"+isCurrencyExist);
				merchantsProductDto.setCfaultreason("create product price fail!listingid:"+listingId+"   currency:"+merchantsProductDto.getCcountrycurrency()
						+"  isCurrencyExist:"+isCurrencyExist);
				throw new ServiceException("create product price fail!isCurrencyExist:"+isCurrencyExist);
			}
			// 设置setCustomLabel1
			setCustomLabel1(product,productbase.getFprice());
			//设置产品多属性
			setMultiAttribute(product,merchantsProductDto.getAttributes());
			
		} catch (Exception e) {
			Logger.error("create product error:" , e);
			throw e;
		}
		return product;
	}
	private void fillHtmlTag(com.google.api.services.content.model.Product product){
		if(product!=null && StringUtils.isNotEmpty(product.getDescription())){
			product.setDescription(HtmlRegexpUtil.getTextFromHtml(product.getDescription()));
		}
	}
	private String doWithImgPath(String path){
		if(StringUtils.isEmpty(path)){
			return null;
		}
		
		if(!path.startsWith("http")){
			path=HOST_IMG+path;
		}
		return path;
		
	}
	private double getProductRate(String ccountrycurrency){
		try{
			
			return  currencyService.getRate(ccountrycurrency);//运费
		}catch(Exception e){
			Logger.error("----------------->get  rate error",e);
		}
		return 0;
	}
	public MerchantsProductDto createMerchantsProductDtoByProduct(
			com.google.api.services.content.model.Product product) {
		MerchantsProductDto merchantsProductDto = null;
		try {
			if (product != null) {
				merchantsProductDto = new MerchantsProductDto();
				String sku = StringUtils.isNotEmpty(product.getOfferId()) ? product
						.getOfferId() : product.getMpn();
				merchantsProductDto.setCsku(sku);
				merchantsProductDto.setCtargetcountry(product
						.getTargetCountry());
				merchantsProductDto.setCchannel(product.getChannel());
				merchantsProductDto.setClanguage(product.getContentLanguage());
				Price price = product.getPrice();
				merchantsProductDto.setProductprice(price.getValue());
				merchantsProductDto.setCcountrycurrency(price.getCurrency());
				merchantsProductDto.setCtitle(product.getTitle());
				merchantsProductDto.setCdescription(product.getDescription());
				merchantsProductDto.setCnodeid(product.getId());
				merchantsProductDto.setCstate("1");
				
				Map<String,Class> map=Maps.newHashMap();
				String bean2xml = XMLBeanUtils.bean2xml(map, product);
				merchantsProductDto.setCnodedata(bean2xml);

				if (StringUtils.isNotEmpty(merchantsProductDto.getCtitle())
						&& merchantsProductDto.getCtitle().length() > 150) {
					merchantsProductDto.setCtitle(merchantsProductDto
							.getCtitle().substring(0, 150));
				}
				if (StringUtils.isNotEmpty(merchantsProductDto
						.getCdescription())
						&& merchantsProductDto.getCdescription().length() > 5000) {
					merchantsProductDto.setCdescription(merchantsProductDto
							.getCdescription().substring(0, 5000));
				}
			}
		} catch (Exception e) {
			Logger.error("---------------------->create MerchantsProductDto error:"
					, e);
		}
		return merchantsProductDto;
	}

	public Product createProductByMerchants(
			MerchantsProductDto merchantsProductDto,Map<String, Integer> feedMap) throws Exception {
		com.google.api.services.content.model.Product product =null;

		try {
			if (StringUtils.isNotEmpty(merchantsProductDto.getCnodedata())) {
				Map<String,Class> map=Maps.newHashMap();
				product=(Product)XMLBeanUtils.xml2Bean(map, merchantsProductDto.getCnodedata());
				if(product!=null ){
					// 更新库存状态
					String avaliability=merchantsProductDto.getIcount()>0?"in stock":"out of stock";
					product.setAvailability(avaliability);
					
					Logger.debug("Ccurrency change opt listingId:"+ merchantsProductDto.getClistingid()+"    currency:"+merchantsProductDto.getCcountrycurrency());
					//update product links
					updateProductLinks(merchantsProductDto.getCcountrycurrency(), 
							merchantsProductDto.getClistingid(),product);
					double isCurrencyExist=currencyService.getRate(merchantsProductDto.getCcountrycurrency());
					if(isCurrencyExist>0){
						Logger.debug("update price listingid:"+merchantsProductDto.getClistingid()+"  currency:"+merchantsProductDto.getCcountrycurrency());
						valueobjects.price.Price priceDto = priceService.getPrice(
								merchantsProductDto.getClistingid(),
								merchantsProductDto.getCcountrycurrency());
						if (priceDto != null) {
							Logger.debug("update price  information  ! price:"+priceDto.getPrice());
							Price price = new Price();
							price.setValue(priceDto.getPrice() + "");
							price.setCurrency(priceDto.getCurrency());
							product.setPrice(price);
						}else{
							merchantsProductDto.setCresult("fail");
							merchantsProductDto.setCfaultreason("update product price fail!");
							Logger.info("update price fail ! priceDto null");
							throw new  ServiceException("update price fail ! priceDto null");
						}
					}else{
						merchantsProductDto.setCresult("fail");
						merchantsProductDto.setCfaultreason("update product price fail!");
						Logger.info("product price  setting fail isCurrencyExist:"+isCurrencyExist);
						throw new  ServiceException("update price fail ! priceDto null");
					}
					
					//更新客户标签
					if(StringUtils.isNotEmpty(merchantsProductDto.getProductprice())){
						Double priceValue = Double.parseDouble(merchantsProductDto.getProductprice());
						setCustomLabel1(product,priceValue);
					}else{
						Logger.info("product label  setting fail ,get price fail");
					}
					//product.setCustomLabel1(merchantsProductDto.getCustomLabel1());
					//更新运费
					updateShippingPrice(product,merchantsProductDto);
						
				}

			}

		} catch (Exception e) {
			Logger.error("------------->create product error:" , e);
			throw e;
		}
		return product;
	}
	
	private  void updateProductLinks(String countrycurrency,
			String listingId,com.google.api.services.content.model.Product product){
		if(product==null || StringUtils.isEmpty(countrycurrency)|| StringUtils.isEmpty(product.getContentLanguage()) 
				|| StringUtils.isEmpty(product.getTargetCountry())|| StringUtils.isEmpty(listingId)){
			Logger.info("updateProductLinks fail!ContentLanguage: "+product.getContentLanguage()+"  TargetCountry:"
				+product.getTargetCountry()+"   listingId:"+listingId+"  countrycurrency:"+countrycurrency);
			return ;
		}
		
		Logger.debug("updateProductLinks listingid:"+listingId+"    product.csku:"+product.getOfferId()+"  --- "+product.getMpn());
		String googleLange=product.getContentLanguage();
		String googleTargetCountry=product.getTargetCountry();
		String feedMapKey=googleLange+"_"+googleTargetCountry;
		/*if(MapUtils.isNotEmpty(feedMap) && feedMap.containsKey(feedMapKey)){
			Integer integer = feedMap.get(feedMapKey);*/
			ProductUrl purl = this.productUrlEnquityDao
					.getProductUrlsByListingId(listingId,1);
			// 设置产品详情链接
			if (purl != null) {
				String link ="";
				if(GOOGLE_LANGE_EN.equals(googleLange)){//if google product lange is en
					
					link = Play.application().configuration()
							.getString("host.link")
							+ purl.getCurl() + ".html?currency="+countrycurrency+"&lang="+googleLange;
				}else{
					String limitHostLink = Play.application().configuration()
							.getString("host.link");
					String replace = limitHostLink.replace("www", googleLange);
					link=replace+purl.getCurl() + ".html?currency="+countrycurrency+"&lang="+googleLange;
				}
				String mobileLink = Play.application().configuration()
						.getString("mobile.link")
						+ purl.getCurl() + ".html";
				// mobile_link
				product.setMobileLink(mobileLink);
				product.setLink(link);
			}else{
				Logger.debug("feedMap purl null");
			}
		/*}else{
			Logger.info("feedMap not contain feedMap:"+feedMap+" key:"+feedMapKey);
		}*/
		List<ProductImage> productImgsByListingId = productImagerMapper
				.getProductImgsByListingId(listingId);
		// 设置图片链接
		if (CollectionUtils.isNotEmpty(productImgsByListingId)) {
		//
			product.setImageLink(doWithImgPath(productImgsByListingId.get(0)
					.getCimageurl()));
			// 子图片链接数有限制
			if (productImgsByListingId.size() > 3) {
				productImgsByListingId = productImgsByListingId.subList(0,
						3);
			}
			List<String> addImgLinks = Lists.transform(
					productImgsByListingId, item -> doWithImgPath(item.getCimageurl()));
			product.setAdditionalImageLinks(addImgLinks);
		}
	}
	
	
	private void updateShippingPrice(Product product,MerchantsProductDto merchantsProductDto){
		double rate = getProductRate(merchantsProductDto.getCcountrycurrency());
		//GB:::6.49 GBP
		Price shippingPrice=new Price();
		shippingPrice.setCurrency(merchantsProductDto.getCcountrycurrency());
			
		if(merchantsProductDto.getShippingprice()>0 && rate>0){//运费和汇率都存在的情况下
				
			String priceValue=merchantsProductDto.getShippingprice()*rate+" ";
			shippingPrice.setValue(priceValue);
		}else if(CollectionUtils.isEmpty(product.getShipping())){
			
			String priceValue="0.01";
			shippingPrice.setValue(priceValue);
		}
		
		if(StringUtils.isNotEmpty(shippingPrice.getValue())){
			ProductShipping shipping=new ProductShipping();
			shipping.setCountry(merchantsProductDto.getCtargetcountry());
			shipping.setPrice(shippingPrice);
			
			List<ProductShipping> shippingList=Lists.newArrayList();
			shippingList.add(shipping);
			product.setShipping(shippingList);
		}
	}
	/**
	 * 
	 *  1. price10--0~10: 0<price<10
		2. price30--10~30: 10<=price<30
		3. price50--30~50: 30<=price<50
		4. priceUp50: price>=50
	 * 
	 */
	private void setCustomLabel1(Product product,Double price){
		if(product==null){
			return;
		}
		String customLabel1="";
		if(price<10){
			customLabel1="price10--0~10";
		}else if(price>=10 && price<30){
			customLabel1="price30--10~30";
		}else if(price>=30 && price<50){
			customLabel1="price50--30~50";
		}else if(price>=50){
			customLabel1="priceUp50";
		}
		product.setCustomLabel1(customLabel1);
	} 
	/**
	 * 设置产品多属性
	 * @param product
	 * @param attributes
	 */
	private void setMultiAttribute(Product product,Map<String,Object> attributes){
		if(product==null || MapUtils.isEmpty(attributes)){
			return;
		}
		Class<? extends Product> productClass = product.getClass();
		Field[] declaredFields = productClass.getDeclaredFields();
		for(Field field: declaredFields){
			if(attributes.containsKey(field.getName())){
				field.setAccessible(true);
					try {
						field.set(product, attributes.get(field.getName()));
					} catch (IllegalArgumentException e) {
						Logger.error("-------------->setMultiAttribute error!");
					} catch (IllegalAccessException e) {
						Logger.error("-------------->setMultiAttribute error!");
					}
				
			}
		}
	 }
}