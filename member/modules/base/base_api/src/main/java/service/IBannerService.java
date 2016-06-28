package service;

import java.util.List;

import valueobjects.Page;
import dto.Banner;

public interface IBannerService {
	public List<Banner> getBanner(int ilanguageid, int iwebsiteid,
			Boolean bstatus);
	
	public Page<Banner> getBannerPage(int page, int pageSize,
			int languageid, List<Integer> websiteids);
	
	public boolean deleteBannerByIid(int iid);
	
	public Banner getBannerByiid(int iid);
	
	public Banner getBannerEntityById(int iid);
	
	public boolean down(int index);
	
	public boolean addBanner(Banner banner);
	
	public int getMaxIndex();
	
	public boolean upadeBanner(Banner banner);
	
	public boolean up(int index);
}
