package services.product;

import java.util.Date;

import javax.inject.Inject;

import com.website.dto.Attribute;

import dto.category.AttributeKey;
import dto.category.AttributeKeyName;
import dto.category.AttributeValue;
import dto.category.AttributeValueName;


public class AttributeUpdateService {

	@Inject
	mapper.product.AttributeMapper attributeMapper;

	public String Insert(Attribute attr, String user) {
		if ((attr.getKey() == null || attr.getKey().trim().length() == 0)) {
			return "invalid key!";
		}
		// ~ 属性的key统一小写
		attr.setKey(attr.getKey().toLowerCase());
		AttributeKeyName dbattrkey = getAttributeKey(attr.getKey(),
				attr.getLanguageId());
		Integer keyid = -1;
		if (null == dbattrkey) {
			AttributeKey attributeKey = new AttributeKey();
			attributeKey.setCcreateuser(user);
			attributeKey.setDcreatedate(new Date());
			attributeMapper.insertKey(attributeKey);
			keyid = attributeKey.getIkeyid();

			AttributeKeyName attrkeyName = new AttributeKeyName();
			attrkeyName.setCkeyname(attr.getKey());
			attrkeyName.setIkeyid(keyid);
			attrkeyName.setIlanguageid(attr.getLanguageId());
			attributeMapper.insertKeyName(attrkeyName);
		} else {
			keyid = dbattrkey.getIkeyid();
		}

		if ((attr.getValue() == null || attr.getValue().trim().length() == 0)) {
			return "invalid value!";
		}
		Integer valueid = null;
		AttributeValueName attValue = attributeMapper
				.selectbyAttributeValueName(attr.getLanguageId(), keyid,
						attr.getValue());
		Boolean foundvaluename = true;
		if (attValue == null) {
			foundvaluename = false;
			AttributeValue attributevalue = new AttributeValue();
			attributevalue.setIkeyid(keyid);
			attributevalue.setCcreateuser(user);
			attributevalue.setDcreatedate(new Date());
			attributeMapper.insertValue(attributevalue);
			valueid = attributevalue.getIvalueid();
		} else {
			valueid = attValue.getIvalueid();
		}
		if (!foundvaluename) {
			AttributeValueName attrValueName = new AttributeValueName();
			attrValueName.setCvaluename(attr.getValue());
			attrValueName.setIvalueid(valueid);
			attrValueName.setIlanguageid(attr.getLanguageId());
			attributeMapper.insertValueName(attrValueName);
		}
		return "";
	}

	private AttributeKeyName getAttributeKey(String keyName, int lang) {
		return attributeMapper.selectBykeyName(keyName, lang);
	}

	public Attribute saveAttribute(Attribute attr, String user) {
		this.Insert(attr, user);
		AttributeKeyName dbattrkey = attributeMapper.selectBykeyName(
				attr.getKey(), attr.getLanguageId());
		attr.setKeyId(dbattrkey.getIkeyid());
		AttributeValueName valueobj = attributeMapper
				.selectbyAttributeValueName(attr.getLanguageId(),
						dbattrkey.getIkeyid(), attr.getValue());
		attr.setValueId(valueobj.getIvalueid());
		return attr;
	}
}
