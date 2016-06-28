package services.image;

import java.util.List;

import dto.image.Img;
import dto.image.ImgUseMapping;
import dto.product.ProductImage;

public interface IImageEnquiryService {

	List<ProductImage> getProductImages(String listingId);

	Img getImageByPath(String path, boolean withContent);

	Img getCachedImageByPath(String path, int width, int height);

	byte[] getImageByte(String path);

	byte[] getImagePart(String path, int start, int len);

	byte[] getCachedImageByPath(String path, int width, int height, int start,
			int len);

	/**
	 * 
	 * @Title: getImageById
	 * @Description: TODO(通过id查询图片)
	 * @param @param id
	 * @param @return
	 * @return Img
	 * @throws
	 * @author yinfei
	 */
	Img getImageById(int id);

	/**
	 * 通过文件用途标签，获取ImgUse的所有数据信息
	 * 
	 * @param clabel
	 * @return
	 */
	public List<ImgUseMapping> getImgUseMappingByClabel(String clabel);

}
