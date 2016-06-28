package services.campaign;

import java.util.List;

public class MultiRules<T> {

	public static enum Match {
		MATCH_ALL, MATCH_ONE
	}

	final List<T> rules;
	final Match match;

	public MultiRules(List<T> rules, Match match) {
		this.rules = rules;
		this.match = match;
	}

	public List<T> getRules() {
		return rules;
	}

	public Match getMatch() {
		return match;
	}

}
