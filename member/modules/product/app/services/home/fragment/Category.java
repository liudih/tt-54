package services.home.fragment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Maps;

import com.google.common.collect.Lists;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.IStorageService;
import services.base.FoundationService;
import services.base.StorageParentService;
import services.base.template.ITemplateFragmentProvider;
import services.product.CategoryEnquiryService;
import valueobjects.product.category.CategoryComposite;
import dto.Storage;
import dto.StorageParent;

public class Category implements ITemplateFragmentProvider {

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	FoundationService foundation;

	@Inject
	IStorageService storageService;
	
	@Inject
	StorageParentService storageParentService;

	@Override
	public String getName() {
		return "category";
	}

	@Override
	public Html getFragment(Context context) {
		//List<Storage> storages = storageService.getAllStorages();
		List<StorageParent> storageParents=storageParentService.getAllStorageParentList();
		Map<Integer, String> storageIdAndNameMap = storageParents.stream().collect(
				Collectors.toMap(a -> a.getIid(), a -> a.getCstoragename()));
		List<CategoryComposite> roots = categoryEnquiryService
				.getRootCategories(foundation.getLanguage(),
						foundation.getSiteID());
		Map<Integer, List<CategoryComposite>> secondLevel = Maps
				.newLinkedHashMap();
		secondLevel = Maps.transformValues(
				Maps.uniqueIndex(roots, r -> r.getSelf().getIid()),
				v -> v.getChildren());
		Map<Integer, Map<CategoryComposite, List<Html>>> partitioned = Maps
				.newLinkedHashMap();
		partitioned = Maps.transformValues(secondLevel, (
				List<CategoryComposite> children) -> {
			Map<CategoryComposite, List<Html>> aaMap = Maps.newLinkedHashMap();
			for (CategoryComposite c : children) {
				List<Html> lines = Lists.newLinkedList();
				for (CategoryComposite cc : c.getChildren()) {
					lines.add(views.html.product.category.category_line
							.render(cc));
				}
				aaMap.put(c, lines);
			}
			return aaMap;
		});
		return views.html.product.category.category_new.render(roots, partitioned,
				storageIdAndNameMap);
	}
}
