package services.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.base.BannerMapper;

import org.springframework.beans.BeanUtils;

import play.Logger;
import services.IBannerService;

import com.google.common.collect.Lists;

import context.WebContext;
import dto.Banner;

public class BannerService implements IBannerService {

	@Inject
	BannerMapper bannerMapper;

	@Inject
	FoundationService foundationService;

	public List<dto.Banner> getBanner(int ilanguageid, int iwebsiteid,
			Boolean bstatus) {
		List<Banner> list = bannerMapper.getBanner(ilanguageid, iwebsiteid,
				bstatus);
		return list;
	}

	@Override
	public List<Banner> getBanner(WebContext context, Boolean bstatus) {
		int siteId = foundationService.getSiteID(context);
		int language = foundationService.getLanguage(context);
		return this.getBanner(language, siteId, bstatus);
	}

	public valueobjects.base.Page<dto.Banner> getBannerPage(int page,
			int pageSize, int languageid, List<Integer> websiteids) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", page);
		paramMap.put("pageSzie", pageSize);
		paramMap.put("ilanguageid", languageid == 0 ? null : languageid);
		paramMap.put("websiteids", websiteids);
		List<Banner> list = bannerMapper.getBannerPageByParamMap(paramMap);
		List<dto.Banner> voList = Lists.transform(list, b -> {
			dto.Banner banner = new dto.Banner();
			BeanUtils.copyProperties(b, banner);
			return banner;
		});
		int total = bannerMapper.getBannerCountByParamMap(paramMap);
		valueobjects.base.Page<dto.Banner> p = new valueobjects.base.Page<dto.Banner>(
				voList, total, page, pageSize);
		return p;
	}

	@Override
	public boolean deleteBannerByIid(int iid) {
		Banner banner = bannerMapper.selectByPrimaryKey(iid);
		if (banner == null) {
			return false;
		}
		int result = bannerMapper.deleteByPrimaryKey(iid);
		if (result == 0) {
			return false;
		}
		int index = banner.getIindex();
		List<Banner> list = bannerMapper.getBannerLgIndex(index);
		list = Lists.transform(list, o -> {
			int i = o.getIindex();
			i -= 1;
			o.setIindex(i);
			return o;

		});
		bannerMapper.batchUpdateIndex(list);
		return true;
	}

	@Override
	public Banner getBannerByiid(int iid) {
		Banner banner = bannerMapper.selectByPrimaryKey(iid);
		return banner;
	}

	@Override
	public Banner getBannerEntityById(int iid) {
		Banner banner = bannerMapper.selectByPrimaryKey(iid);
		Logger.debug(
				"Banner Content: Size={}",
				banner != null && banner.getBfile() != null ? banner.getBfile().length
						: null);
		return banner;
	}

	@Override
	public boolean down(int index) {
		Banner firstbanner = bannerMapper.getBannerByIndex(index);
		Banner secondBanner = bannerMapper.getBannerByIndex(index - 1);
		List<Banner> list = Lists.newArrayList();
		if (firstbanner != null && secondBanner != null) {
			int firster = firstbanner.getIindex();
			int second = secondBanner.getIindex();
			firstbanner.setIindex(second);
			secondBanner.setIindex(firster);
			list.add(firstbanner);
			list.add(secondBanner);
		}
		return bannerMapper.batchUpdateIndex(list) > 0 ? true : false;
	}

	@Override
	public boolean addBanner(Banner banner) {

		int result = bannerMapper.insertSelective(banner);
		return result > 0 ? true : false;
	}

	@Override
	public int getMaxIndex() {
		return bannerMapper.getMaxIndex();
	}

	@Override
	public boolean upadeBanner(Banner banner) {
		int result = bannerMapper.updateByPrimaryKeySelective(banner);
		return result > 0 ? true : false;
	}

	@Override
	public boolean up(int index) {
		Banner firstbanner = bannerMapper.getBannerByIndex(index);
		Banner secondBanner = bannerMapper.getBannerByIndex(index + 1);
		List<Banner> list = Lists.newArrayList();
		if (firstbanner != null && secondBanner != null) {
			int firster = firstbanner.getIindex();
			int second = secondBanner.getIindex();
			firstbanner.setIindex(second);
			secondBanner.setIindex(firster);
			list.add(firstbanner);
			list.add(secondBanner);
		}
		return bannerMapper.batchUpdateIndex(list) > 0 ? true : false;
	}
}
