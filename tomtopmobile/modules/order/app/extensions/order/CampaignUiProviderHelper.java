package extensions.order;

import java.util.Map;
import java.util.Set;

import play.Logger;
import play.twirl.api.Html;
import valueobjects.order_api.cart.ExtraLine;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import extensions.InjectorInstance;
import facades.cart.Cart;

/**
 * Helper
 * 
 * @author lijun
 *
 */
@Singleton
public class CampaignUiProviderHelper {

	Set<CampaignUiProvider> provider;

	Map<String, CampaignUiProvider> map;

	@Inject
	public CampaignUiProviderHelper(Set<CampaignUiProvider> provider) {
		this.provider = provider;
		this.map = Maps.newHashMap();
		FluentIterable.from(provider).forEach(p -> {
			map.put(p.getName(), p);
		});
	}

	/**
	 * 
	 * @param name
	 * @return maybe return null
	 */
	public CampaignUiProvider get(String name) {
		return map.get(name);
	}

	/**
	 * 绘制某个Provider
	 * 
	 * @param name
	 * @return if name is not find return ""
	 */
	public Html render(String name, Cart cart) {
		CampaignUiContext ctx = new CampaignUiContext();
		ctx.setCartId(cart.getId());

		CampaignUiProvider p = this.get(name);

		if (p == null) {
			return Html.apply("");
		} else {
			try {
				Map<String, ExtraLine> extraLines = cart.getExtraLines();
				ExtraLine el = extraLines.get(p.getExtraLineId());
				ctx.setExtraLine(el);

				Html html = p.render(ctx);
				return html;
			} catch (Exception e) {
				Logger.debug("{} provider render error", name, e);
				return Html.apply("");
			}

		}
	}

	public Html renderExtraLine(String name, Cart cart) {
		CampaignUiContext ctx = new CampaignUiContext();
		ctx.setCartId(cart.getId());

		CampaignUiProvider p = this.get(name);

		if (p == null) {
			return Html.apply("");
		} else {
			try {
				Map<String, ExtraLine> extraLines = cart.getExtraLines();
				ExtraLine el = extraLines.get(p.getExtraLineId());
				ctx.setExtraLine(el);

				Html html = p.renderExtraLine(ctx);
				return html;
			} catch (Exception e) {
				Logger.debug("{} provider render error", name, e);
				return Html.apply("");
			}

		}
	}

	/**
	 * 方便在html里面调用
	 * 
	 * @return
	 */
	public static CampaignUiProviderHelper newInstance() {
		return InjectorInstance.getInjector().getInstance(
				CampaignUiProviderHelper.class);
	}
}
