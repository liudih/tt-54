package services.ckeditor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import play.libs.F.Either;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import services.base.utils.FileUtils;
import services.image.IImageUpdateService;
import dto.image.Img;

public class CkeditorService {
	@Inject
	IImageUpdateService update;
	
	public String upload(String fileType, MultipartFormData body) throws IOException {
		String imageUrl = null;
		if (null != body) {
			FilePart images = body.getFile("upload");
			if (null != images) {
				byte[] buffer = FileUtils.toByteArray(images.getFile());
				if (null == buffer) {
					return null;
				}
				Date date = new Date();
				Img image = new Img();
				image.setCpath("ckeditor/image/" + date.getTime() + images.getFilename());
				image.setBcontent(buffer);
				image.setCcontenttype(fileType);
				Either<Exception, Long> t = update
						.createImage(image);
				imageUrl = image.getCpath();
			}
		}
		
		return imageUrl;
	}
	
	
}
