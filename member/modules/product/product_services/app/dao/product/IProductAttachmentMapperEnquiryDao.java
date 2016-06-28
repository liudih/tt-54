package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.ProductAttachmentMapper;
import forms.product.ProductAttachmentMapperSearchForm;

public interface IProductAttachmentMapperEnquiryDao extends IProductEnquiryDao {
	List<ProductAttachmentMapper> getProductAttachmentMapperBySearch(
			ProductAttachmentMapperSearchForm form);

	Integer getCountBySearch(
			ProductAttachmentMapperSearchForm productAttachmentMapperSearchForm);
}
