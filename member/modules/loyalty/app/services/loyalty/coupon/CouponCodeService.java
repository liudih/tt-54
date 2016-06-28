package services.loyalty.coupon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.loyalty.CouponCodeMapper;

import org.springframework.beans.BeanUtils;

import play.Logger;
import services.base.FoundationService;
import valueobjects.base.Page;
import base.util.random.RandomNumberUtil;

import com.google.api.client.util.Maps;

import entity.loyalty.CouponCode;
import entity.loyalty.CouponRule;
import entity.loyalty.business.CouponCodeBo;
import enums.loyalty.coupon.manager.CouponCodeBack;
import enums.loyalty.coupon.manager.CouponRuleBack;
import forms.loyalty.CouponCodeForm;

/**
 * Coupon Code
 * 
 * @author xiaoch
 *
 */
public class CouponCodeService {

	@Inject
	FoundationService fservice;

	@Inject
	CouponRuleService couponRuleService;

	@Inject
	CouponCodeMapper couponCodeMapper;

	// @Inject
	// IAdminuserProvider iAdminuserProvider;

	// 因随机数碰撞导致重新生成随机数的次数
	private static final int GENERATE_TIME = 100;

	// 默认登陆规则id
	private static final int LOGIN_RULEID = 1;

	// 默认订阅规则id
	private static final int SUBSCRIBE_RULEID = 2;

	// 当后台未登录时，规则创建的用户id默认值
	private static final int NULL_CREATEOR = 0;

	public Page<CouponCode> list(int page, int pageSize,
			CouponCodeForm condition) {
		Map paras = Maps.newHashMap();
		paras.put("page", page);
		paras.put("pageSize", pageSize);
		paras.put("ruleId", condition.getIcouponruleid());
		paras.put("code", condition.getCcode());
		paras.put("startDate", condition.getStartDate());
		paras.put("endDate", condition.getEndDate());
		paras.put("exceptStatus", 1);
		List<CouponCode> list = couponCodeMapper.list(paras);
		Integer total = couponCodeMapper.getTotal(paras);
		Page<CouponCode> result = new Page<>(list, total, page, pageSize);
		return result;
	}

	public boolean del(Integer[] ids) {
		List<Integer> list = Arrays.asList(ids);
		int result = couponCodeMapper.del(list);
		return result > 0 ? true : false;
	}

	/**
	 * 私有方法,该方法是为后台管理coupon code管理页面添加code服务的
	 * 
	 * @param amount
	 * @param ruleId
	 * @param creatorId
	 *            后台用户id
	 * @return
	 */
	public boolean add(int amount, int ruleId, int creatorId) {
		int count = 0;
		// 获取站点id
		int siteId = fservice.getSiteID();
		CouponCode couponCode = new CouponCode();
		couponCode.setIcreator(creatorId);
		while (count < amount) {
			int result = this.createCodeIdByRuleId(ruleId, false, siteId,
					creatorId);
			if (result == 0) {
				return false;
			}
			count++;
		}
		return true;
	}

	/**
	 * 对外提供接口，生成Code
	 * 
	 * @param ruleId
	 *            规则ID
	 * @param status
	 *            true:已分配,false:未分配
	 * @param websiteId
	 *            站点id
	 * 
	 * @return 主键id,如果为0则失败
	 */
	public int getCodeIdByRuleId(int ruleId, boolean status, int websiteId,
			int creatorId) {
		// 所有外部调用都是分配出去的
		return this.createCodeIdByRuleId(ruleId, true, websiteId, creatorId);
	}

	/**
	 * 通过ruleId创建code
	 * 
	 * @author lijun
	 * @param ruleId
	 *            规则id
	 * @param status
	 *            true：新创建的code是要分配出去的 false：没有分配给人
	 *            分配和不分配人的区别是：分配出去的code在后台coupon code管理界面中不负责管理的
	 *            未分配出去的是需要在coupon code管理界面负责管理的
	 * @param websiteId
	 * @param creator
	 *            后台用户id
	 * @return
	 */
	private int createCodeIdByRuleId(int ruleId, boolean status, int websiteId,
			Integer creator) {
		int result = 0;
		CouponRule couponRule = couponRuleService.get(ruleId);
		if (null != couponRule
				&& couponRule.getIstatus() == CouponRuleBack.Status.ON
						.getStatusid()) {
			// 应用平台判断
			if (!org.apache.commons.lang3.StringUtils.isEmpty(couponRule
					.getCwebsiteid())) {
				String[] loginTerCheck = org.apache.commons.lang3.StringUtils
						.split(couponRule.getCwebsiteid(), ",");
				List<Integer> checks = new ArrayList<>(loginTerCheck.length);
				for (int i = 0; i < loginTerCheck.length; i++) {
					checks.add(Integer.parseInt(loginTerCheck[i]));
				}
				if (checks.contains(websiteId)) {

					CouponCode couponCode = new CouponCode();
					couponCode.setIcouponruleid(ruleId);
					couponCode.setCcode(String.valueOf(RandomNumberUtil
							.getRandomNumber()));
					// 为code 生成不同的使用状态
					if (status) {
						couponCode.setIusestatus(CouponCodeBack.UseStatus.ALLOT
								.getStatusid());
					} else {
						couponCode
								.setIusestatus(CouponCodeBack.UseStatus.UNALLOT
										.getStatusid());
					}
					// Integer creator = null;
					// 获取code创建人
					try {
						// creator = iAdminuserProvider.getCurrentUser();
						if (null != creator) {
							couponCode.setIcreator(creator);
						} else {
							couponCode.setIcreator(NULL_CREATEOR);
						}
					} catch (Exception e) {
						couponCode.setIcreator(NULL_CREATEOR);
					}
					int repeatTime = 0;
					do {
						try {
							couponCodeMapper.add(couponCode);
							result = couponCode.getIid();
						} catch (Exception e) {
							repeatTime++;
							Logger.error("Random number collision ! Repeat number="
									+ repeatTime);
							couponCode.setCcode(String.valueOf(RandomNumberUtil
									.getRandomNumber()));
						}
					} while (result <= 0 && repeatTime < GENERATE_TIME);

				}
			}

		}

		return result;

	}

	/**
	 * 根据登陆规则生成CODE
	 * 
	 * @return codeId,如果为0则失败
	 */
	public int getCodeIdByLogin(int websiteId) {
		// modify lijun
		CouponRule loginRule = this.couponRuleService.getLoginRule();
		return getCodeIdByRuleId(loginRule.getIid(), true, websiteId, 0);
	}

	/**
	 * 根据订阅规则生成CODE
	 * 
	 * @return codeId,如果为0则失败
	 */
	public int getCodeIdBySubscribe(int websiteId) {
		// modify by lijun
		CouponRule rule = this.couponRuleService.getSubscribeRule();
		return getCodeIdByRuleId(rule.getIid(), true, websiteId, 0);
	}

	/**
	 * 根据codeid查询业务对象 coupon code
	 * 
	 * @param couponRule
	 * @return
	 */
	public List<CouponCodeBo> getCouponCodesByCodeIds(List<Integer> codeIds) {
		List<CouponCodeBo> list = new ArrayList<CouponCodeBo>();
		if (null != codeIds && codeIds.size() > 0) {
			codeIds.forEach((Integer codeId) -> {
				CouponCode couponCode = couponCodeMapper
						.getCouponCodeByCodeId(codeId);
				if (null != couponCode) {
					Integer ruleId = couponCode.getIcouponruleid();
					if (null != ruleId) {
						CouponRule couponRule = couponRuleService
								.getCouponRuleByRuleId(ruleId);
						couponRule.setExcludeCategoryIds(couponRuleService
								.getTreeCheckByRuleId(ruleId));
						CouponCodeBo couponCodeBo = new CouponCodeBo();
						BeanUtils.copyProperties(couponCode, couponCodeBo);
						couponCodeBo.setCouponRule(couponRule);
						list.add(couponCodeBo);
					}
				}

			});
		}
		return list;
	}

	/**
	 * 通过code来查询
	 * 
	 * @author lijun
	 * @param code
	 * @return maybe return null
	 */
	public CouponCode getCouponCodeByCode(String code) {
		if (code == null || code.length() == 0) {
			return null;
		}
		return this.couponCodeMapper.getCouponCodeByCode(code);
	}

	/**
	 * 根据id查询code
	 * 
	 * @param id
	 * @return
	 */
	public String getCodeById(Integer id) {
		return couponCodeMapper.getCodeById(id);
	}

	/**
	 * 获取所有CouponCode的数量
	 * 
	 * @param couponCodeForm
	 * @return
	 */
	public Integer getCount(CouponCodeForm couponCodeForm) {
		return couponCodeMapper.getCount(couponCodeForm.getIcouponruleid(),
				couponCodeForm.getCcode());
	}

	/**
	 * 获取CouponCode的所有数据信息,实现分页功能
	 * 
	 * @return
	 */
	public List<CouponCode> getCouponCodes(CouponCodeForm couponCodeForm) {
		return couponCodeMapper.getCouponCodes(
				couponCodeForm.getIcouponruleid(), couponCodeForm.getCcode(),
				couponCodeForm.getPageSize(), couponCodeForm.getPageNum());
	}

	/**
	 * 获取CouponCode的所有数据信息
	 * 
	 * @return
	 */
	public List<CouponCode> getCouponCodesList() {
		return couponCodeMapper.getCouponCodeList();
	};
}
