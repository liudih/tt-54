package services.product.google.feeds.impl;

import java.util.List;

import play.Logger;

import com.google.inject.Inject;

import dao.product.IGoogleCategoryBaseDao;
import dto.product.google.category.GoogleCategory;
import dto.product.google.category.MerchantsProductDto;
import dto.product.google.category.SearchMerchantsProductDto;
import services.product.google.feeds.IGoogleCategoryBaseService;
import valueobjects.base.Page;

public class GoogleCategoryBaseService implements IGoogleCategoryBaseService {

	@Inject
	IGoogleCategoryBaseDao googleCategoryBaseDao;

	@Override
	public int saveOrUpdate(GoogleCategory googleCategory) {
		if (googleCategory == null)
			return 0;
		/*
		 * GoogleCategory tgc = googleCategoryBaseDao
		 * .getCategoryByCid(googleCategory.getIcategory()); if (tgc != null) {
		 * return googleCategoryBaseDao.updateUsingCategoryId(
		 * googleCategory.getIcategory(), googleCategory.getCpath(),
		 * tgc.getIparentid()); }
		 */
		GoogleCategory tgc = googleCategoryBaseDao.getIdByCpath(googleCategory
				.getCpath().trim());
		String[] paths = googleCategory.getCpath().split(">");
		if (tgc != null) {
			Logger.debug("update---> " + tgc.getCpath());
			return googleCategoryBaseDao.updateUsingCpath(googleCategory
					.getIcategory(), googleCategory.getCpath().trim(), tgc
					.getIparentid(), paths[paths.length - 1]);
		}

		if (paths.length == 1) {
			googleCategory.setIparentid(0);
			googleCategory.setCname(paths[0]);
			return googleCategoryBaseDao.insert(googleCategory);
		} else {
			String cpath = paths[0].trim();
			int pid = getPathId(cpath, 0, -1, cpath);
			Logger.debug(cpath);
			for (int i = 1; paths.length > i; i++) {
				cpath += " > " + paths[i].trim();
				if (i == paths.length-1) {
					pid = getPathId(cpath, pid, googleCategory.getIcategory(),
							paths[i]);
				} else {
					pid = getPathId(cpath, pid, -1, paths[i]);
				}
			}
		}
		return 1;
	}

	private Integer getPathId(String cpath, int parentid, int categoryid,
			String name) {
		GoogleCategory tgc = googleCategoryBaseDao.getIdByCpath(cpath);
		if (tgc == null) {
			GoogleCategory ttgc = new GoogleCategory();
			ttgc.setCpath(cpath);
			ttgc.setIparentid(parentid);
			ttgc.setIcategory(categoryid);
			ttgc.setCname(name);
			googleCategoryBaseDao.insert(ttgc);
			tgc = googleCategoryBaseDao.getIdByCpath(cpath);
			Logger.debug("insert - > " + cpath);
		}
		return tgc.getIid();
	}

	@Override
	public List<GoogleCategory> getFirstCategory() {
		// TODO Auto-generated method stub
		return googleCategoryBaseDao.getFirstCategory();
	}

	@Override
	public List<GoogleCategory> getChildsByParentId(int cid) {
		// TODO Auto-generated method stub
		return googleCategoryBaseDao.getChildsByParentId(cid);
	}

	@Override
	public String getCpathByCid(int cid) {
		// TODO Auto-generated method stub
		return googleCategoryBaseDao.getCpathByCid(cid);
	}

	@Override
	public List<GoogleCategory> getDetailByCid(int cid) {
		// TODO Auto-generated method stub
		return googleCategoryBaseDao.getDetailByCid(cid);
	}

	@Override
	public GoogleCategory getCategoryByCid(int cid) {
		// TODO Auto-generated method stub
		return googleCategoryBaseDao.getCategoryByCid(cid);
	}

	public Page<GoogleCategory> getAll(int p, int i, GoogleCategory googleBase) {
		// TODO Auto-generated method stub
		List<GoogleCategory> categories = googleCategoryBaseDao.getAll(p,i,googleBase.getCpath());
		int total = googleCategoryBaseDao.getCount(googleBase.getCpath());
		return new Page<GoogleCategory>(categories, total, p, i);
	}

	@Override
	public GoogleCategory getCategoryByIid(Integer id) {
		// TODO Auto-generated method stub
		return googleCategoryBaseDao.getCategoryByIid(id);
	}

	

}
