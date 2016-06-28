package services.product;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductImageMapper;
import play.Logger;
import play.libs.F.Either;
import play.libs.F.Promise;
import services.image.ImageUpdateService;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import dto.product.ProductImage;

public class ProductImageService {

	Map<String, String> prefixMapping = Maps.newHashMap();

	@Inject
	ProductImageMapper mapper;

	@Inject
	ImageUpdateService update;

	public ProductImageService() {
		prefixMapping.put("http://www.guphotos.com/images", "p/gu1");
		prefixMapping.put("http://www.guphotos.com/listingImages", "p/gu2");
		prefixMapping
				.put("http://www.tomtop.com/media/catalog/product", "p/tt");
	}

	/**
	 * 返回有多少张图片被内部化了
	 * 
	 * @param listingIDs
	 * @return
	 */
	public int internalizeProductImages(List<String> listingIDs) {
		List<ProductImage> images = mapper
				.getExternalImageUrlsByListingIDs(listingIDs);
		return internalize(images);
	}

	/**
	 * 返回有多少张图片被内部化了
	 * 
	 * @param limit
	 *            处理数量
	 * @return
	 */
	public int internalizeAllProductImages(int limit) {
		List<ProductImage> images = mapper.getExternalImageUrls(limit);
		return internalize(images);
	}

	protected int internalize(List<ProductImage> images) {
		List<Promise<ProductImage>> internalized = FluentIterable
				.from(images)
				.transform(
						(ProductImage i) -> {
							String url = i.getCimageurl();
							String newurl = url;
							for (String prefix : prefixMapping.keySet()) {
								if (url.startsWith(prefix)) {
									newurl = prefixMapping.get(prefix)
											+ url.substring(prefix.length());
									break;
								}
							}
							final String updatedUrl = newurl;
							if (url != updatedUrl) {
								Logger.debug("Internalize: {} -> {}", url,
										newurl);
								return update.copyImageFrom(url, updatedUrl)
										.map((Either<Exception, Long> f) -> {
											return f.right.map(iid -> {
												i.setCimageurl(updatedUrl);
												return i;
											}).getOrElse(null);
										});
							}
							return Promise.pure(i);
						}).toList();

		// save internalized version of product images
		int count = 0;
		for (Promise<ProductImage> p : internalized) {
			ProductImage pi = p.get(60000);
			if (pi != null) {
				mapper.updateImages(pi);
				count++;
			}
		}
		return count;
	}
}
