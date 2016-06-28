package controllers.manager.skusalesreport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.impl.StringDataFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.IWebsiteService;
import services.base.FoundationService;
import services.base.utils.DateFormatUtils;
import services.base.utils.ExcelUtils;
import services.order.IOrderDetailService;
import services.order.IOrderStatusService;
import services.product.IProductCategoryEnquiryService;
import valueobjects.base.Page;
import valueobjects.order_api.SkuSalesReportQuery;
import valueobjects.product.category.CategoryThreeLevel;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.order.OrderDetail;
import dto.order.OrderStatus;

/**
 * 页面管理页面
 * 
 * @author liu
 *
 */
public class SkuSalesReportAction extends Controller {

	/**
	 * 基础服务
	 */
	@Inject
	FoundationService fService;

	/**
	 * 站点服务
	 */
	@Inject
	IWebsiteService websiteService;

	/**
	 * 订单状态
	 */
	@Inject
	IOrderStatusService orderStatusService;

	@Inject
	IOrderDetailService detailService;

	@Inject
	IProductCategoryEnquiryService categoryEnquiryService;

	/**
	 * 管理方法，分页显示数据
	 * 
	 * @param p
	 * @return
	 */
	public Result manage(int p) {
		List<OrderStatus> orderstatus = orderStatusService.getAll();
		String search = request().getQueryString("search");
		if (p == 1 && StringUtils.isBlank(search)) {
			return ok(views.html.manager.skusalesreport.skusalesreport.render(
					new Page<SkuSalesReportQuery>(Lists.newArrayList(), 0, 1,
							15), orderstatus, new SkuSalesReportQuery(), null,
					null));
		} else {
			Form<SkuSalesReportQuery> form = Form.form(
					SkuSalesReportQuery.class).bindFromRequest();
			if (form.hasErrors()) {
				return ok(views.html.manager.skusalesreport.skusalesreport
						.render(new Page<SkuSalesReportQuery>(Lists
								.newArrayList(), 0, 1, 15), orderstatus,
								new SkuSalesReportQuery(), null, null));
			} else {
				SkuSalesReportQuery query = form.get();
				List<SkuSalesReportQuery> list = getList(query);
				Map<Integer, Integer> statusmap = null;
				if (query.getIstatus() != null) {
					statusmap = Maps.uniqueIndex(query.getIstatus(),
							new Function<Integer, Integer>() {
								@Override
								public Integer apply(Integer arg0) {
									// TODO Auto-generated method stub
									return arg0;
								}
							});
				}
				return ok(views.html.manager.skusalesreport.skusalesreport
						.render(new Page<SkuSalesReportQuery>(list, query
								.getCount(), p, 15), orderstatus, query,
								statusmap, query.getCvhosts()));
			}
		}
	}

	/**
	 * 数组转map
	 * 
	 * @return
	 */
	public Map<String, CategoryThreeLevel> arraytoMap(
			List<CategoryThreeLevel> list) {
		if (list != null) {
			Map<String, CategoryThreeLevel> map = Maps.newHashMap();
			list.stream().forEach(e -> {
				if (!map.containsKey(e.getCsku())) {
					map.put(e.getCsku(), e);
				}
			});
			return map;
		} else {
			return Maps.newHashMap();
		}
	}

	/**
	 * 查询数据
	 * 
	 * @param query
	 * @return
	 */
	public List<SkuSalesReportQuery> getList(SkuSalesReportQuery query) {
		Map<String, String[]> qs = request().queryString();
		if (qs.get("status") != null) {
			query.setIstatus(Lists.transform(
					Lists.newArrayList(qs.get("status")),
					new Function<String, Integer>() {
						@Override
						public Integer apply(String arg0) {
							// TODO Auto-generated method stub
							return Integer.valueOf(arg0);
						}
					}));
		}
		if (qs.get("vhosts") != null) {
			List<String> vhost = new ArrayList<String>();
			for (String s : qs.get("vhosts")) {
				if (StringUtils.isNotBlank(s)) {
					vhost.add(s);
				}
			}
			if (vhost.size() > 0) {
				query.setCvhosts(vhost);
			}
		}
		List<String> skus = null;
		if (StringUtils.isNotBlank(query.getCsku())) {
			query.setCsku(query.getCsku().replace("，",",").replace(" ",""));
			skus = Lists.transform(Arrays.asList(query.getCsku().split(",")),p->p.trim());;
		}
		
		List<OrderDetail> list = detailService.getPageBySkuInfo(query.getP(),
				15, skus,query.getIstatus(), query.getCvhosts(),
				query.getDpaymentstartdate(), query.getDpaymentenddate(),query.getCreateStartDate(),query.getCreateEndDate());
		int count = 0;
		if (query.getP() != -1 && list.size() > 0) {
			count = detailService.getCountBySkuInfo(skus,
					query.getIstatus(), query.getCvhosts(),
					query.getDpaymentstartdate(), query.getDpaymentenddate(),query.getCreateStartDate(),query.getCreateEndDate());
		}
		query.setCount(count);
		List<SkuSalesReportQuery> queries = Lists.newArrayList();
		int skugroup = list.size() > 0 ? (list.size() / 100 + 1) : 0;
		Object[] orderDetails = list.toArray();
		for (int i = 0; i < skugroup; i++) {
			int index = i * 100;
			List<Object> orders = (List<Object>) Lists.newArrayList(Arrays
					.copyOfRange(orderDetails, index, (index + 100) > list
							.size() ? list.size() : (index + 100)));
			List<String> cskus = Lists.transform(orders, e -> {
				return ((OrderDetail) e).getCsku();
			});

			List<CategoryThreeLevel> level = categoryEnquiryService
					.getProductThreeLevelCategoryBySku(cskus,
							query.getDshelvesstartdate(),
							query.getDshelvesenddate(), 1);

			List<CategoryThreeLevel> oneList = level.stream()
					.filter(e -> e.getIlevel() == 1).distinct()
					.collect(Collectors.toList());
			List<CategoryThreeLevel> towList = level.stream()
					.filter(e -> e.getIlevel() == 2).distinct()
					.collect(Collectors.toList());
			List<CategoryThreeLevel> threeList = level.stream()
					.filter(e -> e.getIlevel() == 3).distinct()
					.collect(Collectors.toList());
			Map<String, CategoryThreeLevel> onemap = arraytoMap(oneList);
			Map<String, CategoryThreeLevel> towmap = arraytoMap(towList);
			Map<String, CategoryThreeLevel> threemap = arraytoMap(threeList);

			orders.stream().forEach(
					order -> {
						String csku = ((OrderDetail) order).getCsku();
						SkuSalesReportQuery q = new SkuSalesReportQuery();
						q.setCsku(csku);
						q.setFprice(((OrderDetail) order).getFprice());
						q.setIqty(((OrderDetail) order).getIqty());
						q.setSalesamount(((OrderDetail) order)
								.getFtotalprices());
						CategoryThreeLevel oneLevel = onemap.get(csku);
						CategoryThreeLevel towLevel = towmap.get(csku);
						CategoryThreeLevel threeLevel = threemap.get(csku);
						q.setCtitle(oneLevel != null ? oneLevel.getCtitle()
								: null);
						q.setDcreatedate(oneLevel != null ? oneLevel
								.getDcreatedate() : null);
						q.setAcategory(oneLevel != null ? oneLevel.getCname()
								: null);
						q.setBcategory(towLevel != null ? towLevel.getCname()
								: null);
						q.setCcategory(threeLevel != null ? threeLevel
								.getCname() : null);
						queries.add(q);
					});
		}
		return queries;
	}

	/**
	 * 导出excel
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Result exportexcel() {
		Form<SkuSalesReportQuery> form = Form.form(SkuSalesReportQuery.class)
				.bindFromRequest();
		if (!form.hasErrors()) {
			SkuSalesReportQuery query = form.get();
			query.setP(-1);
			ArrayList<ArrayList<Object>> exceldata = new ArrayList<ArrayList<Object>>();
			List<SkuSalesReportQuery> list = getList(query);
			if (list != null && list.size() > 0) {
				exceldata.add(Lists.newArrayList("sku", "a category",
						"tow category", "three category", "title", "bid price",
						"sales volumes", "sales amount", "shelves time"));
				list.forEach(a -> {
					exceldata.add(Lists.newArrayList(a.getCsku(),
							a.getAcategory(), a.getBcategory(),
							a.getCcategory(), a.getCtitle(), a.getFprice(),
							a.getIqty(), a.getSalesamount(), a.getDcreatedate()));
				});
			}
			response().setHeader(
					"Content-Disposition",
					"attachment; filename=\"SkuSalesReport-"
							+ DateFormatUtils.getDateTimeYYYYMMDD(new Date())
							+ ".xls");
			return ok(
					new ExcelUtils().arrayToXLS(exceldata, "Sku Sales Report"))
					.as("application/vnd.ms-excel");
		} else {
			return badRequest("导出错误");
		}
	}
}
