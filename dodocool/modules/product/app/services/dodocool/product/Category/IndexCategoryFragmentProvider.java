package services.dodocool.product.Category;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import play.Play;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.dodocool.base.template.ITemplateFragmentProvider;
import services.product.ICategoryEnquiryService;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.product.CategoryWebsiteWithName;

public class IndexCategoryFragmentProvider implements ITemplateFragmentProvider {

	@Inject
	ICategoryEnquiryService categoryService;

	@Override
	public String getName() {
		return "index_category";
	}

	@Override
	public Html getFragment(Context context) {
		WebContext webContex = ContextUtils.getWebContext(context);
		List<CategoryWebsiteWithName> categoryWebsiteWithNames = categoryService
				.getCategoryItemRootByDisplay(webContex, true);
		
		String isShowCategoryImage = Play.application().configuration().getString("show.category.image");
		Boolean isShow = false; 
		if(StringUtils.isNotEmpty(isShowCategoryImage)){
			if(isShowCategoryImage.equalsIgnoreCase("true")){
				isShow = true;
			}
		}
		return views.html.product.category.index_category
				.render(categoryWebsiteWithNames,isShow);
	}

}
