package services.manager;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import entity.tracking.AffiliateBanner;
import mappers.tracking.ManagerAffiliateBannerMapper;
import valueobjects.base.Page;

public class AffiliateBannerService {

	@Inject
	private ManagerAffiliateBannerMapper affiliateBannerMapper;

	public Page<AffiliateBanner> getAffiliateBanners(AffiliateBanner ab,
			Integer page, Integer pageSize) {
		List<AffiliateBanner> affiliateBanners = affiliateBannerMapper
				.getAffiliateBanners(ab, page, pageSize);
		Integer count = affiliateBannerMapper.count(ab);
		return new Page<AffiliateBanner>(affiliateBanners, count, page,
				pageSize);
	}

	public AffiliateBanner get(int id) {

		AffiliateBanner Banner = affiliateBannerMapper.get(id);
		return Banner;
	}

	public boolean edit(AffiliateBanner affiliateBanner) {
		int result = affiliateBannerMapper.edit(affiliateBanner);
		return result > 0 ? true : false;
	}

	public boolean delete(Integer[] ids) {
		List<Integer> list = Arrays.asList(ids);
		int result = affiliateBannerMapper.delete(list);
		return result > 0 ? true : false;
	}

	public boolean add(AffiliateBanner affiliateBanner) {
		int result = affiliateBannerMapper.add(affiliateBanner);
		return result > 0 ? true : false;
	}
}
