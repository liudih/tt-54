package mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.member.DropShipBase;

public interface DropShipBaseMapper {
	int addDropShipBase(DropShipBase record);

	@Update("update t_dropship_base set istatus = #{1} where iid = #{0}")
	int updateStatusByIid(Integer iid, Integer istatus);

	List<DropShipBase> getDropShipBasesBySearch(
			@Param("status") Integer status,
			@Param("pageSize") Integer pageSize,
			@Param("pageNum") Integer pageNum, @Param("email") String email,
			@Param("level") Integer level);

	Integer getDropShipBasesCount(@Param("status") Integer status,
			@Param("email") String email, @Param("level") Integer level);

	@Update("update t_dropship_base set idropshiplevel = #{1} where iid = #{0}")
	int updateLevelByIid(Integer iid, Integer levelId);

	@Select("select * from t_dropship_base where cemail = #{0} and iwebsiteid= #{1} limit 1")
	DropShipBase getDropShipBaseByEmail(String email, Integer iwebsiteId);

	int updateDropShip(DropShipBase dropShipBase);

	@Select("select * from t_dropship_base where iid = #{0} limit 1")
	DropShipBase getDropShipBaseByIid(Integer iid);

}