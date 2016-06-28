package base.util.httpapi;

import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;

import com.fasterxml.jackson.databind.JsonNode;


public class ApiUtil {
	public String get(String url) {

		WSRequestHolder wsRequest = WS.url(url).setHeader("Content-Type",
				"application/json");
		Promise<String> resultStr = wsRequest.get().map(response -> {
			return response.getBody();
		});
		return resultStr.get(100000);
	}
	
	public String post(String url,JsonNode objjson) {

		WSRequestHolder wsRequest = WS.url(url).setHeader("Content-Type",
				"application/json");
		Promise<String> resultStr = wsRequest.post(objjson).map(response -> {
			return response.getBody();
		});
		return resultStr.get(100000);
	}

}
