package valueobjects.product;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import dto.product.ProductImage;

public class ProductImages implements IProductFragment {

	public static class ProductImageComparator implements
			Comparator<ProductImage> {
		@Override
		public int compare(ProductImage o1, ProductImage o2) {
			return o1.getIorder() - o2.getIorder();
		}
	}

	// 将主图放在缩略图的第一张
	public static class ProductImageComparator2 implements
			Comparator<ProductImage> {
		@Override
		public int compare(ProductImage o1, ProductImage o2) {
			if (o1.getBbaseimage() && !o2.getBbaseimage()) {
				return -1;
			} else if (!o1.getBbaseimage() && o2.getBbaseimage()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	final List<ProductImage> images;

	final List<ProductImage> thumbnails;

	final List<ProductImage> baseImages;

	final List<ProductImage> smallImages;

	public ProductImages(List<ProductImage> images) {
		this.images = images;
		this.thumbnails = Lists.newLinkedList(Collections2.filter(images,
				i -> i.getBthumbnail()));
		this.baseImages = Lists.newLinkedList(Collections2.filter(images,
				i -> i.getBbaseimage()));
		this.smallImages = Lists.newLinkedList(Collections2.filter(images,
				i -> i.getBsmallimage()));
		Collections.sort(thumbnails, new ProductImageComparator());
		Collections.sort(thumbnails, new ProductImageComparator2());
		Collections.sort(baseImages, new ProductImageComparator());
		Collections.sort(smallImages, new ProductImageComparator());
	}

	public List<ProductImage> getImages() {
		return images;
	}

	public List<ProductImage> getThumbnails() {
		return thumbnails;
	}

	public List<ProductImage> getSmallImages() {
		return smallImages;
	}

	public List<ProductImage> getBaseImages() {
		return baseImages;
	}
}
