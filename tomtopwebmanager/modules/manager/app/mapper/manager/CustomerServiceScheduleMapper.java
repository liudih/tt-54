package mapper.manager;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import entity.manager.CustomerServiceSchedule;
import forms.CustomerServiceScheduleSearchForm;

public interface CustomerServiceScheduleMapper {
	@Select("select * from t_customer_service_schedule order by dstartdate desc limit #{1} offset (#{0}-1)*#{1}")
	List<CustomerServiceSchedule> getPage(int page, int pageSize);

	@Insert("insert into t_customer_service_schedule (iuserid, cdayofweek, dstartdate, denddate, iweekofyear) "
			+ "values (#{iuserid}, #{cdayofweek}, #{dstartdate}, #{denddate}, #{iweekofyear})")
	int insert(CustomerServiceSchedule schedule);

	@Delete("delete from t_customer_service_schedule where iid = #{0}")
	int delete(int id);

	@Select("select * from t_customer_service_schedule where iweekofyear = #{0} order by dstartdate desc")
	List<CustomerServiceSchedule> getByWeekOfYear(int weekOfYear);

	@Select("select count(iid) from t_customer_service_schedule")
	int getCount();

	@Select("select * from t_customer_service_schedule where iid = #{0}")
	CustomerServiceSchedule getByID(int id);

	@Select("<script>select * from t_customer_service_schedule "
			+ "where (dstartdate between #{startDate} and #{endDate}) "
			+ "and (denddate between #{startDate} and #{endDate}) "
			+ "<if test=\"userId != null\">and iuserid = #{userId} </if>"
			+ "order by dstartdate desc limit #{pageSize} offset (#{p} - 1) * #{pageSize}</script>")
	List<CustomerServiceSchedule> searchPage(
			CustomerServiceScheduleSearchForm searchForm);

	@Select("select * from t_customer_service_schedule where  #{0} between dstartdate and denddate ")
	List<CustomerServiceSchedule> getList(Date date);

	@Select("<script>select count(iid) from t_customer_service_schedule "
			+ "where (dstartdate between #{startDate} and #{endDate}) "
			+ "and (denddate between #{startDate} and #{endDate} )"
			+ "<if test=\"userId != null\">and iuserid = #{userId} </if></script>")
	int searchCount(CustomerServiceScheduleSearchForm searchForm);

	@Select("select iuserid from t_customer_service_schedule group by iuserid")
	List<Integer> getAllScheduleUser();

}
