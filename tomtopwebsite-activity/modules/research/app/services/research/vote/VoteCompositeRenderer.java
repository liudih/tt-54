package services.research.vote;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import play.twirl.api.Html;
import valueobjects.research.vote.IVoteFragment;
import valueobjects.research.vote.VoteComposite;
import valueobjects.research.vote.VoteRenderContext;

public class VoteCompositeRenderer {
	final Map<String, IVoteFragmentRenderer> renderers;
	
	@Inject
	public VoteCompositeRenderer(final Set<IVoteFragmentPlugin> fragmentPlugins){
		this.renderers = new HashMap<String, IVoteFragmentRenderer>();
		for(IVoteFragmentPlugin r : fragmentPlugins){
			IVoteFragmentRenderer renderer = r.getFragmentRenderer();
			if(null != renderer){
				renderers.put(r.getName(), renderer);
			}
		}
	}
	
	public Html renderFragment(VoteComposite composite, String name){
		IVoteFragment fragment = composite.get(name);
		VoteRenderContext ctx = new VoteRenderContext(composite,this);
		IVoteFragmentRenderer renderer = renderers.get(name);
		if(null != renderer){
			return renderer.render(fragment, ctx);
		}
		return null;
	}
}
