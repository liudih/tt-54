package controllers.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import dto.search.KeywordSearchLog;
import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.search.IKeyWordSuggestService;

public class KeyWord extends Controller{

	@Inject
	IKeyWordSuggestService keywordSuggestService;
	
	/**
	 * 保存搜索关键词
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result push() {
		try {
			Logger.debug("----" + request().body().asJson());
			JsonNode node = request().body().asJson();
			if (node == null) {
				return badRequest("Expecting Json data");
			}
			this.saveKeywordBase(node);
			return ok("successfully");
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	public void saveKeywordBase(JsonNode node) {
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				KeywordSearchLog keywordSearchLog = _initKeywordSearchLog(nodeiterator
						.next());
				keywordSuggestService.saveKeywordSearchLog(keywordSearchLog);
			}
		} else {
			KeywordSearchLog keywordSearchLog = _initKeywordSearchLog(node);
			keywordSuggestService.saveKeywordSearchLog(keywordSearchLog);
		}
	}

	private KeywordSearchLog _initKeywordSearchLog(JsonNode node) {
		KeywordSearchLog keywordSearchLog = new KeywordSearchLog();

		keywordSearchLog.setCkeyword(node.get("query_text").asText());
		keywordSearchLog.setCip(null);
		keywordSearchLog.setCltc(null);
		keywordSearchLog.setCstc(null);
		keywordSearchLog.setIlanguageid(0);
		keywordSearchLog.setIresults(node.get("num_results").asInt());
		keywordSearchLog.setIwebsiteid(node.get("store_id").asInt());
		keywordSearchLog.setDcreatedate(parseToTime(node.get("updated_at")
				.asLong()));

		return keywordSearchLog;
	}

	public Date parseToTime(long val) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time = new Long(val);
		String d = format.format(time);
		try {
			return format.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
