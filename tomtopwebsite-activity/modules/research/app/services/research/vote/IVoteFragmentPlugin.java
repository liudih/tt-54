package services.research.vote;

public interface IVoteFragmentPlugin {
	String getName();
	
	IVoteFragmentProvider getFragmentProvider();
	
	IVoteFragmentRenderer getFragmentRenderer();
}
