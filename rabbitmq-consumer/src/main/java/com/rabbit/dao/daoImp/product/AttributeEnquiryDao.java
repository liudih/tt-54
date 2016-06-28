package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.AttributeMapper;
import com.rabbit.dao.idao.product.IAttributeEnquiryDao;
import com.rabbit.dto.category.AttributeKeyName;
import com.rabbit.dto.transfer.Attribute;
@Component
public class AttributeEnquiryDao implements IAttributeEnquiryDao {

	@Autowired
	AttributeMapper attributeMapper;

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
