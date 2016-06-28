package valueobjects.research.vote;

import play.twirl.api.Html;
import services.research.vote.VoteCompositeRenderer;

public class VoteRenderContext {
	VoteComposite composite;
	VoteCompositeRenderer renderer;
	
	public VoteRenderContext(VoteComposite composite,VoteCompositeRenderer renderer){
		super();
		this.composite = composite;
		this.renderer = renderer;
	}
	
	public Html renderFragment(String name) {
		return renderer.renderFragment(composite, name);
	}
	
	public IVoteFragment getFragment(String name) {
		return composite.get(name);
	}

	public Object getAttribute(String name) {
		return composite.getAttributes().get(name);
	}
}
