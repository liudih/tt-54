package services.product;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import mapper.product.ProductEntityMapMapper;

import org.mybatis.guice.transactional.Transactional;

import services.IFoundationService;

import com.google.common.collect.Maps;

import context.WebContext;
import dto.product.ProductAttributeMessage;
import dto.product.ProductBase;
import dto.product.ProductEntityMap;

public class EntityMapService implements IEntityMapService {
	@Inject
	ProductEntityMapMapper mapMapper;

	@Inject
	ProductBaseMapper baseMapper;

	@Inject
	IFoundationService foundationService;

	@Override
	public List<ProductEntityMap> getEntityMapsByListingId(String listingId) {
		return mapMapper.getProductEntityMapByListingid(listingId);
	}

	@Override
	public List<ProductEntityMap> getProductEntityMapByListingId(
			String listingId, Integer lang) {
		return mapMapper.getProductEntityMapByListingId(listingId, lang);
	}

	@Override
	public List<ProductEntityMap> getProductEntityMapByListingId(
			String listingId, WebContext context) {
		int lang = foundationService.getLanguage(context);
		return this.getProductEntityMapByListingId(listingId, lang);
	}

	@Transactional
	public boolean save(List<ProductEntityMap> list, String listingId,
			String user) {
		int a = 0;
		ProductBase productBase = baseMapper
				.getProductBaseByListingId(listingId);
		for (ProductEntityMap map : list) {
			map.setBshow(true);
			map.setIwebsiteid(productBase.getIwebsiteid());
			map.setCsku(productBase.getCsku());
			map.setClistingid(productBase.getClistingid());
			map.setCcreateuser(user);
			a += mapMapper.insertSelective(map);
		}
		if (a == list.size()) {
			return true;
		}
		return false;
	}

	public void deleteByListingIdAndKeyId(String listingId, Integer keyId) {
		mapMapper.deleteByListingIdAndKeyId(listingId, keyId);
	}

	@Override
	public List<ProductBase> getProductsWithSameParentSku(String cparentsku) {
		return baseMapper.getProductsWithSameParentSkuByParentSku(cparentsku);
	}

	public boolean delete(Integer id) {
		int i = mapMapper.deleteByPrimaryKey(id);
		if (1 == i) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, String> getAttributeMap(String listingID,
			Integer languageID) {
		List<ProductEntityMap> attributes = getProductEntityMapByListingId(
				listingID, languageID);
		Map<String, String> attributeMap = Maps.newHashMap();
		if (null != attributes && attributes.size() > 0) {
			for (ProductEntityMap productEntityMap : attributes) {
				String ckeyName = productEntityMap.getCkeyname();
				String cvalueName = productEntityMap.getCvaluename();
				if (null != ckeyName && null != cvalueName) {
					attributeMap.put(ckeyName, cvalueName);
				}
			}
		}
		return attributeMap;
	}

	@Override
	public Map<String, String> getAttributeMap(String listingID,
			WebContext context) {
		int languageID = foundationService.getLanguage(context);
		return this.getAttributeMap(listingID, languageID);
	}

	@Override
	public List<ProductEntityMap> getProductEntityMapWithSameParentSkuByListingId(
			String listingID, Integer langid, String keyname, Integer websiteid) {
		return mapMapper.getEntityMaps(listingID, langid, keyname, websiteid);
	}

	@Override
	public List<ProductEntityMap> getProductEntityMapWithSameParentSkuByListingId(
			String listingID, String keyname, WebContext context) {
		int siteId = foundationService.getSiteID(context);
		int languageid = foundationService.getLanguage(context);
		return this.getProductEntityMapWithSameParentSkuByListingId(listingID,
				languageid, keyname, siteId);
	}

	@Override
	public List<ProductAttributeMessage> getProductAttributeMessages(
			List<String> listingIds, WebContext webContext, String keyname) {
		int siteId = foundationService.getSiteID(webContext);
		int languageid = foundationService.getLanguage(webContext);
		return mapMapper.getProductAttributeMessages(listingIds, languageid,
				keyname, siteId);
	}

	@Override
	public List<ProductEntityMap> getProductEntityMapListByListingIds(
			List<String> listingIds, WebContext context) {
		int languageid = foundationService.getLanguage(context);
		int websiteid = foundationService.getSiteID(context);
		return mapMapper.getProductEntityMapListByListingIds(listingIds,
				languageid,websiteid);
	}

	public List<ProductEntityMap> getProductEntityMapListByListingIds(
			List<String> listingIds, int languageid,int websiteid) {
		return mapMapper.getProductEntityMapListByListingIds(listingIds,
				languageid,websiteid);
	}

}
