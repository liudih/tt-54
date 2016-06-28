package services.category.fragment;

import java.util.List;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;
import services.product.ICategoryEnquiryService;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.product.CategoryWebsiteWithName;

public class CategoryProvider implements ITemplateFragmentProvider {

	@Inject
	ICategoryEnquiryService categoryEnquiryService;

	@Override
	public String getName() {

		return "category";
	}

	@Override
	public Html getFragment(Context context) {
		WebContext webContex = ContextUtils.getWebContext(Context.current());
		List<CategoryWebsiteWithName> roots = categoryEnquiryService
				.getCategoryItemRootByDisplay(webContex, true);
		return views.html.category.fragment.context.render(roots);
	}

}
