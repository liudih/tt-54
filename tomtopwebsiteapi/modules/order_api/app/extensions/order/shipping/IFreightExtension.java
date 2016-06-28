package extensions.order.shipping;

import com.google.inject.multibindings.Multibinder;

public interface IFreightExtension {

	public void registerFreightPlugin(Multibinder<IFreightPlugin> plugin);

}
