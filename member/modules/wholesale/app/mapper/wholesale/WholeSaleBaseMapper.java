package mapper.wholesale;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import entity.wholesale.WholeSaleBase;

public interface WholeSaleBaseMapper {
	int addWholeSaleBase(WholeSaleBase record);

	@Update("update t_wholesale_base set istatus = #{1} where iid = #{0}")
	int updateStatusByIid(Integer iid, Integer istatus);

	@Select("select * from t_wholesale_base where cemail = #{0} and iwebsiteid= #{1} limit 1 ")
	WholeSaleBase getWholeSaleBaseByEmail(String email, Integer iwebsiteId);

	int updateWholeSaleBase(WholeSaleBase wholeSaleBase);

	List<WholeSaleBase> getWholeSaleBasesBySearch(
			@Param("status") Integer status,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum, @Param("email") String email,
			@Param("websiteId") Integer websiteId);

	Integer getWholeSaleBasesCount(@Param("status") Integer status,
			@Param("email") String email, @Param("websiteId") Integer websiteId);

	@Select("select * from t_wholesale_base where iid = #{0} ")
	WholeSaleBase getWholeSaleBaseByIid(Integer iid);
}