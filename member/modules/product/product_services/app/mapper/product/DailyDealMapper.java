package mapper.product;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.product.DailyDeal;

public interface DailyDealMapper {
	@Insert("insert into t_daily_deals(iwebsiteid, clistingid, csku, ccreateuser, bvalid) "
			+ "values(#{0}, #{1}, #{2}, #{3}, #{4})")
	int insert(Integer iwebsiteid, String clistingid, String csku,
			String ccreateuse, boolean bvalid);

	@Select("select * from t_daily_deals where iwebsiteid = #{0} and bvalid = true "
			+ "order by iid limit #{1} offset (#{1} * #{2})")
	List<DailyDeal> getListingIdByWebsiteId(Integer websiteId,
			Integer pagesize, Integer pageNum);

	@Update("update t_daily_deals set bvalid = #{1} where iid=#{0} ")
	int updateDailyDealBvalid(Integer id, Boolean bvalid);

	@Insert("insert into t_daily_deals(iwebsiteid, clistingid, csku, ccreateuser, bvalid, dcreatedate) "
			+ "values(#{iwebsiteid}, #{clistingid}, #{csku}, #{ccreateuser}, #{bvalid}, #{dcreatedate})")
	int insertDailyDeal(DailyDeal dailyDeal);

	@Select("select * from t_daily_deals where iwebsiteid = #{0} and bvalid = true " 
			+ "and dcreatedate between #{1} and #{2} "
			+ "order by iid")
	List<DailyDeal> getListingIdByWebsiteIdAndDate(Integer websiteId,
			Date startDate, Date endDate);
}