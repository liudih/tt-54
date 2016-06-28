package extensions.base;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ShareProviderService {

	List<IShareProvider> shares;

	@Inject(optional = true)
	Set<IShareProvider> original;

	private boolean orded = false;

	public void order() {
		// 进行排序
		if (original != null) {
			this.shares = FluentIterable.from(original).toSortedList(
					new Comparator<IShareProvider>() {
						@Override
						public int compare(IShareProvider sp1,
								IShareProvider sp2) {
							if (sp1.getDisplayOrder() > sp2.getDisplayOrder()) {
								return 1;
							} else if (sp1.getDisplayOrder() < sp2
									.getDisplayOrder()) {
								return -1;
							} else {
								return 0;
							}
						}
					});

		} else {
			this.shares = null;
		}
		this.orded = true;
	}

	public synchronized List<IShareProvider> getShareProviders() {
		if (!this.orded) {
			this.order();
		}
		return this.shares;
	}
}
