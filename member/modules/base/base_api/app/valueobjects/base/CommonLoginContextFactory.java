package valueobjects.base;

import java.io.Serializable;

import context.WebContext;

public class CommonLoginContextFactory {

	public static LoginContext newLoginContext(WebContext ctx, String memberID,
			int groupID, Serializable payload) {
		return new LoginContext(ctx.getLtc(), ctx.getStc(), memberID, groupID,
				payload);
	}

	public static LoginContext newAnonymousLoginContext(WebContext ctx) {
		return new LoginContext(ctx.getLtc(), ctx.getStc(), null, -1, null);
	}

}
