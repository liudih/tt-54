package services.research.fragment.renderer;

import play.twirl.api.Html;
import services.research.vote.IVoteFragmentRenderer;
import valueobjects.research.vote.IVoteFragment;
import valueobjects.research.vote.VoteRenderContext;

public class VoteItemFragmentRenderer implements IVoteFragmentRenderer{

	@Override
	public Html render(IVoteFragment fragment, VoteRenderContext context) {
		return views.html.research.vote.vote_item.render((valueobjects.research.vote.VoteItemFragment)fragment);
	}
	
}
