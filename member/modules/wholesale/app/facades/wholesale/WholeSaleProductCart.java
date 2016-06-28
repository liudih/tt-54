package facades.wholesale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import play.Logger;
import services.product.EntityMapService;
import services.product.IProductBadgeService;
import services.product.ProductEnquiryService;
import services.wholesale.WholeSaleProductEnquiryService;
import services.wholesale.WholeSaleProductUpdateService;
import services.wholesale.WholesalePriceService;
import valueobjects.product.ProductBadge;
import valueobjects.product.spec.SingleProductSpec;
import valueobjects.wholesale.PriceContext;
import valueobjects.wholesale.WholeSaleProductItem;
import valueobjects.wholesale.WholesalePrice;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.product.ProductBase;
import entity.wholesale.WholeSaleProduct;

public class WholeSaleProductCart {
	@Inject
	WholeSaleProductUpdateService updateService;

	@Inject
	WholeSaleProductEnquiryService wholeSaleProductEnquiryService;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	EntityMapService entityMapService;

	@Inject
	WholesalePriceService wholesalePriceService;

	String email;

	Integer websiteId;

	Integer languageId;

	String currency;

	private List<WholeSaleProduct> selectWholeSaleProducts;

	private List<WholeSaleProductItem> productItems;

	private List<String> selectSkus;

	public WholeSaleProductCart(String email, Integer websiteId,
			Integer languageId, String currency) {
		super();
		this.email = email;
		this.websiteId = websiteId;
		this.languageId = languageId;
		this.currency = currency;
	}

	public boolean addWholeSaleProduct(WholeSaleProduct wholeSaleProduct) {
		return updateService.addWholeSaleProduct(wholeSaleProduct);
	}

	public boolean deleteWholeSaleProduct(Integer iid) {
		return updateService.deleteByIid(iid, email);
	}

	public boolean deleteAllWholeSaleProduct(List<Integer> ids) {
		return updateService.batchDeleteByIid(ids, email);
	}

	public boolean updateWholeSaleProductQty(Integer iid, Integer qty) {
		return updateService.updateQtyByIid(iid, qty);
	}

	public void initialize() {
		if (null == selectWholeSaleProducts
				|| selectWholeSaleProducts.size() <= 0) {
			List<WholeSaleProduct> wholeSaleProducts = wholeSaleProductEnquiryService
					.getWholeSaleProductsByEmail(email, websiteId);
			this.selectWholeSaleProducts = wholeSaleProducts;
		}
	}

	public List<WholeSaleProductItem> getWholeSaleProductItem() {
		try {
			List<WholeSaleProduct> wholeSaleProducts = wholeSaleProductEnquiryService
					.getWholeSaleProductsByEmail(email, websiteId);
			if (null == wholeSaleProducts || wholeSaleProducts.size() <= 0) {
				return null;
			}
			productItems = Lists.newArrayList();
			List<String> skus = Lists.transform(wholeSaleProducts, e -> {
				return e.getCsku();
			});
			Map<String, WholesalePrice> priceMap = new HashMap<String, WholesalePrice>();
			if (null != selectWholeSaleProducts) {
				List<PriceContext> priceContexts = Lists.transform(
						selectWholeSaleProducts,
						e -> {
							return new PriceContext(e.getCsku(), null, e
									.getIqty());
						});
				List<WholesalePrice> price = wholesalePriceService.getPrice(
						priceContexts, websiteId);
				priceMap = price.stream().collect(
						Collectors.toMap(a -> a.getPrice().getListingId(),
								a -> a));
			}
			Map<String, WholeSaleProduct> wholeSaleProductMap = wholeSaleProducts
					.stream().collect(
							Collectors.toMap(a -> a.getCsku(), a -> a));
			if (null != skus && skus.size() > 0) {
				List<ProductBase> productBases = productEnquiryService
						.getProductBaseBySkus(skus, websiteId);
				if (null != productBases && productBases.size() > 0) {
					List<String> listingIds = Lists.transform(productBases,
							i -> i.getClistingid());
					Map<String, ProductBase> baseMap = Maps.uniqueIndex(
							productBases, i -> i.getClistingid());
					List<ProductBadge> productBadges = badgeService
							.getProductBadgesByListingIDs(listingIds,
									languageId, websiteId, currency, null);
					for (ProductBadge productBadge : productBadges) {
						if (null != productBadge) {
							String listingid = productBadge.getListingId();
							if (null == listingid) {
								productBadge.toString();
							}
							WholeSaleProductItem product = new WholeSaleProductItem();
							ProductBase p = baseMap.get(listingid);
							Map<String, String> attributeMap = entityMapService
									.getAttributeMap(listingid, languageId);
							product.setAttributeMap(attributeMap);
							WholeSaleProduct wholeSaleProduct = wholeSaleProductMap
									.get(p.getCsku());
							product.setWholeSaleProduct(wholeSaleProduct);
							product.setImageUrl(productBadge.getImageUrl());
							product.setTitle(productBadge.getTitle());
							product.setUrl(productBadge.getUrl());
							product.setProductBase(p);
							WholesalePrice wholesalePrice = new WholesalePrice();
							if (null == priceMap
									|| null == priceMap.get(listingid)
									|| p.getIstatus() != 1) {
								SingleProductSpec iProductSpec = new SingleProductSpec(
										p.getClistingid(),
										wholeSaleProduct.getIqty());
								wholesalePrice = wholesalePriceService
										.getPrice(iProductSpec);
							} else {
								wholesalePrice = priceMap.get(listingid);
							}
							product.setWholesalePrice(wholesalePrice);
							productItems.add(product);
						}
					}
				}
			}
			return productItems;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.debug("wholesale Product error", e);
			return null;
		}
	}

	public List<WholeSaleProduct> getSelectWholeSaleProducts() {
		return selectWholeSaleProducts;
	}

	public double getSubtotal() {
		Double subtotal = 0.0d;
		for (WholeSaleProductItem b : productItems) {
			if (null != b.getWholesalePrice().getWsPrice()) {
				subtotal += b.getWholesalePrice().getPrice().getUnitBasePrice()
						* b.getWholeSaleProduct().getIqty();
			}
		}
		return subtotal;
	}

	public double getGrandTotal() {
		Double grandTotal = 0.0d;
		for (WholeSaleProductItem b : productItems) {
			if (null != b.getWholesalePrice().getWsPrice()) {
				grandTotal += b.getWholesalePrice().getWsTotalPrice();
			}
		}
		return grandTotal;
	}

	public List<Integer> getSelectWholeSaleProductIds() {
		if (null != selectWholeSaleProducts) {
			List<Integer> ids = Lists.transform(selectWholeSaleProducts, e -> {
				return e.getIid();
			});

			return ids;
		}
		return null;
	}

	public void setSelectWholeSaleProducts(
			List<WholeSaleProduct> selectWholeSaleProducts) {
		this.selectWholeSaleProducts = selectWholeSaleProducts;
	}

	public List<String> getSelectSkus() {
		return selectSkus;
	}

	public void setSelectSkus(List<String> selectSkus) {
		this.selectSkus = selectSkus;
	}
}
