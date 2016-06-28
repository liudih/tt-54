package extensions.research;

import services.research.vote.IVoteFragmentPlugin;
import com.google.inject.multibindings.Multibinder;
import extensions.IExtensionPoint;

public interface IVoteFragmentExtension extends IExtensionPoint{
	
	public void registerVoteFragment(
			Multibinder<IVoteFragmentPlugin> plugins);

}
