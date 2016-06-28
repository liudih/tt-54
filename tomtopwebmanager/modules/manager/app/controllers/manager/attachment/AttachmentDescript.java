package controllers.manager.attachment;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.ILanguageService;
import services.product.IAttachmentDescService;
import session.ISessionService;

import com.google.common.collect.Maps;

import controllers.InterceptActon;
import dto.SimpleLanguage;
import dto.product.AttachmentDesc;
import entity.manager.AdminUser;

@With(InterceptActon.class)
public class AttachmentDescript extends Controller {
	@Inject
	ISessionService sessionService;

	@Inject
	ILanguageService languageService;

	@Inject
	IAttachmentDescService attachmentDescService;

	public Result edit(Integer iid) {
		List<SimpleLanguage> allLanguage = languageService
				.getAllSimpleLanguages();
		List<AttachmentDesc> attachmentDescripts = attachmentDescService
				.getAttachmentDescriptsByAttachmentId(iid);
		LinkedHashMap<SimpleLanguage, AttachmentDesc> attMap = Maps
				.newLinkedHashMap();
		Map<Integer, AttachmentDesc> attachmentDescMap = Maps.newHashMap();
		if (null != attachmentDescripts && 0 < attachmentDescripts.size()) {
			attachmentDescMap = attachmentDescripts.stream().collect(
					Collectors.toMap(a -> a.getIlanguage(), a -> a));
		}

		for (SimpleLanguage simpleLanguage : allLanguage) {
			Integer languageId = simpleLanguage.getIid();
			attMap.put(
					simpleLanguage,
					attachmentDescMap != null
							&& attachmentDescMap.get(languageId) != null ? attachmentDescMap
							.get(languageId) : new AttachmentDesc(iid));
		}
		return ok(views.html.manager.attachment.attachment_desc_edit.render(
				attMap, 1));
	}

	public Result updateAttachmentDesc() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		AttachmentDesc form = Form.form(AttachmentDesc.class).bindFromRequest()
				.get();
		HashMap<String, Boolean> resultMap = Maps.newHashMap();
		boolean result = false;
		if (null != form.getIid()) {
			form.setCupdateuser(user.getCcreateuser());
			form.setDupdatedate(new Date());

			result = attachmentDescService.updateAttachmentDesc(form);
		} else {
			form.setDcreatedate(new Date());
			form.setCcreateuser(user.getCcreateuser());

			result = attachmentDescService.addAttachmentDesc(form);
		}
		resultMap.put("result", result);

		return ok(Json.toJson(resultMap));
	}

}
