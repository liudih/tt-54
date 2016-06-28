package services.research.vote;

import java.util.Set;

import javax.inject.Inject;

import entity.activity.page.Page;
import valueobjects.research.vote.VoteComposite;
import valueobjects.research.vote.VoteContext;

public class VoteCompositeEnquiry {
	
	@Inject
	Set<IVoteFragmentPlugin> fragmentPlugins;
	
	public VoteComposite getVoteComposite(VoteContext context,Page page) {
		VoteComposite composite = new VoteComposite(context);
		for (IVoteFragmentPlugin fp : fragmentPlugins) {
			IVoteFragmentProvider provider = fp.getFragmentProvider();
			if (provider != null) {
				composite.put(fp.getName(), provider.getFragment(context,page));
			}
		}
		return composite;
	}
}
