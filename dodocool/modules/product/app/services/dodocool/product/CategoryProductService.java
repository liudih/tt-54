package services.dodocool.product;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import play.Logger;
import play.mvc.Http.Context;
import services.base.utils.StringUtils;
import services.product.IEntityMapService;
import services.product.IProductBaseEnquiryService;
import services.product.IProductEnquiryService;
import services.product.IProductMessageService;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.product.CategoryWebsiteWithName;
import dto.product.ProductAttributeMessage;
import dto.product.ProductBase;
import dto.product.ProductMessage;
import extensions.InjectorInstance;

public class CategoryProductService {
	@Inject
	IProductMessageService productMessageService;

	@Inject
	IProductBaseEnquiryService productBaseEnquiryService;

	@Inject
	IEntityMapService entityService;

	@Inject
	ProductAmazonUrlService productAmazonUrlService;

	public List<CategoryWebsiteWithName> getCategoryWebsiteWithName() {
		return null;
	}
}
