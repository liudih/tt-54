package mapper.loyalty.promo;

import org.apache.ibatis.annotations.Select;

import entity.loyalty.promo.PromotionCode;

public interface PromotionCodeMapper {

	@Select("SELECT * FROM t_promo_code WHERE ccode = #{0} "
			+ "AND ((now() between dbegindate and denddate) "
			+ "OR (now() >= dbegindate AND denddate IS NULL) "
			+ "OR (now() <= denddate AND dbegindate IS NULL) "
			+ "OR (dbegindate IS NULL AND denddate IS NULL))")
	PromotionCode findPromotionCode(String code);

}
