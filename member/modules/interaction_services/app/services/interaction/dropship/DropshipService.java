package services.interaction.dropship;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import services.dropship.DropShipBaseEnquiryService;
import services.dropship.DropShipLevelEnquiryService;
import services.order.dropShipping.IDropshipService;
import services.product.IProductBadgeService;
import services.product.ProductEnquiryService;
import services.product.ProductLabelService;
import services.search.criteria.ProductLabelType;
import valueobjects.interaction.Dropship;
import valueobjects.member.DropshipBase;
import valueobjects.product.ProductBadge;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import dao.interaction.IDropshipProductDao;
import dao.product.IProductTranslateEnquiryDao;
import dao.product.IProductUrlEnquiryDao;
import dao.product.impl.ProductImageDao;
import dto.interaction.DropshipProduct;
import dto.member.DropShipBase;
import dto.member.DropShipLevel;
import dto.product.ProductBase;
import dto.product.ProductDropShip;

public class DropshipService implements IDropshipService {

	@Inject
	IDropshipProductDao dropshipDao;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	DropShipLevelEnquiryService dropShipLevelEnquiryService;

	@Inject
	DropShipBaseEnquiryService dropShipBaseEnquiryService;

	@Inject
	ProductLabelService productLabelService;

	@Inject
	IProductUrlEnquiryDao productUrlEnquityDao;

	@Inject
	IProductTranslateEnquiryDao productTranslateEnquityDao;

	@Inject
	ProductImageDao imgdao;

	public Boolean setDropShipStatus(Integer id, String email, Boolean status) {
		int result = dropshipDao.setDropShipStatus(id, email, status);
		return result > 0 ? true : false;
	}

	@Override
	public boolean setSkuToDropShip(String sku, String email) {
		Integer result = dropshipDao.setDropshipProductState(sku, email, true);
		return result > 0 ? true : false;
	}

	@Override
	public boolean cancelSkuToDropShip(String sku, String email) {
		Integer result = dropshipDao.setDropshipProductState(sku, email, false);
		return result > 0 ? true : false;
	}

	@Override
	public void cancelSkusToDropShip(List<String> skus, String email,
			Integer siteId) {
		dropshipDao.batchSetDropshipProductState(skus, email, false, siteId);
	}

	@Override
	public void setSkusToDropShip(List<String> skus, String email,
			Integer siteId) {
		dropshipDao.batchSetDropshipProductState(skus, email, true, siteId);
	}

	@Override
	public int getCountDropShipSkuByEmail(String email, Integer siteId) {
		return dropshipDao.getCountDropShipSkuByEmail(email, siteId);
	}

	@Override
	public List<ProductDropShip> getDropshipProductDownloadInfoByEmail(
			String email, Integer languageid, Integer siteid, String currency) {
		Boolean state = true;
		DropshipBase dropshipBase = getDropshipBaseByEmail(email, siteid);
		if (null != dropshipBase) {
			Integer limit = dropshipBase.getMaxDropshipNumberLimit();
			List<String> skus = getDropshipProductSkusByEmailAndState(email,
					state, limit, siteid);
			if (null != skus && skus.size() > 0) {
				return productEnquiryService.getProductDropShip(skus,
						languageid, siteid);
			}
		}
		return Lists.newArrayList();
	}

	public Map<String, valueobjects.interaction.DropshipProduct> getDripshipProductsByEmail(
			String email, Integer languageid, Integer siteid, String currency) {
		Boolean state = null;
		List<String> skus = getDropshipProductSkusByEmailAndState(email, state,
				null, siteid);
		if (null != skus && skus.size() > 0) {
			List<ProductBase> productBases = productEnquiryService
					.getProductBaseBySkus(skus, siteid);

			if (null != productBases && productBases.size() > 0) {
				Map<String, String> skuAndListingIdMap = Maps.newHashMap();
				for (ProductBase productBase : productBases) {
					skuAndListingIdMap.put(productBase.getCsku(),
							productBase.getClistingid());
				}
				List<String> listingIDs = Lists.transform(productBases,
						i -> i.getClistingid());
				List<DropshipProduct> dropshipProducts = dropshipDao
						.getDropshipProductsByEmailAndSkus(email, skus, siteid);
				Map<String, DropshipProduct> dropshipMap = Maps.uniqueIndex(
						dropshipProducts, i -> i.getCsku());
				List<ProductBadge> badges = badgeService
						.getProductBadgesByListingIDs(listingIDs, languageid,
								siteid, currency, null);
				Map<String, ProductBadge> badgeMap = Maps.uniqueIndex(badges,
						i -> {
							return i.getListingId();
						});
				Map<String, valueobjects.interaction.DropshipProduct> badegeMap2 = Maps
						.toMap(skus,
								i -> {
									String listingId = skuAndListingIdMap
											.get(i);
									return new valueobjects.interaction.DropshipProduct(
											dropshipMap.get(i), badgeMap
													.get(listingId));
								});
				return badegeMap2;
			}
		}
		return Maps.newHashMap();
	}

	/**
	 * @param email
	 * @param state
	 * @param limit
	 * @param websiteid
	 *            TODO
	 * @return 根据email取出状态为true的dropship产品
	 */
	List<String> getDropshipProductSkusByEmailAndState(String email,
			Boolean state, Integer limit, Integer websiteid) {
		return dropshipDao.getDropshipProductSkusByEmailAndState(email, state,
				limit, websiteid);
	}

	public boolean checkSkuIsExsitInProductBase(String sku, Integer siteId) {
		Integer state = 1;
		ProductBase p = productEnquiryService.getProductBySku(sku, siteId,
				state);
		if (null == p) {
			return false;
		}
		return true;
	}

	public boolean checkSkuIsExistInDropProduct(String sku, Integer siteId,
			String email) {
		DropshipProduct dropshipProduct = dropshipDao
				.getDropshipProductByEmailAndSku(email, sku, siteId);
		if (null != dropshipProduct) {
			return true;
		} else {
			return false;
		}
	}

	public boolean addDropshipProduct(DropshipProduct dropship) {
		return dropshipDao.addDropshipProduct(dropship);
	}

	public void batchDeleteDropshipProduct(List<Integer> ids) {
		dropshipDao.batchDeleteDropshipProduct(ids);
	}

	public boolean deleteDropshipProduct(Integer id) {
		int result = dropshipDao.deleteDropshipProduct(id);
		return result > 0 ? true : false;
	}

	public void batchSetDropshipProductStatus(String email, List<Integer> ids,
			Boolean status) {
		dropshipDao.batchSetDropshipProductStatus(email, ids, status);
	}

	/**
	 * @param status
	 * @param sku
	 * @param email
	 * @param siteid
	 * @param languageid
	 * @param currency
	 * @return 把所有符合查询条件的产品的
	 */
	public List<Dropship> getdropshipProductListByPage(String status,
			String sku, String email, Integer siteid, Integer languageid,
			String currency) {
		List<Dropship> dropships = Lists.newArrayList();
		List<ProductBase> productBasesFinal = Lists.newArrayList();
		List<String> skus = Lists.newArrayList();
		if ("".equals(sku)) {
			skus = getDropshipProductSkusByEmailAndState(email, true, null,
					siteid);
		} else {
			skus.add(sku);
		}
		if (null != skus && skus.size() > 0) {
			List<DropshipProduct> dropshipProducts = dropshipDao
					.getDropshipProductsByEmailAndSkus(email, skus, siteid);
			Map<String, Integer> dropshipMap = Maps.newHashMap();

			for (DropshipProduct dropshipProduct : dropshipProducts) {
				dropshipMap.put(dropshipProduct.getCsku(),
						dropshipProduct.getIid());
			}
			Set<String> clearanceListingIds = productLabelService
					.getListingsByType(ProductLabelType.Clearstocks.toString());

			List<ProductBase> productBases = productEnquiryService
					.getProductBaseBySkus(skus, siteid);

			if (null != productBases && productBases.size() > 0) {
				List<String> listingIds = Lists.transform(productBases,
						i -> i.getClistingid());
				List<String> clearListingid = productLabelService
						.getListByListingIdsAndType(listingIds,
								ProductLabelType.Clearstocks.toString());
				if ("all".equals(status)) {
					productBasesFinal = productBases;
				} else if ("stop".equals(status)) {
					productBasesFinal = Lists.newArrayList(Collections2.filter(
							productBases, i -> 2 == i.getIstatus()));
				} else if ("normal".equals(status)) {
					productBasesFinal = Lists.newArrayList(Collections2.filter(
							productBases,
							i -> (1 == i.getIstatus() && !clearListingid
									.contains(i.getClistingid()))));
				} else if ("clear".equals(status)) {
					productBasesFinal = Lists.newArrayList(Collections2.filter(
							productBases,
							j -> clearListingid.contains(j.getClistingid())));
				}

				if (null != productBasesFinal && productBasesFinal.size() > 0) {
					Set<String> listingidList = Sets.newHashSet(Lists
							.transform(productBasesFinal,
									i -> i.getClistingid()));
					List<String> list = Lists.newArrayList();
					list.addAll(listingidList);

					Map<String, ProductBase> baseMap = Maps.uniqueIndex(
							productBasesFinal, i -> i.getClistingid());

					Map<String, String> listingAndSkuMap = Maps.asMap(
							listingidList, i -> {
								ProductBase productBase = baseMap.get(i);
								return productBase.getCsku();
							});
					List<ProductBadge> productBadges = badgeService
							.getProductBadgesByListingIDs(list, languageid,
									siteid, currency, null);
					for (ProductBadge productBadge : productBadges) {
						if (null != productBadge) {
							String listingid = productBadge.getListingId();
							if (null == listingid) {
								productBadge.toString();
							}
							String dropshipSku = listingAndSkuMap
									.get(listingid);
							ProductBase p = baseMap.get(listingid);
							Integer state = p.getIstatus();
							Dropship dropship = new Dropship();
							dropship.setDropshipid(dropshipMap.get(dropshipSku));
							dropship.setImageUrl(productBadge.getImageUrl());
							dropship.setTitle(productBadge.getTitle());
							dropship.setUrl(productBadge.getUrl());
							dropship.setProductBase(p);
							if (p.getIstatus() == 1
									&& clearanceListingIds.contains(listingid)) {
								dropship.setStatus("Clearance");
							} else if (p.getIstatus() == 1) {
								dropship.setStatus("Normal");
							} else if (state == 2 || state == 3) {
								dropship.setStatus("Stop Selling");
							}
							dropships.add(dropship);
						}
					}
				}
			}
		}
		return dropships;
	}

	public DropshipBase getDropshipBaseByEmail(String email, Integer websiteId) {
		Integer status = 1; // 状态为1表示审核通过，成为dropship用户
		DropShipBase dropShipBase = dropShipBaseEnquiryService
				.getDropShipBaseByEmail(email, websiteId);
		if (null != dropShipBase && status == dropShipBase.getIstatus()) {
			Integer level = dropShipBase.getIdropshiplevel();
			DropShipLevel dropShipLevel = dropShipLevelEnquiryService
					.getDropShipLevelById(level);
			if (null != dropShipLevel)
				return new DropshipBase(email, dropShipLevel.getClevelname(),
						dropShipLevel.getDiscount(),
						dropShipLevel.getIproductcount());
		}
		return null;
	}

	public boolean addDropshipProductBylistingidAndEmail(String listingId,
			String email, Integer websiteid) {
		ProductBase p = productEnquiryService.getBaseByListingId(listingId);
		if (null != p) {
			String sku = p.getCsku();
			if (null != sku) {
				DropshipProduct d = dropshipDao
						.getDropshipProductByEmailAndSku(email, sku, websiteid);
				if (null == d) {
					DropshipProduct dropship = new DropshipProduct();
					dropship.setCemail(email);
					dropship.setCsku(sku);
					dropship.setBstate(false);
					dropship.setIwebsiteid(websiteid);
					dropship.setDcreatedate(new Date());
					boolean result = dropshipDao.addDropshipProduct(dropship);
					if (result) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean getDropshipProductByListingAndEmail(String email,
			String listingid, Integer siteId) {
		ProductBase p = productEnquiryService.getBaseByListingId(listingid);
		if (null != p) {
			String sku = p.getCsku();
			DropshipProduct dropshipProduct = dropshipDao
					.getDropshipProductByEmailAndSku(email, sku, siteId);
			if (null != dropshipProduct) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> getDropShipSkusByEmailAndSite(String email, int site) {
		boolean state = true;
		return dropshipDao.getDropshipProductSkusByEmailAndState(email, state,
				null, site);
	}

}
