package controllers.product;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Hex;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.product.CategoryEnquiryService;
import services.product.CategoryLabelBaseService;
import services.product.NewArrivalCategoryService;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import dto.Category;
import dto.category.CategoryBackgroundImages;
import dto.product.CategoryBase;
import dto.product.CategoryLabelName;
import dto.product.CategoryName;

public class CategoryAPI extends Controller {

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	CategoryLabelBaseService categoryLabelBaseService;

	@Inject
	NewArrivalCategoryService newArrivalCategoryService;

	public Result getAllCategory() {
		Integer language = 1;
		List<CategoryBase> categoryBases = categoryEnquiryService
				.getAllCategory();

		if (null != categoryBases && !categoryBases.isEmpty()) {
			Collection<Category> categories = Collections2.transform(
					categoryBases, new Function<CategoryBase, dto.Category>() {
						@Override
						public dto.Category apply(CategoryBase catbase) {
							if (null == catbase) {
								return null;
							}
							CategoryName categoryName = categoryEnquiryService
									.getCategoryNameByCategoryIdAndLanguage(
											catbase.getIid(), language);
							if (null == categoryName) {
								return null;
							}
							return new Category(catbase.getIid(), catbase
									.getIparentid(), categoryName.getCname());
						}
					});
			List<Category> list = removeNull(categories);
			if (null != list && !list.isEmpty()) {
				return ok(Json.toJson(list));
			}
		}
		return notFound();
	}

	private List<Category> removeNull(Collection<Category> categories) {
		if (null != categories && !categories.isEmpty()) {
			return null;
		}
		Set<Category> set = new HashSet<Category>(categories);
		set.remove(null);
		if (set.isEmpty()) {
			return null;
		}
		return new ArrayList<Category>(set);
	}

	public Result getCategoryImage(Integer iid, String imageType) {
		if ("backgroundimages".equals(imageType)) {
			CategoryBackgroundImages categoryBackgroundImages = categoryEnquiryService
					.getBackgroundImagesById(iid);
			if (null != categoryBackgroundImages
					&& null != categoryBackgroundImages.getCbackgroundimages()) {
				String etag = generateETag(categoryBackgroundImages
						.getCbackgroundimages());
				String previous = request().getHeader(IF_NONE_MATCH);
				if (etag != null && etag.equals(previous)) {
					return status(NOT_MODIFIED);
				}
				response().setHeader(CACHE_CONTROL, "max-age=604800");
				response().setHeader(ETAG, etag);
				return ok(categoryBackgroundImages.getCbackgroundimages()).as(
						"image/png");
			}
		}

		if ("categorylabelType".equals(imageType)) {
			CategoryLabelName categoryLabelName = categoryLabelBaseService
					.getCategoryLabelName(iid);

			if (null != categoryLabelName
					&& null != categoryLabelName.getCimages()) {
				String etag = generateETag(categoryLabelName.getCimages());
				String previous = request().getHeader(IF_NONE_MATCH);
				if (etag != null && etag.equals(previous)) {
					return status(NOT_MODIFIED);
				}
				response().setHeader(CACHE_CONTROL, "max-age=604800");
				response().setHeader(ETAG, etag);
				return ok(categoryLabelName.getCimages()).as("image/png");
			}
		}

		return badRequest();
	}

	/**
	 * updateNewArrivalCategory 用于更新t_product_label和t_category_label新品数据 jiang
	 */
	public Result updateNewArrivalCategory() {
		try {
			newArrivalCategoryService.newArrivalCategory();
		} catch (Exception e) {
			return ok("update failure");
		}
		return ok("update new arrival category ok");
	}

	private String generateETag(byte[] values) {
		if (values != null) {
			return md5(values);
		}
		return null;
	}

	private String md5(byte[] value) {
		try {
			String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5")
					.digest(value));
			return md5;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Should not happen", e);
		}
	}

}