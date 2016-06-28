package extensions.mobile;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface IHtmlHookExtension extends IExtensionPoint {

	void registerHtmlHook(Multibinder<HtmlRenderHook> hooks);

}
