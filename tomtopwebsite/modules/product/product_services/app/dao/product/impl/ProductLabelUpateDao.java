package dao.product.impl;

import java.util.List;

import mapper.product.ProductLabelMapper;

import org.springframework.beans.BeanUtils;

import play.Logger;

import com.google.inject.Inject;

import dao.product.IProductLabelUpdateDao;
import dto.product.ProductLabel;

public class ProductLabelUpateDao implements IProductLabelUpdateDao {

	@Inject
	ProductLabelMapper productLabelMapper;

	@Override
	public int insert(ProductLabel productLabel) {
		return this.productLabelMapper.insert(productLabel);
	}

	@Override
	public int insertOrUpdate(ProductLabel productLabel) {
		if (null != productLabel) {
			String listingId = productLabel.getClistingid();
			String type = productLabel.getCtype();
			if (productLabel.getIwebsiteid() == null) {
				Logger.warn("can save productlable :{} webiste is null ",
						listingId);
				return 0;
			}
			int iwebsiteid = productLabel.getIwebsiteid();
			ProductLabel p = productLabelMapper
					.getProductLabelByListingIdAndTypeAndWebsite(listingId,
							iwebsiteid, type);
			if (null != p) {
				BeanUtils.copyProperties(productLabel, p);
				return this.productLabelMapper.updateProductLable(p);
			}
			return this.productLabelMapper.insert(productLabel);
		}
		return 0;

	}

	@Override
	public int deleteBySiteAndType(int site, String type) {
		return this.productLabelMapper.deleteBySiteAndType(site, type);
	}

	@Override
	public int deleteByListingId(String listingid) {
		return this.productLabelMapper.deleteByListingId(listingid);
	}

	@Override
	public void batchInsert(List<ProductLabel> productLabels) {
		this.productLabelMapper.batchInsert(productLabels);
	}

	@Override
	public void deleteProductLabelByType(Integer iwebsiteid, String type) {
		this.productLabelMapper.deleteProductLabelByType(iwebsiteid, type);
	}

	@Override
	public int deleteByListingAndType(String listingid, String type) {
		return this.productLabelMapper.deleteByListingAndType(listingid, type);
	}

	@Override
	public int deleteByListingIdAndWebsiteId(String listingId, Integer websiteId) {
		return this.productLabelMapper.deleteByListingIdAndWebsiteId(listingId,
				websiteId);
	}

	@Override
	public int deleteByListingIdsAndType(List<String> listingids, String type) {
		return this.productLabelMapper.deleteByListingIdsAndType(listingids,
				type);
	}

	@Override
	public boolean isExists(String listingid, String type) {
		int n = productLabelMapper.isExists(listingid, type);
		if (n == 0) {
			return false;
		}
		return true;
	}
}
