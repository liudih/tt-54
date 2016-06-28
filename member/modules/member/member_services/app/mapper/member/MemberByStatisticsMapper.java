package mapper.member;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.member.MemberByStatistics;

public interface MemberByStatisticsMapper {

	@Insert("insert into t_member_buy_statistics (cemail, famount, icount) values (#{cemail}, #{famount}, 1)")
	int insert(MemberByStatistics memberByStatistics);

	@Update("update t_member_buy_statistics set famount = (famount + #{famount}), "
			+ "icount = (icount + 1) where cemail = #{cemail}")
	int update(MemberByStatistics memberByStatistics);

	@Select("select * from t_member_buy_statistics where cemail= #{0} limit 1")
	MemberByStatistics select(String email);

}
