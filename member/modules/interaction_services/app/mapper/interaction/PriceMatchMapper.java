package mapper.interaction;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.interaction.PriceMatch;

public interface PriceMatchMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(PriceMatch record);

	int insertSelective(PriceMatch record);

	PriceMatch selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(PriceMatch record);

	int updateByPrimaryKey(PriceMatch record);

	@Select({
			"<script>",
			"select clistingid, csourceurl,flowerprice,cinquiry,dcreatedate,cstatic from t_price_match where cuseremail=#{0} and iwebsiteid=#{1} ",
			"<if test=\"list!=null and list!=''\">",
			"and clistingid in",
			"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
			"</if>", "<if test=\"filter!=null and filter=='pe'\">",
			" and cstatic='Pending'", "</if>",
			"<if test=\"filter!=null and filter=='ap'\">",
			" and cstatic='Approved'", "</if>",
			"<if test=\"filter!=null and filter=='fa'\">",
			" and cstatic='Failed'", "</if>",
			"<if test=\"start!=null and end!=null\">",
			" and dcreatedate between #{start} and #{end}", "</if>",
			" limit #{2} offset #{3}", "</script>" })
	List<PriceMatch> getPriceMatchByEmail(String email, Integer iwebsiteid,
			Integer pageSize, Integer page,
			@Param("list") List<String> listingids,
			@Param("filter") String filter, @Param("start") Date start,
			@Param("end") Date end);

	@Select("select count(*) from t_price_match where cuseremail=#{0}")
	int getCountByEmail(String email);

	@Select("select clistingid from t_price_match where cuseremail=#{0}")
	List<PriceMatch> getClistingidByEmail(String email);

	@Select({ "<script>", "select clistingid from t_price_match",
			"<if test=\"filter!=null and filter=='pe'\">",
			" where cstatic='Pending'", "</if>",
			"<if test=\"filter!=null and filter=='ap'\">",
			" where cstatic='Approved'", "</if>",
			"<if test=\"filter!=null and filter=='fa'\">",
			" where cstatic='Failed'", "</if>",
			"<if test=\"start!=null and end!=null\">",
			" and dcreatedate between #{start} and #{end}", "</if>",
			"</script>"

	})
	List<String> selectClistingidByfilter(@Param("filter") String filter,
			@Param("start") Date start, @Param("end") Date end);
}