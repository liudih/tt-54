package valueobjects.research.vote;

import java.util.List;
import java.util.Map;

import values.activity.page.PageItemCount;
import entity.activity.page.PageItem;

public class VoteItemFragment implements IVoteFragment {
	private String voteItem;

	private List<PageItem> pageItemList;

	Map<Integer, PageItemCount> itemHitCount;

	public String getVoteItem() {
		return voteItem;
	}

	public void setVoteItem(String voteItem) {
		this.voteItem = voteItem;
	}

	public List<PageItem> getPageItemList() {
		return pageItemList;
	}

	public void setPageItemList(List<PageItem> pageItemList) {
		this.pageItemList = pageItemList;
	}

	public Map<Integer, PageItemCount> getItemHitCount() {
		return itemHitCount;
	}

	public void setItemHitCount(Map<Integer, PageItemCount> itemHitCount) {
		this.itemHitCount = itemHitCount;
	}

}
