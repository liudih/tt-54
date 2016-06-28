package services.product.fragment.renderer;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import play.Logger;
import play.twirl.api.Html;
import services.product.HtmlTidy;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;
import extensions.product.IProductDescriptionVariableProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;

@Singleton
public class ProductDescriptionRenderer implements IProductFragmentRenderer {

	final Set<IProductDescriptionVariableProvider> vars;

	@Inject
	public ProductDescriptionRenderer(
			Set<IProductDescriptionVariableProvider> varProvider) {
		this.vars = varProvider;
	}

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		String description = (String) context.getAttribute("description");
		if (description == null) {
			return Html.apply("");
		}
		try {
			Template t = new Template("description", new StringReader(
					description), new Configuration(
					Configuration.VERSION_2_3_21));
			Map<String, Object> root = new HashMap<String, Object>();

			// variable extensions
			for (IProductDescriptionVariableProvider v : vars) {
				for (String name : v.availableVariableNames()) {
					root.put(name, v.get(name, context));
				}
			}
			StringWriter out = new StringWriter();
			t.process(root, out);

			return Html.apply(HtmlTidy.tidy(out.toString()));
		} catch (Exception e) {
			Logger.error("Template generation error", e);
			return Html.apply(HtmlTidy.tidy((String) context
					.getAttribute("description")));
		}

	}

}
