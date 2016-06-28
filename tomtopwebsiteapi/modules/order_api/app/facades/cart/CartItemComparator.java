package facades.cart;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import valueobjects.order_api.cart.BundleCartItem;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.SingleCartItem;

import com.google.common.collect.Lists;

public class CartItemComparator implements Comparator<CartItem> {

	@Override
	public int compare(CartItem o1, CartItem o2) {
		if (o1 instanceof SingleCartItem && o2 instanceof SingleCartItem) {
			return o1.getClistingid().compareTo(o2.getClistingid());
		}
		if (o1 instanceof BundleCartItem && o2 instanceof BundleCartItem) {
			List<String> id1 = idExtractor((BundleCartItem) o1);
			List<String> id2 = idExtractor((BundleCartItem) o2);
			if (id1.equals(id2)) {
				return 0;
			} else {
				return String.join(",", id1).compareTo(String.join(",", id2));
			}
		}
		return -1;
	}

	public static List<String> idExtractor(BundleCartItem bundle) {
		String main = null;
		List<String> nonMain = Lists.newArrayList();
		for (SingleCartItem si : bundle.getChildList()) {
			if (si.getBismain()) {
				main = si.getClistingid();
			} else {
				nonMain.add(si.getClistingid());
			}
		}
		List<String> res = Lists.newArrayList(main);
		Collections.sort(nonMain);
		res.addAll(nonMain);
		return res;
	}
}
