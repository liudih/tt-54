package services.product.impl;

import javax.inject.Inject;

import services.image.ImageEnquiryService;
import services.product.IImageService;
import valueobjects.product.ImageBo;
import dto.image.Img;

/**
 * 数据库取图片服务类
 * 
 * @author lijun
 *
 */
public class DatabaseImageService implements IImageService {

	@Inject
	ImageEnquiryService enquiry;

	@Override
	public int getPriority() {
		return 2;
	}

	@Override
	public ImageBo getImage(String dir) {
		// 数据库取数据
		Img img = enquiry.getImageByPath(dir, true);
		if (img != null) {
			ImageBo bo = new ImageBo(img.getBcontent(),img.getCcontenttype());
			return bo;
		}
		return null;
	}

	@Override
	public void save(Img img) {
	}

	@Override
	public void save(Img img, Integer width, Integer height) {
		
	}

}
