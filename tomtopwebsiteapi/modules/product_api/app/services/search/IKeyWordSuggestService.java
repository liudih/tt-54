package services.search;

import java.util.List;

import dto.search.KeywordSearchLog;
import dto.search.KeywordSuggest;
import valueobjects.search.Suggestion;

public interface IKeyWordSuggestService {
	
	 List<Suggestion> getSuggestions(final String partial,
			final int siteId, final int languageId);
	
	 List<KeywordSuggest> getHotProductSuggestions(final int siteId,
			final int languageId);
	 
	 Integer saveKeywordSearchLog(KeywordSearchLog keywordSearchLog);
}
