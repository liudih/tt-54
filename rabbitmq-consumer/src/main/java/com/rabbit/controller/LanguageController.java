package com.rabbit.controller;

import java.util.Collection;
import java.util.List;












import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.rabbit.dto.base.LanguageLocal;
import com.rabbit.dto.transfer.Language;
import com.rabbit.services.serviceImp.base.LanguageService;
@Controller
public class LanguageController{

	/*@Autowired
	FoundationService foundation;*/

	@Autowired
	LanguageService languageService;

	private static final String IF_NONE_MATCH = "If-None-Match";

	private static final String CACHE_CONTROL = "Cache-Control";

	private static final String ETAG = "Etag"; 

	private static final int NOT_MODIFIED = 304;
	/*public String switchLanguage(int langId) {
		foundation.setLanguage(langId);
		String fromAddress = request().getHeader("Referer");
		if (!StringUtils.isEmpty(fromAddress)) {
			return redirect(fromAddress);
		}
		return redirect("/");
	}*/
	@RequestMapping(value = "/base/language")
	@ResponseBody
	public String getAllLanguage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<LanguageLocal> languages = languageService.getAllLanguage();
		String etag = generateETag(languages);
		String previous = request.getHeader(IF_NONE_MATCH);
		if (etag != null && etag.equals(previous)) {
			return null;//throw new Exception(NOT_MODIFIED+"");//status(NOT_MODIFIED);
		}
		response.setHeader(CACHE_CONTROL, "max-age=604800");
		response.setHeader(ETAG, etag);

		Collection<Language> dtoLanguages = null;

		if (null != languages && languages.size() > 0) {
			dtoLanguages = Collections2.transform(languages, obj -> {
				Language lang = new Language();
				lang.setId(obj.getIid());
				lang.setName(obj.getCname());
				return lang;
			});
		}

		if (null == dtoLanguages) {
			return null;
		} else {
			return JSON.toJSONString(dtoLanguages);
		}
	}

	protected String generateETag(List<LanguageLocal> languages) {
		List<String> allName = Lists.transform(languages, c -> c.getCname());
		StringBuilder sb = new StringBuilder();
		for (String s : allName) {
			sb.append(s);
		}
		return "language-" + Integer.toHexString(sb.toString().hashCode());
	}

}
