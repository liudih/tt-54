package utils;

import java.io.IOException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;


/**
 * HTTP请求工具类
 */
public class HttpSendRequest {


	/**
	 * 发送Get请求
	 *
	 * @param urlString  请求地址
	 *
	 * @throws IOException
	 */
	public static String sendGet(String urlString) {

		String result = "";
		try {
			NetHttpTransport transport = new NetHttpTransport();

			GenericUrl url = new GenericUrl(urlString);

			HttpRequest request = transport.createRequestFactory()
					.buildGetRequest(url);

			result = request.execute().parseAsString();

		} catch (IOException e) {
			result = e.getMessage();
			e.printStackTrace();
		}

		return result ;
	}
}
