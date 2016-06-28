package mapper.image;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import play.api.libs.iteratee.internal;
import dto.image.Img;

public interface ImageMapper {

	@Select("select iid, cpath, bcontent, ccontenttype, cmd5,cdnpath from t_img where cpath=#{0}")
	Img getImageByPath(String path);

	@Select("select iid, cpath, ccontenttype, cmd5,cdnpath from t_img where cpath=#{0}")
	Img getImageByPathWithoutContent(String path);

	@Insert("INSERT INTO t_img (cpath, bcontent, ccontenttype, cmd5,cdnpath,iwebsiteid) "
			+ "VALUES (#{cpath}, #{bcontent}, #{ccontenttype}, #{cmd5},#{cdnpath},#{iwebsiteid})")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	public long createImage(Img img);
	
	@Insert("INSERT INTO t_img (cpath,ccontenttype,cdnpath,iwebsiteid) "
			+ "VALUES (#{cpath}, #{ccontenttype},#{cdnpath},#{iwebsiteid})")
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	public long createCdnImage(Img img);

	@Select("<script>select * from t_img where iwebsiteid=#{iwebsiteid} <if test=\"contenttype != null\">"
			+ "and ccontenttype like '%${contenttype}%' </if><if test=\"path != null\">and cpath like '%${path}%' </if>"
			+ "order by iid desc limit #{pageSize} offset (#{pageSize} * (#{pageNum} - 1))"
			+ "</script>")
	List<Img> getImgByPage(@Param("iwebsiteid")int iwebsiteid,@Param("contenttype") String contenttype,
			@Param("path") String path, @Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	@Select("<script>select count(*) from t_img where iwebsiteid=#{iwebsiteid} and bcontent is NOT NULL <if test=\"contenttype != null\">"
			+ "and ccontenttype like '%${contenttype}%' </if><if test=\"path != null\">and cpath like '%${path}%' </if>"
			+ "</script>")
	Integer getImgCount(@Param("iwebsiteid")int iwebsiteid,@Param("contenttype") String contenttype,
			@Param("path") String path);

	@Delete("DELETE FROM t_img WHERE iid = #{0}")
	int deleteImageById(Integer iid);

	@Insert("UPDATE t_img SET bcontent = #{bcontent}, cmd5 = #{cmd5} where cpath = #{cpath} and iwebsiteid = #{iwebsiteid}")
	public long updateImage(Img img);
	
	@Insert("UPDATE t_img SET cdnpath=#{cdnpath} where cpath = #{cpath} and iwebsiteid = #{iwebsiteid}")
	public long updateCdnImage(Img img);

	@Delete("<script>delete from t_img where cpath in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach></script>")
	public int deleteImageByPaths(@Param("list") List<String> paths);

	/**
	 * 
	 * @Title: getImageById
	 * @Description: TODO(通过id查询图片)
	 * @param @param id
	 * @param @return
	 * @return Img
	 * @throws
	 * @author yinfei
	 */
	@Select("select * from t_img where iid = #{templateId}")
	Img getImageById(int id);
}
