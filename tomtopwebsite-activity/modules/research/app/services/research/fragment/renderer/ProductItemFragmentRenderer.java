package services.research.fragment.renderer;

import play.twirl.api.Html;
import services.research.vote.IVoteFragmentRenderer;
import valueobjects.research.vote.IVoteFragment;
import valueobjects.research.vote.VoteRenderContext;

public class ProductItemFragmentRenderer implements IVoteFragmentRenderer{

	@Override
	public Html render(IVoteFragment fragment, VoteRenderContext context) {
		return views.html.research.vote.product_item.render((valueobjects.research.vote.ProductItemFragment)fragment);
	}

}
