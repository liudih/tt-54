package factory.common;

import java.net.URL;

import services.mobile.MobileService;
import valuesobject.mobile.member.MobileLoginContext;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;

import context.WebContext;
import extensions.InjectorInstance;

public class CookieProxy extends HessianProxy {

	private static final long serialVersionUID = 1L;

	public CookieProxy(URL url, HessianProxyFactory factory, Class<?> type) {
		super(url, factory, type);
	}

	@Override
	protected void addRequestHeaders(HessianConnection conn) {
		super.addRequestHeaders(conn);
		MobileService mobileService = InjectorInstance
				.getInstance(MobileService.class);
		WebContext context = mobileService.getWebContext();
		StringBuffer sb = new StringBuffer();
		sb.append("TT_LANG=").append(mobileService.getLanguageID() + "")
				.append(";");
		sb.append("TT_DEVICE=").append(context.getVhost()).append(";");
		sb.append("TT_LTC=").append(context.getLtc()).append(";");
		sb.append("TT_STC=").append(mobileService.getUUID());
		// 新登录接口添加
		MobileLoginContext mcontext = mobileService.getMLoginContext();
		if (mcontext != null) {
			sb.append("TT_TOKEN=").append(mcontext.getCookieToken());
			sb.append("TT_UUID=").append(mcontext.getCookieUUID());
		}
		conn.addHeader("Cookie", sb.toString());
	}
}
