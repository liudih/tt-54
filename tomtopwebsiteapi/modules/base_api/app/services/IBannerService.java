package services;

import java.util.List;

import context.WebContext;
import dto.Banner;

public interface IBannerService {

	public boolean deleteBannerByIid(int iid);

	public Banner getBannerByiid(int iid);

	public Banner getBannerEntityById(int iid);

	public boolean down(int index);

	public boolean addBanner(Banner banner);

	public int getMaxIndex();

	public boolean upadeBanner(Banner banner);

	public boolean up(int index);

	public List<dto.Banner> getBanner(WebContext context, Boolean bstatus);
}
