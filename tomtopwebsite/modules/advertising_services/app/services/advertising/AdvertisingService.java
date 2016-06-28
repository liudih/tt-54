package services.advertising;

import interceptors.CacheResult;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import mapper.advertising.AdvertisingBaseMapper;
import mapper.advertising.AdvertisingContentMapper;
import mapper.advertising.AdvertisingDistributionMapper;
import play.Logger;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import services.base.utils.StringUtils;
import services.image.IImageUpdateService;
import valueobjects.base.Page;
import valueobjects.product.ProductAdertisingContext;

import com.google.api.client.util.Maps;

import dto.advertising.Advertising;
import dto.advertising.AdvertisingBase;
import dto.advertising.AdvertisingContent;
import dto.advertising.AdvertisingDistribution;
import dto.advertising.AdvertisingTypeForSelect;
import dto.advertising.Position;
import dto.image.Img;
import extensions.InjectorInstance;

public class AdvertisingService {

	final static int PAGE_SIZE = 10;

	@Inject
	AdvertisingBaseMapper mapper;

	@Inject
	AdvertisingDistributionMapper advertisingMapper;

	@Inject
	AdvertisingContentMapper contentMapper;

	@Inject
	IImageUpdateService update;

	public static AdvertisingService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				AdvertisingService.class);
	}

	public List<Position> getAllPositions() {
		return mapper.getAllPositions();
	}

	public List<AdvertisingTypeForSelect> getAllAdvertisingTypes() {
		return mapper.getAllAdvertisingTypes();
	}

	public List<Advertising> getAdvertisingByContext(
			ProductAdertisingContext context) {
		return this.getAdvertisingByContext(context.getBusinessId(),
				context.getAdvertisingType(), context.getWebsiteId(),
				context.getLanguageId(), context.getPositonId(),
				context.getDevice());
	}

	@CacheResult("Advertising")
	private List<Advertising> getAdvertisingByContext(String businessId,
			Integer advertisingType, Integer websiteId, Integer languageId,
			Integer positonId, String device) {
		ProductAdertisingContext pcontext = new ProductAdertisingContext(
				businessId, advertisingType, websiteId, languageId, positonId,
				device);
		List<Advertising> advertisingBaseList = mapper
				.getAdvertisingByContext(pcontext);
		return advertisingBaseList;
	}

	public List<Advertising> getAdvertisingList() {
		List<Advertising> list = advertisingMapper.getAdvertisingAll();

		return list;
	}

	public Page<Advertising> getAdvertisingPage(int page) {
		List<Advertising> list = advertisingMapper.getAdvertisingPage(page,
				PAGE_SIZE);

		int total = advertisingMapper.getAdvertisingCount();

		Page<Advertising> p = new Page<Advertising>(list, total, page,
				PAGE_SIZE);

		return p;
	}

	public Advertising getAdvertising(Long iid) {

		Advertising advertising = advertisingMapper.selectById(iid);

		return advertising;
	}

	public boolean updateAdvertising(Advertising advertising) {
		if (null == advertising || null == advertising.getIid()) {
			return false;
		}
		AdvertisingBase advertisingBase = mapper.selectByPrimaryKey(advertising
				.getIid());

		advertisingBase.setCimageurl(advertising.getCimageurl());
		advertisingBase.setIposition(advertising.getIposition());

		AdvertisingDistribution advertisingDistribution = advertisingMapper
				.getProductAdvertisingById(advertising.getIid());
		advertisingDistribution.setIwebsiteid(advertising.getIwebsiteid());
		advertisingMapper.updateByPrimaryKeySelective(advertisingDistribution);

		int result = mapper.updateByPrimaryKeySelective(advertisingBase);

		// int result =
		// advertisingMapper.updateByPrimaryKeySelective(advertising);

		return result > 0 ? true : false;
	}

	public boolean addAdvertising(Advertising advertising,
			MultipartFormData body) {
		AdvertisingBase advertisingBase = new AdvertisingBase();
		try {

			advertisingBase.setIposition(advertising.getIposition());
			int result = mapper.insertSelective(advertisingBase);

			if (null != body) {
				FilePart cadvertisingimages = body
						.getFile("cadvertisingimages");
				if (null != cadvertisingimages) {
					byte[] buffer = this.toBinaryFile(cadvertisingimages);
					if (null == buffer) {
						return false;
					}

					Img image = new Img();
					image.setCpath("advertising/image/"
							+ advertisingBase.getIid());
					image.setBcontent(buffer);
					image.setCcontenttype(cadvertisingimages.getContentType());
					update.createImage(image);

				}

			}
			if (StringUtils.isEmpty(advertising.getCimageurl())) {
				advertisingBase.setCimageurl("advertising/image/"
						+ advertisingBase.getIid());
			} else {
				advertisingBase.setCimageurl(advertising.getCimageurl());
			}

			mapper.updateByPrimaryKeySelective(advertisingBase);

			result = saveAdvertContent(advertising, advertisingBase, body);

			return result > 0 ? true : false;
		} catch (FileNotFoundException e) {
			Logger.debug("文件未找到:" + e.getMessage());
			return false;
		} catch (IOException e) {
			Logger.debug("上传文件出错:" + e.getMessage());
			return false;
		}

	}

	private int saveAdvertContent(Advertising advert,
			AdvertisingBase advertisingBase, MultipartFormData body) {

		AdvertisingContent content = new AdvertisingContent();
		content.setIadvertisingid(advertisingBase.getIid());
		content.setCtitle(advert.getCtitle());
		content.setChrefurl(advert.getChrefurl());
		content.setIlanguageid(advert.getIlanguageid());
		content.setCbgcolor(advert.getCbgcolor());
		content.setBstatus(advert.isBstatus());
		content.setBbgimgtile(advert.isBbgimgtile());
		content.setIindex(advert.getIindex());
		int result = contentMapper.insertSelective(content);
		try {

			if (null != body) {
				FilePart cbgimages = body.getFile("cbgimages");
				if (null != cbgimages) {
					byte[] buffer = this.toBinaryFile(cbgimages);
					if (null != buffer) {

						Img image = new Img();
						image.setCpath("advertising/bgimage/"
								+ content.getIid());
						image.setBcontent(buffer);
						image.setCcontenttype(cbgimages.getContentType());
						update.createImage(image);

						if (StringUtils.isEmpty(advert.getCbgimageurl())) {
							content.setCbgimageurl("advertising/bgimage/"
									+ content.getIid());
							content.setBhasbgimage(true);
						} else {
							content.setCbgimageurl(advert.getCbgimageurl());
							content.setBhasbgimage(true);
						}
					}

				}
			}

			contentMapper.updateByPrimaryKeySelective(content);
		} catch (FileNotFoundException e) {
			Logger.debug("广告背景图片未找到:" + e.getMessage());
		} catch (IOException e) {
			Logger.debug("上传广告背景图片出错:" + e.getMessage());
		}

		return result;
	}

	public boolean deleteAdvertising(Integer iid) {
		int result = this.mapper.deleteByPrimaryKey(Long.valueOf(iid));

		this.advertisingMapper.deleteRelationById(Long.valueOf(iid));

		this.contentMapper.deleteContentById(Long.valueOf(iid));

		return result > 0 ? true : false;
	}

	public boolean deleteAdvertContent(Integer iid) {
		int result = this.contentMapper.deleteByPrimaryKey(Long.valueOf(iid));

		return result > 0 ? true : false;
	}

	public boolean deleteAdvertRelation(Integer iid) {
		int result = advertisingMapper.deleteByPrimaryKey(Long.valueOf(iid));

		return result > 0 ? true : false;
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

	public List<AdvertisingDistribution> getAdvertRelationList(
			Long advertisingId) {
		return advertisingMapper.getAdvertRelationList(advertisingId);
	}

	public AdvertisingDistribution addAdvertRelation(
			AdvertisingDistribution record) {
		advertisingMapper.insertSelective(record);

		return record;
	}

	public Page<Advertising> searchAdvertisingPage(int page,
			Integer ilanguageid, Integer iwebsiteid, Integer iposition,
			Integer itype) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("page", page);
		param.put("pageSize", PAGE_SIZE);
		param.put("ilanguageid", ilanguageid);
		param.put("iwebsiteid", iwebsiteid);
		param.put("iposition", iposition);
		param.put("itype", itype);
		List<Advertising> list = advertisingMapper.searchAdvertisingPage(param);
		Integer total = advertisingMapper.searchAdvertisingCount(param);

		Page<Advertising> p = new Page<Advertising>(list, total, page,
				PAGE_SIZE);

		return p;
	}
}
