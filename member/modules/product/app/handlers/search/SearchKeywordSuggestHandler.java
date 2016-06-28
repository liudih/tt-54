package handlers.search;

import java.util.Date;

import javax.inject.Inject;

import mapper.search.KeywordSuggestMapper;

import com.google.common.eventbus.Subscribe;

import dto.search.KeywordSuggest;
import events.search.KeywordSearchEvent;

public class SearchKeywordSuggestHandler {

	static final int RANDK_VALUE = 1;

	@Inject
	KeywordSuggestMapper suggestMapper;

	@Subscribe
	public void onKeywordSearched(KeywordSearchEvent event) {
		if (event.getResults().totalCount() > 0) {
			String keyWord = event.getKeyword().toLowerCase();
			int languageid = event.getLanguageID();
			int websiteid = event.getWebsiteID();
			int result = event.getResults().totalCount();
			KeywordSuggest suggest = suggestMapper.getKeywordSuggetByKeyWord(
					keyWord, websiteid, languageid);
			if (suggest != null) {
				int rank = suggest.getIrank();
				suggest.setIrank(rank + RANDK_VALUE);
				suggest.setIresults(result);
				suggestMapper.updateByPrimaryKey(suggest);
			} else {

				suggest = new KeywordSuggest();
				suggest.setIresults(result);
				suggest.setCkeyword(keyWord);
				suggest.setIlanguageid(languageid);
				suggest.setIwebsiteid(websiteid);
				suggest.setDcreatedate(new Date());
				suggest.setIrank(RANDK_VALUE);
				suggestMapper.insert(suggest);
			}

		}
	}
}
