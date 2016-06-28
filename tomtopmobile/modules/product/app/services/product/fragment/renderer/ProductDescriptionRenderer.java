package services.product.fragment.renderer;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;



import com.google.common.collect.Maps;

import extensions.product.IProductDescriptionVariableProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import play.Logger;
import play.twirl.api.Html;
import services.product.IProductFragmentRenderer;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

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
		
		
		try {
			Template t = new Template("description", new StringReader(
					description), new Configuration(
					Configuration.VERSION_2_3_21));
			Map<String, Object> root = Maps.newHashMap();

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
