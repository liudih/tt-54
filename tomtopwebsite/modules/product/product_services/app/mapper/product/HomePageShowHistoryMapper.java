package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.product.HomePageShowHistory;

public interface HomePageShowHistoryMapper {

	@Select("select clistingid from  t_home_page_show_history where ctype=#{0} and iwebsiteid = #{1} "
			+ "and dshowtime between (now() - interval '7 day')  and now()")
	List<String> getAWeekendPageShowHistoriesByType(String type, int siteId);

	@Select("select * from t_home_page_show_history where clistingid=#{0} and iwebsiteid =#{1} and ctype=#{2}")
	HomePageShowHistory getHomePageProductHistory(String listingId,
			Integer siteId, String type);

	@Insert("insert into t_home_page_show_history (iwebsiteid,clistingid,ctype,dshowtime) values (#{iwebsiteid},#{clistingid},#{ctype},now())")
	int addHomePageProductHistory(HomePageShowHistory homePageShowHistory);

	@Update("update  t_home_page_show_history set dshowtime=now() where iid=#{iid}")
	int updateHomePageProductHistory(HomePageShowHistory homePageShowHistory);
}
