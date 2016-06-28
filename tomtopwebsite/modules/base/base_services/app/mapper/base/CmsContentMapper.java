package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.CmsContent;
import dto.CmsContentLanguage;

public interface CmsContentMapper {

	List<CmsContent> getCmsContentAll();

	@Select("select * from ("
			+ "select a.*, m.cname  as cmenuname"
			+ "  from t_cms_content a left join  t_cms_menu m on a.imenuid=m.iid "
			+ ") a order by iid desc limit #{1} offset (#{0}-1)*#{1} ")
	List<CmsContent> getCmsContentPage(Integer page, Integer size);

	@Select("select * from ("
			+ " select a.*, m.cname  as cmenuname"
			+ " from t_cms_content a  left join  t_cms_menu m on a.imenuid=m.iid"
			+ " where ctitle ~* #{2} or ckey ~* #{2}"
			+ ") a  order by iid desc limit #{1} offset (#{0}-1)*#{1} ")
	List<CmsContent> searchCmsContentPage(Integer page, Integer size,
			String ckey);

	List<CmsContent> getCmsContentByMenuIds(
			@Param("list") List<Integer> imenuids, Integer languageId,
			Integer siteId, @Param("device")String device);

	List<CmsContent> getCmsContentByMenuId(Integer imenuid, Integer languageId,
			Integer siteId, @Param("device")String device);

	@Select("select c.iid,c.imenuid,c.ccreateuser,c.clastupdateduser,c.dlastupdateddate,"
			+ "c.iisnominate,c.iisnominate,l.ckey,l.ctitle,l.ccontent from"
			+ " t_cms_content c left join t_cms_content_language_map l "
			+ " on c.iid = l.icmscontentid  where  c.iisnominate = 1 and l.ilanguageid=#{0} order by c.iid")
	List<CmsContent> getNominatetCmsContent(Integer languageId);

	int deleteByPrimaryKey(Integer iid);

	int insert(CmsContent record);

	int insertSelective(CmsContent record);

	CmsContent selectByPrimaryKey(Integer iid, Integer languageId);

	int updateByPrimaryKeySelective(CmsContent record);

	int updateByPrimaryKey(CmsContent record);

	@Select("select count(iid) from t_cms_content ")
	int getCmsContentCount();

	@Select("select count(iid) from t_cms_content where ctitle ~* #{0} or ckey ~* #{0}")
	int searchCmsContentCount(String ckey);

	@Select("select * from t_cms_content_language_map l where icmscontentid=#{0}")
	List<CmsContentLanguage> getAllLangContentByMenuId(Integer menuId);

	void insertCmsContentLanguage(CmsContentLanguage cmsContentLanguage);

	int deleteCmsContentMoreLanguage(Integer iid);

	@Select("select * from t_cms_content_language_map l where icmscontentid=#{0} and ilanguageid = #{1}")
	CmsContentLanguage getCmsContentByLangIdAndContentId(Integer icmscontentid,
			Integer ilanguageid);

}
