package controllers.member;

import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICountryService;
import services.dodocool.base.FoundationService;
import services.member.IMemberPhotoService;

import com.google.inject.Inject;

import context.ContextUtils;
import dto.member.MemberPhoto;

public class MemberProfile extends Controller {
	@Inject
	IMemberPhotoService memberPhotoService;

	@Inject
	FoundationService foundationService;

	@Inject
	ICountryService countryService;

	public Result getPhoto(String email) {
		/*ByteArrayOutputStream file = new ByteArrayOutputStream();
		boolean flag = true;
		Integer start = 0;
		Integer length = 1024 * 5;
		while (flag) {
			byte[] memberPhotoPart = memberPhotoService.getMemberPhotoPart(
					email, ContextUtils.getWebContext(Context.current()),
					start, length);
			if (memberPhotoPart != null) {
				try {
					file.write(memberPhotoPart);
					start = start + length;
				} catch (IOException e) {
					e.printStackTrace();
					Logger.debug("image error:{}", e.toString());

					return redirect(routes.Assets.at("images/act.jpg"));
				}
			} else {
				flag = false;
			}
		}
		if (file != null && file.size() > 0) {
			response().setHeader(CACHE_CONTROL, "max-age=604800");

			return ok(file.toByteArray()).as("image/jpg");
		}

		return redirect(routes.Assets.at("images/act.jpg"));*/
		
		MemberPhoto photo = memberPhotoService.getPhoto(email,
				ContextUtils.getWebContext(Context.current()));
		if (photo != null && photo.getBfile() != null) {
			String etag = photo.getCmd5();
			String match = request().getHeader(IF_NONE_MATCH);
			if (etag != null && etag.equals(match)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);

			return ok(photo.getBfile()).as(photo.getCcontenttype());
		}

		return redirect(routes.Assets.at("images/act.jpg"));
	}
}
