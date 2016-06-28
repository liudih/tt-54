package services.research.vote;

import play.twirl.api.Html;
import valueobjects.research.vote.IVoteFragment;
import valueobjects.research.vote.VoteRenderContext;

public interface IVoteFragmentRenderer {
	
	Html render(IVoteFragment fragment, VoteRenderContext context);

}
