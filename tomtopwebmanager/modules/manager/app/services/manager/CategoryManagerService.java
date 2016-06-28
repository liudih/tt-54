package services.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import mapper.category.CategoryFilterAttributeMapper;
import mapper.product.CategoryBaseMapper;
import mapper.product.CategoryNameMapper;
import mapper.product.CategoryWebsiteMapper;

import org.mybatis.guice.transactional.Transactional;

import play.Logger;
import play.data.DynamicForm;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import services.ILanguageService;
import services.base.WebsiteService;
import services.base.utils.FileUtils;
import services.product.CategoryEnquiryService;
import services.product.CategoryUpdateService;
import session.ISessionService;
import util.AppsUtil;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import dto.SimpleLanguage;
import dto.Website;
import dto.category.CategoryFilterAttribute;
import dto.product.CategoryBase;
import dto.product.CategoryName;
import dto.product.CategoryPlatform;
import entity.manager.AdminUser;
import extensions.InjectorInstance;
import forms.product.category.CategoryMessageForm;

public class CategoryManagerService {
	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	CategoryUpdateService categoryUpdateService;

	@Inject
	WebsiteService enquiry;

	@Inject
	ISessionService sessionService;

	@Inject
	AdminUserWebsiteMapService adminUserWebsiteMapService;

	@Inject
	CategoryFilterAttributeMapper categoryFilterAttributeMapper;

	@Inject
	CategoryWebsiteMapper categoryWebsiteMapper;

	@Inject
	CategoryBaseMapper categoryBaseMapper;
	@Inject
	CategoryNameMapper categoryNameMapper;
	@Inject
	ILanguageService languageService;

	public static CategoryManagerService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				CategoryManagerService.class);
	}

	public Collection<dto.Website> getWebsiteChoose() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		List<Website> entityList = new ArrayList<Website>();
		if (user.isBadmin()) {
			entityList = enquiry.getAll();
		} else {
			int userId = user.getIid();
			List<Integer> websiteIds = adminUserWebsiteMapService
					.getAdminUserWebsitMapsByUserId(userId);
			if (!websiteIds.isEmpty()) {
				entityList = enquiry.getWebsitesByWebsiteId(websiteIds);
			}
		}
		Collection<dto.Website> dtos = Collections2.transform(entityList,
				new Function<Website, dto.Website>() {
					@Override
					public dto.Website apply(Website ws) {
						return new dto.Website(ws.getIid(), ws.getCcode());
					}
				});

		return dtos;

	}

	public boolean categoryUpdate(CategoryMessageForm categoryNameForm,
			MultipartFormData body) {
		if (null != body) {
			FilePart file = body.getFile("backgroundimages");
			String reg = "image/";
			if (null != file) {
				if (file.getContentType().startsWith(reg)) {
					categoryNameForm.setCbackgroundimages(FileUtils
							.toByteArray(file.getFile()));
				}
			}
		}
		Boolean saveOrUpdateCategoryName = categoryUpdateService
				.saveOrUpdateCategoryNameForm(categoryNameForm);
		if (saveOrUpdateCategoryName) {
			CategoryPlatform category = categoryWebsiteMapper
					.selectByPrimaryKey(categoryNameForm
							.getIcatetorywebsiteiid());
			category.setBhasbgimage(true);
			int result = categoryWebsiteMapper.updateByPrimaryKey(category);
			return result > 0 ? true : false;
		} else {
			return false;
		}

	}

	public boolean updateCategoryAttribute(Integer attributeid,
			Integer categoryid, String valueid) {
		categoryFilterAttributeMapper.deleteCategoryFilterAttribute(categoryid,
				attributeid);
		if (null != valueid && "" != valueid) {
			// Logger.debug("valueid : {}",valueid);
			String[] valueids = valueid.split(";");
			for (String attributeValueId : valueids) {
				Logger.debug("attributeValueId : {}", attributeValueId);
				Integer vid = Integer.parseInt(attributeValueId);
				CategoryFilterAttribute categoryFilterAttribute = new CategoryFilterAttribute();
				categoryFilterAttribute.setIattributekeyid(attributeid);
				categoryFilterAttribute.setIattributevalueid(vid);
				categoryFilterAttribute.setIcategoryid(categoryid);
				categoryFilterAttributeMapper.insert(categoryFilterAttribute);
			}
		}

		return true;
	}

	@Transactional(rollbackOnly = false, rethrowExceptionsAs = RuntimeException.class)
	public void addCategory(DynamicForm in) {
		CategoryBase cb = new CategoryBase();
		CategoryPlatform cp = new CategoryPlatform();
		String parentid = in.get("parentid");
		String parentid2 = in.get("parentid2");
		String siteId = in.get("siteid");
		String cname1 = in.get("cname1");
		String cpath = AppsUtil.replaceNoEnStr(cname1);

		Integer parentcategoryid = 0;
		Integer parentcategoryid2 = 0;
		CategoryPlatform p = new CategoryPlatform();
		if (parentid != null && !"".equals(parentid)) {
			parentcategoryid = Integer.parseInt(parentid);
		}
		if (parentid2 != null && !"".equals(parentid2)) {
			parentcategoryid2 = Integer.parseInt(parentid2);
		}
		Integer level = getLevel(parentcategoryid, parentcategoryid2);
		Integer cbid = categoryBaseMapper.getMaxIid();
		Integer cnid = categoryNameMapper.getMaxIid();
		Integer cwid = categoryWebsiteMapper.getMaxIid();
		cb.setIid(++cbid);
		cb.setIlevel(level);
		cp.setIid(++cwid);
		cp.setIlevel(level);
		cpath = generatePath(cpath, parentcategoryid, parentcategoryid2);
		Integer cateid = null;
		if (parentcategoryid != 0) {
			cateid = parentcategoryid;
		}
		if (parentcategoryid2 != 0) {
			cateid = parentcategoryid2;
			p = categoryWebsiteMapper.getPlatformCategories(parentcategoryid2,
					Integer.parseInt(siteId));
		} else if (parentcategoryid == 0 && parentcategoryid2 == 0) {
			cp.setIparentid(null);
		} else {
			p = categoryWebsiteMapper.getPlatformCategories(parentcategoryid,
					Integer.parseInt(siteId));
		}
		Integer position = categoryBaseMapper.getMaxPosition(cateid);
		Integer childrencount = categoryBaseMapper.getCategoryBaseCount(cateid);
		// set category base and insert
		cb.setIparentid(cateid);
		cb.setCpath(cpath);
		cp.setCpath(cpath);
		if (position == null) {
			cb.setIposition(1);
			cp.setIposition(1);
		} else {
			cb.setIposition(++position);
			cp.setIposition(++position);
		}
		cb.setIchildrencount(++childrencount);
		cp.setIchildrencount(++childrencount);
		cp.setBshow(true);
		cp.setBhasbgimage(true);
		cp.setIwebsiteid(Integer.parseInt(siteId));
		cp.setIcategoryid(cbid);
		cp.setIparentid(p.getIid());
		
		Integer categoryId = this.categoryBaseMapper.getCategoryBaseIdByCpath(cpath);
		if(null == categoryId){
			Logger.info("=====cpath============="+cpath);
			categoryWebsiteMapper.insertSelective(cp);
			int res = categoryBaseMapper.insertSelective(cb);
			if (res <= 0) {
				Logger.error("category base insert error - [" + cbid + "]-["
						+ cateid + "]-[" + cpath + "]");
				throw new RuntimeException("category base insert error");
			}
			// set category name
			this.putCategoryName(cnid, cbid, in);
		}else{
			Logger.info("=====categoryId============="+categoryId);
			cp.setIcategoryid(categoryId);
			categoryWebsiteMapper.insertSelective(cp);
		}
		
	}
	

	public boolean valiedateCategoryWebsite(String cpath, int siteId){
		
		Integer categoryId = this.categoryBaseMapper.getCategoryBaseIdByCpath(cpath);
		if(null == categoryId){
			return true;
		}
		CategoryPlatform category = categoryWebsiteMapper.getPlatformCategories(categoryId, siteId);
		if(null == category){
			return true;
		}else{
			return false;
		}
		
	}

	private Integer getLevel(Integer parentid, Integer parentid2) {
		Integer level = 1;
		if (parentid2 != 0) {
			level = 3;
		} else if (parentid != 0) {
			level = 2;
		}
		return level;
	}

	public String generatePath(String cpath, Integer parent, Integer parent2) {
		CategoryBase selcb = null;
		if (parent2 != 0) {
			selcb = categoryBaseMapper.selectByPrimaryKey(parent2);
			cpath = selcb.getCpath() + "/" + cpath;
		} else if (parent != 0) {
			selcb = categoryBaseMapper.selectByPrimaryKey(parent);
			cpath = selcb.getCpath() + "/" + cpath;
		}
		return cpath;
	}

	public boolean isPathExist(String cpath) {
		Integer iid = categoryBaseMapper.getCategorieIdByPath(cpath);
		if (iid == null) {
			return true;// 不存在
		} else {
			return false;// 已存在
		}
	}

	private void putCategoryName(Integer cnid, Integer categoryid,
			DynamicForm in) {
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		String cnameEn = in.get("cname1");
		String ctitleEn = in.get("ctitle1");
		String cname = "";
		String ctitle = "";
		Integer languageId = 1;
		CategoryName categoryname = null;
		Integer res = -1;
		for (SimpleLanguage simpleLanguage : languageList) {
			languageId = simpleLanguage.getIid();
			categoryname = new CategoryName();
			categoryname.setIid(++cnid);
			categoryname.setIcategoryid(categoryid);
			categoryname.setIlanguageid(languageId);
			cname = in.get("cname" + languageId);
			if (cname == null || "".equals(cname)) {
				categoryname.setCname(cnameEn);
			} else {
				categoryname.setCname(cname);
			}
			ctitle = in.get("ctitle" + languageId);
			if (ctitle == null || "".equals(ctitle)) {
				categoryname.setCtitle(ctitleEn);
			} else {
				categoryname.setCtitle(ctitle);
			}

			res = categoryNameMapper.insertSelective(categoryname);
			if (res <= 0) {
				Logger.error("category name insert error - ["
						+ categoryname.getIid() + "]-[" + categoryid + "]-["
						+ cname + "]");
				throw new RuntimeException("category name insert error");
			}
		}
	}
}
