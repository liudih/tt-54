package valueobjects.order_api.cart;

import java.io.Serializable;

/**
 * Used for Discounts, Point Usage, etc, extensions during checkout process.
 * This represents extra lines at the bottom of a particular order.
 *
 * @author kmtong
 *
 */
public class ExtraLine implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String pluginId;

	String payload;

	/**
	 * @see IOrderExtrasProvider#getId()
	 * @return
	 */
	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "ExtraLine [pluginId=" + pluginId + ", payload=" + payload + "]";
	}

}
