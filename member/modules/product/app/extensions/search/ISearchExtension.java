package extensions.search;

import valueobjects.search.ISearchQueryParser;
import valueobjects.search.ISearchUIProvider;

import com.google.inject.multibindings.Multibinder;

import extensions.IExtensionPoint;

public interface ISearchExtension extends IExtensionPoint {

	void registerSearchBehaviours(
			Multibinder<ISearchIndexProvider> indexProviders,
			Multibinder<ISearchQueryParser> queryParsers,
			Multibinder<ISearchUIProvider> uiProviders);
}
