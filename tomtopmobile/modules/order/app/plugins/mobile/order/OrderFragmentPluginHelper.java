package plugins.mobile.order;

import java.util.Map;
import java.util.Set;

import play.twirl.api.Html;
import services.base.FoundationService;
import valueobjects.order_api.OrderContext;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class OrderFragmentPluginHelper {

	private final Map<String, IOrderFragmentPlugin> plugins;

	@Inject
	FoundationService foundationService;

	@Inject
	public OrderFragmentPluginHelper(Set<IOrderFragmentPlugin> plugins) {
		this.plugins = Maps.uniqueIndex(plugins, p -> p.getName());
	}

	/**
	 * 获取某个插件
	 * 
	 * @param pluginName
	 * @return if not find return null
	 */
	public IOrderFragmentPlugin getPlugin(String pluginName) {
		if (plugins.containsKey(pluginName)) {
			return this.plugins.get(pluginName);
		}

		return null;

	}

	/**
	 * 获取所有的plugin
	 * 
	 * @return
	 */
	public Map<String, IOrderFragmentPlugin> getAllPlugin() {
		return this.plugins;
	}

	/**
	 * 绘制pluginName插件
	 * 
	 * @param pluginName
	 * @param context
	 * @return
	 */
	public Html renderPlugin(String pluginName, OrderContext context) {
		if (pluginName == null || pluginName.length() == 0) {
			return Html.apply("");
		}

		IOrderFragmentPlugin plugin = this.getPlugin(pluginName);
		if (plugin == null) {
			return Html.apply("");
		}
		return plugin.render(context);
	}
}
