package mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.member.DropShipLevel;

public interface DropShipLevelMapper {
	@Select("select * from t_dropship_level order by iid")
	List<DropShipLevel> getDropShipLevels();

	@Select("select * from t_dropship_level where iid=#{0}")
	DropShipLevel getDropShipLevelById(Integer levelid);

	@Select("select * from t_dropship_level where #{0} >= fstartprice and #{0} < fendprice "
			+ "order by discount limit 1")
	DropShipLevel getByTotalPrice(double total);
}