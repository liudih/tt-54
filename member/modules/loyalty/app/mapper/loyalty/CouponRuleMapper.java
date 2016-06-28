package mapper.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.loyalty.CouponCode;
import entity.loyalty.CouponRule;
import entity.loyalty.CouponRuleProductFilter;
import forms.loyalty.CouponRuleForm;

public interface CouponRuleMapper {

	List<CouponRule> getAll(@Param("page") int page,
			@Param("pageSize") int pageSize,
			@Param("condition") CouponRuleForm condition);

	Integer getTotal(@Param("condition") CouponRuleForm condition);

	int add(CouponRule rule);

	int del(List<Integer> list);

	@Select("select * from t_coupon_rule where istatus=1")
	List<CouponRule> getCouponRules();

	@Select("select * from t_coupon_rule where iid=#{0}")
	CouponRule get(int id);

	int edit(CouponRule couponRule);

	@Select("select cname from t_coupon_rule where iid=#{0}")
	String getRuleNameById(int id);

	int addTreeCheck(List<CouponRuleProductFilter> checks);

	int delTreeCheckByRuleId(Integer ruleId);

	@Select("select icategoryid from t_coupon_rule_categoryfilter where iruleid=#{0}")
	List<Integer> getTreeCheckByRuleId(Integer ruleId);
	
	@Select("select iid from t_coupon_rule where cname=#{0}")
	Integer getRuleIidByName(String cname);
	
	@Select("SELECT DISTINCT(t.cname) FROM t_coupon_rule t")
	List<CouponRule> getCouponRuleNames();
	
	@Select("select * from t_coupon_rule")
	List<CouponRule> getCouponRulesList();
	
	
	int ChangeStatusOn(Integer id);

	int ChangeStatusOff(Integer id);

	/**
	 * 设置规则的状态为delete
	 * 
	 * @author lijun
	 * @param ruleId
	 * @return
	 */
	int ChangeStatusDelete(Integer ruleId);

	/**
	 * 判断该name的规则是否已经存在,数据库中是不能有相同名称的规则(除删除的规则)
	 * 
	 * @author lijun
	 * @param name
	 * @return
	 */
	int isExisted(String name);

	/**
	 * @author lijun
	 * @return
	 */
	CouponRule getLoginRule();

	/**
	 * @author lijun
	 * @return
	 */
	CouponRule getSubscribeRule();
}
