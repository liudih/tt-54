package services.dodocool.base.template;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import valueobjects.dodocool.base.template.TemplateComposite;

import com.google.common.collect.Maps;

import extensions.InjectorInstance;

@Singleton
public class TemplateService {

	final Map<String, ITemplateFragmentProvider> tfProviders;

	@Inject
	public TemplateService(Set<ITemplateFragmentProvider> providers) {
		this.tfProviders = Maps.uniqueIndex(providers, e -> e.getName());
	}

	public static TemplateService getInstance() {
		return InjectorInstance.getInjector()
				.getInstance(TemplateService.class);
	}

	public TemplateComposite getContents() {
		return new TemplateComposite(tfProviders);
	}

}
