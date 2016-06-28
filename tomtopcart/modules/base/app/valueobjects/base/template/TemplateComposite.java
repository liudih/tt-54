package valueobjects.base.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import play.Logger;
import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.cart.base.template.ITemplateFragmentProvider;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TemplateComposite {

	final Map<String, ITemplateFragmentProvider> tfProviders;

	public TemplateComposite(Map<String, ITemplateFragmentProvider> tfProviders) {
		this.tfProviders = tfProviders;
	}

	public Html get(String name) {
		if (tfProviders.containsKey(name)) {
			return tfProviders.get(name).getFragment(Context.current());
		} else {
			Logger.error("Template Fragment '{}' not found", name);
			return null;
		}
	}

	public Map<String, Html> multiGet(String... names) {
		return multiGetPromise(names).get(10, TimeUnit.SECONDS);
	}

	public Promise<Map<String, Html>> multiGetPromise(List<String> names) {
		final Context ctx = Context.current();
		List<Promise<F.Tuple<String, Html>>> htmls = FluentIterable
				.from(names)
				.filter(name -> tfProviders.containsKey(name))
				.transform(name -> tfProviders.get(name))
				.filter(p -> p != null)
				.transform(
						provider -> F.Promise.promise(() -> F.Tuple(
								provider.getName(), provider.getFragment(ctx))))
				.toList();

		Promise<Map<String, Html>> result = F.Promise.sequence(htmls).map(
				htmlList -> Maps.transformValues(
						Maps.uniqueIndex(htmlList, t -> t._1), v -> v._2));
		return result;
	}
	
	/**
	 * @author lijun
	 * @param names
	 * @return
	 */
	public Promise<Map<String, Html>> multiGetPromise(String... names) {
		ArrayList<String> ns = Lists.newArrayList(names);
		return this.multiGetPromise(ns);
		
	}
}
