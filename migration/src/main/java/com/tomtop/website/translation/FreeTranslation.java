package com.tomtop.website.translation;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.FluentIterable;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * See www.freetranslation.com
 * 
 * @author kmtong
 *
 */
public class FreeTranslation {

	final static String url = "http://www.freetranslation.com/gw-mt-proxy-service-web/mt-translation";
	String TERM_SEP = "~~!";

	public String translate(String text, Language from, Language to)
			throws Exception {
		ObjectNode node = JsonNodeFactory.instance.objectNode();
		node.put("text", URLEncoder.encode(text, "UTF-8"));
		node.put("from", from.iso3());
		node.put("to", to.iso3());
		JsonNode send = new JsonNode(node.toString());
		HttpResponse<JsonNode> response = Unirest
				.post(url)
				.header("Content-Type", "application/json")
				.header("Tracking",
						"applicationKey=dlWbNAC2iLJWujbcIHiNMQ%3D%3D applicationInstance=freetranslation")
				.header("Accept", "application/json").body(send).asJson();
		try {
			return response.getBody().getObject().getString("translation");
		} catch (JSONException e) {
			System.err.println("Response: " + response.getBody());
			return text;
		}
	}

	public String[] translate(String[] text, Language from, Language to)
			throws Exception {
		String joined = String.join(" " + TERM_SEP + " ", text);
		String joinedTrans = translate(joined, from, to);
		return FluentIterable.from(Arrays.asList(joinedTrans.split(TERM_SEP)))
				.transform(s -> s.trim()).toArray(String.class);
	}

	public Document translate(Document doc, Language from, Language to)
			throws Exception {
		Document translated = doc.clone();
		StringBuilder sb = new StringBuilder();
		for (Node elm : translated.childNodes()) {
			processElement(elm, sb);
		}
		System.out.println("Translating: " + sb.toString());
		String txl = translate(sb.toString(), from, to);
		System.out.println("Translated: " + txl);
		List<String> tokens = new LinkedList<String>(FluentIterable
				.from(Arrays.asList(txl.split(TERM_SEP)))
				.transform(s -> s.trim()).toList());
		for (Node elm : translated.childNodes()) {
			decodeElement(elm, tokens);
		}
		return translated;
	}

	public String translateHtml(String html, Language from, Language to)
			throws Exception {
		Document doc = Jsoup.parseBodyFragment(html);
		Document txl = translate(doc, from, to);
		return txl.body().html();
	}

	protected void decodeElement(Node elm, List<String> tokens) {
		if (tokens.size() > 0) {
			if (elm instanceof TextNode) {
				TextNode tn = (TextNode) elm;
				tn.text(tokens.get(0));
				tokens.remove(0);
			}
			for (Node c : elm.childNodes()) {
				decodeElement(c, tokens);
			}
		}
	}

	protected void processElement(Node elm, StringBuilder sb) {
		if (elm instanceof TextNode) {
			TextNode tn = (TextNode) elm;
			appendTerm(sb, tn.text());
		}
		for (Node c : elm.childNodes()) {
			processElement(c, sb);
		}
	}

	protected void appendTerm(StringBuilder sb, String text) {
		sb.append(text);
		sb.append(" " + TERM_SEP + " ");
	}
}
