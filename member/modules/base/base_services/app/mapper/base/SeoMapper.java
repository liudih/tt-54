package mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.Seo;
/**
 * 首页管理映射类
 * 
 * @author guozy
 *
 */
public interface SeoMapper {

	@Insert("INSERT INTO t_seo(iwebsiteid,ilanguageid,ctitle,ckeywords,ctype,cdescription,ccreatename,dcreatedate) "
			+ "values(#{iwebsiteid},#{ilanguageid},#{ctitle},#{ckeywords},#{ctype},#{cdescription},#{ccreatename},#{dcreatedate})")
	int insertSeo(Seo seo);

	@Delete("delete from t_seo where iid=#{iid}")
	int deleteSeo(Integer iid);

	@Update("UPDATE t_seo SET ctitle=#{ctitle},ckeywords=#{ckeywords},ctype=#{ctype},iwebsiteid=#{iwebsiteid},ilanguageid=#{ilanguageid},cdescription=#{cdescription},cmodifiedname=#{cmodifiedname},dmodifieddate=#{dmodifieddate} WHERE iid=#{iid}")
	int updateSeo(Seo homeManager);

	@Select("<script>"
			+ "select COUNT(iid) from t_seo where 1=1 "
			+ "<if test=\"iwebsiteid != null  \">and iwebsiteid=#{iwebsiteid} </if>"
			+ "<if test=\"ilanguageid !=null \">and ilanguageid=#{ilanguageid} </if>"
			+ "<if test=\"ctype !=null \">and ctype=#{ctype}</if>"
			+ "</script>")
	Integer getCount(@Param("iwebsiteid") Integer iwebsiteid,
			@Param("ilanguageid") Integer ilanguageid,
			@Param("ctype") String ctype);

	@Select("<script>"
			+ "select * from t_seo where 1=1 "
			+ "<if test=\"iwebsiteid != null  \">and iwebsiteid=#{iwebsiteid} </if>"
			+ "<if test=\"ilanguageid !=null \">and ilanguageid=#{ilanguageid} </if>"
			+ "<if test=\"ctype !=null \">and ctype=#{ctype}</if> ORDER BY Iid desc limit #{pageSize} offset #{pageSize} * (#{pageNum} - 1)"
			+ "</script>")
	List<Seo> getList(@Param("iwebsiteid") Integer iwebsiteid,
			@Param("ilanguageid") Integer ilanguageid,
			@Param("ctype") String ctype, @Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	@Select("select t.ctype,t.ctitle,t.cdescription from t_seo t where t.ilanguageid=#{1} and t.iwebsiteid=#{0} and t.ctype=#{2}")
	Seo getSeoBylanguageIdAndSiteIdAndType(Integer iwebsiteid, Integer ilanguageid,
			String ctype);
}
