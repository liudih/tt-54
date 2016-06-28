package mapper.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import entity.loyalty.Activity;
import entity.loyalty.DotypeRuleElement;

public interface ActivityMapper {
	
	@Select("select * from t_activity_plan_base where cdotype=#{0} "
			+" and iwebsiteid=#{1} and benabled=true " 
			+" and now() between dbegindate and denddate")
	Activity getActivityFor(String cdotype,Integer iwebsiteid);

	@Select("select * from t_activity_dotype_rule_element where cdotype=#{0}")
	List<DotypeRuleElement> getRuleElement(String cdotype);
}
