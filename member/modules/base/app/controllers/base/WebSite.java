package controllers.base;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.WebsiteService;
import com.google.common.collect.Collections2;

import dto.Website;

public class WebSite extends Controller {

	@Inject
	WebsiteService enquiry;

	public Result getAllWebsites() {
		List<Website> entityList = enquiry.getAll();
		Collection<com.website.dto.Website> dtos = Collections2
				.transform(
						entityList,
						obj -> {
							com.website.dto.Website wsite = new com.website.dto.Website();
							wsite.setCode(obj.getCcode());
							wsite.setId(obj.getIid());
							return wsite;
						});
		return ok(Json.toJson(dtos));
	}

}
