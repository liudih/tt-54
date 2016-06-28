package extensions.member.login;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.common.collect.Sets;

import services.configuration.ApplicationConfigService;

import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginProviderHelper {

	@Inject
	private Set<ILoginprovider> providerSet;

	@Inject
	ApplicationConfigService applicationConfigService;

	/**
	 * @author liuxin
	 * @param name
	 * @return maybe return null
	 */
	public ILoginprovider getProvider(String name) {
		Map<String, ILoginprovider> providers = FluentIterable
				.from(providerSet).uniqueIndex(p -> p.getName());
		return providers.get(name);
	}

	/**
	 * @author liuxin
	 * @return not return null
	 */
	public Set<ILoginprovider> getProviders() {
		List<String> logins = applicationConfigService.getThridLogins();
		return Sets.filter(providerSet, obj -> logins.contains(obj.getName()));
	}

}
