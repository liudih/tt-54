package services.image;

import java.security.MessageDigest;
import java.util.List;

import javax.inject.Inject;

import mapper.image.ImageCacheMapper;
import mapper.image.ImageMapper;
import mapper.image.ImgUseMappingMapper;

import org.apache.commons.codec.binary.Hex;
import org.mybatis.guice.transactional.Transactional;

import play.Logger;
import play.libs.F.Either;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import services.product.impl.ImageHelper;
import dto.image.Img;
import dto.image.ImgUseMapping;

public class ImageUpdateService implements IImageUpdateService {

	@Inject
	ImageMapper mapper;

	@Inject
	ImageCacheMapper cacheMapper;

	@Inject
	ImgUseMappingMapper imgUseMappingMapper;

	//add by lijun
	@Inject
	ImageHelper imgHelper;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see services.image.IImageUpdateService#createImage(dto.image.Img)
	 */
	@Transactional
	public Either<Exception, Long> createImage(Img image) {
		try {
			// Calculate MD5
			String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5")
					.digest(image.getBcontent()));
			image.setCmd5(md5);
			if (mapper.getImageByPathWithoutContent(image.getCpath()) != null) {
				mapper.updateImage(image);
				return Either.Right(image.getIid());
			}
			mapper.createImage(image);
			
			//add by lijun
			//imgHelper.save(image);
			
			return Either.Right(image.getIid());
		} catch (Exception e) {
			Logger.error("Error createImage", e);
			return Either.Left(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.image.IImageUpdateService#deleteImage(java.lang.Integer)
	 */
	@Transactional
	public boolean deleteImage(Integer iid) {
		return mapper.deleteImageById(iid) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.image.IImageUpdateService#copyImageFrom(java.lang.String,
	 * java.lang.String)
	 */
	public Promise<Either<Exception, Long>> copyImageFrom(String url,
			String filename) {
		return WS.client().url(url).setFollowRedirects(true).get()
				.map(new Function<WSResponse, Either<Exception, Long>>() {
					@Override
					public Either<Exception, Long> apply(WSResponse resp)
							throws Throwable {
						if (resp.getStatus() == 200) {
							Img image = new Img();
							image.setCpath(filename);
							image.setBcontent(resp.asByteArray());
							image.setCcontenttype(resp
									.getHeader("Content-Type"));
							return createImage(image);
						} else {
							return Either.Left(new Exception(
									"Non-200 Status code: " + resp.getStatus()
											+ ", Body=" + resp.getBody()));
						}
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.image.IImageUpdateService#createImageCache(dto.image.Img,
	 * int, int)
	 */
	@Transactional
	public Either<Exception, Long> createImageCache(Img image, int width,
			int height) {
		try {
			// Calculate MD5
			String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5")
					.digest(image.getBcontent()));
			image.setCmd5(md5);
//			if (cacheMapper.getImageByPathWithoutContent(image.getCpath(),
//					width, height) != null) {
//				cacheMapper.updateImage(image, width, height);
//				return Either.Right(image.getIid());
//			}
//			cacheMapper.createImage(image, width, height);
			
			//add by lijun
			//imgHelper.save(image, width, height);
			
			
			return Either.Right(image.getIid());
		} catch (Exception e) {
			Logger.error("Error createImage", e);
			return Either.Left(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.image.IImageUpdateService#addNewImgUseMapping(dto.image.
	 * ImgUseMapping)
	 */
	public boolean addNewImgUseMapping(ImgUseMapping imgUseMapping2) {
		int result = imgUseMappingMapper.addNewImgUseMapping(imgUseMapping2);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.image.IImageUpdateService#deleteImgUserMapping(java.lang.Integer
	 * )
	 */
	public boolean deleteImgUserMapping(Integer fileId) {
		int result = imgUseMappingMapper.deleteImgUserMappingByFileId(fileId);
		return result > 0 ? true : false;
	}

	@Override
	public boolean createCacheImage(String path, Integer width, Integer height) {
		try {
			Img image = mapper.getImageByPath(path);
			String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5")
					.digest(image.getBcontent()));
			image.setCmd5(md5);
			if (cacheMapper.getImageByPathWithoutContent(image.getCpath(),
					width, height) != null) {
				long updateImage = cacheMapper
						.updateImage(image, width, height);
				return updateImage > 0 ? true : false;
			}
			long createImage = cacheMapper.createImage(image, width, height);

			return createImage > 0 ? true : false;
		} catch (Exception e) {
			Logger.error("Error createImage", e);
			return false;
		}
	}
	
	@Override
	public boolean deleteImByPaths(List<String> paths) {
		return mapper.deleteImageByPaths(paths) > 0 ? true : false;
	}
	
	@Transactional
	public Either<Exception, Long> createCdnImage(Img image) {
		try {
			if (mapper.getImageByPathWithoutContent(image.getCpath()) != null) {
				mapper.updateCdnImage(image);
				return Either.Right(image.getIid());
			}
			mapper.createCdnImage(image);
			
			return Either.Right(image.getIid());
		} catch (Exception e) {
			Logger.error("Error createImage", e);
			return Either.Left(e);
		}
	}
}
