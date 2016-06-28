package services.cart;

import java.util.Map;

import valueobjects.order_api.cart.ExtraLine;

public interface IExtraLineService {

	public abstract String addExtraLine(String cartId, ExtraLine line);

	public abstract Map<String, ExtraLine> getExtraLinesByCartId(String cartId);

	public abstract boolean deleteByCartIdAndPluginId(String cartId,
			String pluginId);

	public abstract boolean deleteExtraLineByCartId(String cartId);

	public abstract boolean validExtraLine(String cartId, String extraId);

}