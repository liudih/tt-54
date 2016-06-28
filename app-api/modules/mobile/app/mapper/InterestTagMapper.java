package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.mobile.InterestTag;
import entity.mobile.TagType;

public interface InterestTagMapper {

	@Insert({ "<script>insert into t_interest_tag (tagid,imei,sysversion,phonename,"
			+ "appid,cremoteaddress,createdate,status) "
			+ "values (#{tagid},#{imei},#{sysversion},#{phonename},"
			+ "#{appid},#{cremoteaddress},now(),#{status})</script>" })
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(InterestTag tag);

	@Select("select tagid,imei,sysversion,phonename,appid,cremoteaddress,createdate,status "
			+ "from t_interest_tag where imei=#{imei} and status = 1")
	List<InterestTag> getInterestTags(String imei);

	@Update("update t_interest_tag set status = 0 where imei=#{imei}")
	int delete(String imei);

	@Select("select iid,tagname,tagvalue,tagtype,imgurl,lang,operator,createdate,status from t_tag_type where status = 1 and lang = #{lang}")
	List<TagType> getTagTypes(int lang);

	@Select({ "<script>select iid,tagname,tagvalue,tagtype,imgurl,lang,operator,createdate,status "
			+ "from t_tag_type where tagtype=#{type} and iid in "
			+ "<foreach item='item' index='index' collection='list' open='(' separator=',' close=')' >#{item}</foreach>"
			+ "</script>" })
	List<TagType> getTagTypeByTagIds(@Param("list") List<Integer> tagids,
			@Param("type") int type);

	@Insert({ "<script>insert into t_tag_type(tagname,tagtype,tagvalue,imgurl,lang,operator,createdate,status) "
			+ "values(#{tagname},#{tagvalue},#{tagtype},#{imgurl},#{lang},#{operator},#{createdate},#{status})</script>" })
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insertTagType(TagType tagtype);

	@Update("update t_tag_type set status = #{status} where type=#{type}")
	int updateTagType(String type, int status);

}
