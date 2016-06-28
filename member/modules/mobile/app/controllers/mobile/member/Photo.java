package controllers.mobile.member;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.FoundationService;
import services.interaction.ProductPhotosService;
import services.member.IMemberPhotoService;
import context.ContextUtils;
import controllers.mobile.routes;
import dto.interaction.InteractionProductMemberPhotos;
import dto.member.MemberPhoto;

public class Photo extends Controller {

	@Inject
	IMemberPhotoService memberPhotoService;

	@Inject
	FoundationService foundationService;

	@Inject
	ProductPhotosService photosService;

	public Result at(String email) {
		MemberPhoto photo = memberPhotoService.getPhoto(email,
				ContextUtils.getWebContext(Context.current()));
		if (photo != null) {
			String etag = photo.getCmd5();
			String match = request().getHeader(IF_NONE_MATCH);
			if (etag != null && etag.equals(match)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);
			return ok(photo.getBfile()).as(photo.getCcontenttype());
		}
		return redirect(routes.Assets.at("/images/HeadPic.jpg"));
	}

	public Result at2(Integer iid) {
		InteractionProductMemberPhotos p = photosService.getPhotoById(iid);
		if (p != null) {
			return ok(p.getBfile()).as(p.getCcontenttype());
		}
		return redirect(routes.Assets.at("/images/HeadPic.jpg"));
	}

}
