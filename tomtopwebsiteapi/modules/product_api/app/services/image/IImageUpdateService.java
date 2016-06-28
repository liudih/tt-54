package services.image;

import java.util.List;

import play.libs.F.Either;
import play.libs.F.Promise;
import dto.image.Img;
import dto.image.ImgUseMapping;

public interface IImageUpdateService {

	/**
	 * It will calculate MD5 and create the image record, return the primary
	 * key.
	 * 
	 * @param image
	 * @return
	 */
	public abstract Either<Exception, Long> createImage(Img image);

	public abstract boolean deleteImage(Integer iid);

	/**
	 * Copy external resources to image db.
	 * 
	 * @param url
	 *            source URL to be copied from
	 * @param filename
	 *            destination path
	 * @return
	 */
	public abstract Promise<Either<Exception, Long>> copyImageFrom(String url,
			String filename);

	public abstract Either<Exception, Long> createImageCache(Img image,
			int width, int height);

	public abstract boolean addNewImgUseMapping(ImgUseMapping imgUseMapping2);

	public abstract boolean deleteImgUserMapping(Integer fileId);

	public abstract boolean createCacheImage(String path, Integer width,
			Integer height);
	
	public abstract boolean deleteImByPaths(List<String> paths);
	
	public abstract Either<Exception, Long> createCdnImage(Img image);
	
	
}