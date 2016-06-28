package services.product;

import java.util.List;

import javax.inject.Inject;

import mapper.category.CategoryBackgroundImagesMapper;
import mapper.category.CategoryFilterAttributeMapper;
import mapper.product.CategoryBaseMapper;
import mapper.product.CategoryNameMapper;
import mapper.product.CategoryWebsiteMapper;
import mapper.product.ProductCategoryRankMapper;
import play.Logger;

import org.mybatis.guice.transactional.Transactional;

import valueobjects.product.category.CategoryItem;

import com.website.dto.category.WebsiteCategory;

import dto.category.AttributeKeyName;
import dto.category.AttributeValueName;
import dto.category.CategoryBackgroundImages;
import dto.category.CategoryFilterAttribute;
import dto.product.CategoryBase;
import dto.product.CategoryName;
import dto.product.CategoryPlatform;
import dto.product.ProductCategoryRank;
import forms.product.category.CategoryMessageForm;

public class CategoryUpdateService {

	@Inject
	CategoryBaseMapper categoryMapper;

	@Inject
	CategoryNameMapper categoryNameMapper;

	@Inject
	CategoryWebsiteMapper cpaltformMapper;

	@Inject
	ProductCategoryRankMapper rankMapper;

	@Inject
	CategoryFilterAttributeMapper categoryFilterAttributeMapper;

	@Inject
	AttributeUpdateService attributeUpdateService;

	@Inject
	mapper.product.AttributeMapper attributeMapper;

	@Inject
	CategoryBackgroundImagesMapper categoryBackgroundImagesMapper;

	public Boolean saveOrUpdateCategoryBase(CategoryItem cbase) {
		CategoryBase dbbase = this.createCategoryBase(cbase);
		CategoryName dbbasename = this.createCategoryName(cbase);
		Boolean baseresult = this.saveOrUpdateCategoryBase(dbbase);
		Boolean basenameresult = this.saveOrUpdateCategoryName(dbbasename);
		return (baseresult && basenameresult);
	}

	private CategoryBase createCategoryBase(CategoryItem cbase) {
		CategoryBase dbbase = new CategoryBase();
		dbbase.setIid(cbase.getId());
		dbbase.setCpath(cbase.getPath());
		dbbase.setIchildrencount(cbase.getChildrenCount());
		dbbase.setIid(cbase.getId());
		dbbase.setIlevel(cbase.getLevel());
		dbbase.setIparentid(cbase.getParentId());
		dbbase.setIposition(cbase.getPosition());
		return dbbase;
	}

	private CategoryName createCategoryName(CategoryItem cbase) {
		CategoryName dbbase = new CategoryName();
		dbbase.setCdescription(cbase.getDescription());
		dbbase.setCkeywords(cbase.getKeywords());
		dbbase.setCname(cbase.getName());
		dbbase.setCtitle(cbase.getTitle());
		dbbase.setIcategoryid(cbase.getId());
		dbbase.setIlanguageid(cbase.getLanguageId());
		return dbbase;
	}

	@Transactional
	private Boolean saveOrUpdateCategoryBase(CategoryBase cbase) {
		if (null == cbase)
			return false;
		int rows = 0;
		if (null != categoryMapper.selectByPrimaryKey(cbase.getIid())) {
			rows = categoryMapper.updateByPrimaryKey(cbase);
		} else {
			rows = categoryMapper.insert(cbase);
		}
		return (rows > 0);
	}

	@Transactional
	private Boolean saveOrUpdateCategoryName(CategoryName cbase) {
		if (null == cbase)
			return false;
		int rows = 0;
		CategoryName dbbasename = categoryNameMapper
				.getCategoryNameByCategoryIdAndLanguage(cbase.getIcategoryid(),
						cbase.getIlanguageid());
		if (dbbasename != null) {
			cbase.setIid(dbbasename.getIid());
			rows = categoryNameMapper.updateByPrimaryKey(cbase);
		} else {
			rows = categoryNameMapper.insert(cbase);
		}
		return (rows > 0);
	}

	public Boolean saveOrUpdatePlatformCategory(WebsiteCategory cbase) {
		CategoryPlatform dbbase = new CategoryPlatform();
		dbbase.setIid(cbase.getId());
		dbbase.setCmetadescription(cbase.getDescription());
		dbbase.setCmetakeyword(cbase.getKeywords());
		dbbase.setCmetatitle(cbase.getTitle());
		dbbase.setCpath(cbase.getPath());
		dbbase.setIcategoryid(cbase.getCategoryId());
		dbbase.setIid(cbase.getId());
		dbbase.setIlevel(cbase.getLevel());
		dbbase.setIparentid(cbase.getParentId());
		dbbase.setIwebsiteid(cbase.getWebsiteId());
		dbbase.setIposition(cbase.getPosition());
		dbbase.setBshow(cbase.getVisible());
		return this.saveOrUpdatePlatformCategory(dbbase);
	}

	@Transactional
	private Boolean saveOrUpdatePlatformCategory(CategoryPlatform cbase) {
		if (null == cbase)
			return false;
		int rows = 0;
		CategoryPlatform dbbasename = cpaltformMapper.getPlatformCategories(
				cbase.getIcategoryid(), cbase.getIwebsiteid());
		if (dbbasename != null) {
			cbase.setIid(dbbasename.getIid());
			rows = cpaltformMapper.updateByPrimaryKeySelective(cbase);
		} else {
			rows = cpaltformMapper.insert(cbase);
		}
		return (rows > 0);
	}

	@Transactional
	public Boolean saveOrUpdateCategoryNameForm(
			CategoryMessageForm categoryMessageForm) {
		CategoryName categoryName = new CategoryName();
		categoryName.setCcontent(categoryMessageForm.getCcontent());
		categoryName.setCdescription(categoryMessageForm.getCdescription());
		categoryName.setCkeywords(categoryMessageForm.getCkeywords());
		categoryName.setCmetadescription(categoryMessageForm
				.getCmetadescription());
		categoryName.setCmetakeyword(categoryMessageForm.getCmetakeyword());
		categoryName.setCmetatitle(categoryMessageForm.getCmetatitle());
		categoryName.setCname(categoryMessageForm.getCname());
		categoryName.setCtitle(categoryMessageForm.getCtitle());
		categoryName.setIcategoryid(categoryMessageForm.getIcategoryid());
		categoryName.setIid(categoryMessageForm.getIid());
		categoryName.setIlanguageid(categoryMessageForm.getIlanguageid());

		CategoryPlatform categoryPlatform = new CategoryPlatform();
		categoryPlatform.setIid(categoryMessageForm.getIcatetorywebsiteiid());
		categoryPlatform.setIposition(categoryMessageForm.getIposition());
		categoryPlatform.setBshow(categoryMessageForm.isBshow());
		if (categoryMessageForm.getIbottom() != null
				&& categoryMessageForm.getIright() != null) {
			CategoryBackgroundImages categoryBackgroundImages = new CategoryBackgroundImages();
			categoryBackgroundImages.setIcategorynameid(categoryMessageForm
					.getIid());
			categoryBackgroundImages.setIbottom(categoryMessageForm
					.getIbottom());
			categoryBackgroundImages.setIright(categoryMessageForm.getIright());
			categoryBackgroundImages.setCbackgroundimages(categoryMessageForm
					.getCbackgroundimages());
			categoryBackgroundImages.setCurl(categoryMessageForm.getCurl());
			categoryBackgroundImages.setIwebsiteid(categoryMessageForm
					.getIwebsiteid());
			int updateBackground = 0;
			
			if (categoryMessageForm.getIbackgroundid() != null) {
				categoryBackgroundImages.setIid(categoryMessageForm
						.getIbackgroundid());
				updateBackground = categoryBackgroundImagesMapper
						.updateByPrimaryKeySelective(categoryBackgroundImages);
			} else {
				updateBackground = categoryBackgroundImagesMapper
						.insertSelective(categoryBackgroundImages);
			}
			if (updateBackground <= 0) {
				return false;
			}
		}

		int updateByPrimaryKeySelective = cpaltformMapper
				.updateByPrimaryKeySelective(categoryPlatform);
		if (updateByPrimaryKeySelective > 0) {
			return saveOrUpdateCategoryName(categoryName);
		} else {
			return (updateByPrimaryKeySelective > 0);
		}
	}

	public boolean updateRank(Integer iqty, String clistingid) {
		int i = rankMapper.updateSalesByListingId(iqty, clistingid);
		if (1 == i) {
			return true;
		}
		return false;
	}

	public boolean insertRank(ProductCategoryRank rank) {
		int i = rankMapper.insertSelective(rank);
		if (1 == i) {
			return true;
		}
		return false;
	}

	public String saveCategoryAttribute(String path,
			List<com.website.dto.Attribute> attrs, String user) {
		Integer id = categoryMapper.getCategorieIdByPath(path);
		if (null == id)
			return "not found category path :" + path;
		String result = "";
		for (com.website.dto.Attribute att : attrs) {
			int keyid = 0;
			Integer vid = 0;
			try {
				if (att.getKey() == null)
					continue;
				attributeUpdateService.Insert(att, user);
				AttributeKeyName dbattrkey = attributeMapper.selectBykeyName(
						att.getKey(), att.getLanguageId());
				keyid = dbattrkey.getIkeyid();
				vid = null;
				if (att.getValue() != null) {
					AttributeValueName attvalueobj = attributeMapper
							.selectbyAttributeValueName(att.getLanguageId(),
									keyid, att.getValue());
					vid = attvalueobj.getIvalueid();
				}
				Boolean exist = false;
				if (vid == null) {
					List<CategoryFilterAttribute> alist = categoryFilterAttributeMapper
							.getCategoryFilterAttributes(id,
									dbattrkey.getIkeyid());
					exist = (alist != null && alist.size() > 0);
				} else {
					exist = (null != categoryFilterAttributeMapper
							.getCategoryFilterAttribute(id,
									dbattrkey.getIkeyid(), vid));
				}
				if (exist == false) {
					CategoryFilterAttribute attr = new CategoryFilterAttribute();
					attr.setIcategoryid(id);
					attr.setIattributekeyid(dbattrkey.getIkeyid());
					attr.setIattributevalueid(vid);
					categoryFilterAttributeMapper.insertSelective(attr);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				result += id + " insert key: "
						+ (att.getKey() == null ? "" : att.getKey()) + keyid
						+ " value: "
						+ (att.getValue() == null ? "" : att.getValue()) + vid
						+ " error->" + ex.getMessage() + System.lineSeparator();
			}
		}
		return result;
	}
}
