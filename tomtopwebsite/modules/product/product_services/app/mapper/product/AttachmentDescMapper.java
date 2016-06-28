package mapper.product;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.product.AttachmentDesc;

public interface AttachmentDescMapper {
	@Insert("insert into t_attachment_desc(ilanguage, ctitle, cdescribe, iattachmentid, ccreateuser, dcreatedate) "
			+ "values(#{0}, #{1}, #{2}, #{3}, #{4}, #{5})")
	int addAttachmentDesc(Integer ilanguage, String ctitle, String cdescribe,
			Integer iattachmentid, String ccreateuser, Date dcreatedate);

	@Delete("delete from t_attachment_desc where iid = #{0}")
	int deleteAttachmentDescByIid(Integer iid);

	@Select({ "<script>", "select * from t_attachment_desc d where 1 = 1 ",
			"<if test=\"ctitle !=null\">and ctitle like '%${ctitle}%' </if>",
			"<if test=\"ilanguage !=null\">and ilanguage = #{ilanguage} </if>",
			"<if test=\"iid !=null\">and ilanguage = #{iid} </if>",
			"order by iid </script>" })
	List<AttachmentDesc> getAttachmentDescriptBySearch(
			@Param("ctitle") String ctitle,
			@Param("ilanguage") Integer ilanguage, @Param("iid") Integer iid);

	@Select("select * from t_attachment_desc ad where ad.iattachmentid = #{0}")
	List<AttachmentDesc> getAttachmentDescriptsByAttachmentId(
			Integer attachmentid);

	@Update("update t_attachment_desc set ctitle = #{0}, cdescribe = #{1}, cupdateuser = #{2}, dupdatedate = #{3} where iid = #{4} ")
	int updateAttachmentDesc(String ctitle, String cdescribe,
			String updateUser, Date updateDate, Integer iid);

	@Select({
			"<script> select * ",
			"from t_attachment_desc ad ",
			"where ad.iid in ",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</script>" })
	List<AttachmentDesc> getAttachmentDescriptsByIids(
			@Param("list") List<Integer> iids);

	@Delete("delete from t_attachment_desc where iattachmentid = #{0}")
	int deleteAttachmentDescByAttachmentid(Integer iattachmentid);
}