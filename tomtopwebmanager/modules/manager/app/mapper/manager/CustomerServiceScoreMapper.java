package mapper.manager;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.LivechatLeaveMsgStatistics;
import dto.LivechatSessionScoreStatistics;
import entity.manager.CustomerServiceScore;

public interface CustomerServiceScoreMapper {
	@Select("select * from t_customer_service_score order by dcreatedate desc limit #{1} offset (#{0} - 1) * #{1}")
	List<CustomerServiceScore> getPage(int page, int pageSize);

	@Select("<script>select * from t_customer_service_score where "
			+ "ccustomerservicealias like '%${name}%' "
			+ "<if test=\"typeID != null\">and itype = #{typeID} </if>"
			+ "order by dcreatedate desc limit #{pageSize} offset (#{page} - 1) * #{pageSize}</script>")
	List<CustomerServiceScore> searchPage(@Param("name") String name,
			@Param("typeID") Integer typeID, @Param("page") int page,
			@Param("pageSize") int pageSize);

	@Select("select count(iid) from t_customer_service_score")
	int count();

	@Select("<script>select count(iid) from t_customer_service_score "
			+ "where ccustomerservicealias like '%${name}%' "
			+ "<if test=\"typeID != null\">and itype = #{typeID}</if></script>")
	int searchCount(@Param("name") String name, @Param("typeID") Integer typeID);

	@Insert("insert into t_customer_service_score(csessionid,ccustomerserviceltc,ccustomerltc,ccustomerservicealias,ccustomeralias,ccontent,itype,iscore,dcreatedate,ctopic) "
			+ "values(#{csessionid},#{ccustomerserviceltc},#{ccustomerltc},#{ccustomerservicealias},#{ccustomeralias},#{ccontent},#{itype},#{iscore},#{dcreatedate},#{ctopic})")
	int insert(CustomerServiceScore scroe);

	@Select("<script> select ccustomerservicealias as userName,(sum(iscore)/count(iscore)) as sessionScore,to_char(dcreatedate,#{2}) as latitude "
			+ " from t_customer_service_score where "
			+ " to_char(dcreatedate,#{2}) between #{0} and #{1} "
			+ " <if test=\"userName != null and userName !=''\" > "
			+ " and #{userName}=ccustomerservicealias "
			+ " </if>"
			+ "group by ccustomerservicealias,latitude </script>")
	List<LivechatSessionScoreStatistics> getScoreStatistics(Date beginDate,
			Date endDate, String dateFormat, @Param("userName") String userName);

}
