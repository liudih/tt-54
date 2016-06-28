package controllers.base;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import services.base.FoundationService;
import services.base.utils.StringUtils;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class Language extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	ILanguageService languageService;

	public Result switchLanguage(int langId) {
		foundation.setLanguage(langId);
		String fromAddress = request().getHeader("Referer");
		if (!StringUtils.isEmpty(fromAddress)) {
			return redirect(fromAddress);
		}
		return redirect("/");
	}

	public Result getAllLanguage() {
		List<dto.Language> languages = languageService.getAllLanguage();
		String etag = generateETag(languages);
		String previous = request().getHeader(IF_NONE_MATCH);
		if (etag != null && etag.equals(previous)) {
			return status(NOT_MODIFIED);
		}
		response().setHeader(CACHE_CONTROL, "max-age=604800");
		response().setHeader(ETAG, etag);

		Collection<com.website.dto.Language> dtoLanguages = null;

		if (null != languages && languages.size() > 0) {
			dtoLanguages = Collections2.transform(languages, obj -> {
				com.website.dto.Language lang = new com.website.dto.Language();
				lang.setId(obj.getIid());
				lang.setName(obj.getCname());
				return lang;
			});
		}

		if (null == dtoLanguages) {
			return notFound();
		} else {
			return ok(Json.toJson(dtoLanguages));
		}
	}

	protected String generateETag(List<dto.Language> languages) {
		List<String> allName = Lists.transform(languages, c -> c.getCname());
		StringBuilder sb = new StringBuilder();
		for (String s : allName) {
			sb.append(s);
		}
		return "language-" + Integer.toHexString(sb.toString().hashCode());
	}

}
