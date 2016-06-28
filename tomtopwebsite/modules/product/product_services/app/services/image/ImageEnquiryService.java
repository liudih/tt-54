package services.image;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import play.Logger;
import play.api.libs.iteratee.internal;
import mapper.image.ImageCacheMapper;
import mapper.image.ImageMapper;
import mapper.image.ImgUseMappingMapper;
import mapper.product.ProductImageMapper;
import dto.image.Img;
import dto.image.ImgUseMapping;
import dto.product.ProductImage;
import forms.img.ImgPageForm;

public class ImageEnquiryService implements IImageEnquiryService {

	@Inject
	ImageMapper imageMapper;

	@Inject
	ImageCacheMapper cacheMapper;

	@Inject
	ImgUseMappingMapper ImgUseMappingMapper;

	@Inject
	ProductImageMapper productImagerMapper;

	public Img getImageByPath(String path, boolean withContent) {
		Logger.error("img path  ==== " + path);
		Img img =null;
		if (withContent) {
			img = imageMapper.getImageByPath(path);
//			Logger.error("img id ==== " + img.getIid());
			return img;
		} else {
			return imageMapper.getImageByPathWithoutContent(path);
		}
	}

	public List<Img> getAllImageByPage(ImgPageForm pageForm,int siteId) {
		return imageMapper.getImgByPage(siteId,pageForm.getContentType(),
				pageForm.getPath(), pageForm.getPageSize(),
				pageForm.getPageNum());
	}

	public Integer getImgCount(ImgPageForm pageForm,int siteId) {
		return imageMapper.getImgCount(siteId,pageForm.getContentType(),
				pageForm.getPath());
	}

	public Img getCachedImageByPath(String path, int width, int height) {
		return cacheMapper.getImageByPath(path, width, height);
	}

	public ImgUseMapping getImgUseMappingByPathAndLabel(String path,
			String clabel) {
		return ImgUseMappingMapper.getImgUseMapping(path, clabel);
	}

	public List<ImgUseMapping> getImgUseMappingByImgIds(List<Long> imgids) {
		return ImgUseMappingMapper.getImgUseMappingByImgIds(imgids);
	}

	@Override
	public List<ProductImage> getProductImages(String listingId) {
		return productImagerMapper.getProductImgsByListingId(listingId);
	}

	@Override
	public byte[] getImageByte(String path) {
		Img img = this.getImageByPath(path, true);
		if (img == null)
			return null;
		return img.getBcontent();
	}

	@Override
	public byte[] getImagePart(String path, int start, int len) {
		Img img = this.getImageByPath(path, true);
		if (null == img || (start >= img.getBcontent().length)) {
			return null;
		}
		return getImageByteByLength(img.getBcontent(), start, len);
	}

	private byte[] getImageByteByLength(byte[] bytes, int start, int len) {
		if (null == bytes || (start >= bytes.length)) {
			return null;
		}
		int tlen = start + len;
		if (tlen > bytes.length) {
			tlen = bytes.length;
		}
		Logger.debug("start {} len  {}   {}", start, len, bytes.length);
		return Arrays.copyOfRange(bytes, start, tlen);
	}

	@Override
	public byte[] getCachedImageByPath(String path, int width, int height,
			int start, int len) {
		Img img = cacheMapper.getImageByPath(path, width, height);
		if (null == img || (start >= img.getBcontent().length)) {
			return null;
		}
		return getImageByteByLength(img.getBcontent(), start, len);
	}

	/*
	 * (non-Javadoc) <p>Title: getImageById</p> <p>Description: 通过id查询图片</p>
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 * @see services.image.IImageEnquiryService#getImageById(int)
	 */
	@Override
	public Img getImageById(int id) {
		return imageMapper.getImageById(id);
	}

	@Override
	public List<ImgUseMapping> getImgUseMappingByClabel(String clabel) {
		return ImgUseMappingMapper.getImgUseMappingByClabel(clabel);
	}
}
