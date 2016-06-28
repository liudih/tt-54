package services.search;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Inject;

import mapper.product.ProductCategoryMapperMapper;
import mapper.product.ProductEntityMapMapper;
import mapper.product.ProductStorageMapMapper;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptService;

import play.Logger;
import play.libs.F;
import play.libs.F.Either;
import play.libs.Json;
import services.base.WebsiteService;
import services.base.utils.Utils;
import services.price.PriceService;
import services.product.CategoryEnquiryService;
import services.product.CategoryProductRecommendService;
import services.product.ProductBaseTranslateService;
import services.product.ProductEnquiryService;
import valueobjects.price.Price;
import valueobjects.price.PriceCalculationContext;
import valueobjects.product.ProductBaseTranslate;
import valueobjects.product.ProductViewCount;
import valueobjects.product.index.ChildMappingType;
import valueobjects.product.index.ExtraMappingExtension;
import valueobjects.product.index.MappingType;
import valueobjects.product.index.ProductIndexDocument;
import valueobjects.product.index.RecommendDoc;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.ProductSpecBuilder;
import valueobjects.search.ProductIndexingContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Range;

import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductSalePriceEnquiryDao;
import dao.product.IProductViewCountEnquiryDao;
import dto.Website;
import dto.category.CategoryProductRecommend;
import dto.product.CategoryWebsiteWithName;
import dto.product.ProductCategoryMapper;
import dto.product.ProductEntityMap;
import dto.product.ProductLabel;
import dto.product.ProductSalePrice;
import dto.product.ProductStorageMap;
import extensions.search.ISearchIndexProvider;

public class IndexingService implements IOperatingIndex {

	@Inject
	Client esclient;

	@Inject
	Set<ISearchIndexProvider> indexProviders;

	@Inject
	ProductEnquiryService enquiry;

	@Inject
	ProductEntityMapMapper attributeMapper;

	@Inject
	ProductCategoryMapperMapper categoryMapper;

	@Inject
	CategoryEnquiryService categoryEnquiry;

	@Inject
	ProductBaseTranslateService productBaseTranslateService;

	@Inject
	PriceService priceService;

	@Inject
	WebsiteService websiteEnquiry;

	@Inject
	IProductViewCountEnquiryDao productViewCountEnquityDao;

	@Inject
	IProductSalePriceEnquiryDao productSalePriceEnquityDao;

	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;

	@Inject
	IProductBaseEnquiryDao productBaseDao;

	@Inject
	ProductStorageMapMapper storageMapper;

	@Inject
	CategoryProductRecommendService categoryProductRecommendService;

	final String nodekey = "product";

	@Override
	public void createIndex(int siteId) {
		ObjectNode on = generateMapping(ProductIndexDocument.class);

		List<Website> ws = FluentIterable.from(websiteEnquiry.getAll())
				.filter(w -> w.getIid() == siteId).toList();
		if (ws == null || ws.size() <= 0) {
			Logger.debug("create index faile not found site  {}", siteId);
			return;
		}
		/*
		 * if
		 * (esclient.admin().indices().prepareExists(nodekey).get().isExists())
		 * { try { PutMappingRequest pmap = new PutMappingRequest(nodekey).type(
		 * Integer.toString(siteId)).source(on.toString()); PutMappingResponse
		 * prespon = esclient.admin().indices() .putMapping(pmap).actionGet();
		 * Logger.debug("index product create map {} exist {} -- {}", siteId,
		 * on, prespon); } catch (Exception ex) {
		 * Logger.error("create index product map err", ex); } return; }
		 */

		CreateIndexRequestBuilder req = esclient.admin().indices()
				.prepareCreate(nodekey);
		Logger.debug("Mapping: {}", on);
		for (Website w : ws) {
			req.addMapping(Integer.toString(w.getIid()), on.toString());
		}
		CreateIndexResponse resp = req.get();
		Logger.debug("Index Created: {}", resp);
	}

	@Override
	public void dropIndex() {
		esclient.admin().indices().prepareDelete("product").get();
		Logger.debug("Index Dropped siteId {} ");
	}

	@Override
	public void indexAll(boolean dropIndex, boolean createIndex, Integer siteId) {
		try {
			if (dropIndex) {
				dropIndex();
				// deleteAll(siteId);
			}
		} catch (Exception e1) {
			Logger.warn("Index Dropping Error", e1);
		}
		try {
			if (createIndex) {
				createIndex(siteId);
			}
		} catch (Exception e1) {
			Logger.warn("Index Creation Error", e1);
		}

		privateIndex(null, 200, siteId);

		Logger.debug("Product ALL Indexing DONE");
	}

	public void batchIndex(List<String> listingIds, Integer siteId) {
		int batchSize = 200;
		List<List<String>> parts = Utils.partition(listingIds, batchSize);
		for (List<String> part : parts) {
			privateIndex(part, batchSize, siteId);
		}
		Logger.debug("Product Batch Indexing DONE: size {}", listingIds.size());
	}

	@Override
	public void deleteAll(int siteId) {
		MatchAllQueryBuilder allQueryBuilder = QueryBuilders.matchAllQuery();// 查询所有的documents
		esclient.prepareDeleteByQuery(nodekey)
				.setTypes(Integer.toString(siteId)).setQuery(allQueryBuilder)
				.execute().actionGet();
		DeleteMappingRequest deleteMapping = new DeleteMappingRequest(nodekey)
				.types(String.valueOf(siteId));
		DeleteMappingResponse delemapresponse = esclient.admin().indices()
				.deleteMapping(deleteMapping).actionGet();
		Logger.debug("Product Indexing delete mapping:  {}", delemapresponse);
	}

	@Override
	public void deleteByListing(String listingId) {
		QueryBuilder queryBuilder = QueryBuilders.termQuery("_id", listingId);
		esclient.prepareDeleteByQuery("product").setQuery(queryBuilder)
				.execute().actionGet();
	}

	@Override
	public void index(String listingID) {
		try {
			List<ProductBaseTranslate> translates = productBaseTranslateService
					.getTranslateByListingid(listingID);
			List<ProductIndexingContext> indexingContexts = Lists.transform(
					translates,
					p -> createProductIndexingContext(listingID, translates));
			Logger.info("Indexing Single Listing2:{}-- {} -- {}",
					translates.size(), indexingContexts.size(), listingID);

			for (ProductIndexingContext ctx : indexingContexts) {
				try {
					String json = makeIndexSourceDocument(ctx);
					IndexRequestBuilder requestBuilder = esclient.prepareIndex(
							"product", Integer.toString(ctx.getSiteId()))
							.setRefresh(true);
					IndexResponse indexreq = requestBuilder.setSource(json)
							.setId(ctx.getListingId()).execute().get();
					Logger.info("Indexing Single result json:{} -- {}--{}",
							listingID, indexreq.isCreated());
				} catch (Exception e) {
					Logger.error(
							"Product Indexing Error for ListingID: "
									+ ctx.getListingId(), e);
				}
			}
		} catch (Exception ex) {
			Logger.error("Product Indexing Error for dberror ListingID: "
					+ listingID, ex);
		}
	}

	public boolean index_new(String listingID) {
		boolean result = true;
		try {
			List<ProductBaseTranslate> translates = productBaseTranslateService
					.getTranslateByListingid(listingID);
			List<ProductIndexingContext> indexingContexts = Lists.transform(
					translates,
					p -> createProductIndexingContext(listingID, translates));
			Logger.info("Indexing Single Listing2:{}-- {} -- {}",
					translates.size(), indexingContexts.size(), listingID);

			for (ProductIndexingContext ctx : indexingContexts) {
				try {
					String json = makeIndexSourceDocument(ctx);
					IndexRequestBuilder requestBuilder = esclient.prepareIndex(
							"product", Integer.toString(ctx.getSiteId()))
							.setRefresh(true);
					IndexResponse indexreq = requestBuilder.setSource(json)
							.setId(ctx.getListingId()).execute().actionGet();
					Logger.info("Indexing Single result json:{} -- {}",
							listingID, indexreq.isCreated());
				} catch (Exception e) {
					Logger.error(
							"Product Indexing Error for ListingID: "
									+ ctx.getListingId(), e);
					result = false;
				}
			}
		} catch (Exception ex) {
			Logger.error("Product Indexing Error for dberror ListingID: "
					+ listingID, ex);
			result = false;
		}
		return result;
	}

	public boolean indexJson(ProductIndexingContext productIndexingContext) {
		boolean result = false;
		try {
			if (productIndexingContext.getListingId() == null) {
				return result;
			}
			try {
				String json = makeIndexSourceDocument(productIndexingContext);
				IndexRequestBuilder requestBuilder = esclient.prepareIndex(
						"product",
						Integer.toString(productIndexingContext.getSiteId()))
						.setRefresh(true);
				IndexResponse indexreq = requestBuilder.setSource(json)
						.setId(productIndexingContext.getListingId()).execute()
						.actionGet();
				Logger.info("Indexing Single result json:{} -- {}",
						productIndexingContext.getListingId(),
						indexreq.isCreated());
				result = true;
			} catch (Exception e) {
				Logger.error("Product Indexing Error for ListingID: "
						+ productIndexingContext.getListingId(), e);
			}
		} catch (Exception ex) {
			Logger.error("Product Indexing Error for dberror ListingID: "
					+ productIndexingContext.getListingId(), ex);
		}
		return result;
	}

	public <T> ObjectNode generateMapping(Class<T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		ObjectNode node = JsonNodeFactory.instance.objectNode();
		ObjectNode properties = node.putObject("properties");
		for (Field f : fields) {
			String name = f.getName();
			MappingType mt = f.getAnnotation(MappingType.class);
			ChildMappingType ct = f.getAnnotation(ChildMappingType.class);
			if (mt != null) {
				ObjectNode fieldNode = properties.putObject(name);
				fieldNode.put("type", mt.type());
				if (!StringUtils.isEmpty(mt.index())) {
					fieldNode.put("index", mt.index());
				}
			}
			if (ct != null) {
				Class<?> child = ct.type();
				ObjectNode childnode = generateMapping(child);
				if (!StringUtils.isEmpty(ct.fieldType())) {
					childnode.put("type", ct.fieldType());
				}
				properties.set(name, childnode);
			}
			ExtraMappingExtension et = f
					.getAnnotation(ExtraMappingExtension.class);
			if (et != null) {
				ObjectNode extraMappings = properties.putObject(name);
				ObjectNode extraProperties = extraMappings
						.putObject("properties");
				if (indexProviders != null) {
					for (ISearchIndexProvider sp : indexProviders) {
						ObjectNode spNode = extraProperties.putObject(sp
								.partName());
						ObjectNode spProperties = spNode
								.putObject("properties");
						sp.decorateMapping(spProperties);
					}
				}
			}
		}
		return node;
	}

	/**
	 * 只更新部分索引文件，方便扩展模块自行更新而不用全部商品属性都弄出来
	 * 
	 * @param listingID
	 * @param language
	 *            null 表示所有语言都更新
	 * @param script
	 *            e.g. "ctx._source.extras.interactions.reviewCount+=1"
	 */
	@Override
	public void update(String listingID, String script) {
		List<ProductBaseTranslate> translates = productBaseTranslateService
				.getTranslateByListingid(listingID);

		// 保证站点1的索引是正常的，站点1没索引直接创建
		index(listingID);
		List<UpdateResponse> resp = Lists.transform(
				translates,
				p -> esclient
						.prepareUpdate("product",
								Integer.toString(p.getIwebsiteid()),
								p.getClistingid())
						.setScript(script, ScriptService.ScriptType.INLINE)
						.get());
		for (UpdateResponse r : resp) {
			Logger.debug("Index Updated: {}", r.getId());
		}
	}

	/**
	 * 用以下字段作为索引的内容：
	 * <ul>
	 * <li>category</li>
	 * <li>title</li>
	 * <li>description</li>
	 * 
	 * <li>attributes</li>
	 * <li>price / discount (for sorting, filtering)</li>
	 * <li>new product from / to date</li>
	 * </ul>
	 * 
	 * @param indexingContext
	 * @return JSON字符串
	 * @throws Exception
	 */
	protected String makeIndexSourceDocument(
			ProductIndexingContext indexingContext) throws Exception {
		ProductIndexDocument doc = new ProductIndexDocument(indexingContext);

		// --- extra index providers
		List<F.Tuple<String, ObjectNode>> extras = FluentIterable
				.from(indexProviders)
				.transform(
						ip -> F.Tuple(ip.partName(),
								ip.indexPart(indexingContext))).toList();
		Map<String, ObjectNode> nodes = Maps.transformValues(
				Maps.uniqueIndex(extras, e -> e._1), t -> t._2);
		doc.setExtras(nodes);

		ObjectMapper m = new ObjectMapper();
		return m.writeValueAsString(doc);
	}

	/**
	 * 从ProductBase找出相关索引要求。
	 * 
	 * @param base
	 * @param siteId
	 *            TODO
	 * @return
	 */
	protected ProductIndexingContext createProductIndexingContext(
			String listingID, List<ProductBaseTranslate> listingTranslates) {
		return createProductIndexingContextPreloadPrice(listingID,
				listingTranslates, null, null, null, null, null, null, null,
				null, null);
	}

	protected ProductIndexingContext createProductIndexingContextPreloadPrice(
			String listingID, List<ProductBaseTranslate> listingTranslates,
			Map<String, Price> prices,
			ListMultimap<String, ProductEntityMap> attributeMap,
			ListMultimap<String, ProductSalePrice> salesMap,
			ListMultimap<String, ProductCategoryMapper> categoriesMap,
			ListMultimap<String, ProductLabel> tagsMap,
			Map<String, ProductViewCount> viewCountMap,
			ListMultimap<String, String> relatedSkuMap,
			ListMultimap<String, ProductStorageMap> storageIdMap,
			ListMultimap<String, CategoryProductRecommend> recommendMap) {

		ProductBaseTranslate base = listingTranslates.size() > 0 ? listingTranslates
				.get(0) : null;
		if (base == null) {
			throw new RuntimeException("Should not happened");
		}
		int siteId = base.getIwebsiteid();
		String sku = base.getCsku();

		List<ProductEntityMap> attributes = attributeMap != null ? attributeMap
				.get(listingID) : attributeMapper
				.getProductEntityMapByListingid(listingID);

		List<ProductSalePrice> sales = salesMap != null ? salesMap
				.get(listingID) : this.productSalePriceEnquityDao
				.getAllProductSalePriceByListingId(listingID);

		List<ProductCategoryMapper> categories = categoriesMap != null ? Lists
				.newLinkedList(categoriesMap.get(listingID)) : categoryMapper
				.selectByListingId(listingID);

		List<ProductLabel> tags = tagsMap != null ? tagsMap.get(listingID)
				: this.productLabelEnquiryDao.getProductLabel(listingID);
		List<ProductStorageMap> storageid = storageIdMap != null ? Lists
				.newLinkedList(storageIdMap.get(listingID)) : storageMapper
				.getStorageIdByListingid(listingID);

		// sorry, no flatMap in java
		List<CategoryWebsiteWithName> parents = Lists.newArrayList();
		for (ProductCategoryMapper c : categories) {
			parents.addAll(categoryEnquiry.getAllParentCategories(
					c.getIcategory(), base.getIlanguageid(), siteId));
		}
		categories.addAll(Lists.transform(parents, p -> {
			ProductCategoryMapper pm = new ProductCategoryMapper();
			pm.setClistingid(listingID);
			pm.setCsku(sku);
			pm.setIcategory(p.getIcategoryid());
			return pm;
		}));
		Logger.trace("Categories: {}",
				Lists.transform(categories, c -> c.getIcategory()));

		// pre-loaded price available?
		Price price = null;
		if (prices == null) {
			price = getSimplePrice(listingID);
		} else {
			price = prices.get(listingID);
		}

		ProductViewCount viewCount = null;
		if (viewCountMap == null) {
			viewCount = getViewCount(listingID);
		} else {
			viewCount = viewCountMap.get(listingID);
		}

		List<String> relatedSku = Lists.transform(
				productBaseDao.getRelatedSkuByClistingid(listingID),
				l -> l.getCsku());

		List<CategoryProductRecommend> redList = Lists.newArrayList();
		if (recommendMap != null) {
			redList = recommendMap.get(sku);
		} else {
			redList = categoryProductRecommendService
					.getProductRecommendBySkus(Lists.newArrayList(sku));
		}
		List<RecommendDoc> recList = Lists.transform(
				redList,
				r -> {
					if (r != null) {
						return new RecommendDoc(r.getCategoryid(), r
								.getSequence(), r.getIwebsiteid(), r
								.getCdevice());
					} else {
						return null;
					}
				});
		return new ProductIndexingContext(siteId, listingID, base, categories,
				listingTranslates, attributes, sales, tags, price,
				viewCount != null ? viewCount.getIviewcount() : 0, relatedSku,
				storageid, recList);
	}

	protected ProductViewCount getViewCount(String listingID) {
		List<ProductViewCount> count = this.productViewCountEnquityDao
				.getViewCountListByListingIds(Lists.newArrayList(listingID));
		if (count.isEmpty()) {
			return null;
		} else {
			return count.get(0);
		}
	}

	protected Price getSimplePrice(String listingID) {
		PriceCalculationContext ctx = new PriceCalculationContext("USD");
		return priceService.getPrice(ProductSpecBuilder.build(listingID).get(),
				ctx);
	}

	protected Map<String, Price> getPrices(List<String> listingIDs) {
		List<IProductSpec> specs = FluentIterable.from(listingIDs)
				.transform(s -> ProductSpecBuilder.build(s).get()).toList();
		List<List<IProductSpec>> partitionedListingIDs = Utils.partition(specs,
				50);
		PriceCalculationContext ctx = new PriceCalculationContext("USD");
		List<List<Price>> prices = FluentIterable.from(partitionedListingIDs)
				.transform(ps -> priceService.getPrice(ps, ctx)).toList();
		List<Price> priceList = Utils.flatten(prices);
		return Maps.uniqueIndex(priceList, p -> p.getListingId());
	}

	private void privateIndex(List<String> listingIds, int batchSize,
			Integer siteId) {
		int total = listingIds != null ? productBaseTranslateService
				.getTranslateCount(listingIds) : productBaseTranslateService
				.getAllTranslateCount(siteId);
		int totalPages = total / batchSize + (total % batchSize == 0 ? 0 : 1);
		Logger.debug("Indexing {} Products (With Translations), Batches {}",
				total, totalPages);

		Set<Integer> pageSet = ContiguousSet.create(
				Range.closedOpen(0, totalPages), DiscreteDomain.integers());
		Stream<Either<Exception, BulkResponse>> result = pageSet
				.parallelStream()
				.map(page -> {
					try {
						List<ProductBaseTranslate> translates = listingIds != null ? productBaseTranslateService
								.getTranslatePaged(listingIds, page, batchSize)
								: productBaseTranslateService
										.getAllTranslatePaged(page, batchSize,
												siteId);
						List<String> skus = Lists.transform(translates,
								t -> t.getCsku());
						Logger.debug("ssssssssssku" + skus.toString());
						ListMultimap<String, ProductBaseTranslate> translateByListingID = Multimaps
								.index(translates, t -> t.getClistingid());

						List<String> listingIDs = Lists
								.newLinkedList(translateByListingID.keySet());
						// Price in batch
						Map<String, Price> prices = getPrices(listingIDs);

						ListMultimap<String, ProductEntityMap> attributeMap = Multimaps.index(
								attributeMapper
										.getProductEntityMapByListingids(listingIDs),
								e -> e.getClistingid());

						ListMultimap<String, ProductSalePrice> salesMap = Multimaps.index(
								this.productSalePriceEnquityDao
										.getAllProductSalePriceByListingIds(listingIDs),
								s -> s.getClistingid());

						ListMultimap<String, ProductCategoryMapper> categoriesMap = Multimaps
								.index(categoryMapper
										.selectByListingIds(listingIDs), c -> c
										.getClistingid());

						ListMultimap<String, ProductLabel> tagsMap = Multimaps
								.index(this.productLabelEnquiryDao
										.getBatchProductLabel(listingIDs),
										c -> c.getClistingid());

						Map<String, ProductViewCount> viewCountMap = Maps.uniqueIndex(
								this.productViewCountEnquityDao
										.getViewCountListByListingIds(listingIDs),
								vc -> vc.getClistingid());

						ListMultimap<String, String> relatedSkus = Multimaps
								.transformValues(Multimaps.index(productBaseDao
										.getRelatedSkuByListingids(listingIDs),
										p -> p.getClistingid()), l -> l
										.getCsku());
						ListMultimap<String, ProductStorageMap> storageIds = Multimaps
								.index(storageMapper.getStorageIds(listingIDs),
										s -> s.getClistingid());

						ListMultimap<String, CategoryProductRecommend> recommendList = Multimaps
								.index(categoryProductRecommendService
										.getProductRecommendBySkus(skus),
										s -> s.getSku());

						List<ProductIndexingContext> indexingContexts = FluentIterable
								.from(translateByListingID.keySet())
								.transform(
										id -> {
											List<ProductBaseTranslate> listingTranslates = translateByListingID
													.get(id);
											return createProductIndexingContextPreloadPrice(
													id, listingTranslates,
													prices, attributeMap,
													salesMap, categoriesMap,
													tagsMap, viewCountMap,
													relatedSkus, storageIds,
													recommendList);
										}).toList();

						// bulk indexing
						List<IndexRequestBuilder> builders = FluentIterable
								.from(indexingContexts)
								.transform(
										ctx -> {
											try {
												String json = makeIndexSourceDocument(ctx);
												return esclient
														.prepareIndex(
																"product",
																Integer.toString(ctx
																		.getSiteId()))
														.setSource(json)
														.setId(ctx
																.getListingId());
											} catch (Exception e) {
												Logger.error(
														"Index JSON Doc Error: "
																+ ctx.getListingId(),
														e);
												return null;
											}
										}).filter(x -> x != null).toList();

						BulkRequestBuilder bulkReqs = esclient.prepareBulk();
						for (IndexRequestBuilder i : builders) {
							bulkReqs.add(i);
						}

						Either<Exception, BulkResponse> either = Either
								.Right(bulkReqs.execute().actionGet());
						Logger.debug("Product Indexing Bulk {} DONE", page);
						return either;
					} catch (Exception e) {
						Logger.error(
								"Product Indexing Bulk " + page + " Error", e);
						return Either.Left(e);
					}
				});
		result.count();
	}

}
