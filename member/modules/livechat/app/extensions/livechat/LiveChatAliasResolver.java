package extensions.livechat;

import valueobjects.livechat.AliasResolution;

public interface LiveChatAliasResolver {

	/**
	 * Can this resolver handle this kind of alias?
	 * 
	 * @param alias
	 * @return
	 */
	boolean canResolve(String alias);

	/**
	 * Resolve to the actual LTC
	 * 
	 * @param alias
	 * @param siteID
	 * @param languageID
	 * @return
	 */
	AliasResolution resolve(String alias, int siteID, int languageID);

}
