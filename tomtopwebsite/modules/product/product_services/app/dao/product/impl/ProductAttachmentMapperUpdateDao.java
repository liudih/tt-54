package dao.product.impl;

import javax.inject.Inject;

import mapper.product.ProductAttachmentMapperMapper;
import dao.product.IProductAttachmentMapperUpdateDao;
import dto.product.ProductAttachmentMapper;

public class ProductAttachmentMapperUpdateDao implements
		IProductAttachmentMapperUpdateDao {
	@Inject
	ProductAttachmentMapperMapper mapper;

	@Override
	public boolean addProductAttachmentMapper(
			ProductAttachmentMapper productAttachmentMapper) {
		return mapper.addProductAttachmentMapper(
				productAttachmentMapper.getIwebsiteid(),
				productAttachmentMapper.getCsku(),
				productAttachmentMapper.getClistingid(),
				productAttachmentMapper.getIlanguage(),
				productAttachmentMapper.getIattachmentdescid(),
				productAttachmentMapper.getCcreateuser(),
				productAttachmentMapper.getDcreatedate()) > 0 ? true : false;
	}

	@Override
	public boolean deleteProductAttachmentMapperByIid(Integer iid) {
		return mapper.deleteProductAttachmentMapperByIid(iid) > 0 ? true
				: false;
	}

}
