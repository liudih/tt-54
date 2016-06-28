package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import com.website.dto.Attribute;

import dto.category.AttributeKeyName;

public class AttributeEnquiryDao implements dao.product.IAttributeEnquiryDao {

	@Inject
	mapper.product.AttributeMapper attributeMapper;

	@Override
	public Integer getKeyId(String keyName, Integer lang) {
		AttributeKeyName key = attributeMapper.selectBykeyName(keyName, lang);
		if (key == null) {
			return null;
		}
		return key.getIkeyid();
	}

	@Override
	public List<Attribute> getAll(Integer languageid) {
		return attributeMapper.getAllAttibutes(languageid);
	}

}
