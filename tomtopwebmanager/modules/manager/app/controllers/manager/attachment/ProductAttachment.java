package controllers.manager.attachment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.ILanguageService;
import services.IWebsiteService;
import services.product.IAttachmentDescService;
import services.product.IProductAttachmentMapperService;
import services.product.ProductEnquiryService;
import session.ISessionService;

import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;

import controllers.InterceptActon;
import dto.SimpleLanguage;
import dto.Website;
import dto.product.AttachmentDesc;
import dto.product.ProductAttachmentMapper;
import dto.product.ProductAttachmentMessage;
import dto.product.ProductBase;
import entity.manager.AdminUser;
import forms.product.ProductAttachmentMapperSearchForm;

@With(InterceptActon.class)
public class ProductAttachment extends Controller {
	@Inject
	IProductAttachmentMapperService productAttachmentMapperService;

	@Inject
	IAttachmentDescService attachmentDescService;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	ILanguageService languageService;

	@Inject
	IWebsiteService websiteService;
	
	@Inject
	ISessionService sessionService;
	
	public Result index() {
		return ok(views.html.manager.attachment.mapper.attachment_mapper_manager
				.render());
	}

	public Result search() {
		ProductAttachmentMapperSearchForm form = Form
				.form(ProductAttachmentMapperSearchForm.class)
				.bindFromRequest().get();

		List<ProductAttachmentMapper> productAttachmentMappers = productAttachmentMapperService
				.getProductAttachmentMapperBySearch(form);
		if (null == productAttachmentMappers
				|| 0 >= productAttachmentMappers.size()) {
			return ok(views.html.manager.attachment.mapper.attachment_mapper_table_list
					.render(null, null, null, 0, form.getPageNum(), 0));
		}
		Integer count = productAttachmentMapperService.getCountBySearch(form);

		List<Integer> attachmentdescids = productAttachmentMappers.stream()
				.map(ProductAttachmentMapper::getIattachmentdescid)
				.collect(Collectors.toList());
		List<AttachmentDesc> attachmentDescriptsByIids = attachmentDescService
				.getAttachmentDescriptsByIids(attachmentdescids);
		Map<Integer, AttachmentDesc> attachmentDescMaps = attachmentDescriptsByIids
				.stream().collect(Collectors.toMap(a -> a.getIid(), a -> a));
		List<ProductAttachmentMessage> productAttachmentMessages = Lists
				.newArrayList();
		for (ProductAttachmentMapper productAttachmentMapper : productAttachmentMappers) {
			Integer attachmentdescId = productAttachmentMapper
					.getIattachmentdescid();
			ProductAttachmentMessage productAttachmentMessage = new ProductAttachmentMessage();
			productAttachmentMessage
					.setProductAttachmentMapper(productAttachmentMapper);
			productAttachmentMessage.setAttachmentDesc(attachmentDescMaps
					.get(attachmentdescId));
			productAttachmentMessages.add(productAttachmentMessage);
		}
		List<SimpleLanguage> allSimpleLanguages = languageService
				.getAllSimpleLanguages();
		Map<Integer, String> languagesMap = allSimpleLanguages.stream()
				.collect(Collectors.toMap(a -> a.getIid(), a -> a.getCname()));
		List<Website> websites = websiteService.getAll();
		Map<Integer, String> websiteMap = websites.stream().collect(
				Collectors.toMap(a -> a.getIid(), a -> a.getCcode()));

		Integer pageTotal = count / form.getPageSize()
				+ ((count % form.getPageSize() > 0) ? 1 : 0);
		return ok(views.html.manager.attachment.mapper.attachment_mapper_table_list
				.render(productAttachmentMessages, languagesMap, websiteMap,
						count, form.getPageNum(), pageTotal));
	}

	public Result addProductAttachmentMapper() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		ProductAttachmentMapper form = Form.form(ProductAttachmentMapper.class)
				.bindFromRequest().get();
		HashMap<String, String> resultMap = Maps.newHashMap();
		ProductBase productBySku = productEnquiryService.getProductBySku(
				form.getCsku(), form.getIwebsiteid(), 1);
		if (null == form.getIattachmentdescid()) {
			resultMap.put("fail", "please choose file path!");

			return ok(Json.toJson(resultMap));
		}
		if (null == productBySku) {
			resultMap.put("fail",
					"not found this product, please change the sku!");

			return ok(Json.toJson(resultMap));
		}
		form.setClistingid(productBySku.getClistingid());
		form.setDcreatedate(new Date());
		form.setCcreateuser(user.getCcreateuser());
		boolean result = productAttachmentMapperService
				.addProductAttachmentMapper(form);
		resultMap.put("result", result + "");

		return ok(Json.toJson(resultMap));
	}

	public Result getDescByLanguageIdAndTitle(Integer languageId, String title) {
		AttachmentDesc attachmentDesc = new AttachmentDesc();
		attachmentDesc.setIlanguage(languageId);
		attachmentDesc.setCtitle(title);
		List<AttachmentDesc> attachmentDescriptBySearch = attachmentDescService
				.getAttachmentDescriptBySearch(attachmentDesc);

		return ok(views.html.manager.attachment.mapper.attachment_desc_list
				.render(attachmentDescriptBySearch));
	}

	public Result deleteProductAttachmentMapper(Integer iid) {
		boolean delete = productAttachmentMapperService
				.deleteProductAttachmentMapperByIid(iid);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", delete);
		return ok(Json.toJson(result));
	}
}
