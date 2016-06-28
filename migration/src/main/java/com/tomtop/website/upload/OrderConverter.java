package com.tomtop.website.upload;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.website.dto.order.Order;
import com.website.dto.order.OrderAddress;
import com.website.dto.order.OrderDetail;

public class OrderConverter {

	public JsonNode getOrder(JsonNode jnode) {
		return this.getOrderItem(jnode);
	}

	private JsonNode getOrderItem(JsonNode jnode) {
		ObjectMapper jsonMapper = new ObjectMapper();
		com.website.dto.order.Order order = new Order();
		JsonNode addressNode = jnode.get("addresses");
		Iterator<JsonNode> addlist = Iterators.filter(addressNode.iterator(),
				tnode -> tnode.get("address_type").asText().equals("shipping"));
		JsonNode addNode = addlist.next();
		order.setAddress(this.getAddress(addNode));
		order.setCreatedate(this.parseToTime(jnode.get("created_at").asLong()));
		order.setCurrency(this.getValue(jnode, "order_currency_code"));
		order.setDetails(this.getItems(jnode));
		order.setEmail(this.getValue(jnode, "customer_email"));
		// order.setExtra(extra);
		order.setGrandTotal(jnode.get("grand_total").asDouble());
		order.setId(jnode.get("entity_id").asInt());
		order.setIp(this.getValue(jnode, "remote_ip"));
		order.setMemberEmail(this.getValue(jnode, "customer_email"));
		order.setMessage(this.getValue(jnode, "customer_note"));
		order.setOrderSubtotal(jnode.get("subtotal").asDouble());
		// order.setOrigin(origin);
		JsonNode paymentNode = jnode.get("payment");
		if (null != paymentNode) {
			order.setPaymentMethod(this.getValue(jnode, "method"));
		}
		JsonNode historynode = jnode.get("status_history");
		if (null != historynode) {
			Iterator<JsonNode> hlist = Iterators.filter(historynode.iterator(),
					obj -> obj.get("pending_payment") != null);
			if (hlist != null && hlist.hasNext()) {
				order.setPaymentdate(this.parseToTime(hlist.next()
						.get("created_at").asLong()));
			}
		}
		// order.setShippingMethodId(jnode.get("shipping_method").asText());
		order.setShippingMethodMethod(jnode.get("shipping_method").asText());
		order.setShippingPrice(jnode.get("shipping_amount").asDouble());
		order.setShow(1);
		String status = this.getValue(jnode, "status");
		if (null != status && status.length() > 0) {
			int sid = this.getStatusId(status);
			order.setStatus(sid);
		}
		// order.setStorageId(-1);
		if (jnode.get("store_name").asText() != null
				&& jnode.get("store_name").asText().toLowerCase()
						.contains("tomtop.com"))
			order.setWebsiteId(1);
		order.setHistoryStatus(this.getHistoryStatus(jnode));

		/*
		 * try { System.out.println("note->> " +
		 * jsonMapper.writeValueAsString(order)); } catch
		 * (JsonProcessingException e) { e.printStackTrace(); }
		 */
		return jsonMapper.convertValue(order, JsonNode.class);
	}

	/*
	 * enum OrderStatus { pending_payment, payment_confirmed, canceled,
	 * processing, holded, shipped, complete, refunded, fraud
	 * 
	 * }
	 */
	enum OrderStatus {
		pending_payment, payment_confirmed, canceled, processing, holded, shipped, refunded, complete, fraud

	}

	private int getStatusId(String status) {
		if (status.trim().toLowerCase().equals("pending")) {
			return OrderStatus.pending_payment.ordinal() + 1;
		}
		if (status.trim().toLowerCase().equals("payment_review")) {
			return OrderStatus.payment_confirmed.ordinal() + 1;
		}
		if (status.trim().toLowerCase().equals("complete_courier")) {
			return OrderStatus.complete.ordinal() + 1;
		}
		if (status.trim().toLowerCase().equals("processing_cleared")) {
			return OrderStatus.processing.ordinal() + 1;
		}
		if (status.trim().toLowerCase().equals("closed")) {
			return OrderStatus.complete.ordinal() + 1;
		}
		if (status.trim().toLowerCase().equals("refund")) {
			return OrderStatus.refunded.ordinal() + 1;
		}
		if (status.trim().toLowerCase().equals("cancel")) {
			return OrderStatus.canceled.ordinal() + 1;
		}
		return OrderStatus.valueOf(status.trim().toLowerCase()).ordinal() + 1;
	}

	private Date parseToTime(long val) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time = new Long(val);
		String d = format.format(time);
		try {
			return format.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private OrderAddress getAddress(JsonNode jnode) {
		if (jnode == null)
			return null;
		OrderAddress addr = new OrderAddress();
		addr.setCity(this.getValue(jnode, "city"));
		addr.setCountry(this.getValue(jnode, "country_id"));
		addr.setCountrysn(this.getValue(jnode, "country_id"));
		addr.setFirstName(this.getValue(jnode, "firstname"));
		addr.setLastName(this.getValue(jnode, "lastname"));
		addr.setMiddleName(this.getValue(jnode, "middlename"));
		addr.setPostalCode(this.getValue(jnode, "postcode"));
		addr.setProvince(this.getValue(jnode, "region"));
		addr.setStreetAddress(this.getValue(jnode, "street"));
		addr.setTelephone(this.getValue(jnode, "telephone"));
		return addr;
	}

	private List<com.website.dto.order.OrderStatus> getHistoryStatus(
			JsonNode node) {
		ObjectMapper jsonMapper = new ObjectMapper();
		JsonNode itemsnode = node.get("status_history");
		if (itemsnode == null) {
			// System.out.println("not found history-------");
			return null;
		}
		Iterator<JsonNode> its = itemsnode.iterator();
		Iterator<com.website.dto.order.OrderStatus> relist = Iterators
				.transform(
						its,
						jnode -> {
							com.website.dto.order.OrderStatus os = new com.website.dto.order.OrderStatus();
							int sid = this.getStatusId(jnode.get("status")
									.asText().toLowerCase());
							// System.out.println(jnode.get("status")
							// .asText());
							os.setId(sid);
							os.setCreateDate(this.parseToTime(jnode.get(
									"created_at").asLong()));
							return os;
						});
		return Lists.newArrayList(relist);
	}

	private String getValue(JsonNode jnode, String name) {
		if (null != jnode.get(name)) {
			return jnode.get(name).asText();
		}
		return null;
	}

	private List<OrderDetail> getItems(JsonNode node) {
		ObjectMapper jsonMapper = new ObjectMapper();
		ObjectNode oNode1 = jsonMapper.createObjectNode();
		JsonNode itemsnode = node.get("items");
		if (itemsnode == null)
			return null;
		Iterator<OrderDetail> relist = Iterators.transform(
				itemsnode.iterator(), jnode -> {
					// ObjectNode oNode = jsonMapper.createObjectNode();
				OrderDetail odetail = new OrderDetail();
				odetail.setCreatedate(this.parseToTime(jnode.get("created_at")
						.asLong()));
				odetail.setListingId("");
				odetail.setOrderId(jnode.get("order_id").asInt());
				odetail.setPrice(jnode.get("price").asDouble());
				odetail.setQty(jnode.get("qty_ordered").asInt());
				odetail.setSku(jnode.get("sku").asText());
				odetail.setTitle(jnode.get("name").asText());
				odetail.setTotalPrices(jnode.get("row_total").asDouble());
				return odetail;
			});
		return Lists.newArrayList(relist);
	}

}
