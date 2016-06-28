package valueobjects.research.vote;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class VoteComposite {
	final Map<String, IVoteFragment> fragmentsMap = new HashMap<String, IVoteFragment>();

	final Map<String, Object> attributes = new LinkedHashMap<String, Object>();
	
	VoteContext voteContext;
	
	public VoteComposite(VoteContext voteContext){
		this.voteContext = voteContext;
	}
	
	public void put(String name, IVoteFragment fragment) {
		fragmentsMap.put(name, fragment);
	}

	public IVoteFragment get(String name) {
		return fragmentsMap.get(name);
	}

	public Set<String> keySet() {
		return fragmentsMap.keySet();
	}

	public Map<String, IVoteFragment> getFragmentsMap() {
		return fragmentsMap;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public VoteContext getVoteContext() {
		return voteContext;
	}

	public void setVoteContext(VoteContext voteContext) {
		this.voteContext = voteContext;
	}
	
	
}
