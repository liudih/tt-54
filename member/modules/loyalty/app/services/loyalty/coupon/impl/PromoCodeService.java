package services.loyalty.coupon.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.libs.Json;
import services.base.FoundationService;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.cart.IHandleCartRefreshEventPlugin;
import services.loyalty.coupon.CouponRuleService;
import services.loyalty.coupon.ICouponMainService;
import services.loyalty.coupon.IPromoCodeService;
import valueobjects.base.Page;
import valueobjects.order_api.ParValue;
import valueobjects.order_api.cart.ExtraLine;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Maps;
import com.google.inject.Inject;

import context.WebContext;
import dao.loyalty.coupon.IPromoCodeDao;
import entity.loyalty.PromoCode;
import extensions.loyalty.campaign.coupon.PromoCodeUseAction;
import extensions.loyalty.campaign.rule.memberactive.ConponActionRule;
import facades.cart.Cart;

/**
 * 推广码
 * 
 * @author lijun
 *
 */
public class PromoCodeService implements IPromoCodeService,
		IHandleCartRefreshEventPlugin {

	@Inject
	IPromoCodeDao dao;

	@Inject
	CouponRuleService ruleService;

	@Inject
	CampaignContextFactory ctxFactory;

	@Inject
	ICouponMainService couponService;
	
	@Inject
	FoundationService foundationService;

	@Override
	public int valid(String code) {
		return this.dao.valid(code);
	}

	@Override
	public ParValue getParValue(String code) {
		return this.dao.getParValue(code);
	}

	@Override
	public int updateStatus(String code, Integer state) {
		return this.dao.updateStatus(code, state);
	}

	@Override
	public Page<PromoCode> selectForPage(int page, int pageSize) {
		Map<String, Object> paras = new HashMap<String, Object>(2);
		paras.put("page", page);
		paras.put("pageSize", pageSize);

		List<PromoCode> list = this.dao.select(paras);
		int total = this.dao.getTotal(null);
		return new Page<PromoCode>(list, total, page, pageSize);
	}

	@Override
	public int add(PromoCode code) {
		if (code == null) {
			return 0;
		}
		return this.dao.add(code);
	}

	@Override
	public List<PromoCode> search(Map paras) {
		List<PromoCode> list = this.dao.select(paras);
		return list;
	}

	@Override
	public int getTotal() {
		return this.dao.getTotal(null);
	}

	@Override
	public PromoCode selectPromoCodeByCode(String code) {
		if (code == null || code.length() == 0) {
			return null;
		}
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("code", code);
		List<PromoCode> list = this.dao.select(paras);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public void handleCartRefreshEvent(Cart cart) {
		Map<String, ExtraLine> el = cart.getExtraLines();
		ExtraLine couponLine = el.get(PromoCodeUseAction.ID);
		if (couponLine != null) {
			String payload = couponLine.getPayload();
			JsonNode payloadJson = Json.parse(payload);
			JsonNode codeNode = payloadJson.get("code");
			if (codeNode != null) {
				String code = codeNode.asText();
				boolean isMatch = false;
				ConponActionRule actionRule = couponService.getActionRuleForPormoCode(code);
				if(actionRule != null){
					CampaignContext context = ctxFactory.createContext(null, null);
					// 把pc端的上下文设置进去
					WebContext webContext = foundationService.getWebContext();
					context.setWebContext(webContext);
					context.setActionOn(cart);
					isMatch = actionRule.match(context, null);
				}
				
				// 如果不匹配rule了则需要把该购物车的优惠券移除
				if (!isMatch) {
					Logger.debug(
							"^^^^^^^^^^^^^^^^^^^^购物车：{} 里面的推广码{} 不再符合规则,需移除该推广码",
							cart.getId(), code);
					// 清除额外行
					cart.delExtraLine(PromoCodeUseAction.ID);
					Logger.debug("^^^^^^^^^^^^^^^^^^^^移除成功");
				}
			}
		}
	}

	@Override
	public boolean isUsed(String email, String code) {
		Map<String, Object> paras = Maps.newHashMap();
		paras.put("email", email);
		paras.put("code", code);
		boolean result = this.dao.isUsed(paras);
		return result;
	}
}
