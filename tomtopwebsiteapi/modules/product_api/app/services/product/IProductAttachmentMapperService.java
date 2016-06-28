package services.product;

import java.util.List;

import dto.product.ProductAttachmentMapper;
import forms.product.ProductAttachmentMapperSearchForm;

public interface IProductAttachmentMapperService {
	public List<ProductAttachmentMapper> getProductAttachmentMapperBySearch(
			ProductAttachmentMapperSearchForm form);

	public Integer getCountBySearch(
			ProductAttachmentMapperSearchForm productAttachmentMapperSearchForm);

	public boolean addProductAttachmentMapper(
			ProductAttachmentMapper productAttachmentMapper);

	public boolean deleteProductAttachmentMapperByIid(Integer iid);
}
