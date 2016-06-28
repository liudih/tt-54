package services.dodocool.product.fragment;

import java.util.List;
import java.util.Map;
import java.util.Set;

import play.Logger;
import play.mvc.Http.Context;
import services.dodocool.product.IProductFragmentProvider;
import services.product.IProductMultiatributeService;
import valueobjects.dodocool.product.ProductMultiAttribute;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductContext;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.product.ProductMultiattributeEntity;

public class ProductAttributeFragmentProvider implements
		IProductFragmentProvider {

	@Inject
	IProductMultiatributeService productMultiatributeService;

	@Override
	public IProductFragment getFragment(ProductContext context,
			Map<String, Object> processingContext) {
		String listingid = context.getListingID();
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		Map<String, List<ProductMultiattributeEntity>> productMultiatribute = productMultiatributeService
				.getProductMultiatribute(listingid, listingid, null, webContext);
		Logger.debug("productMultiatribute------------"+JSON.toJSONString(productMultiatribute));
		Map<String, List<ProductMultiattributeEntity>> fianlProductMultiatribute = Maps
				.newHashMap();
		List<ProductMultiattributeEntity> finaAttributeEntities = Lists
				.newArrayList();
		if (null != productMultiatribute
				&& productMultiatribute.containsKey("color")) {
			List<ProductMultiattributeEntity> colorAttributeEntities = productMultiatribute
					.get("color");
			Set<String> listingSet = Sets.newHashSet();
			if (null != colorAttributeEntities) {
				for (ProductMultiattributeEntity productMultiattributeEntity : colorAttributeEntities) {
					if (listingSet.contains(productMultiattributeEntity
							.getClistingid())) {
						continue;
					} else {
						listingSet.add(productMultiattributeEntity
								.getClistingid());
						finaAttributeEntities.add(productMultiattributeEntity);
					}
				}
			}
		}
		fianlProductMultiatribute.put("color", finaAttributeEntities);
		Logger.debug("finaAttributeEntities------------"+JSON.toJSONString(finaAttributeEntities));
		return new ProductMultiAttribute(fianlProductMultiatribute, listingid);
	}

}
