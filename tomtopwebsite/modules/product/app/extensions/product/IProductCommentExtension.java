package extensions.product;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IProductCommentExtension extends IExtensionPoint {
	public void registerProductCommentFragment(
			Multibinder<IProductCommentProvider> plugins);
}
