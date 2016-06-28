package mapper.google.category;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.product.google.category.GoogleAttributForm;
import dto.product.google.category.GoogleAttribute;
import dto.product.google.category.GoogleAttributeMapperDto;

public interface GoogleAttributeMapper {

	@Select("select * from t_google_filter_attribute where icategoryid = #{0}")
	List<GoogleAttribute> getAttributeByCid(Integer icategory);

	@Select({ "<script> "
			+ " select a.*,g.cname ,b.ckeyname,c.ckeyname webkeyname from t_google_filter_attribute a inner join"
			+ " t_google_category_base g on g.icategory = a.icategoryid inner join t_google_attribute_key b"
			+ " on a.iattributekeyid = b.iid inner join t_attribute_key_name c on a.iwebkeyid=c.ikeyid where c.ilanguageid = 1"
			+ " <if test=\"cname != null and cname != '' \"> <bind name=\"cname\" value=\"'%' + _parameter.cname + '%'\" /> and g.cname like #{cname}</if>"
			+ " limit #{pageSize} offset (#{page}-1)*#{pageSize}" + "</script>" })
	List<GoogleAttributForm> getAttributeByGid(@Param("page") int page,
			@Param("pageSize") int pageSize, @Param("cname") String cname);

	@Insert("insert into t_google_filter_attribute (icategoryid,iattributekeyid,iwebkeyid) values (#{0},#{1},#{2})")
	int insertAttr(int cid, int ckeyid, Integer wkeyId);

	@Delete("delete from t_google_filter_attribute where iid = #{0}")
	int delAttrByIid(int id);

	@Select({ "<script> "
			+ "select count(*) from t_google_filter_attribute where 1=1"
			+ "<if test=\"cname != null and cname != '' \">and icategoryid = #{icategoryid}</if>"
			+ "</script>" })
	int getCount(@Param("cname") String cname,
			@Param("icategoryid") int icategoryid);

	@Select("select * from t_google_filter_attribute where iid = #{0}")
	GoogleAttribute getAttrByIid(int id);

	@Select("SELECT gk.ckeyname ,ga.iwebkeyid from t_google_attribute_key gk INNER JOIN t_google_filter_attribute ga "
			+ "on ga.iattributekeyid=gk.iid and ga.icategoryid=#{0}")
	List<GoogleAttributeMapperDto> getAttrByIcategoryid(int id);

	@Select("select * from t_google_filter_attribute where iattributekeyid = #{0} and icategoryid = #{1} ")
	GoogleAttribute getAttrByKeyId(int aKey, int cid);

	@Select("select count(*) from t_google_filter_attribute where 1=1")
	int getTCount();

	@Select("select * from t_google_filter_attribute where iwebkeyid = #{0} and icategoryid = #{1} ")
	GoogleAttribute getWebAttrByKeyId(int attkey, int cid);

}
