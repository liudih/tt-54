package services.research.vote;

import entity.activity.page.Page;
import valueobjects.research.vote.IVoteFragment;
import valueobjects.research.vote.VoteContext;

public interface IVoteFragmentProvider {

	public IVoteFragment getFragment(VoteContext context, Page page);
}
