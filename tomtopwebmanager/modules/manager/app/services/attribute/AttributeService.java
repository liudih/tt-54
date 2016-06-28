package services.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.attribute.AttributeKeyMapper;
import mapper.attribute.AttributeKeyNameMapper;
import mapper.attribute.AttributeValueMapper;
import mapper.attribute.AttributeValueNameMapper;

import org.mybatis.guice.transactional.Transactional;

import play.Logger;
import session.ISessionService;

import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;

import dto.AttributeKeyItem;
import dto.AttributeValueItem;
import entity.attribute.AttributeKey;
import entity.attribute.AttributeKeyName;
import entity.attribute.AttributeValue;
import entity.attribute.AttributeValueName;
import entity.manager.AdminUser;

public class AttributeService {
	@Inject
	mapper.attribute.AttributeItemMapper attributeMapper;

	@Inject
	ISessionService sessionService;

	@Inject
	AttributeKeyMapper attributeKeyMapper;

	@Inject
	AttributeKeyNameMapper attributeKeyNameMapper;

	@Inject
	AttributeValueMapper attributeValueMapper;

	@Inject
	AttributeValueNameMapper attributeValueNameMapper;

	public List<AttributeValueItem> getAttributeKeyByAttributeId(
			Integer ikeyid, Integer ilanguageid) {
		List<AttributeValueItem> attributeValueList = attributeMapper
				.getAllAttributeValueByKeyIdAndLanguageId(ikeyid, ilanguageid);

		return attributeValueList;
	}

	public AttributeKeyItem getAttributeKeyByAttributeIdAndLanguageId(
			Integer ikeyid, Integer ilanguageid) {
		AttributeKeyItem attributeKey = attributeMapper
				.getAllAttributeKeyByKeyIdAndLanguageId(ikeyid, ilanguageid);
		if (null != attributeKey) {
			List<AttributeValueItem> attributeValueList = getAttributeKeyByAttributeId(
					ikeyid, ilanguageid);
			attributeKey.setAttributeValue(attributeValueList);
		}

		return attributeKey;
	}

	public List<AttributeKeyItem> getAttributeKeyByLanguageId(
			Integer ilanguageid) {
		List<AttributeKeyItem> attributeKeyList = attributeMapper
				.getAllAttributeKeyByLanguageId(ilanguageid);
		List<String> keyNames = Lists.newArrayList();
		for (int i = 0; i < attributeKeyList.size(); i++) {
			AttributeKeyItem attributeKey = attributeKeyList.get(i);
			Integer ikeyid = attributeKey.getIkeyid();
			if (keyNames.contains(attributeKey.getCkeyname())) {
				attributeKeyList.remove(attributeKey);
				i--;
			} else {
				keyNames.add(attributeKey.getCkeyname());
			}
			List<AttributeValueItem> attributeValueList = getAttributeKeyByAttributeId(
					ikeyid, ilanguageid);
			attributeKey.setAttributeValue(attributeValueList);
		}

		return attributeKeyList;
	}

	public List<AttributeKeyItem> getAttributeKeyByLanguageIdAndCategoryId(
			Integer ilanguageid, Integer categoryId) {
		List<AttributeKeyItem> attributeKeyList = attributeMapper
				.getAllAttributeKeyByLanguageIdAndCategoryId(ilanguageid,
						categoryId);

		for (AttributeKeyItem attributeKey : attributeKeyList) {
			Integer ikeyid = attributeKey.getIkeyid();
			List<AttributeValueItem> attributeValueList = getAttributeKeyByAttributeId(
					ikeyid, ilanguageid);
			attributeKey.setAttributeValue(attributeValueList);
		}

		return attributeKeyList;
	}

	public List<AttributeKeyItem> getAttributeKeyByKeyId(Integer ikeyid) {
		List<AttributeKeyItem> attributeKeyList = attributeMapper
				.getAllAttributeKeyByKeyId(ikeyid);
		for (AttributeKeyItem attributeKey : attributeKeyList) {
			Integer ilanguageid = attributeKey.getIlanguageid();
			List<AttributeValueItem> attributeValueList = getAttributeKeyByAttributeId(
					ikeyid, ilanguageid);
			attributeKey.setAttributeValue(attributeValueList);
		}

		return attributeKeyList;
	}

	@Transactional
	public boolean updateAttribute(Map result) {
		// {id=10, ilanguageid=1, ikeyid=5, ckeyname=斯蒂芬, valuemap=[{id=48,
		// ivalueid=18, ilanguageid=1, cvaluename=斯蒂芬12},
		// {id=51, ivalueid=21, ilanguageid=1, cvaluename=斯蒂芬23}, {id=54,
		// ivalueid=22, ilanguageid=1, cvaluename=sdfsdfv}]}

		Integer iid = Integer.parseInt(result.get("id").toString());
		String ckeyname = result.get("ckeyname").toString();
		Integer ikeyid = Integer.parseInt(result.get("ikeyid").toString());
		Integer ilanguageid = Integer.parseInt(result.get("ilanguageid")
				.toString());
		AttributeKeyName attributeKeyName = new AttributeKeyName();
		attributeKeyName.setCkeyname(ckeyname);
		attributeKeyName.setIkeyid(ikeyid);
		attributeKeyName.setIlanguageid(ilanguageid);
		if (iid != -1) {
			attributeKeyName.setIid(iid);
			attributeKeyNameMapper.updateByPrimaryKey(attributeKeyName);
		} else {
			attributeKeyNameMapper.insert(attributeKeyName);
		}

		ArrayList<HashMap> valueList = new ArrayList<HashMap>();
		valueList = (ArrayList<HashMap>) result.get("valuemap");
		for (HashMap valueMap : valueList) {
			AttributeValueName attributeValueName = new AttributeValueName();
			Integer id = Integer.parseInt(valueMap.get("id").toString());
			String cvaluename = valueMap.get("cvaluename").toString();
			Integer ivalueid = Integer.parseInt(valueMap.get("ivalueid")
					.toString());
			Integer languageid = Integer.parseInt(valueMap.get("ilanguageid")
					.toString());
			attributeValueName.setCvaluename(cvaluename);
			attributeValueName.setIvalueid(ivalueid);
			attributeValueName.setIlanguageid(languageid);
			if (id != -1) {
				attributeValueName.setIkeyid(id);
				attributeValueNameMapper.updateByPrimaryKey(attributeValueName);
			} else {
				attributeValueNameMapper.insert(attributeValueName);
			}

		}

		return true;
	}

	@Transactional
	private boolean updateAttributeKey(AttributeKeyItem attributeKey) {
		return attributeKeyNameMapper.updateAttributeKey(attributeKey.getIid(),
				attributeKey.getCkeyname());
	}

	@Transactional
	private boolean updateAttributeValue(AttributeValueItem attributeValue) {
		return attributeValueNameMapper.updateAttributeValue(
				attributeValue.getIid(), attributeValue.getCvaluename());
	}

	@Transactional
	public boolean addAttribute(Map attributeMsg) {
		Integer ilanguageid = 1;
		String keyname = (String) attributeMsg.get("keyname");
		Integer ikeyid = saveAttributeKey();
		// 保存keyname;
		AttributeKeyName attributeKeyName = new AttributeKeyName();
		attributeKeyName.setCkeyname(keyname);
		attributeKeyName.setIkeyid(ikeyid);
		attributeKeyName.setIlanguageid(ilanguageid);
		attributeKeyNameMapper.insert(attributeKeyName);
		List<String> valuename = (List<String>) attributeMsg.get("valuename");
		Logger.debug("valuename: {}", valuename);
		if (valuename.size() > 0) {
			for (String vname : valuename) {
				Integer ivalueid = saveAttributeValue(ikeyid);
				AttributeValueName attributeValueName = new AttributeValueName();
				attributeValueName.setIvalueid(ivalueid);
				attributeValueName.setCvaluename(vname);
				attributeValueName.setIlanguageid(ilanguageid);
				attributeValueNameMapper.insert(attributeValueName);
			}
		}

		return true;
	}

	@Transactional
	public Integer saveAttributeKey() {
		AttributeKey attributeKey = new AttributeKey();
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		attributeKey.setCcreateuser(user.getCusername());
		attributeKeyMapper.insert(attributeKey);

		return attributeKey.getIkeyid();
	}

	@Transactional
	public Integer saveAttributeValue(Integer ikeyid) {
		AttributeValue attributeValue = new AttributeValue();
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		attributeValue.setCcreateuser(user.getCusername());
		attributeValue.setIkeyid(ikeyid);
		attributeValueMapper.insert(attributeValue);

		return attributeValue.getIvalueid();
	}

	// public boolean saveAttribute(Map result) {
	// Integer languageid =
	// Integer.parseInt(result.get("languageid").toString());
	// Integer keyid = Integer.parseInt(result.get("keyid").toString());
	// String keyname = (String) result.get("keyname");
	// Map<Object, String> valueMap = (Map<Object, String>) result.get("value");
	// Logger.debug("valueMap : {}", valueMap);
	// AttributeKeyName attributeKeyName = new AttributeKeyName();
	// attributeKeyName.setIkeyid(keyid);
	// attributeKeyName.setIlanguageid(languageid);
	// attributeKeyName.setCkeyname(keyname);
	// attributeKeyNameMapper.insert(attributeKeyName);
	//
	// Iterator<Entry<Object, String>> valueIterator =
	// valueMap.entrySet().iterator();
	// while(valueIterator.hasNext()) {
	// Logger.debug("valueMap : {}", valueMap);
	// Entry<Object, String> valueMsg = valueIterator.next();
	// Integer valueid = Integer.parseInt(valueMsg.getKey().toString());
	// String valuename = valueMsg.getValue();
	// Logger.debug("valueid : {}", valueid);
	// Logger.debug("valuename : {}", valuename);
	// AttributeValueName attributeValueName = new AttributeValueName();
	// attributeValueName.setIlanguageid(languageid);
	// attributeValueName.setIvalueid(valueid);
	// attributeValueName.setCvaluename(valuename);
	//
	// attributeValueNameMapper.insert(attributeValueName);
	// }
	//
	// return true;
	// }

	public void saveAttributeValue(AttributeValueName attributeValueName) {
		Integer valueId = saveAttributeValue(attributeValueName.getIkeyid());

		attributeValueName.setIvalueid(valueId);
		attributeValueNameMapper.insert(attributeValueName);
	}

	public AttributeKeyItem changeAttributeValueItem(
			AttributeKeyItem defaultItem, AttributeKeyItem attributeKeyByKeyId) {
		List<AttributeValueItem> attributeValue = defaultItem
				.getAttributeValue();
		List<AttributeValueItem> attributeValueList = attributeKeyByKeyId
				.getAttributeValue();
		Map<Integer, AttributeValueItem> attributeValueMap = Maps.newHashMap();
		for (AttributeValueItem b : attributeValueList) {
			attributeValueMap.put(b.getIvalueid(), b);
		}
		for (AttributeValueItem attributeValueItem : attributeValue) {
			Integer valueId = attributeValueItem.getIvalueid();
			if (!attributeValueMap.containsKey(valueId)) {
				AttributeValueItem notRealValueItem = new AttributeValueItem();
				notRealValueItem.setCvaluename(attributeValueItem
						.getCvaluename());
				notRealValueItem.setIid(attributeValueItem.getIid());
				notRealValueItem.setIlanguageid(attributeValueItem
						.getIlanguageid());
				notRealValueItem.setIvalueid(attributeValueItem.getIvalueid());
				notRealValueItem.setNotReal(true);
				attributeValueList.add(notRealValueItem);
			}
		}
		attributeKeyByKeyId.setAttributeValue(attributeValueList);

		return attributeKeyByKeyId;
	}

	public AttributeKeyItem changeAttributeValueItem(
			AttributeKeyItem defaultItem, Integer languageId) {
		AttributeKeyItem attributeKeyItem = new AttributeKeyItem();
		List<AttributeValueItem> attributeValue = defaultItem
				.getAttributeValue();
		List<AttributeValueItem> attributeValueList = new ArrayList<AttributeValueItem>();
		attributeKeyItem.setCkeyname(defaultItem.getCkeyname());
		attributeKeyItem.setIkeyid(defaultItem.getIkeyid());
		attributeKeyItem.setIlanguageid(languageId);
		attributeKeyItem.setNotReal(true);
		for (AttributeValueItem attributeValueItem : attributeValue) {
			Integer valueId = attributeValueItem.getIvalueid();
			AttributeValueItem notRealValueItem = new AttributeValueItem();
			notRealValueItem.setCvaluename(attributeValueItem.getCvaluename());
			notRealValueItem.setIid(attributeValueItem.getIid());
			notRealValueItem.setIlanguageid(languageId);
			notRealValueItem.setIvalueid(attributeValueItem.getIvalueid());
			notRealValueItem.setNotReal(true);
			attributeValueList.add(notRealValueItem);
		}
		attributeKeyItem.setAttributeValue(attributeValueList);

		return attributeKeyItem;
	}
}
