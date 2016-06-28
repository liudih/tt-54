package extensions.tracking;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import mapper.order.DetailMapper;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.cart.ICartLifecycleService;
import services.product.CategoryEnquiryService;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import dto.order.Order;
import dto.order.OrderDetail;
import dto.product.CategoryWebsiteWithName;
import extensions.filter.FilterExecutionChain;
import extensions.filter.IFilter;
import extensions.payment.IPaymentHTMLPlugIn;

@Singleton
public class PiwikScriptAddition implements IFilter, IPaymentHTMLPlugIn {

	final public static String CONTEXT_VAR = "piwik";

	@Inject
	FoundationService foundation;

	@Inject
	SystemParameterService sysparam;

	@Inject
	CategoryEnquiryService categoryEnq;

	@Inject
	ICartLifecycleService cart;

	@Inject
	DetailMapper detailMapper;

	@Inject
	CurrencyService currency;

	@Override
	public int priority() {
		return 99;
	}

	@Override
	public Promise<Result> call(Context context, FilterExecutionChain chain)
			throws Throwable {

		try {
			int siteId = foundation.getSiteID(context);
			String piwik_url = sysparam.getSystemParameter(siteId, null,
					"piwik.url");
			int piwik_siteid = sysparam.getSystemParameterAsInt(siteId, null,
					"piwik.siteId", 1);
			if (piwik_url == null) {
				Logger.error("Piwik URL not configured in system parameter table");
				Logger.error("Please set 'piwik.url' and 'piwik.siteId'");
				return chain.executeNext(context);
			}

			Cookie aid = context.request().cookie(
					IAffiliateIDTracking.ORIGIN_STRING);

			if (aid != null) {
				addPiwikContextMap(context, "'setCustomVariable',1,'aid','"
						+ q(aid.value()) + "'");
			}

			if (foundation.getLoginContext(context).isLogin()) {
				String loginUser = foundation.getLoginContext(context)
						.getMemberID();
				addPiwikContextMap(context, "'setUserId', '" + q(loginUser)
						+ "'");
			}

			views.html.tracking.piwik.render(piwik_url, piwik_siteid, context);
		} catch (Exception e) {
			Logger.warn("Piwik Tracking Exception", e);
		}
		return chain.executeNext(context);
	}

	@Override
	public Html getHtml(Order order) {
		Context context = Context.current();
		List<OrderDetail> details = detailMapper.getOrderDetailByOrderId(order
				.getIid());
		if (order != null && details != null) {
			List<String> listingIds = Lists.newArrayList(Sets.newHashSet(Lists
					.transform(details, d -> d.getClistingid())));
			Multimap<String, CategoryWebsiteWithName> catItem = categoryEnq
					.getCategoriesByListingIdsGroupByListingId(listingIds, 1,
							order.getIwebsiteid());
			for (OrderDetail d : details) {
				Collection<CategoryWebsiteWithName> cs = catItem.get(d
						.getClistingid());
				addPiwikContextMap(
						context,
						"'addEcommerceItem','" + q(d.getCsku()) + "','"
								+ q(d.getCtitle()) + "',[" + categoryNames(cs)
								+ "],"
								+ inUSD(order.getCcurrency(), d.getFprice())
								+ "," + d.getIqty());
			}
			addPiwikContextMap(
					context,
					"'trackEcommerceOrder','"
							+ order.getIid()
							+ "',"
							+ inUSD(order.getCcurrency(),
									order.getFgrandtotal())
							+ ","
							+ inUSD(order.getCcurrency(),
									order.getFordersubtotal()));
		}
		return null;
	}

	@Override
	public int getDisplayOrder() {
		return 1000;
	}

	@SuppressWarnings("unchecked")
	public static List<String> ensureContext(Context context) {
		List<String> piwik = null;
		if (!context.args.containsKey(CONTEXT_VAR)) {
			piwik = Lists.newLinkedList();
			context.args.put(CONTEXT_VAR, piwik);
		} else {
			piwik = (List<String>) context.args.get(CONTEXT_VAR);
		}
		return piwik;
	}

	protected String categoryNames(Collection<CategoryWebsiteWithName> cs) {
		return StringUtils.join(FluentIterable.from(cs).filter(c -> c != null)
				.transform(c -> "'" + q(c.getCname()) + "'").limit(5), ",");
	}

	protected void addPiwikContextMap(Context context, String pushedTo) {
		List<String> cmds = ensureContext(context);
		cmds.add(pushedTo);
	}

	/**
	 * Escape single quote for JS string
	 *
	 * @param value
	 * @return
	 */
	protected String q(String value) {
		if (value != null) {
			return value.replaceAll("'", "\\'");
		}
		return value;
	}

	protected double inUSD(String ccy, double amount) {
		return currency.exchange(amount, ccy, "USD");
	}

	@Override
	public int getType() {
		return WAIT_PAY;
	}
}
