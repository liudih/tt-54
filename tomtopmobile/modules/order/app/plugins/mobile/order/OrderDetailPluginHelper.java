package plugins.mobile.order;

import java.util.Map;
import java.util.Set;

import play.twirl.api.Html;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import dto.order.Order;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class OrderDetailPluginHelper {

	private final Map<String, IOrderDetailPlugin> plugins;

	@Inject
	public OrderDetailPluginHelper(Set<IOrderDetailPlugin> plugins) {
		this.plugins = Maps.uniqueIndex(plugins, p -> p.getName());
	}

	/**
	 * 获取某个插件
	 * 
	 * @param pluginName
	 * @return if not find return null
	 */
	public IOrderDetailPlugin getPlugin(String pluginName) {
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
	public Map<String, IOrderDetailPlugin> getAllPlugin() {
		return this.plugins;
	}

	/**
	 * 绘制pluginName插件
	 * 
	 * @param pluginName
	 * @param context
	 * @return
	 */
	public Html renderPlugin(String pluginName, Order order) {
		if (pluginName == null || pluginName.length() == 0 || order == null) {
			return Html.apply("");
		}
		IOrderDetailPlugin plugin = this.getPlugin(pluginName);
		if (plugin == null) {
			return Html.apply("");
		}
		return plugin.render(order);
	}
}
