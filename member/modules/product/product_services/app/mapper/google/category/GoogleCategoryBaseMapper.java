package mapper.google.category;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.product.google.category.GoogleCategory;
import dto.product.google.category.SearchGoogleCategory;

public interface GoogleCategoryBaseMapper {

	@Select("select * from t_google_category_base where iparentid is Null or iparentid=0")
	List<GoogleCategory> getFirstCategory();

	@Select("select * from t_google_category_base where iparentid=#{0}")
	List<GoogleCategory> getChildsByParentId(int cid);

	@Select("select cpath from t_google_category_base where icategory=#{0}")
	String getCpathByCid(int cid);

	@Select("select cpath from t_google_category_base where icategory=#{0}")
	List<GoogleCategory> getDetailByCid(int cid);

	@Select("select * from t_google_category_base where icategory=#{0}")
	GoogleCategory getCategoryByCid(int cid);

	@Select("select * from t_google_category_base where cpath=#{0}")
	GoogleCategory getIdByCpath(String cpath);

	@Options(keyProperty = "iid", useGeneratedKeys = true)
	@Insert("insert into t_google_category_base(iparentid,icategory, cpath,cname, ilevel, iposition, ichildrencount) "
			+ " values( #{iparentid,jdbcType=INTEGER},#{icategory,jdbcType=INTEGER}, #{cpath,jdbcType=VARCHAR},  #{cname,jdbcType=VARCHAR},"
			+ " #{ilevel,jdbcType=INTEGER}, #{iposition,jdbcType=INTEGER}, #{ichildrencount,jdbcType=INTEGER}) ")
	int insert(GoogleCategory googleCategory);

	@Update("update t_google_category_base set cpath =#{1},iparentid=#{2} "
			+ " where icategory =#{0} ")
	int updateUsingCategoryId(int icategory, String cpath, int parentid);

	@Update("update t_google_category_base set icategory =#{0} ,iparentid=#{2},cname=#{3} "
			+ " where  cpath =#{1} ")
	int updateUsingCpath(int icategory, String cpath, int parentid, String cname);

	@Select({
			"<script> ",
			" select * from t_google_category_base where 1=1 ",
			" <if test=\"cpath != null and cpath != '' \"><bind name=\"cpath\" value=\"'%' + _parameter.cpath + '%'\" /> and cpath like #{cpath} </if>",
			" limit #{pageSize} offset (#{page}-1)*#{pageSize} ", "</script>" })
	List<GoogleCategory> getAll(@Param("page") int page,
			@Param("pageSize") int pageSize, @Param("cpath") String cpath);

	@Select({
			"<script>",
			"select count(*) from t_google_category_base where 1=1",
			" <if test=\"cpath != null and cpath != '' \"><bind name=\"cpath\" value=\"'%' + _parameter.cpath + '%'\" /> and cpath like #{cpath} </if>",
			"</script>" })
	int getCount(@Param("cpath") String cpath);

	@Select("select * from t_google_category_base where iid = #{0}")
	GoogleCategory getCategoryByIid(Integer id);
	
	@Select("SELECT c.cpath,c.cname ,r.icategory ,r.igooglecategory  igooglecategoryid from t_google_category_base c,t_google_category_relation r "+
			"where r.igooglecategory=c.icategory "+
			"ORDER BY c.icategory limit #{pageSize} offset (#{page}-1)*#{pageSize}" )
	List<SearchGoogleCategory> autoMerchantGoogleCategoryProduct(@Param("page") int page, @Param("pageSize")int pageSize);
}
