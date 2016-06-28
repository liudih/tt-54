package service.report.tag.fragement;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import service.affiliate.DateRange;
import service.tracking.IAffiliateService;
import services.base.template.ITemplateFragmentProvider;
import services.member.login.LoginService;
import services.order.IOrderEnquiryService;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.google.common.collect.Multimaps;

import dto.order.Order;

public class TrasactionTotalTagFragment implements ITemplateFragmentProvider {

	@Override
	public String getName() {

		return "trasaction-total-tag";
	}

	@Inject
	LoginService loginService;

	@Inject
	IAffiliateService affiliateService;

	@Inject
	IOrderEnquiryService orderService;

	@Override
	public Html getFragment(Context context) {
		String email = loginService.getLoginData().getEmail();
		String aid = affiliateService.getAidByEmail(email);
		DateRange range = new DateRange(-30);
		List<Order> orders = orderService.getOrdersByDateRange(aid,
				range.getBegin(), range.getEnd());

		ListMultimap<String, Integer> orderMultimap = Multimaps
				.transformValues(FluentIterable.from(orders).index(k -> {
					return DateRange.format(k.getDcreatedate());
				}), v -> {

					return 1;

				});

		Map<String, Integer> map = Maps.transformEntries(orderMultimap.asMap(),
				new EntryTransformer<String, Collection<Integer>, Integer>() {

					@Override
					public Integer transformEntry(String key,
							Collection<Integer> values) {
						int tmp = 0;
						for (Integer it : values) {
							tmp += it;
						}
						return tmp;
					}
				});

		int total = 0;
		for (Integer item : map.values()) {
			total += item;
		}
		return views.html.report.tag.fragement.trasaction_total_tag
				.render(total);
	}

}
