package valueobjects.base;

import java.io.Serializable;

import play.mvc.Http.Context;
import filters.common.CookieTrackingFilter;

public class PlayLoginContextFactory {

	public static LoginContext newLoginContext(String memberID, int groupID,
			Serializable payload) {
		return newLoginContext(Context.current(), memberID, groupID, payload);
	}

	public static LoginContext newLoginContext(Context ctx, String memberID,
			int groupID, Serializable payload) {
		return new LoginContext(CookieTrackingFilter.getLongTermCookie(ctx),
				CookieTrackingFilter.getShortTermCookie(ctx), memberID,
				groupID, payload);
	}

	public static LoginContext newAnonymousLoginContext() {
		return newAnonymousLoginContext(Context.current());
	}

	public static LoginContext newAnonymousLoginContext(Context ctx) {
		return new LoginContext(CookieTrackingFilter.getLongTermCookie(ctx),
				CookieTrackingFilter.getShortTermCookie(ctx), null, -1, null);
	}
}
