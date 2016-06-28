package services.order.dropShipping;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.base.CountryService;
import services.base.SystemParameterService;
import services.base.utils.DateFormatUtils;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.common.UUIDGenerator;
import services.dropship.DropShipLevelEnquiryService;
import services.price.PriceService;
import services.product.ProductEnquiryService;
import valueobjects.order_api.dropshipping.DropShippingRow;
import valueobjects.price.Price;
import valueobjects.product.ProductLite;
import valueobjects.product.spec.SingleProductSpec;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.Country;
import dto.member.DropShipLevel;
import dto.order.dropShipping.DropShipping;
import dto.order.dropShipping.DropShippingMap;
import dto.order.dropShipping.DropShippingOrder;
import dto.order.dropShipping.DropShippingOrderDetail;

public class DropShippingOrderConvertService {
	private StringBuffer errorMessage;
	private String dropShippingSku;

	@Inject
	private ProductEnquiryService productEnquiry;
	@Inject
	private CountryService countryEnquiry;
	@Inject
	private PriceService priceService;
	@Inject
	private DropShippingOrderUpdateService orderUpdate;
	@Inject
	private DropShippingOrderDetailUpdateService detailUpdate;
	@Inject
	private DropShippingMapUpdateService dropShippingMapUpdate;
	@Inject
	private DropShippingUpdateService dropShippingUpdate;
	@Inject
	private DropShippingEnquiryService dropShippingEnquiry;
	@Inject
	private DropShipLevelEnquiryService dropShipLevelEnquiry;
	@Inject
	private IDropshipService dropshipService;
	@Inject
	private SystemParameterService systemParameter;

	public String convertExcelToDropShippingOrderAndInsert(
			List<List<String>> dataList, String description, Integer websiteID,
			String userEmail, Integer languageID, String currency) {
		errorMessage = new StringBuffer();
		if (dataList == null || dataList.isEmpty()) {
			errorMessage.append("dataList is null or empty. ");
			return null;
		}
		Map<String, Integer> headerIndex = convertHeader(dataList.get(0));
		if (headerIndex == null || headerIndex.isEmpty()) {
			errorMessage.append("headerIndex is null or empty. ");
			return null;
		}
		List<DropShippingRow> rows = convertToDropShippingRows(dataList,
				headerIndex);
		List<String> skuList = dropshipService.getDropShipSkusByEmailAndSite(
				userEmail, websiteID);
		skuList.add(getDropShippingSku(websiteID, languageID));
		String dropShippingID = convertAndInsert(rows, description, websiteID,
				userEmail, languageID, currency, skuList);
		insertDropShipping(dropShippingID, currency, userEmail, websiteID);
		return dropShippingID;
	}

	public void insertDropShipping(String dropShippingID, String currency,
			String userEmail, int site) {
		DropShipping ds = new DropShipping();
		ds.setCcurrency(currency);
		ds.setCdropshippingid(dropShippingID);
		ds.setCuseremail(userEmail);
		ds.setIwebsiteid(site);
		dropShippingUpdate.insert(ds);
	}

	public String convertAndInsert(List<DropShippingRow> rows,
			String description, Integer websiteID, String userEmail,
			Integer languageID, String currency, List<String> skuList) {
		List<DropShippingOrder> orders = Lists.newArrayList();
		String dropshippingID = UUIDGenerator.createAsString() + "-DS";
		double discount = getDisCount(userEmail, websiteID);
		for (DropShippingRow row : rows) {
			DropShippingOrder dsOrder = convertToDropShippingOrder(row,
					description, dropshippingID, websiteID, userEmail,
					languageID, currency, discount, skuList);
			if (dsOrder != null) {
				orders.add(dsOrder);
			}
		}
		List<DropShippingMap> list = Lists.transform(orders, o -> {
			DropShippingMap ds = new DropShippingMap();
			ds.setCdescription(description);
			ds.setCdropshippingid(dropshippingID);
			ds.setCuseremail(userEmail);
			ds.setIorderid(o.getIid());
			return ds;
		});
		return dropShippingMapUpdate.batchInsert(list) ? dropshippingID : null;
	}

	public double getDisCount(String userEmail, int site) {
		Double total = dropShippingEnquiry.getSumPriceByEmail(userEmail, site);
		DropShipLevel level = dropShipLevelEnquiry.getByTotalPrice(total);
		if (level != null && level.getDiscount() != null) {
			return level.getDiscount();
		}
		return 1.0;
	}

	private List<DropShippingRow> convertToDropShippingRows(
			List<List<String>> dataList, Map<String, Integer> headerIndex) {
		dataList.remove(0);// remove header
		List<DropShippingRow> list = Lists
				.transform(
						dataList,
						e -> {
							StringBuffer errorLog = new StringBuffer();
							DropShippingRow row = new DropShippingRow();
							if (e.size() != headerIndex.size()) {
								errorLog.append("Expected data row size is "
										+ headerIndex.size()
										+ ", but in fact is " + e.size() + ". ");
							} else {
								row.setCity(e.get(headerIndex
										.get(DropShippingExcelHeader.CITY
												.getName())));
								row.setNote(e.get(headerIndex
										.get(DropShippingExcelHeader.NOTE
												.getName())));
								row.setCountrysn(e.get(headerIndex
										.get(DropShippingExcelHeader.COUNTRY
												.getName())));
								row.setName(e.get(headerIndex
										.get(DropShippingExcelHeader.NAME
												.getName())));
								row.setZipCode(e.get(headerIndex
										.get(DropShippingExcelHeader.ZIPCODE
												.getName())));
								row.setState(e.get(headerIndex
										.get(DropShippingExcelHeader.STATE
												.getName())));
								row.setAddress(e.get(headerIndex
										.get(DropShippingExcelHeader.ADDRESS
												.getName())));
								row.setPhone(e.get(headerIndex
										.get(DropShippingExcelHeader.PHONE
												.getName())));
								row.setDsOrderID(e.get(headerIndex
										.get(DropShippingExcelHeader.DS_ORDER_NO
												.getName())));
								row.setSku(e.get(headerIndex
										.get(DropShippingExcelHeader.SKU
												.getName())));
								row.setQty(e.get(headerIndex
										.get(DropShippingExcelHeader.QTY
												.getName())));
								Date date = null;
								String dateStr = e.get(headerIndex
										.get(DropShippingExcelHeader.DS_ORDER_DATE
												.getName()));
								try {
									date = DateFormatUtils.parseDate(dateStr,
											"yyyy/MM/dd HH:mm:ss");
								} catch (Exception e1) {
									errorLog.append("the "
											+ DropShippingExcelHeader.DS_ORDER_DATE
													.getName()
											+ ": "
											+ dateStr
											+ ", can't format to (yyyy/MM/dd HH:mm:ss). ");
								}
								Country country = countryEnquiry
										.getCountryByShortCountryName(row
												.getCountrysn());
								if (country == null) {
									errorLog.append("we can't found country by this counry code: "
											+ row.getCountrysn() + ". ");
								} else {
									row.setCountry(country.getCname());
								}
								row.setDsOrderDate(date);
								row.setErrorLog(errorLog.toString());
							}
							return row;
						});
		return list;
	}

	@SuppressWarnings("unchecked")
	private DropShippingOrder convertToDropShippingOrder(DropShippingRow row,
			String description, String dropshippingID, Integer websiteID,
			String userEmail, Integer languageID, String currency,
			double discount, List<String> skuList) {
		StringBuffer errorLog = new StringBuffer();
		DropShippingOrder dsOrder = new DropShippingOrder();
		dsOrder.setCcity(row.getCity());
		dsOrder.setCcnote(row.getNote());
		dsOrder.setCcountrysn(row.getCountrysn());
		dsOrder.setCfirstname(row.getName());
		dsOrder.setCpostalcode(row.getZipCode());
		dsOrder.setCprovince(row.getState());
		dsOrder.setCstreetaddress(row.getAddress());
		dsOrder.setCtelephone(row.getPhone());
		dsOrder.setCuserorderid(row.getDsOrderID());
		dsOrder.setCcountry(row.getCountry());
		dsOrder.setDuserdate(row.getDsOrderDate());
		dsOrder.setIwebsiteid(websiteID);
		dsOrder.setCuseremail(userEmail);
		dsOrder.setCdropshippingid(dropshippingID);
		dsOrder.setCcurrency(currency);
		errorLog.append(row.getErrorLog() == null ? "" : row.getErrorLog());
		Map<String, Object> detailMap = convertToDetail(row, errorLog,
				websiteID, languageID, currency, discount, skuList);
		dsOrder.setCerrorlog(StringUtils.isEmpty(errorLog.toString()) ? null
				: errorLog.toString());
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(
				(Double) detailMap.get("total"));
		dsOrder.setFtotal(dcu.doubleValue());
		List<DropShippingOrderDetail> details = (List<DropShippingOrderDetail>) detailMap
				.get("details");
		if (orderUpdate.insert(dsOrder)
				&& detailUpdate.batchInsert(details, dsOrder.getIid())) {
			return dsOrder;
		}
		return null;
	}

	private Map<String, Object> convertToDetail(DropShippingRow row,
			StringBuffer errorLog, Integer websiteID, Integer languageID,
			String currency, double discount, List<String> skuList) {
		List<DropShippingOrderDetail> detailList = Lists.newArrayList();
		Double total = 0.0;
		String skuStr = row.getSku() + ","
				+ getDropShippingSku(websiteID, languageID);// 加上dropShipping标记
		String qtyStr = row.getQty() + ",1";// 加上dropShipping标记
		String[] skuArr = skuStr.split(",");
		String[] qtyArr = qtyStr.split(",");
		if (skuArr.length != qtyArr.length) {
			errorLog.append("SKU length != QTY length. ");
		} else {
			List<String> skus = Arrays.asList(skuArr);
			List<String> listings = Lists.transform(skus,
					s -> productEnquiry.getListingsBySku(s, websiteID));
			List<ProductLite> productLites = productEnquiry
					.getProductLiteByListingIDs(listings, websiteID, languageID);
			Map<String, ProductLite> productMap = Maps.uniqueIndex(
					productLites, p -> p.getSku());
			for (int i = 0; i < qtyArr.length; i++) {
				String sku = skuArr[i];
				if (!skuList.contains(sku)) {
					errorLog.append("SKU: " + sku
							+ ", you can't use this sku. ");
					continue;
				}
				ProductLite product = productMap.get(sku);
				if (product == null) {
					errorLog.append("SKU: " + sku
							+ ", can't found this in TOMTOP. ");
					continue;
				}
				Integer qty = null;
				try {
					qty = Double.valueOf(qtyArr[i]).intValue();
				} catch (NumberFormatException e) {
					errorLog.append("QTY: " + qtyArr[i]
							+ ", can't be a number. ");
					continue;
				}
				SingleProductSpec sps = new SingleProductSpec(
						product.getListingId(), qty.intValue());
				Price price = priceService.getPrice(sps, currency);
				if (price == null) {
					errorLog.append("sorry, the product: " + sku
							+ " is stop selling. ");
					continue;
				}
				DropShippingOrderDetail detail = new DropShippingOrderDetail();
				detail.setClistingid(product.getListingId());
				detail.setCsku(sku);
				detail.setIqty(qty);
				detail.setCtitle(product.getTitle());
				detail.setForiginalprice(price.getUnitBasePrice());
				setDetailPrice(detail, price, discount);
				detailList.add(detail);
				total += detail.getFtotalprice();
			}
		}
		Map<String, Object> map = Maps.newHashMap();
		map.put("details", detailList);
		map.put("total", total);
		return map;
	}

	/**
	 * 判断折扣价和dropshipping价的最低价
	 * 
	 * @param detail
	 * @param price
	 * @param discount
	 */
	private void setDetailPrice(DropShippingOrderDetail detail, Price price,
			double discount) {
		double basePrice = price.getUnitBasePrice();
		double unitPrice = price.getUnitPrice();
		double dsPrice = new DoubleCalculateUtils(basePrice).multiply(
				(1 - discount)).doubleValue();
		if (dsPrice >= unitPrice) {
			detail.setFprice(unitPrice);
			detail.setFtotalprice(price.getPrice());
		} else {
			detail.setFprice(dsPrice);
			detail.setFtotalprice(new DoubleCalculateUtils(dsPrice).multiply(
					detail.getIqty()).doubleValue());
		}
	}

	private Map<String, Integer> convertHeader(List<String> excelHeader) {
		DropShippingExcelHeader[] headers = DropShippingExcelHeader.values();
		List<String> headList = Lists.transform(Arrays.asList(headers),
				h -> h.getName());
		if (excelHeader.size() == headers.length) {
			Map<String, Integer> map = Maps.newHashMap();
			for (int i = 0; i < excelHeader.size(); i++) {
				if (headList.contains(excelHeader.get(i))) {
					map.put(excelHeader.get(i), i);
				}
			}
			if (map.size() == excelHeader.size()) {
				return map;
			} else {
				errorMessage.append("header map size != header list size.");
			}
		}
		return null;
	}

	public String getDropShippingSku(int site, int lang) {
		if (dropShippingSku == null) {
			String listingID = systemParameter.getSystemParameter(site, lang,
					"DropShipping");
			if (listingID != null) {
				dropShippingSku = productEnquiry.getSKUByListingID(listingID);
			}
			if (dropShippingSku == null && errorMessage != null) {
				errorMessage.append("can't found dropshipping flag sku.");
			}
		}
		return dropShippingSku;
	}

	public StringBuffer getErrorMessage() {
		return errorMessage != null ? errorMessage : new StringBuffer();
	}

	public void setErrorMessage(StringBuffer errorMessage) {
		this.errorMessage = errorMessage;
	}

}
