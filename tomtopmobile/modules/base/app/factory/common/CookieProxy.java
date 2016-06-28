package factory.common;

import java.net.URL;
import java.util.Map;

import play.mvc.Http.Context;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;

public class CookieProxy extends HessianProxy {

	private static final long serialVersionUID = 1L;

	public CookieProxy(URL url, HessianProxyFactory factory, Class<?> type) {
		super(url, factory, type);
	}

	@Override
	protected void addRequestHeaders(HessianConnection conn) {
		super.addRequestHeaders(conn);
		Context ctx = Context.current();

		Map<String, String[]> headers = ctx.request().headers();
		String[] cookie = headers.get("Cookie");
		if (cookie != null) {
			String value = String.join(",", cookie);
			conn.addHeader("Cookie", value);
		}

		// FluentIterable.from(headers.keySet()).forEach(k -> {
		// String[] values = headers.get(k);
		// String value = String.join(",", values);
		// if ("Cookie".equals(k)) {
		// conn.addHeader(k, value);
		// }
		// });

	}
}
