package services.loyalty.coupon;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import valueobjects.base.Page;
import entity.loyalty.CouponRule;
import entity.loyalty.CouponRuleProductFilter;
import forms.loyalty.CouponRuleForm;
import mapper.loyalty.CouponCodeMapper;
import mapper.loyalty.CouponRuleMapper;

/**
 * coupon 生成规则
 * 
 * @author xiaoch
 *
 */
public class CouponRuleService {

	@Inject
	CouponCodeMapper couponCodeMapper;

	@Inject
	CouponRuleMapper couponRuleMapper;

	public Page<CouponRule> getAll(int page, int pageSize,
			CouponRuleForm condition) {
		List<CouponRule> list = couponRuleMapper.getAll(page, pageSize,
				condition);
		Integer total = couponRuleMapper.getTotal(condition);
		Page<CouponRule> result = new Page<>(list, total, page, pageSize);
		return result;
	}

	public boolean add(CouponRule rule) {
		int result = couponRuleMapper.add(rule);
		return result > 0 ? true : false;
	}

	public boolean del(Integer[] ids) {
		List<Integer> list = Arrays.asList(ids);
		int result = couponRuleMapper.del(list);
		return result > 0 ? true : false;
	}

	/**
	 * 给其他模块调用 返回所有coupon规则
	 * 
	 * @return
	 */
	public List<CouponRule> getCouponRules() {
		return couponRuleMapper.getCouponRules();
	}

	public String getRuleNameById(int id) {
		return couponRuleMapper.getRuleNameById(id);
	}

	public CouponRule get(int id) {
		return couponRuleMapper.get(id);
	}

	/**
	 * 根据规则id获取含有排除品类的coupon rule
	 * 
	 * @param ruleId
	 * @return
	 */
	public CouponRule getCouponRuleByRuleId(int ruleId) {
		CouponRule couponRule = couponRuleMapper.get(ruleId);
		List<Integer> checkIds = getTreeCheckByRuleId(ruleId);
		couponRule.setExcludeCategoryIds(checkIds);
		return couponRule;
	}

	public boolean edit(CouponRule couponRule) {
		int result = couponRuleMapper.edit(couponRule);
		return result > 0 ? true : false;
	}

	/**
	 * 保存前台check选中的品类id
	 * 
	 * @param checks
	 * @return
	 */
	public boolean addTreeCheck(List<CouponRuleProductFilter> checks) {
		int result = couponRuleMapper.addTreeCheck(checks);
		return result > 0 ? true : false;
	}

	public boolean delTreeCheckByRuleId(Integer ruleId) {
		int result = couponRuleMapper.delTreeCheckByRuleId(ruleId);
		return result > 0 ? true : false;
	}

	/**
	 * 根据规则id获取所有选中的品类id
	 * 
	 * @param ruleId
	 * @return
	 */
	public List<Integer> getTreeCheckByRuleId(Integer ruleId) {
		List<Integer> checkIds = couponRuleMapper.getTreeCheckByRuleId(ruleId);
		return checkIds;
	}

	public boolean ChangeStatusOn(Integer id) {
		int result = couponRuleMapper.ChangeStatusOn(id);
		return result > 0 ? true : false;
	}

	public boolean ChangeStatusOff(Integer id) {
		int result = couponRuleMapper.ChangeStatusOff(id);
		return result > 0 ? true : false;
	}

	/**
	 * 根据Code密码查询含有排除品类的coupon rule
	 * 
	 * @param ruleId
	 * @return
	 */
	public CouponRule getCouponRuleByCode(String code) {
		Integer ruleId = couponCodeMapper.getRuleIdByCode(code);
		CouponRule couponRule = getCouponRuleByRuleId(ruleId);
		return couponRule;
	}

	/**
	 * 设置规则状态为delete
	 * 
	 * @author lijun
	 * @param ruleId
	 * @return
	 */
	public boolean ChangeStatusDelete(int ruleId) {
		int changeRows = this.couponRuleMapper.ChangeStatusDelete(ruleId);
		return changeRows > 0 ? true : false;
	}

	/**
	 * 判断该name的规则是否已经存在,数据库中是不能有相同名称的规则(除删除的规则)
	 * 
	 * @author lijun
	 * @param name
	 * @return
	 */
	public boolean isExisted(String name) {
		if(name == null || name.length() == 0){
			return true;
		}
		return this.couponRuleMapper.isExisted(name) > 0 ? true : false;
	}
	
	/**
	 * Login 规则比较特殊
	 * 
	 * @author lijun
	 */
	public CouponRule getLoginRule(){
		CouponRule result = this.couponRuleMapper.getLoginRule();
		return result;
	}
	
	public CouponRule getSubscribeRule(){
		CouponRule result = this.couponRuleMapper.getSubscribeRule();
		return result;
	}
	
	/**
	 * 通过规则名称获取规则编号
	 * @param cname
	 * @return
	 */
	public Integer getRuleIidByName(String cname){
		return couponRuleMapper.getRuleIidByName(cname);
	}
	
	
	/**
	 * 获取规则表的所有数据信息
	 * @return
	 */
	public List<CouponRule> getCouponRulesList(){
		return couponRuleMapper.getCouponRulesList();
	}
}
