package mapper.interaction;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.interaction.WholesaleInquiry;

public interface WholesaleInquiryMapper {
	int deleteByPrimaryKey(Integer iid);

	int insert(WholesaleInquiry record);

	int insertSelective(WholesaleInquiry record);

	WholesaleInquiry selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(WholesaleInquiry record);

	int updateByPrimaryKey(WholesaleInquiry record);

	@Select("<script>"
			+ "select COUNT(iid) from t_wholesale_inquiry where 1=1 "
			+ "<if test=\"startDate != null and endDate!=null\">and dcreatedate  BETWEEN #{startDate} AND #{endDate} </if>  "
			+ "</script>")
	public Integer getCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	@Select("<script>"
			+ "select * from t_wholesale_inquiry where 1=1 "
			+ "<if test=\"startDate != null and endDate!=null\">and dcreatedate  BETWEEN #{startDate} AND #{endDate} </if> ORDER BY iid desc limit #{pageSize} offset #{pageSize} * (#{pageNum} - 1)"
			+ "</script>")
	List<WholesaleInquiry> getWholesaleInquiries(
			@Param("startDate") Date startDate,
			@Param("endDate") Date endDate,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum);
}