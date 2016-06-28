package services.base.utils;

import play.mvc.Http.Context;
import valueobject.common.MetaBuilder;

/**
 * Used to manipulate HTML meta tags for the current request.
 * 
 * @author kmtong
 *
 */
public class MetaUtils {

	public static final String ARGS_NAME = "TT_META";

	public static MetaBuilder currentMetaBuilder() {
		MetaBuilder builder = (MetaBuilder) Context.current().args
				.get(ARGS_NAME);
		if (builder == null) {
			builder = new MetaBuilder();
			Context.current().args.put(ARGS_NAME, builder);
		}
		return builder;
	}

}
