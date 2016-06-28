package com.tomtop.product.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.product.mappers.product.ProductDtlMapper;
import com.tomtop.product.models.bo.PrdouctDescBo;
import com.tomtop.product.models.bo.ProductAttributeBo;
import com.tomtop.product.models.bo.ProductDetailsBo;
import com.tomtop.product.models.bo.ProductImageBo;
import com.tomtop.product.models.bo.ProductPriceBo;
import com.tomtop.product.models.bo.ProductSeoBo;
import com.tomtop.product.models.dto.ProductAttributeDto;
import com.tomtop.product.models.dto.ProductBase;
import com.tomtop.product.models.dto.ProductImageUrlDto;
import com.tomtop.product.models.dto.ProductSeoDto;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.services.IProductDetailService;
import com.tomtop.product.services.price.IPriceService;
import com.tomtop.product.services.price.impl.SingleProductSpec;
import com.tomtop.product.utils.PriceInterceptUtils;

/**
 * 商品详情业务逻辑类
 * 
 * @author renyy
 *
 */
@Service
public class ProductDetailServiceImpl implements IProductDetailService {

	@Autowired
	ProductDtlMapper productDtlMapper;

	@Autowired
	private IPriceService priceService;

	@Resource(name = "priceInterceptUtils")
	private PriceInterceptUtils priceIntercept;

	/**
	 * 获取商品详情基本信息
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @param currency
	 * 
	 * @return List<ProductDetailsBo>
	 * @author renyy
	 */
	@Override
	public List<ProductDetailsBo> getProductDetailsBoList(String listingId,
			Integer langId, Integer siteId, String currency) {
		List<ProductDetailsBo> pdbList = new ArrayList<ProductDetailsBo>();
		if (listingId == null) {
			return pdbList;
		}
		ProductBase pb = productDtlMapper.getProductBaseByListingId(listingId,
				langId, siteId);

		String parentSku = pb.getCparentsku();
		List<ProductBase> pbList = new ArrayList<ProductBase>();
		List<String> list = new ArrayList<String>();
		Map<String, HashMap<String, String>> listAttri = new HashMap<String, HashMap<String, String>>();
		if (parentSku != null) {
			// 有多属性时
			pbList = productDtlMapper.getProductBaseByParentSku(listingId,
					langId, parentSku, siteId);
			pbList.add(pb);
			if (pbList != null && pbList.size() > 0) {
				for (int i = 0; i < pbList.size(); i++) {
					list.add(pbList.get(i).getClistingid());
				}
				List<ProductAttributeDto> padList = productDtlMapper
						.getProductAttributeDtoByListingIds(list, langId,
								siteId);
				if (padList != null && padList.size() > 0) {
					HashMap<String, String> kvMap = null;
					for (int i = 0; i < padList.size(); i++) {
						String listid = padList.get(i).getClistingid();
						String keyName = padList.get(i).getCkeyname();
						String valueName = padList.get(i).getCvaluename();
						if (listAttri.containsKey(listid)) {
							kvMap = listAttri.get(listid);
						} else {
							kvMap = new HashMap<String, String>();
						}
						kvMap.put(keyName, valueName);

						listAttri.put(listid, kvMap);
					}
				}
			}
		} else {
			// 没有多属性时
			pbList.add(pb);
			list.add(pb.getClistingid());
		}
		List<ProductImageUrlDto> imgUrl = productDtlMapper
				.getProductImageUrlDtoByListingIds(list, langId);
		Map<String, ArrayList<ProductImageBo>> listUrl = new HashMap<String, ArrayList<ProductImageBo>>();
		if (imgUrl != null && imgUrl.size() > 0) {
			ArrayList<ProductImageBo> imgList = null;
			ProductImageBo pimg = null;
			String listid = "";
			String url = "";
			boolean isSmall = false;
			boolean isMain = false;
			for (int i = 0; i < imgUrl.size(); i++) {
				listid = imgUrl.get(i).getClistingid();
				url = imgUrl.get(i).getCimageurl();
				isSmall = imgUrl.get(i).getBsmallimage();
				isMain = imgUrl.get(i).getBbaseimage();
				if (listUrl.containsKey(listid)) {
					imgList = listUrl.get(listid);
				} else {
					imgList = new ArrayList<ProductImageBo>();
				}
				pimg = new ProductImageBo();
				pimg.setImgUrl(url);
				pimg.setIsMain(isMain);
				pimg.setIsSmall(isSmall);
				imgList.add(pimg);
				listUrl.put(listid, imgList);
			}
		}

		ProductDetailsBo pdb = null;
		for (int i = 0; i < pbList.size(); i++) {
			// 设置ProductDetailsBo
			pdb = new ProductDetailsBo();
			pb = pbList.get(i);
			pdb.setTitle(pb.getCtitle());
			pdb.setListingId(pb.getClistingid());
			pdb.setSku(pb.getCsku());
			pdb.setQty(pb.getIqty());
			pdb.setStatus(pb.getIstatus());
			pdb.setAttributeMap(listAttri.get(pb.getClistingid()));
			pdb.setImgList(listUrl.get(pb.getClistingid()));
			pdbList.add(pdb);
		}

		return pdbList;
	}

	/**
	 * 获取商品详情的Description
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @return PrdouctDescBo
	 * @author renyy
	 * 
	 */
	@Override
	public PrdouctDescBo getPrdouctDescBo(String listingId, Integer langId,
			Integer siteId) {
		PrdouctDescBo pdbo = new PrdouctDescBo();
		if (listingId == null) {
			pdbo.setRes(-31001);
			pdbo.setMsg("listingId is null");
			return pdbo;
		}
		String desc = productDtlMapper.getProductDescByListingId(listingId,
				langId);
		if (desc == null || "".equals(desc)) {
			pdbo.setRes(-31002);
			pdbo.setMsg("desc not find");
			return pdbo;
		}
		pdbo.setRes(1);
		pdbo.setDesc(desc);

		return pdbo;
	}

	/**
	 * 获取商品详情的SEO
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @return ProductSeoBo
	 * @author renyy
	 */
	@Override
	public ProductSeoBo getProductSeoBo(String listingId, Integer langId,
			Integer siteId) {
		ProductSeoBo psb = new ProductSeoBo();
		if (listingId == null) {
			psb.setRes(-31003);
			psb.setMsg("listingId is null");
			return psb;
		}
		ProductSeoDto psd = productDtlMapper
				.getProductSeoDto(listingId, langId);
		if (psd == null) {
			psb.setRes(-31004);
			psb.setMsg("seo is null");
			return psb;
		}
		String keyword = psd.getCkeyword();
		if (keyword == null || "".equals(keyword.trim())) {
			psb.setRes(-31005);
			psb.setMsg("seo keyword is null");
			return psb;
		}
		psb.setMetaDescription(keyword);
		psb.setMetaKeyword(psd.getCmetakeyword());
		psb.setMetaTitle(psd.getCmetatitle());
		psb.setRes(1);
		return psb;
	}

	/**
	 * 获取商品单品价格
	 * 
	 * @param listingId
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @return ProductBasePriceInfoBo
	 * @author renyy
	 */
	@Override
	public ProductPriceBo getProductBasePriceBo(String listingId,
			Integer langId, Integer siteId, String currency) {
		ProductPriceBo bo = new ProductPriceBo();
		Price price = priceService.getPrice(
				new SingleProductSpec(listingId, 1),
				new PriceCalculationContext(currency));
		if (price != null) {
			bo.setNowprice(priceIntercept.money(price.getPrice(), currency));
			bo.setOrigprice(priceIntercept.money(price.getUnitBasePrice(),
					currency));
			bo.setSymbol(price.getSymbol());
		} else {
			return null;
		}
		return bo;
	}

	@Override
	public List<ProductAttributeBo> getProductAttributeDtoByListingIds(
			List<String> listingIds, Integer langId, Integer siteId) {
		return Lists.transform(
				productDtlMapper.getProductAttributeDtoByListingIds(listingIds,
						langId, siteId), p -> BeanUtils.mapFromClass(p,
						ProductAttributeBo.class));
	}
	/**
	 * 获取根据listingId获取库存
	 * 
	 * @param listingId
	 * 
	 * @return Integer
	 * @author renyy
	 */
	@Override
	public Integer getProductQty(String listingId) {
		return productDtlMapper.getProductQtyByListingId(listingId);
	}

}
