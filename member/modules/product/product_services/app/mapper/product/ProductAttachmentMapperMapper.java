package mapper.product;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.product.ProductAttachmentMapper;

public interface ProductAttachmentMapperMapper {
	@Insert("insert into t_product_attachment_mapper(iwebsiteid, csku, clistingid, ilanguage, iattachmentdescid, ccreateuser, dcreatedate) "
			+ "values(#{0}, #{1}, #{2}, #{3}, #{4}, #{5}, #{6})")
	int addProductAttachmentMapper(Integer iwebsiteid, String csku,
			String clistingid, Integer ilanguage, Integer iattachmentdescid,
			String ccreateuser, Date dcreatedate);

	@Delete("delete from t_product_attachment_mapper where iid = #{0}")
	int deleteProductAttachmentMapperByIid(Integer iid);

	@Select({
			"<script>",
			"select * from t_product_attachment_mapper d ",
			"<if test=\"title !=null\">inner join t_attachment_desc ade on ade.iid = d.iattachmentdescid and ade.ctitle like '%${title}%' </if>",
			"where 1 = 1 ",
			"<if test=\"iwebsiteid !=null\">and d.iwebsiteid = #{iwebsiteid} </if>",
			"<if test=\"csku !=null\">and d.csku = #{csku} </if>",
			"<if test=\"ilanguage !=null\">and d.ilanguage = #{ilanguage} </if>",
			"order by d.iid limit #{pageSize} offset (#{pageNum} - 1) * #{pageSize} </script>" })
	List<ProductAttachmentMapper> getProductAttachmentMapperBySearch(
			@Param("iwebsiteid") Integer iwebsiteid,
			@Param("csku") String csku, @Param("ilanguage") Integer ilanguage,
			@Param("title") String title, @Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	@Select({
			"<script>",
			"select count(d.iid) from t_product_attachment_mapper d ",
			"<if test=\"title !=null\">inner join t_attachment_desc ade on ade.iid = d.iattachmentdescid and ade.ctitle like '%${title}%' </if>",
			"where 1 = 1 ",
			"<if test=\"iwebsiteid !=null\">and d.iwebsiteid = #{iwebsiteid} </if>",
			"<if test=\"csku !=null\">and d.csku = #{csku} </if>",
			"<if test=\"ilanguage !=null\">and d.ilanguage = #{ilanguage} </if> </script>" })
	Integer getCountBySearch(@Param("iwebsiteid") Integer iwebsiteid,
			@Param("csku") String csku, @Param("ilanguage") Integer ilanguage,
			@Param("title") String title);

}