package mapper.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.CmsMenu;
import dto.CmsMenuLanguage;
import dto.CmsMenuWebsite;

public interface CmsMenuMapper {

	List<CmsMenu> getCmsMenuByLevelId(Map<String, Object> param);

	List<CmsMenu> getCmsMenuByLevelIdAndType(Map<String, Object> param);

	List<CmsMenu> getNominatetCmsMenu(Map<String, Object> param);

	int deleteByPrimaryKey(Integer iid);

	int insert(CmsMenu record);

	CmsMenu selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(CmsMenu record);

	int updateByPrimaryKey(CmsMenu record);

	@Select("select * from t_cms_menu_language_map l where imenuid=#{0}")
	List<CmsMenuLanguage> getAllLangMenuByMenuId(Integer menuId);

	void insertCmsMenuLanguage(CmsMenuLanguage cmsMenuLanguage);

	int deleteCmsMenuMoreLanguage(Integer iid);

	@Select("select * from t_cms_menu_language_map l where imenuid=#{0} and ilanguageid = #{1} limit 1")
	CmsMenuLanguage getCmsMenuMoreLanguage(Integer imenuid, Integer ilanguageid);

	@Select("select * from t_cms_menu_wetsite_map l where imenuid=#{0}")
	List<CmsMenuWebsite> getAllMenuWebsiteByMenuId(Integer menuId);

	void insertCmsMenuWebsite(CmsMenuWebsite cmsMenuWebsite);

	int deleteCmsMenuMoreWebsite(Integer iid);

	@Select("select * from t_cms_menu_wetsite_map l where imenuid=#{0} and iwebsiteid = #{1} and cdevice = #{2}  limit 1")
	CmsMenuWebsite validateCmsMenuMoreWebsite(Integer imenuid,
			Integer ilanguageid, String cdevice);

	List<CmsMenu> getCmsMenuByLevelIdAndSiteId(
			@Param("levelId") Integer levelId,
			@Param("iparentid") Integer iparentid,
			@Param("siteId") Integer siteId);

}
