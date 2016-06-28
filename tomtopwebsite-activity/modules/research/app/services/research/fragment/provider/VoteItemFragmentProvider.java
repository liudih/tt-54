package services.research.fragment.provider;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import entity.activity.page.Page;
import entity.activity.page.PageItem;
import service.activity.IPageService;
import service.activity.IVoteRecordService;
import services.research.vote.IVoteFragmentProvider;
import valueobjects.research.vote.IVoteFragment;
import valueobjects.research.vote.VoteContext;
import valueobjects.research.vote.VoteItemFragment;
import values.activity.page.PageItemCount;

public class VoteItemFragmentProvider implements IVoteFragmentProvider {
	@Inject
	IPageService pageService;
	@Inject
	IVoteRecordService iVoteRecordService;

	public static final String NAME = "vote-item";

	public String getName() {
		return NAME;
	}

	@Override
	public IVoteFragment getFragment(VoteContext context, Page page) {
		VoteItemFragment vif = new VoteItemFragment();
		vif.setVoteItem("vote-item");
		List<PageItem> pageItemList = pageService.getPageItemByPageId(page
				.getIid());
		List<PageItemCount> countlist = iVoteRecordService.getPageAllItemCount(
				page.getIid(), context.getWebsiteId());
		Map<Integer, PageItemCount> itemcountmap = Maps.uniqueIndex(countlist,
				p -> p.getPageItemId());
		vif.setItemHitCount(itemcountmap);
		if (pageItemList != null && pageItemList.size() > 0) {
			for (PageItem pi : pageItemList) {
				String pageItemName = pageService.getPINameByPIIdAndLId(
						pi.getIid(), context.getLanguageId());
				pi.setPageItemName(pageItemName);
			}
			vif.setPageItemList(FluentIterable
					.from(pageItemList)
					.toSortedList(
							(p1, p2) -> {
								if (p1.getIpriority() != null
										&& p2.getIpriority() != null) {
									return p1.getIpriority().compareTo(
											p2.getIpriority());
								} else if (p2.getIpriority() != null
										|| p1.getIpriority() != null) {
									return -1;
								} else {
									return 0;
								}
							}).asList());
		}
		return vif;
	}

}
