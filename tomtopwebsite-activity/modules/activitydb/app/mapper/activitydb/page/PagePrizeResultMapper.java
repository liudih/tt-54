package mapper.activitydb.page;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import values.activity.page.PagePrizeResultQuery;
import entity.activity.page.PagePrize;
import entity.activity.page.PagePrizeResult;
import valueobjects.base.activity.result.PrizeResult;

/**
 * 页面奖品统计报表映射类
 * 
 * @author Guozy
 *
 */
public interface PagePrizeResultMapper {
	@Insert({"INSERT INTO t_page_prize_result ",
			 "(ipageid, cemail, iprizeid, cprizevalue, ccountry, iwebsiteid, cvhost, dcreatedate)",
			 " VALUES (#{ipageid}, #{cemail},  #{iprizeid},  #{cprizevalue}, #{ccountry}, #{iwebsiteid}, #{cvhost}, #{dcreatedate})"
	})
	int insert(PagePrizeResult result);

	@Select("select * from t_page_prize_result")
	List<PagePrizeResult> getPagePrizeResults();

	@Select("<script>"
			+ "SELECT COUNT(DISTINCT ipageid) FROM t_page_prize_result  where 1=1 "
			+ "<if test=\"ipageid != null  \">and ipageid=#{ipageid} </if>"
			+ "<if test=\"startDate != null and endDate!=null\">and dcreatedate  BETWEEN #{startDate} AND #{endDate} </if>  "
			+ "</script>")
	int getCount(@Param("ipageid") Integer activityNameId,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Select("<script>"
			+ "select ipageid,count(ipageid) as totalflow from t_page_prize_result where 1=1 "
			+ "<if test=\"ipageid != null  \">and ipageid=#{ipageid} </if>"
			+ "<if test=\"startDate != null and endDate!=null\">and dcreatedate  BETWEEN #{startDate} AND #{endDate} </if> GROUP BY ipageid  limit #{pageSize} offset #{pageSize} * (#{pageNum} - 1)"
			+ "</script>")
	List<PagePrizeResultQuery> getPagePrizeResultsByDCreateDateAndAcitvityName(
			@Param("ipageid") Integer activityNameid,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	@Select("<script>"
			+ "SELECT COUNT(iid) FROM t_page_prize_result  where 1=1 "
			+ "<if test=\"ipageid != null  \">and ipageid=#{ipageid} </if>"
			+ "<if test=\"cemail != null  \">and cemail=#{cemail} </if>"
			+ "<if test=\"ccountry != null  \">and ccountry=#{ccountry} </if>"
			+ "<if test=\"iprizeid != null  \">and iprizeid=#{iprizeid} </if>"
			+ "<if test=\"startDate != null and endDate!=null\">and dcreatedate  BETWEEN #{startDate} AND #{endDate} </if>  "
			+ "</script>")
	int getPagePrizeLotteryCount(@Param("ipageid") Integer activityNameId,
			@Param("cemail") String cemail, @Param("ccountry") String ccountry,
			@Param("iprizeid") Integer iprizeid,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Select("<script>"
			+ "select * from t_page_prize_result where 1=1 "
			+ "<if test=\"ipageid != null  \">and ipageid=#{ipageid} </if>"
			+ "<if test=\"cemail != null  \">and cemail=#{cemail} </if>"
			+ "<if test=\"ccountry != null  \">and ccountry=#{ccountry} </if>"
			+ "<if test=\"iprizeid != null  \">and iprizeid=#{iprizeid} </if>"
			+ "<if test=\"startDate != null and endDate!=null\">and dcreatedate  BETWEEN #{startDate} AND #{endDate} </if>  limit #{pageSize} offset #{pageSize} * (#{pageNum} - 1)"
			+ "</script>")
	List<PagePrizeResultQuery> getPagePrizeResultsByDateAndNameAndPrizeAndEmailAndCountry(
			@Param("ipageid") Integer activityNameId,
			@Param("cemail") String cemail, @Param("ccountry") String ccountry,
			@Param("iprizeid") Integer iprizeid,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);

	@Select("select pr.cemail,pp.cname from t_page_prize_result pr join t_page_prize pp on pr.iprizeid = pp.iid "
			+ "where pr.ipageid = #{pageId} and pr.iwebsiteid = #{website} "
			+ "order by pr.dcreatedate desc")
	List<PrizeResult> getPrizeResultByPageId(@Param("pageId")int pageId, @Param("website")int website);
	
	@Select("<script>"
			+ "select * from t_page_prize_result "
			+ "where ipageid = #{pageId} "
			+ "<if test=\"memberID != null\">and cemail = #{memberID} </if>"
			+ "and iprizeid = #{prizeId}"
			+ "</script>")
	List<PagePrizeResult> getPrizeResultByPIdAndMId(@Param("pageId")int activityPageId, @Param("memberID")String memberID, @Param("prizeId")int pid);
}