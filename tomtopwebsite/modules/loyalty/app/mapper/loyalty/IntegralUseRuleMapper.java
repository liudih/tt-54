package mapper.loyalty;

import interceptors.CacheResult;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.loyalty.IntegralUseRule;

public interface IntegralUseRuleMapper {
	@CacheResult
	@Select("select iintegral, fmoney, ccurrency, imaxuse "
			+ "from t_integral_use_rule where iwebsiteid = ${siteId} and imembergroupid = ${groupId}")
	IntegralUseRule getBySiteIdAndGroupId(@Param("siteId") Integer siteId,
			@Param("groupId") Integer groupId);
}
