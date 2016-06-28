package controllers.topic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Hex;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.utils.MetaUtils;
import services.topic.TopicPageService;
import valueobjects.topic.CollectionsPage;

import com.google.api.client.util.Lists;
import com.google.common.collect.Iterables;

import controllers.image.Image;
import dto.SimpleLanguage;
import dto.topic.TopicPage;
import forms.topic.RequestTopicPageForm;

public class Topic extends Controller {
	@Inject
	TopicPageService pageService;

	@Inject
	FoundationService foundationService;

	@Inject
	Image image;

	public Result show() {
		List<SimpleLanguage> slList = pageService.getLanguages();
		List<SimpleLanguage> temp = Lists.newArrayList(Iterables.filter(slList,
				e -> e.getIid().equals(foundationService.getSiteID())));
		Integer defaultLanguageId = null;
		if (!temp.isEmpty()) {
			defaultLanguageId = temp.get(0).getIid();
		} else if (!slList.isEmpty()) {
			defaultLanguageId = slList.get(0).getIid();
		} else {
			defaultLanguageId = foundationService.getLanguage();
		}
		List<Integer> years = pageService.getYears();
		List<String> types = pageService.getType(foundationService.getSiteID());
		Map<String, List<TopicPage>> map = pageService
				.getMapByLanguageIdAndYear(defaultLanguageId, years.get(0));
		MetaUtils.currentMetaBuilder().setTitle("Collections");
		return ok(views.html.topic.collections.render(new CollectionsPage(
				types, slList, map, years, defaultLanguageId)));
	}

	public Result refresh() {
		Form<RequestTopicPageForm> form = Form.form(RequestTopicPageForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			Logger.error("form error: {}", form.errorsAsJson());
			return badRequest();
		}
		RequestTopicPageForm pageForm = form.get();
		List<TopicPage> list = pageService.filterTopicPage(pageForm.getType(),
				foundationService.getSiteID(), pageForm.getLanguageId(),
				pageForm.getYear(), pageForm.getMonth(), null);
		return ok(views.html.topic.topicPageList.render(pageForm.getType(),
				list));
	}

	public Result showImage(Integer id) {
		byte[] bytes = pageService.getImage(id);
		if (bytes != null) {
			String etag = generateETag(bytes);
			String previous = request().getHeader(IF_NONE_MATCH);
			if (etag != null && etag.equals(previous)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);
			return ok(bytes).as("image/jpeg");
		}
		return badRequest();
	}

	public Result showHtml(String name) {
		return image.view(name);
	}

	protected String generateETag(byte[] bytes) {
		try {
			String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5")
					.digest(bytes));
			return md5;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
