package services.categorylable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.twirl.api.Html;
import services.base.utils.FileUtils;
import services.product.CategoryLabelBaseService;
import valueobjects.product.CategoryLabelBase;
import dto.product.CategoryLabelName;
import forms.CategoryLabelSearchForm;

public class CategoryLabelManagerService {
	@Inject
	CategoryLabelBaseService categoryLabelBaseService;

	public Html getList(CategoryLabelSearchForm categoryLabelSearchForm) {
		List<CategoryLabelBase> categoryLabels = categoryLabelBaseService
				.getCategoryLabelBases(categoryLabelSearchForm.getSiteId(), 1,
						categoryLabelSearchForm.getType(),
						categoryLabelSearchForm.getPageSize(),
						categoryLabelSearchForm.getPageNum());
		Integer count = categoryLabelBaseService.getCategoryBaseCount(
				categoryLabelSearchForm.getSiteId(),
				categoryLabelSearchForm.getType());
		Integer pageTotal = count / categoryLabelSearchForm.getPageSize()
				+ ((count % categoryLabelSearchForm.getPageSize() > 0) ? 1 : 0);

		return views.html.manager.categorylabel.categorylabel_table_list
				.render(categoryLabels, categoryLabelSearchForm.getSiteId(),
						categoryLabelSearchForm.getType(), count,
						categoryLabelSearchForm.getPageNum(), pageTotal);
	}

	public boolean saveCategoryLabelName(CategoryLabelName categoryLabelName,
			MultipartFormData body)  {
		if (null != body) {
			FilePart file = body.getFile("image");
			if (null != file) {
				String contentType = file.getContentType();
				if (contentType.startsWith("image/")) {
					byte[] buff=FileUtils.toByteArray(file.getFile());
					categoryLabelName.setCimages(buff);
				}
			}

		}
		return categoryLabelBaseService
				.saveOrUpdateCategoryLabelName(categoryLabelName);
	}

}
