package com.tomtop.product.services.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tomtop.product.models.bo.CurrencyBo;
import com.tomtop.product.models.bo.ProductPriceRuleBo;
import com.tomtop.product.services.IProductPriceRule;
import com.tomtop.search.entiry.PromotionPrice;

@Service("productPriceRule")
public class ProductPriceRuleImpl implements IProductPriceRule {

	@Override
	public ProductPriceRuleBo getPrice(Double constPrice, Double price,
			List<PromotionPrice> promsPrice, CurrencyBo currency) {
		price = price >= constPrice ? price : constPrice;
		price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		constPrice = new BigDecimal(constPrice).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		ProductPriceRuleBo bo = new ProductPriceRuleBo();
		bo.setOriginalPrice(new BigDecimal(price)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		double dprice = 0.0;
		if (promsPrice != null && promsPrice.size() > 0) {
			for (PromotionPrice p : promsPrice) {
				try {
					Date begin = DateUtils.parseDate(p.getBeginDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date end = DateUtils.parseDate(p.getEndDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date day = DateUtils.parseDate(DateFormatUtils.formatUTC(
							new Date(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd HH:mm:ss");
					if (day.before(end) && day.after(begin)) {
						dprice = p.getPrice();
						break;
					}
				} catch (Exception e) {
					LoggerFactory.getLogger(this.getClass()).error(
							"转换折扣价日期失败,errormsg:", e.getMessage());
				}
			}
		}
		if (dprice == 0.0) {
			dprice = price;
		} else {
			if (dprice > price) {
				dprice = price;
			} else if (dprice < constPrice) {
				dprice = constPrice;
			}
		}
		bo.setPrice(new BigDecimal(dprice)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		return bo;
	}
	
	@Override
	public ProductPriceRuleBo getPriceEndDate(Double constPrice, Double price,
			List<PromotionPrice> promsPrice, CurrencyBo currency) {
		price = price >= constPrice ? price : constPrice;
		price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		constPrice = new BigDecimal(constPrice).setScale(2,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		ProductPriceRuleBo bo = new ProductPriceRuleBo();
		bo.setOriginalPrice(new BigDecimal(price)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		double dprice = 0.0;
		String endDate = "";
		if (promsPrice != null && promsPrice.size() > 0) {
			for (PromotionPrice p : promsPrice) {
				try {
					Date begin = DateUtils.parseDate(p.getBeginDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date end = DateUtils.parseDate(p.getEndDate(),
							"yyyy-MM-dd HH:mm:ss");
					Date day = DateUtils.parseDate(DateFormatUtils.formatUTC(
							new Date(), "yyyy-MM-dd HH:mm:ss"),
							"yyyy-MM-dd HH:mm:ss");
					if (day.before(end) && day.after(begin)) {
						dprice = p.getPrice();
						endDate = p.getEndDate();
						break;
					}
				} catch (Exception e) {
					LoggerFactory.getLogger(this.getClass()).error(
							"转换折扣价日期失败,errormsg:", e.getMessage());
				}
			}
		}
		if (dprice == 0.0) {
			dprice = price;
		} else {
			if (dprice > price) {
				dprice = price;
			} else if (dprice < constPrice) {
				dprice = constPrice;
			}
		}
		bo.setPrice(new BigDecimal(dprice)
				.multiply(new BigDecimal(currency.getCurrentRate()))
				.setScale(currency.getCode().equals("JPY") ? 0 : 2,
						BigDecimal.ROUND_HALF_UP).toString());
		bo.setEndDate(endDate);
		return bo;
	}
}
