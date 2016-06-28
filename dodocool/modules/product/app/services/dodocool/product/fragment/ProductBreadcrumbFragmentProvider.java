package services.dodocool.product.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.mvc.Http.Context;
import services.dodocool.product.IProductFragmentProvider;
import services.product.ICategoryEnquiryService;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;
import valueobjects.product.category.CategoryComposite;
import valueobjects.product.category.CategoryReverseComposite;
import context.ContextUtils;
import context.WebContext;
import dto.product.CategoryWebsiteWithName;
import dto.product.ProductCategoryMapper;

public class ProductBreadcrumbFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	ICategoryEnquiryService categoryEnquiryService;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		Logger.debug("getFragment---getListingID()1"+context.getListingID());
		if(context == null || StringUtils.isBlank(context.getListingID())){
			return null;
		}
		Logger.debug("getFragment---getListingID()2"+context.getListingID());
		List<ProductCategoryMapper> categories = new ArrayList<ProductCategoryMapper>();
		try{
			categories = categoryEnquiryService
					.selectByListingId(context.getListingID());
		}catch(Exception e){
			if("79edf4f6-4730-436c-adf4-f64730236c33".equals(context.getListingID())){
				ProductCategoryMapper pm = new ProductCategoryMapper();
				pm.setIcategory(1410);
				categories.add(pm);
			}
			Logger.error("selectByListingId error ,e:",e);
			
		}
		
		Logger.debug("====categories size 1:   " + categories.size());
		processingContext.put("listingId", context.getListingID());
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		if (categories != null && categories.size() > 0) {
			Logger.debug("====categories size 2:   " + categories.size());
			ProductCategoryMapper pcm = categories.get((categories.size() - 1));
			Logger.debug("====ProductCategoryMapper pcm:   " + pcm);
			if(pcm == null){
				return null;
			}
			CategoryReverseComposite rev = categoryEnquiryService
					.getReverseCategory(pcm.getIcategory(), webContext);
			if (null == rev){
				return null;
			}
			CategoryReverseComposite parent = rev.getParent();

			if (null != parent) {
				//Integer icategoryid = parent.getSelf().getIcategoryid();
				List<Integer> categoryIds = parent.getChildren().stream()
						.map(CategoryComposite::getSelf)
						.map(CategoryWebsiteWithName::getIcategoryid)
						.collect(Collectors.toList());
				categoryIds.remove(rev.getSelf().getIcategoryid());
				processingContext.put("categoryIds", categoryIds);
			} 
			return rev;
		}

		return null;
	}

}
