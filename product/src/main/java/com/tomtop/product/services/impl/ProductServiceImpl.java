package com.tomtop.product.services.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.dao.IProductDao;
import com.tomtop.product.dao.IProductReviewsDao;
import com.tomtop.product.models.dto.FindProductsDto;
import com.tomtop.product.models.dto.OCShelfDto;
import com.tomtop.product.models.dto.ProductBadge;
import com.tomtop.product.models.dto.ProductsDto;
import com.tomtop.product.models.dto.ProductsImgDto;
import com.tomtop.product.models.dto.ProductsQueryDto;
import com.tomtop.product.models.dto.ReviewCountAndScoreDto;
import com.tomtop.product.services.IProductService;

@Service("productService")
public class ProductServiceImpl implements IProductService {

	@Resource(name = "productDao")
	private IProductDao productDao;

	@Resource(name = "productReviewsDao")
	private IProductReviewsDao reviewDao;

	@Override
	public Result findDetailProductsInfo(String sku) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProductBadge getProductBadgeByListingId(String listingId,
			int languageid, int iwebsiteid) {
		return productDao.getProductBadgeByListingId(listingId, languageid,
				iwebsiteid);
	}

	@Override
	public Result findDetailProductIdInfo(String listingId, int languageid,
			int iwebsiteid) {
		ProductBadge badge = productDao.getProductBadgeByListingId(listingId,
				languageid, iwebsiteid);
		if (badge != null) {
			ReviewCountAndScoreDto score = reviewDao.getScoreByListingId(
					listingId, 1);
			if (score != null) {
				badge.setAvgScore(score.getAvgScore());
				badge.setReviewCount(score.getReviewCount());
			}
		}
		return new Result(Result.SUCCESS, badge);
	}

	@Override
	public List<ProductBadge> findDetailProductIdInfo(List<String> listingId,
			int languageid, int iwebsiteid) {
		// TODO Auto-generated method stub
		List<ProductBadge> badges = productDao.getProductBadgeListByListingIds(
				listingId, languageid, iwebsiteid, 1);
		if (badges != null && badges.size() > 0) {
			List<ReviewCountAndScoreDto> dtos = reviewDao
					.getScoreListByListingIds(listingId, 1);
			if (dtos != null && dtos.size() > 0) {
				Map<String, ReviewCountAndScoreDto> dtosMaps = Maps
						.uniqueIndex(dtos, e -> {
							return e.getListingId();
						});
				if (dtosMaps != null && dtosMaps.size() > 0) {
					badges.stream()
							.forEach(
									e -> {
										ReviewCountAndScoreDto dto = dtosMaps
												.get(e.getListingId());
										if (dto != null) {
											e.setAvgScore(dtosMaps.get(
													e.getListingId())
													.getAvgScore());
											e.setReviewCount(dtosMaps.get(
													e.getListingId())
													.getReviewCount());
										}
									});
				}
			}
		}
		return badges;
	}

	@Override
	public List<ProductBadge> getProductBadgeListByListingIds(
			List<String> listingId, int languageid, int iwebsiteid, int istatus) {
		// TODO Auto-generated method stub
		List<ProductBadge> badges = productDao.getProductBadgeListByListingIds(
				listingId, languageid, iwebsiteid, istatus);
		return badges;
	}

	@Override
	public Result findDetailProductsInfo(String id, String site) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result updateProducts(ProductsDto ProductsDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result deleteProducts(ProductsDto ProductsDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result findProducts(ProductsQueryDto ProductsQueryDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result findDetailProductsInfo(ProductsDto ProductsDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result findProductsInfoBySpecCode(
			ProductsQueryDto ProductsInfoQueryDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result findProductsInfoForChange(ProductsDto ProductsDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result listByProductsCodes(List<String> ProductsCodes)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result updateAvailableStock(List<ProductsDto> ProductsDtos)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result updateStockByWMS(List<ProductsDto> ProductsDtos)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result updateIsopenshelf(OCShelfDto ocShelfDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result queryProducts() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result findDetailProductsPage(ProductsDto ProductsDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result findProductsListPage(FindProductsDto findProductsDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductsImgDto> findProductsImg(ProductsImgDto ProductsImgPo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result findSpecListPage(String specCodes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result findProductsListBySpecPage(String specCodes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result findStockAndPricePage(FindProductsDto findProductsDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
