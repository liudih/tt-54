package services.product;

import java.util.List;

import javax.inject.Inject;

import dao.product.IProductAttachmentMapperEnquiryDao;
import dao.product.IProductAttachmentMapperUpdateDao;
import dto.product.ProductAttachmentMapper;
import forms.product.ProductAttachmentMapperSearchForm;

public class ProductAttachmentMapperService implements
		IProductAttachmentMapperService {
	@Inject
	IProductAttachmentMapperEnquiryDao enquiryDao;

	@Inject
	IProductAttachmentMapperUpdateDao updateDao;

	@Override
	public List<ProductAttachmentMapper> getProductAttachmentMapperBySearch(
			ProductAttachmentMapperSearchForm form) {
		return enquiryDao.getProductAttachmentMapperBySearch(form);
	}

	@Override
	public Integer getCountBySearch(
			ProductAttachmentMapperSearchForm productAttachmentMapperSearchForm) {
		return enquiryDao.getCountBySearch(productAttachmentMapperSearchForm);
	}

	@Override
	public boolean addProductAttachmentMapper(
			ProductAttachmentMapper productAttachmentMapper) {
		return updateDao.addProductAttachmentMapper(productAttachmentMapper);
	}

	@Override
	public boolean deleteProductAttachmentMapperByIid(Integer iid) {
		return updateDao.deleteProductAttachmentMapperByIid(iid);
	}

}
