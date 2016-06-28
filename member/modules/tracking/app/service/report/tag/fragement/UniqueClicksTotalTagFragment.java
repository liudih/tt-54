package service.report.tag.fragement;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import service.affiliate.DateRange;
import service.tracking.IAffiliateService;
import service.tracking.IVisitLogService;
import services.base.template.ITemplateFragmentProvider;
import services.member.login.LoginService;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.google.common.collect.Multimaps;

import dto.VisitLog;

public class UniqueClicksTotalTagFragment implements ITemplateFragmentProvider {

	@Override
	public String getName() {

		return "unique-clicks-total-tag";
	}

	@Inject
	LoginService loginService;

	@Inject
	IAffiliateService affiliateService;

	@Inject
	IVisitLogService visitLogService;

	@Override
	public Html getFragment(Context context) {

		String email = loginService.getLoginData().getEmail();
		String aid = affiliateService.getAidByEmail(email);
		DateRange range = new DateRange(-30);
		List<VisitLog> logs = visitLogService.getVisitLogByDateRange(aid,
				range.getBegin(), range.getEnd());
		ListMultimap<String, VisitLog> multimap = Multimaps.transformValues(
				FluentIterable.from(logs).index(k -> {
					return DateRange.format(k.getDcreatedate());
				}), v -> v);

		Collection<Integer> colle = Maps.transformEntries(multimap.asMap(),
				new EntryTransformer<String, Collection<VisitLog>, Integer>() {

					@Override
					public Integer transformEntry(String key,
							Collection<VisitLog> values) {

						ListMultimap<String, Integer> multimap = Multimaps
								.transformValues(FluentIterable.from(values)
										.index(k -> {
											return k.getCip();
										}), v -> {
									return 1;
								});

						return multimap.asMap().keySet().size();
					}

				}).values();
		int total = 0;
		for (Integer i : colle) {
			total += i;
		}

		return views.html.report.tag.fragement.unique_clicks_total_tag
				.render(total);
	}

}
