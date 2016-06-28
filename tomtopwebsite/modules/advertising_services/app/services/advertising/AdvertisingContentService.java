package services.advertising;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import mapper.advertising.AdvertisingContentMapper;
import play.Logger;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import services.base.utils.StringUtils;
import services.image.IImageUpdateService;
import mapper.advertising.AdvertisingContentMapper;
import dto.advertising.AdvertisingContent;
import services.image.ImageUpdateService;
import dto.advertising.AdvertisingContent;
import dto.image.Img;

public class AdvertisingContentService {

	@Inject
	AdvertisingContentMapper mapper;

	@Inject
	IImageUpdateService update;

	public List<AdvertisingContent> getAdvertisingContentList(Long iid) {
		List<AdvertisingContent> adContentList = mapper
				.getAdvertisingContentList(iid);

		return adContentList;
	}

	public AdvertisingContent addAdvertContent(AdvertisingContent record,
			MultipartFormData body) {
		mapper.insertSelective(record);
		try {
			if (null != body) {
				FilePart cbgimages = body.getFile("cbgimages");
				if (null != cbgimages) {
					byte[] buffer = this.toBinaryFile(cbgimages);
					if (null != buffer) {
						Img image = new Img();
						image.setCpath("advertising/bgimage/"
								+ record.getIid());
						image.setBcontent(buffer);
						image.setCcontenttype(cbgimages.getContentType());
						update.createImage(image);
					}
					
					if (StringUtils.isEmpty(record.getCbgimageurl())) {
						record.setCbgimageurl("advertising/bgimage/"
								+ record.getIid());
					} else {
						record.setCbgimageurl(record.getCbgimageurl());
					}
					record.setBhasbgimage(true);
				}
			}
			mapper.updateByPrimaryKeySelective(record);

		} catch (FileNotFoundException e) {
			Logger.debug("广告背景图片未找到:" + e.getMessage());
		} catch (IOException e) {
			Logger.debug("上传广告背景图片出错:" + e.getMessage());
		}
		

		return record;
	}

	private byte[] toBinaryFile(FilePart file) throws IOException {
		File imageFile = file.getFile();
		BufferedImage bi = ImageIO.read(imageFile);
		if (bi == null) {
			return null;
		}
		FileInputStream fis = new FileInputStream(file.getFile());
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		if (null != fis) {
			fis.close();
		}

		return buffer;
	}
	
	public AdvertisingContent getAdvertContentByAdvertIdAndLangId(Integer iadvertisingid,
			Integer ilanguageid) {
		AdvertisingContent adContent = mapper
				.getAdvertContentByAdvertIdAndLangId(iadvertisingid, ilanguageid);

		return adContent;
	}
	
	public boolean validateAdvertContent(Integer iadvertisingid,
			Integer ilanguageid) {
		AdvertisingContent adContent = mapper
				.getAdvertContentByAdvertIdAndLangId(iadvertisingid, ilanguageid);
		
		return adContent == null ? true : false;
	}
}
