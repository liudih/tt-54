package services;

import java.util.List;
import java.util.Map;

import context.WebContext;
import dto.CmsMenu;
import dto.CmsMenuComposite;
import dto.CmsMenuLanguage;
import dto.CmsMenuWebsite;

public interface ICmsMenuService {

	public abstract List<CmsMenu> getCmsMenuByLevelIdAndType(Integer levelId,
			Integer iparentid, String cmsType, WebContext webContext);

	public abstract List<Integer> getMenuIdByLevelIdAndType(Integer levelId,
			String cmsType, WebContext webContext);

	public abstract List<CmsMenu> getNominatetCmsMenu(Integer levelId,
			String cmsType, WebContext webContext);
	
	public abstract List<CmsMenuComposite> getCmsMenuCompositeList(
			WebContext webContext);
	
	public abstract List<CmsMenu> getOneFootCmsMenu(WebContext webContext);

	public abstract Map<Integer, List<CmsMenu>> getTwoFootCmsMenuMap(
			WebContext webContext);

	public abstract boolean addCmsMenu(CmsMenu cmsMenu);

	public abstract CmsMenu getCmsMenu(Integer iid);

	public abstract boolean deleteCmsMenu(Integer iid);

	public abstract boolean updateCmsMenu(CmsMenu cmsMenu);

	public abstract CmsMenu selectByPrimaryKey(Integer iid);

	public abstract List<dto.CmsType> getAllCmsType();

	public abstract List<CmsMenuLanguage> getAllLangMenuByMenuId(Integer menuId);

	public abstract CmsMenuLanguage addCmsMenuMoreLanguage(
			CmsMenuLanguage cmsMenuLanguage);

	public abstract boolean deleteCmsMenuMoreLanguage(Integer iid);

	public abstract boolean validateCmsMenuMoreLanguage(Integer imenuid,
			Integer ilanguageid);

	public abstract List<CmsMenuWebsite> getAllMenuWebsiteByMenuId(
			Integer menuId);

	public abstract CmsMenuWebsite addCmsMenuMoreWebsite(
			CmsMenuWebsite cmsMenuWebsite);

	public abstract boolean deleteCmsMenuMoreWebsite(Integer iid);

	public abstract boolean validateCmsMenuMoreWebsite(Integer imenuid,
			Integer iwebsiteid, String cdevice);

	public abstract CmsMenuLanguage getCmsMenuMoreLanguage(Integer imenuid,
			Integer ilanguageid);
	
	
	public abstract List<CmsMenu> getCmsMenuByLevelIdAndSiteId(Integer levelId,
			Integer iparentid, Integer siteId);

}