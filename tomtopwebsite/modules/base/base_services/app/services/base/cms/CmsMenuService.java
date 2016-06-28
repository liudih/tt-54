package services.base.cms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import mapper.base.CmsMenuMapper;
import services.ICmsMenuService;
import services.base.FoundationService;

import com.google.api.client.util.Maps;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import context.WebContext;
import dto.CmsMenu;
import dto.CmsMenuComposite;
import dto.CmsMenuLanguage;
import dto.CmsMenuWebsite;
import enumtype.CmsType;
import extensions.InjectorInstance;

public class CmsMenuService implements ICmsMenuService {

	final static int PAGE_SIZE = 10;

	@Inject
	FoundationService foundation;

	@Inject
	CmsMenuMapper cmsMenuMapper;

	public static ICmsMenuService getInstance() {
		return InjectorInstance.getInjector().getInstance(CmsMenuService.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#getCmsMenuByLevelIdAndType(java.lang
	 * .Integer, java.lang.Integer, java.lang.String, int, int)
	 */
	@Override
	public List<CmsMenu> getCmsMenuByLevelIdAndType(Integer levelId,
			Integer iparentid, String cmsType, WebContext ctx) {
		CmsMenu cmsMenu = new CmsMenu();
		cmsMenu.setIlevel(levelId);
		cmsMenu.setIparentid(iparentid);

		Map<String, Object> param = Maps.newHashMap();
		param.put("cmsMenu", cmsMenu);
		param.put("cmsType", cmsType);
		param.put("siteId", foundation.getSiteID(ctx));
		param.put("languageId", foundation.getLanguage(ctx));
		param.put("device", foundation.getDevice(ctx));

		return this.cmsMenuMapper.getCmsMenuByLevelIdAndType(param);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#getMenuIdByLevelIdAndType(java.lang
	 * .Integer, java.lang.String, int, int)
	 */
	@Override
	public List<Integer> getMenuIdByLevelIdAndType(Integer levelId,
			String cmsType, WebContext ctx) {

		CmsMenu cmsMenu = new CmsMenu();
		cmsMenu.setIlevel(levelId);
		cmsMenu.setIparentid(0);

		Map<String, Object> param = Maps.newHashMap();
		param.put("cmsMenu", cmsMenu);
		param.put("cmsType", cmsType);
		param.put("siteId", foundation.getSiteID(ctx));
		param.put("languageId", foundation.getLanguage(ctx));
		param.put("device", foundation.getDevice(ctx));
		List<CmsMenu> menuList = this.cmsMenuMapper
				.getCmsMenuByLevelIdAndType(param);

		List<Integer> menuids = Lists.transform(menuList, obj -> {
			return obj.getIid();
		});

		return menuids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#getNominatetCmsMenu(java.lang.Integer,
	 * java.lang.String, int, int)
	 */
	@Override
	public List<CmsMenu> getNominatetCmsMenu(Integer levelId, String cmsType,
			WebContext ctx) {
		
		Map<String, Object> param = Maps.newHashMap();
		param.put("levelId", levelId);
		param.put("cmsType", cmsType);
		param.put("siteId", foundation.getSiteID(ctx));
		param.put("languageId", foundation.getLanguage(ctx));
		param.put("device", foundation.getDevice(ctx));
		
		return this.cmsMenuMapper.getNominatetCmsMenu(param);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.cms.ICmsMenuService#addCmsMenu(dto.CmsMenu)
	 */
	@Override
	public boolean addCmsMenu(CmsMenu cmsMenu) {

		int result = cmsMenuMapper.insert(cmsMenu);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.cms.ICmsMenuService#getCmsMenu(java.lang.Integer)
	 */
	@Override
	public CmsMenu getCmsMenu(Integer iid) {

		CmsMenu cmsMenu = cmsMenuMapper.selectByPrimaryKey(iid);

		return cmsMenu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.cms.ICmsMenuService#deleteCmsMenu(java.lang.Integer)
	 */
	@Override
	public boolean deleteCmsMenu(Integer iid) {
		int result = cmsMenuMapper.deleteByPrimaryKey(iid);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.cms.ICmsMenuService#updateCmsMenu(dto.CmsMenu)
	 */
	@Override
	public boolean updateCmsMenu(CmsMenu cmsMenu) {
		int result = cmsMenuMapper.updateByPrimaryKeySelective(cmsMenu);
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.cms.ICmsMenuService#getRootCategories(int, int)
	 */
	@Override
	public List<CmsMenuComposite> getCmsMenuCompositeList(WebContext ctx) {

		CmsMenu cmsMenu = new CmsMenu();
		cmsMenu.setIlevel(1);
		cmsMenu.setIparentid(0);

		Map<String, Object> param = Maps.newHashMap();
		param.put("cmsMenu", cmsMenu);
		param.put("siteId", foundation.getSiteID(ctx));
		param.put("languageId", foundation.getLanguage(ctx));
		param.put("device", foundation.getDevice(ctx));

		List<CmsMenu> cmsMenuList = this.cmsMenuMapper
				.getCmsMenuByLevelId(param);
		
		return Lists.transform(cmsMenuList,
				new Function<CmsMenu, CmsMenuComposite>() {
					@Override
					public CmsMenuComposite apply(CmsMenu self) {
						return new CmsMenuComposite(self, getCmsMenuCompList(
								self.getIid(), self.getIlevel() + 1,
								foundation.getSiteID(ctx),
								foundation.getLanguage(ctx),
								foundation.getDevice(ctx)));
					}
				});
	}

	private List<CmsMenuComposite> getCmsMenuCompList(final int parentid,
			final int levelid, int siteId, int languageId, String device) {

		CmsMenu cmsMenu = new CmsMenu();
		cmsMenu.setIlevel(levelid);
		cmsMenu.setIparentid(parentid);

		Map<String, Object> param = Maps.newHashMap();
		param.put("cmsMenu", cmsMenu);
		param.put("siteId", siteId);
		param.put("languageId", languageId);
		param.put("device", device);

		List<CmsMenu> pf = this.cmsMenuMapper.getCmsMenuByLevelId(param);
		return Lists.transform(pf, new Function<CmsMenu, CmsMenuComposite>() {
			@Override
			public CmsMenuComposite apply(CmsMenu self) {
				if (levelid == 0) {
					return new CmsMenuComposite(self,
							new LinkedList<CmsMenuComposite>());
				}
				return new CmsMenuComposite(self, getCmsMenuCompList(
						self.getIid(), self.getIlevel() + 1, siteId,
						languageId, device));
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#selectByPrimaryKey(java.lang.Integer)
	 */
	@Override
	public CmsMenu selectByPrimaryKey(Integer iid) {
		return this.cmsMenuMapper.selectByPrimaryKey(iid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.cms.ICmsMenuService#getAllCmsType()
	 */
	@Override
	public List<dto.CmsType> getAllCmsType() {

		List<dto.CmsType> cmsTypeList = Lists.newArrayList();
		for (CmsType e : CmsType.values()) {
			cmsTypeList.add(new dto.CmsType(e.getType(), e.getType()));
		}

		return cmsTypeList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.cms.ICmsMenuService#getOneFootCmsMenu()
	 */
	@Override
	public List<CmsMenu> getOneFootCmsMenu(WebContext ctx) {

		List<CmsMenu> footOneLevelMenu = this.getCmsMenuByLevelIdAndType(1, 0,
				CmsType.FOOT.getType(), ctx);

		return footOneLevelMenu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.cms.ICmsMenuService#getTwoFootCmsMenuMap()
	 */
	@Override
	public Map<Integer, List<CmsMenu>> getTwoFootCmsMenuMap(WebContext ctx) {

		List<CmsMenu> twoCmsMenuList = this.getCmsMenuByLevelIdAndType(2, 0,
				CmsType.FOOT.getType(), ctx);

		Map<Integer, List<CmsMenu>> cmsMenuMap = Maps.newHashMap();

		for (CmsMenu cm : twoCmsMenuList) {

			List<CmsMenu> cmsMenuList = null;
			if (cmsMenuMap.containsKey(cm.getIparentid())) {
				cmsMenuList = cmsMenuMap.get(cm.getIparentid());
				cmsMenuList.add(cm);

			} else {
				cmsMenuList = new ArrayList<CmsMenu>();
				cmsMenuList.add(cm);
			}

			cmsMenuMap.put(cm.getIparentid(), cmsMenuList);
		}

		return cmsMenuMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#getAllLangMenuByMenuId(java.lang.Integer
	 * )
	 */
	@Override
	public List<CmsMenuLanguage> getAllLangMenuByMenuId(Integer menuId) {
		return this.cmsMenuMapper.getAllLangMenuByMenuId(menuId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#addCmsMenuMoreLanguage(dto.CmsMenuLanguage
	 * )
	 */
	@Override
	public CmsMenuLanguage addCmsMenuMoreLanguage(
			CmsMenuLanguage cmsMenuLanguage) {

		this.cmsMenuMapper.insertCmsMenuLanguage(cmsMenuLanguage);

		return cmsMenuLanguage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#deleteCmsMenuMoreLanguage(java.lang
	 * .Integer)
	 */
	@Override
	public boolean deleteCmsMenuMoreLanguage(Integer iid) {
		int result = this.cmsMenuMapper.deleteCmsMenuMoreLanguage(iid);

		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#validateCmsMenuMoreLanguage(java.lang
	 * .Integer, java.lang.Integer)
	 */
	@Override
	public boolean validateCmsMenuMoreLanguage(Integer imenuid,
			Integer ilanguageid) {
		CmsMenuLanguage cmsMenuLanguage = cmsMenuMapper.getCmsMenuMoreLanguage(
				imenuid, ilanguageid);
		return cmsMenuLanguage == null ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#getAllMenuWebsiteByMenuId(java.lang
	 * .Integer)
	 */
	@Override
	public List<CmsMenuWebsite> getAllMenuWebsiteByMenuId(Integer menuId) {
		return this.cmsMenuMapper.getAllMenuWebsiteByMenuId(menuId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#addCmsMenuMoreWebsite(dto.CmsMenuWebsite
	 * )
	 */
	@Override
	public CmsMenuWebsite addCmsMenuMoreWebsite(CmsMenuWebsite cmsMenuWebsite) {

		this.cmsMenuMapper.insertCmsMenuWebsite(cmsMenuWebsite);

		return cmsMenuWebsite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#deleteCmsMenuMoreWebsite(java.lang.
	 * Integer)
	 */
	@Override
	public boolean deleteCmsMenuMoreWebsite(Integer iid) {
		int result = this.cmsMenuMapper.deleteCmsMenuMoreWebsite(iid);

		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#validateCmsMenuMoreWebsite(java.lang
	 * .Integer, java.lang.Integer)
	 */
	@Override
	public boolean validateCmsMenuMoreWebsite(Integer imenuid,
			Integer iwebsiteid, String cdevice) {
		CmsMenuWebsite cmsMenuWebsite = cmsMenuMapper
				.validateCmsMenuMoreWebsite(imenuid, iwebsiteid, cdevice);
		return cmsMenuWebsite == null ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.base.cms.ICmsMenuService#getCmsMenuMoreLanguage(java.lang.Integer
	 * , java.lang.Integer)
	 */
	@Override
	public CmsMenuLanguage getCmsMenuMoreLanguage(Integer imenuid,
			Integer ilanguageid) {
		return cmsMenuMapper.getCmsMenuMoreLanguage(imenuid, ilanguageid);
	}

	@Override
	public List<CmsMenu> getCmsMenuByLevelIdAndSiteId(Integer levelId,
			Integer iparentid, Integer siteId) {
		return cmsMenuMapper.getCmsMenuByLevelIdAndSiteId(levelId, iparentid,
				siteId);
	}
}
