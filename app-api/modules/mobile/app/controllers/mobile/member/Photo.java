package controllers.mobile.member;

import javax.inject.Inject;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.interaction.IProductPhotosService;
import services.member.IMemberPhotoService;
import context.ContextUtils;
import dto.interaction.InteractionProductMemberPhotos;
import dto.member.MemberPhoto;

public class Photo extends Controller {

	@Inject
	IMemberPhotoService memberPhotoService;

	@Inject
	IProductPhotosService productPhotosService;

	public Result at(String email) {
		try {
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
		} catch (Exception e) {
			Logger.error("at get member photo error! " + e.fillInStackTrace());
		}
		return redirect(controllers.mobile.routes.Assets
				.at("/images/HeadPic.jpg"));
	}

	public Result at2(Integer iid) {
		try {
			InteractionProductMemberPhotos p = productPhotosService
					.getPhotoById(iid);
			if (p != null) {
				return ok(p.getBfile()).as(p.getCcontenttype());
			}
		} catch (Exception e) {
			Logger.error("at2 get member photo error! " + e.fillInStackTrace());
		}
		return redirect(controllers.mobile.routes.Assets
				.at("/images/HeadPic.jpg"));
	}

	public Result at3(Integer iid) {
		try {
			InteractionProductMemberPhotos p = productPhotosService
					.getPhotoById(iid);
			if (p != null) {
				return ok(p.getBfile()).as(p.getCcontenttype());
			}
		} catch (Exception e) {
			Logger.error("at3 get member photo error! " + e.fillInStackTrace());
		}
		return redirect(controllers.mobile.routes.Assets
				.at("/images/HeadPic.jpg"));
	}

}
