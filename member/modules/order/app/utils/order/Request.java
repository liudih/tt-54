package utils.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Request {

	private static ThreadLocal<HttpServletRequest> requests = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> responses = new ThreadLocal<HttpServletResponse>();

	public static HttpServletRequest currentRequest() {
		return requests.get();
	}

	public static void setRequest(HttpServletRequest request) {
		requests.set(request);
	}

	public static HttpServletResponse currentResponse() {
		return responses.get();
	}

	public static void setResponse(HttpServletResponse response) {
		responses.set(response);
	}
	
	public static void destroy(){
		requests.remove();
		responses.remove();
	}
}
