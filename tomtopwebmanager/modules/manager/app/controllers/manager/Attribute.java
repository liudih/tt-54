package controllers.manager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.ILanguageService;
import services.attribute.AttributeService;
import services.product.CategoryEnquiryService;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.InterceptActon;
import dto.AttributeKeyItem;
import dto.SimpleLanguage;
import entity.attribute.AttributeValueName;

@With(InterceptActon.class)
public class Attribute extends Controller {
	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	ILanguageService languageService;

	@Inject
	AttributeService attributeService;

	public Result getAttributeList(Integer ilanguageid) {
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		List<AttributeKeyItem> attributeKeys = attributeService
				.getAttributeKeyByLanguageId(ilanguageid);
		Logger.debug("attributeMap: {}", attributeKeys);
		return ok(views.html.manager.attribute.attribute_list.render(
				attributeKeys, languageList, ilanguageid));
	}

	public Result getAttributeManager(Integer attributekeyid,
			Integer ilanguageid) {
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		LinkedHashMap<SimpleLanguage, AttributeKeyItem> attributeMap = new LinkedHashMap<SimpleLanguage, AttributeKeyItem>();
		AttributeKeyItem defaultItem = null;
		for (SimpleLanguage simpleLanguage : languageList) {
			Integer iid = simpleLanguage.getIid();
			AttributeKeyItem attributeKeyByKeyId = attributeService
					.getAttributeKeyByAttributeIdAndLanguageId(attributekeyid, iid);
			LinkedHashMap<String, AttributeKeyItem> realAttributeKeyItem = new LinkedHashMap<String, AttributeKeyItem>();

			if (1 == iid && null != attributeKeyByKeyId) {
				defaultItem = attributeKeyByKeyId;
			}
			if (null == attributeKeyByKeyId && null != defaultItem) {
				attributeKeyByKeyId = new AttributeKeyItem();
				attributeKeyByKeyId.setIlanguageid(iid);
				attributeKeyByKeyId = attributeService
						.changeAttributeValueItem(defaultItem, iid);
			} else {
				if (defaultItem.getAttributeValue().size() != attributeKeyByKeyId
						.getAttributeValue().size()) {
					attributeKeyByKeyId = attributeService
							.changeAttributeValueItem(defaultItem, attributeKeyByKeyId);
				}
			}

			attributeMap.put(simpleLanguage, attributeKeyByKeyId);
		}
		return ok(views.html.manager.attribute.attribute_edit.render(
				attributeMap, ilanguageid));
	}

	@JsonGetter
	public Result updateAttribute() {
		JsonNode asJson = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		Map result = mapper.convertValue(asJson, Map.class);
		Logger.debug("result:{}", result);
		boolean updateAttribute = attributeService.updateAttribute(result);

		return ok(updateAttribute + "");
	}

	@JsonGetter
	public Result addAttribute() {
		JsonNode asJson = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		Map result = mapper.convertValue(asJson, Map.class);
		boolean addAttribute = attributeService.addAttribute(result);

		return ok(addAttribute + "");
	}

//	@JsonGetter
//	public Result saveAttribute() {
//		JsonNode asJson = request().body().asJson();
//		ObjectMapper mapper = new ObjectMapper();
//		Map result = mapper.convertValue(asJson, Map.class);
//		boolean saveAttribute = attributeService.saveAttribute(result);
//		
//		return ok(true + "");
//	}

	public Result addAttributeValue() {
		Form<AttributeValueName> attributeValue = Form.form(
				AttributeValueName.class).bindFromRequest();
		Logger.debug("AttributeValue:{}", attributeValue);
		attributeService.saveAttributeValue(attributeValue.get());

		return ok("");
	}
}
