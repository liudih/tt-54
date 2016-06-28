package services.configuration;

import java.util.Arrays;
import java.util.List;

import com.google.api.client.util.Lists;

import play.Play;
import extensions.InjectorInstance;

public class ApplicationConfigService {

	public String getString(String pathName) {
		return Play.application().configuration().getString(pathName);
	}

	/**
	 * 获取css各个品牌网站的basic.css的路径路径
	 * 
	 * @return
	 */
	public static String _getCssPath() {
		return _getCurrentWebsiteName();
	}

	/**
	 * 获取各个品牌网站logo图片路径
	 * 
	 * @return
	 */
	public static String _getLogoImagePath() {
		return _getCurrentWebsiteName();
	}

	/**
	 * 首页底部facebook分享链接
	 * 
	 * @return
	 */
	public static String _getShareUrlFacebook() {
		String pathName = "share.url.facebook";
		return InjectorInstance.getInstance(ApplicationConfigService.class)
				.getString(pathName);
	}

	/**
	 * 首页底部youtube分享链接
	 * 
	 * @return
	 */
	public static String _getShareUrlYoutube() {
		String pathName = "share.url.youtube";
		return InjectorInstance.getInstance(ApplicationConfigService.class)
				.getString(pathName);
	}

	/**
	 * 首页底部twitter分享链接
	 * 
	 * @return
	 */
	public static String _getShareUrlTwitter() {
		String pathName = "share.url.twitter";
		return InjectorInstance.getInstance(ApplicationConfigService.class)
				.getString(pathName);
	}

	public static String _getCurrentWebsiteName() {
		String pathName = "current.website.name";
		return InjectorInstance.getInstance(ApplicationConfigService.class)
				.getString(pathName);
	}

	public static String _getFooterCopyright() {
		String pathName = "copyright";
		return InjectorInstance.getInstance(ApplicationConfigService.class)
				.getString(pathName);
	}

	public static String _getHotlineContactInformation() {
		String pathName = "hotline.contact.information";
		return InjectorInstance.getInstance(ApplicationConfigService.class)
				.getString(pathName);
	}

	public List<String> getThridLogins() {
		String pathName = "thrid.login";
		String loginvalues = this.getString(pathName);
		if (null != loginvalues) {
			return Arrays.asList(loginvalues.split(","));
		}
		return Lists.newArrayList();
	}

}
