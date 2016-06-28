package handlers.search;

import javax.inject.Inject;

import mapper.search.KeywordSearchLogMapper;

import com.google.common.eventbus.Subscribe;

import dto.search.KeywordSearchLog;
import events.search.KeywordSearchEvent;

public class SearchLogHandler {

	@Inject
	KeywordSearchLogMapper mapper;

	@Subscribe
	public void onKeywordSearched(KeywordSearchEvent event) {
		KeywordSearchLog log = new KeywordSearchLog();
		log.setCip(event.getIp());
		log.setCkeyword(event.getKeyword());
		log.setCltc(event.getLTC());
		log.setCstc(event.getSTC());
		log.setDcreatedate(event.getTimestamp());
		log.setIlanguageid(event.getLanguageID());
		log.setIresults(event.getResults().totalCount());
		log.setIwebsiteid(event.getWebsiteID());
		mapper.insert(log);

	}
}
