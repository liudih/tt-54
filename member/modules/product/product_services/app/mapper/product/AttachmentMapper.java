package mapper.product;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.product.Attachment;

public interface AttachmentMapper {
	@Insert("insert into t_attachment(cdescribe, ctype, cfilename, cpath, ccreateuser, dcreatedate) "
			+ "values(#{0}, #{1}, #{2}, #{3}, #{4}, #{5})")
	int addAttachment(String cdescribe, String ctype, String cfilename,
			String cpath, String ccreateuser, Date dcreatedate);

	@Delete("delete from t_attachment where iid = #{0}")
	int deleteAttachmentByIid(Integer iid);

	@Select({
			"<script>",
			"select * from t_attachment d where 1 = 1 ",
			"<if test=\"filename !=null\">and cfilename like '%${filename}%' </if>",
			"<if test=\"path !=null\">and cpath like '%${path}%' </if>",
			"<if test=\"ctype !=null\">and ctype = #{ctype} </if>",
			"order by iid limit #{pageSize} offset (#{pageNum} - 1) * #{pageSize} </script>" })
	List<Attachment> getAttachmentByFilenameAndType(
			@Param("filename") String filename, @Param("ctype") String ctype,
			@Param("path") String path, @Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	@Select({
			"<script>",
			"select count(iid) from t_attachment d where 1 = 1 ",
			"<if test=\"filename !=null\">and cfilename like '%${filename}%' </if>",
			"<if test=\"path !=null\">and cpath like '%${path}%' </if>",
			"<if test=\"ctype !=null\">and ctype = #{ctype} </if> </script>" })
	Integer getCountBySearch(@Param("filename") String filename,
			@Param("ctype") String ctype, @Param("path") String path);

	@Select("select count(d.iid) from t_product_attachment_mapper d "
			+ "inner join t_attachment_desc ade on ade.iid = d.iattachmentdescid "
			+ "where ade.iattachmentid = #{0}")
	Integer getMapperCountByAttachmentIid(Integer iid);

	@Select("select count(d.iid) from t_attachment d "
			+ "where d.ctype = #{0} and d.cpath = #{1}")
	Integer vaildateAttachmentByTypeAndPath(String ctype, String cpath);

	@Select("select * from t_attachment d where d.iid = #{0}")
	Attachment getAttachmentByIid(Integer iid);
}