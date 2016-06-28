package dao.product.impl;

import java.util.List;

import mapper.product.ProductExplainMapper;

import com.google.inject.Inject;

import dao.product.IProductExplainEnquiryDao;
import dto.product.ProductExplain;

public class ProductExplainEnquiryDao implements IProductExplainEnquiryDao {
	@Inject
	ProductExplainMapper productExplainMapper;

	@Override
	public String getContentForSiteAndLanAndType(int site, int lan, String type) {
		return productExplainMapper.getContentForSiteAndLanAndType(site, lan,
				type);
	}

	@Override
	public List<ProductExplain> getProductExplainsBySiteAndLan(int site, int lan) {
		return productExplainMapper.getProductExplainsBySiteAndLan(site, lan);
	}

	@Override
	public ProductExplain getProductExplainBySiteIdAndLanIdAndType(int site,
			int lan, String type) {
		return productExplainMapper.getProductExplainsBySiteIdAndLanIdAndType(
				site, lan, type);
	}

}
