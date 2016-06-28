package services.product.impl;

import java.util.Comparator;
import java.util.Set;

import javax.inject.Singleton;

import play.Logger;
import services.product.IImageService;
import valueobjects.product.ImageBo;

import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;

import dto.image.Img;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class ImageHelper {

	private Set<IImageService> imageService;

	@Inject
	public ImageHelper(Set<IImageService> imageService) {
		this.imageService = FluentIterable.from(imageService).toSortedSet(
				new Comparator<IImageService>() {

					@Override
					public int compare(IImageService o1, IImageService o2) {
						return o1.getPriority() - o2.getPriority();
					}

				});
	}

	public ImageBo getImage(String dir) {
		for (IImageService s : imageService) {
			ImageBo image = s.getImage(dir);
			if (image != null) {
				return image;
			}
		}
		return null;
	}
	
	public void save(Img img) {
		for (IImageService s : imageService) {
			try {
				s.save(img);
			} catch (Exception e) {
				Logger.error("save image to fastdfs error",e);
			}
			
		}
		return;
	}
	
	public void save(Img img,Integer width, Integer height) {
		for (IImageService s : imageService) {
			s.save(img,width,height);
		}
		return;
	}
	
}
