package services.base;

import java.util.Set;

import play.mvc.Http.Context;
import valueobjects.base.HtmlMisc;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import extensions.InjectorInstance;
import extensions.base.HtmlRenderHook;

/**
 * Used to manipulate HTML head section and before body, used to insert
 * stylesheets/scripts.
 * 
 * @author kmtong
 *
 */
public class HtmlUtils {

	public static final String ARGS_NAME = "TT_HTML_MISC";
	final static TypeLiteral<Set<HtmlRenderHook>> setOfHooks = new TypeLiteral<Set<HtmlRenderHook>>() {
	};

	public static HtmlMisc misc() {
		return misc(Context.current());
	}

	public static void beforeRender() {
		beforeRender(Context.current());
	}

	public static void afterRender() {
		afterRender(Context.current());
	}

	private static void beforeRender(Context current) {
		for (HtmlRenderHook hook : InjectorInstance.getInstance(Key
				.get(setOfHooks))) {
			hook.beforeRender(current);
		}
	}

	private static void afterRender(Context current) {
		for (HtmlRenderHook hook : InjectorInstance.getInstance(Key
				.get(setOfHooks))) {
			hook.afterRender(current);
		}
	}

	public static HtmlMisc misc(Context context) {
		HtmlMisc misc = (HtmlMisc) context.args.get(ARGS_NAME);
		if (misc == null) {
			misc = new HtmlMisc();
			Context.current().args.put(ARGS_NAME, misc);
		}
		return misc;
	}
}
