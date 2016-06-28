package controllers.annotation;

import play.Configuration;
import play.Play;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.utils.StringUtils;

public class ApiPermissionAction extends Action<ApiPermission> {
	@Override
	public Promise<Result> call(Context context) throws Throwable {
		String token = context.request().getHeader("token");
		if (StringUtils.notEmpty(token)) {
			Configuration config = Play.application().configuration()
					.getConfig("productApi");
			String localToken = config.getString("token");
			if (token.equals(localToken)) {
				return delegate.call(context);
			}
		}
		return F.Promise.pure(internalServerError("you have not permission"));
	}
}
