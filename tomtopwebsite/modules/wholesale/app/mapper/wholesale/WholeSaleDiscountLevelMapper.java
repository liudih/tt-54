package mapper.wholesale;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.wholesale.WholeSaleDiscountLevel;

public interface WholeSaleDiscountLevelMapper {
	@Select("select * from t_wholesale_discount_level where #{1} >= fstartprice "
			+ "and #{1} < fendprice and iwebsiteid = #{0} limit 1")
	WholeSaleDiscountLevel getBySiteAndPrice(int siteID, double price);

	@Select("select * from t_wholesale_discount_level where iid =#{0} limit 1")
	WholeSaleDiscountLevel getByID(int id);

	@Select("select * from t_wholesale_discount_level where iwebsiteid =#{0} order by fmindiscount")
	List<WholeSaleDiscountLevel> getWholeSaleDiscountLevelByWebSiteId(
			int WebSiteId);
	
	@Select("select * from t_wholesale_discount_level where iwebsiteid = #{0}")
	List<WholeSaleDiscountLevel> getBySite(int siteID);

	@Update("<script>update t_wholesale_discount_level "
			+ "<set><if test=\"iwebsiteid != null\">iwebsiteid = #{iwebsiteid},</if>"
			+ "<if test=\"fstartprice != null\">fstartprice = #{fstartprice},</if>"
			+ "<if test=\"fendprice != null\">fendprice = #{fendprice},</if>"
			+ "<if test=\"fmindiscount != null\">fmindiscount = #{fmindiscount},</if>"
			+ "<if test=\"fmaxdiscount != null\">fmaxdiscount = #{fmaxdiscount},</if></set> "
			+ "where iid = #{iid}</script>")
	int update(WholeSaleDiscountLevel discount);

	@Insert("insert into t_wholesale_discount_level (iwebsiteid, fstartprice, fendprice, "
			+ "fmindiscount, fmaxdiscount) values (#{iwebsiteid}, #{fstartprice}, #{fendprice}, "
			+ "#{fmindiscount}, #{fmaxdiscount})")
	int insert(WholeSaleDiscountLevel discount);

	@Delete("delete from t_wholesale_discount_level where iid = #{0}")
	int delete(int id);
}
