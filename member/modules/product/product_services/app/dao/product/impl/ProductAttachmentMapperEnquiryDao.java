package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductAttachmentMapperMapper;
import dao.product.IProductAttachmentMapperEnquiryDao;
import dto.product.ProductAttachmentMapper;
import forms.product.ProductAttachmentMapperSearchForm;

public class ProductAttachmentMapperEnquiryDao implements
		IProductAttachmentMapperEnquiryDao {
	@Inject
	ProductAttachmentMapperMapper mapper;

	@Override
	public List<ProductAttachmentMapper> getProductAttachmentMapperBySearch(
			ProductAttachmentMapperSearchForm form) {
		return mapper.getProductAttachmentMapperBySearch(form.getWebsiteId(),
				form.getSku(), form.getLanguageId(), form.getTitle(),
				form.getPageSize(), form.getPageNum());
	}

	@Override
	public Integer getCountBySearch(ProductAttachmentMapperSearchForm form) {
		return mapper.getCountBySearch(form.getWebsiteId(), form.getSku(),
				form.getLanguageId(), form.getTitle());
	}
}
