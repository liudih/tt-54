package extensions.livechat.role;

import play.mvc.Http.Context;

public interface LiveChatRoleProvider {

	boolean isInRole(Context context);

	String getAlias(Context context);

}
