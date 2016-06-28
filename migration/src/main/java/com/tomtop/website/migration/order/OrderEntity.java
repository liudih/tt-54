package com.tomtop.website.migration.order;

import java.util.Date;

public class OrderEntity {
	Integer entity_id;
	String state;
	String status;
	String coupon_code;
	String protect_code;
	String shipping_description;
	Boolean is_virtual;
	Boolean store_id;
	Integer customer_id;
	Double base_discount_amount;
	Double base_discount_canceled;
	Double base_discount_invoiced;
	Double base_discount_refunded;
	Double base_grand_total;
	Double base_shipping_amount;
	Double base_shipping_canceled;
	Double base_shipping_invoiced;
	Double base_shipping_refunded;
	Double base_shipping_tax_amount;
	Double base_shipping_tax_refunded;
	Double base_subtotal;
	Double base_subtotal_canceled;
	Double base_subtotal_invoiced;
	Double base_subtotal_refunded;
	Double base_tax_amount;
	Double base_tax_canceled;
	Double base_tax_invoiced;
	Double base_tax_refunded;
	Double base_to_global_rate;
	Double base_to_order_rate;
	Double base_total_canceled;
	Double base_total_invoiced;
	Double base_total_invoiced_cost;
	Double base_total_offline_refunded;
	Double base_total_online_refunded;
	Double base_total_paid;
	Double base_total_qty_ordered;
	Double base_total_refunded;
	Double discount_amount;
	Double discount_canceled;
	Double discount_invoiced;
	Double discount_refunded;
	Double grand_total;
	Double shipping_amount;
	Double shipping_canceled;
	Double shipping_invoiced;
	Double shipping_refunded;
	Double shipping_tax_amount;
	Double shipping_tax_refunded;
	Double store_to_base_rate;
	Double store_to_order_rate;
	Double subtotal;
	Double subtotal_canceled;
	Double subtotal_invoiced;
	Double subtotal_refunded;
	Double tax_amount;
	Double tax_canceled;
	Double tax_invoiced;
	Double tax_refunded;
	Double total_canceled;
	Double total_invoiced;
	Double total_offline_refunded;
	Double total_online_refunded;
	Double total_paid;
	Double total_qty_ordered;
	Double total_refunded;
	Boolean can_ship_partially;
	Boolean can_ship_partially_item;
	Boolean customer_is_guest;
	Boolean customer_note_notify;
	Integer billing_address_id;
	Boolean customer_group_id;
	Integer edit_increment;
	Boolean email_sent;
	Boolean forced_shipment_with_invoice;
	Integer gift_message_id;
	Integer payment_auth_expiration;
	Integer paypal_ipn_customer_notified;
	Integer quote_address_id;
	Integer quote_id;
	Integer shipping_address_id;
	Double adjustment_negative;
	Double adjustment_positive;
	Double base_adjustment_negative;
	Double base_adjustment_positive;
	Double base_shipping_discount_amount;
	Double base_subtotal_incl_tax;
	Double base_total_due;
	Double payment_authorization_amount;
	Double shipping_discount_amount;
	Double subtotal_incl_tax;
	Double total_due;
	Double weight;
	Date customer_dob;
	String increment_id;
	String applied_rule_ids;
	String base_currency_code;
	String customer_email;
	String customer_firstname;
	String customer_lastname;
	String customer_middlename;
	String customer_prefix;
	String customer_suffix;
	String customer_taxvat;
	String discount_description;
	String ext_customer_id;
	String ext_order_id;
	String global_currency_code;
	String hold_before_state;
	String hold_before_status;
	String order_currency_code;
	String original_increment_id;
	String relation_child_id;
	String relation_child_real_id;
	String relation_parent_id;
	String relation_parent_real_id;
	String remote_ip;
	String shipping_method;
	String store_currency_code;
	String store_name;
	String x_forwarded_for;
	String customer_note;
	Date created_at;
	Date updated_at;
	Boolean total_item_count;
	Integer customer_gender;
	Double base_custbalance_amount;
	Integer currency_base_id;
	String currency_code;
	Double currency_rate;
	Double custbalance_amount;
	Integer is_hold;
	Integer is_multi_payment;
	String real_order_id;
	Double tax_percent;
	String tracking_numbers;
	Double hidden_tax_amount;
	Double base_hidden_tax_amount;
	Double shipping_hidden_tax_amount;
	Double base_shipping_hidden_tax_amnt;
	Double hidden_tax_invoiced;
	Double base_hidden_tax_invoiced;
	Double hidden_tax_refunded;
	Double base_hidden_tax_refunded;
	Double shipping_incl_tax;
	Double base_shipping_incl_tax;
	String coupon_rule_name;
	String income_type;
	Double affiliateplus_discount;
	Double base_affiliateplus_discount;
	Double base_affiliate_credit;
	Double affiliate_credit;
	String affiliateplus_coupon;

	public Integer getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(Integer entity_id) {
		this.entity_id = entity_id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public String getProtect_code() {
		return protect_code;
	}

	public void setProtect_code(String protect_code) {
		this.protect_code = protect_code;
	}

	public String getShipping_description() {
		return shipping_description;
	}

	public void setShipping_description(String shipping_description) {
		this.shipping_description = shipping_description;
	}

	public Boolean getIs_virtual() {
		return is_virtual;
	}

	public void setIs_virtual(Boolean is_virtual) {
		this.is_virtual = is_virtual;
	}

	public Boolean getStore_id() {
		return store_id;
	}

	public void setStore_id(Boolean store_id) {
		this.store_id = store_id;
	}

	public Integer getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	public Double getBase_discount_amount() {
		return base_discount_amount;
	}

	public void setBase_discount_amount(Double base_discount_amount) {
		this.base_discount_amount = base_discount_amount;
	}

	public Double getBase_discount_canceled() {
		return base_discount_canceled;
	}

	public void setBase_discount_canceled(Double base_discount_canceled) {
		this.base_discount_canceled = base_discount_canceled;
	}

	public Double getBase_discount_invoiced() {
		return base_discount_invoiced;
	}

	public void setBase_discount_invoiced(Double base_discount_invoiced) {
		this.base_discount_invoiced = base_discount_invoiced;
	}

	public Double getBase_discount_refunded() {
		return base_discount_refunded;
	}

	public void setBase_discount_refunded(Double base_discount_refunded) {
		this.base_discount_refunded = base_discount_refunded;
	}

	public Double getBase_grand_total() {
		return base_grand_total;
	}

	public void setBase_grand_total(Double base_grand_total) {
		this.base_grand_total = base_grand_total;
	}

	public Double getBase_shipping_amount() {
		return base_shipping_amount;
	}

	public void setBase_shipping_amount(Double base_shipping_amount) {
		this.base_shipping_amount = base_shipping_amount;
	}

	public Double getBase_shipping_canceled() {
		return base_shipping_canceled;
	}

	public void setBase_shipping_canceled(Double base_shipping_canceled) {
		this.base_shipping_canceled = base_shipping_canceled;
	}

	public Double getBase_shipping_invoiced() {
		return base_shipping_invoiced;
	}

	public void setBase_shipping_invoiced(Double base_shipping_invoiced) {
		this.base_shipping_invoiced = base_shipping_invoiced;
	}

	public Double getBase_shipping_refunded() {
		return base_shipping_refunded;
	}

	public void setBase_shipping_refunded(Double base_shipping_refunded) {
		this.base_shipping_refunded = base_shipping_refunded;
	}

	public Double getBase_shipping_tax_amount() {
		return base_shipping_tax_amount;
	}

	public void setBase_shipping_tax_amount(Double base_shipping_tax_amount) {
		this.base_shipping_tax_amount = base_shipping_tax_amount;
	}

	public Double getBase_shipping_tax_refunded() {
		return base_shipping_tax_refunded;
	}

	public void setBase_shipping_tax_refunded(Double base_shipping_tax_refunded) {
		this.base_shipping_tax_refunded = base_shipping_tax_refunded;
	}

	public Double getBase_subtotal() {
		return base_subtotal;
	}

	public void setBase_subtotal(Double base_subtotal) {
		this.base_subtotal = base_subtotal;
	}

	public Double getBase_subtotal_canceled() {
		return base_subtotal_canceled;
	}

	public void setBase_subtotal_canceled(Double base_subtotal_canceled) {
		this.base_subtotal_canceled = base_subtotal_canceled;
	}

	public Double getBase_subtotal_invoiced() {
		return base_subtotal_invoiced;
	}

	public void setBase_subtotal_invoiced(Double base_subtotal_invoiced) {
		this.base_subtotal_invoiced = base_subtotal_invoiced;
	}

	public Double getBase_subtotal_refunded() {
		return base_subtotal_refunded;
	}

	public void setBase_subtotal_refunded(Double base_subtotal_refunded) {
		this.base_subtotal_refunded = base_subtotal_refunded;
	}

	public Double getBase_tax_amount() {
		return base_tax_amount;
	}

	public void setBase_tax_amount(Double base_tax_amount) {
		this.base_tax_amount = base_tax_amount;
	}

	public Double getBase_tax_canceled() {
		return base_tax_canceled;
	}

	public void setBase_tax_canceled(Double base_tax_canceled) {
		this.base_tax_canceled = base_tax_canceled;
	}

	public Double getBase_tax_invoiced() {
		return base_tax_invoiced;
	}

	public void setBase_tax_invoiced(Double base_tax_invoiced) {
		this.base_tax_invoiced = base_tax_invoiced;
	}

	public Double getBase_tax_refunded() {
		return base_tax_refunded;
	}

	public void setBase_tax_refunded(Double base_tax_refunded) {
		this.base_tax_refunded = base_tax_refunded;
	}

	public Double getBase_to_global_rate() {
		return base_to_global_rate;
	}

	public void setBase_to_global_rate(Double base_to_global_rate) {
		this.base_to_global_rate = base_to_global_rate;
	}

	public Double getBase_to_order_rate() {
		return base_to_order_rate;
	}

	public void setBase_to_order_rate(Double base_to_order_rate) {
		this.base_to_order_rate = base_to_order_rate;
	}

	public Double getBase_total_canceled() {
		return base_total_canceled;
	}

	public void setBase_total_canceled(Double base_total_canceled) {
		this.base_total_canceled = base_total_canceled;
	}

	public Double getBase_total_invoiced() {
		return base_total_invoiced;
	}

	public void setBase_total_invoiced(Double base_total_invoiced) {
		this.base_total_invoiced = base_total_invoiced;
	}

	public Double getBase_total_invoiced_cost() {
		return base_total_invoiced_cost;
	}

	public void setBase_total_invoiced_cost(Double base_total_invoiced_cost) {
		this.base_total_invoiced_cost = base_total_invoiced_cost;
	}

	public Double getBase_total_offline_refunded() {
		return base_total_offline_refunded;
	}

	public void setBase_total_offline_refunded(
			Double base_total_offline_refunded) {
		this.base_total_offline_refunded = base_total_offline_refunded;
	}

	public Double getBase_total_online_refunded() {
		return base_total_online_refunded;
	}

	public void setBase_total_online_refunded(Double base_total_online_refunded) {
		this.base_total_online_refunded = base_total_online_refunded;
	}

	public Double getBase_total_paid() {
		return base_total_paid;
	}

	public void setBase_total_paid(Double base_total_paid) {
		this.base_total_paid = base_total_paid;
	}

	public Double getBase_total_qty_ordered() {
		return base_total_qty_ordered;
	}

	public void setBase_total_qty_ordered(Double base_total_qty_ordered) {
		this.base_total_qty_ordered = base_total_qty_ordered;
	}

	public Double getBase_total_refunded() {
		return base_total_refunded;
	}

	public void setBase_total_refunded(Double base_total_refunded) {
		this.base_total_refunded = base_total_refunded;
	}

	public Double getDiscount_amount() {
		return discount_amount;
	}

	public void setDiscount_amount(Double discount_amount) {
		this.discount_amount = discount_amount;
	}

	public Double getDiscount_canceled() {
		return discount_canceled;
	}

	public void setDiscount_canceled(Double discount_canceled) {
		this.discount_canceled = discount_canceled;
	}

	public Double getDiscount_invoiced() {
		return discount_invoiced;
	}

	public void setDiscount_invoiced(Double discount_invoiced) {
		this.discount_invoiced = discount_invoiced;
	}

	public Double getDiscount_refunded() {
		return discount_refunded;
	}

	public void setDiscount_refunded(Double discount_refunded) {
		this.discount_refunded = discount_refunded;
	}

	public Double getGrand_total() {
		return grand_total;
	}

	public void setGrand_total(Double grand_total) {
		this.grand_total = grand_total;
	}

	public Double getShipping_amount() {
		return shipping_amount;
	}

	public void setShipping_amount(Double shipping_amount) {
		this.shipping_amount = shipping_amount;
	}

	public Double getShipping_canceled() {
		return shipping_canceled;
	}

	public void setShipping_canceled(Double shipping_canceled) {
		this.shipping_canceled = shipping_canceled;
	}

	public Double getShipping_invoiced() {
		return shipping_invoiced;
	}

	public void setShipping_invoiced(Double shipping_invoiced) {
		this.shipping_invoiced = shipping_invoiced;
	}

	public Double getShipping_refunded() {
		return shipping_refunded;
	}

	public void setShipping_refunded(Double shipping_refunded) {
		this.shipping_refunded = shipping_refunded;
	}

	public Double getShipping_tax_amount() {
		return shipping_tax_amount;
	}

	public void setShipping_tax_amount(Double shipping_tax_amount) {
		this.shipping_tax_amount = shipping_tax_amount;
	}

	public Double getShipping_tax_refunded() {
		return shipping_tax_refunded;
	}

	public void setShipping_tax_refunded(Double shipping_tax_refunded) {
		this.shipping_tax_refunded = shipping_tax_refunded;
	}

	public Double getStore_to_base_rate() {
		return store_to_base_rate;
	}

	public void setStore_to_base_rate(Double store_to_base_rate) {
		this.store_to_base_rate = store_to_base_rate;
	}

	public Double getStore_to_order_rate() {
		return store_to_order_rate;
	}

	public void setStore_to_order_rate(Double store_to_order_rate) {
		this.store_to_order_rate = store_to_order_rate;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getSubtotal_canceled() {
		return subtotal_canceled;
	}

	public void setSubtotal_canceled(Double subtotal_canceled) {
		this.subtotal_canceled = subtotal_canceled;
	}

	public Double getSubtotal_invoiced() {
		return subtotal_invoiced;
	}

	public void setSubtotal_invoiced(Double subtotal_invoiced) {
		this.subtotal_invoiced = subtotal_invoiced;
	}

	public Double getSubtotal_refunded() {
		return subtotal_refunded;
	}

	public void setSubtotal_refunded(Double subtotal_refunded) {
		this.subtotal_refunded = subtotal_refunded;
	}

	public Double getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(Double tax_amount) {
		this.tax_amount = tax_amount;
	}

	public Double getTax_canceled() {
		return tax_canceled;
	}

	public void setTax_canceled(Double tax_canceled) {
		this.tax_canceled = tax_canceled;
	}

	public Double getTax_invoiced() {
		return tax_invoiced;
	}

	public void setTax_invoiced(Double tax_invoiced) {
		this.tax_invoiced = tax_invoiced;
	}

	public Double getTax_refunded() {
		return tax_refunded;
	}

	public void setTax_refunded(Double tax_refunded) {
		this.tax_refunded = tax_refunded;
	}

	public Double getTotal_canceled() {
		return total_canceled;
	}

	public void setTotal_canceled(Double total_canceled) {
		this.total_canceled = total_canceled;
	}

	public Double getTotal_invoiced() {
		return total_invoiced;
	}

	public void setTotal_invoiced(Double total_invoiced) {
		this.total_invoiced = total_invoiced;
	}

	public Double getTotal_offline_refunded() {
		return total_offline_refunded;
	}

	public void setTotal_offline_refunded(Double total_offline_refunded) {
		this.total_offline_refunded = total_offline_refunded;
	}

	public Double getTotal_online_refunded() {
		return total_online_refunded;
	}

	public void setTotal_online_refunded(Double total_online_refunded) {
		this.total_online_refunded = total_online_refunded;
	}

	public Double getTotal_paid() {
		return total_paid;
	}

	public void setTotal_paid(Double total_paid) {
		this.total_paid = total_paid;
	}

	public Double getTotal_qty_ordered() {
		return total_qty_ordered;
	}

	public void setTotal_qty_ordered(Double total_qty_ordered) {
		this.total_qty_ordered = total_qty_ordered;
	}

	public Double getTotal_refunded() {
		return total_refunded;
	}

	public void setTotal_refunded(Double total_refunded) {
		this.total_refunded = total_refunded;
	}

	public Boolean getCan_ship_partially() {
		return can_ship_partially;
	}

	public void setCan_ship_partially(Boolean can_ship_partially) {
		this.can_ship_partially = can_ship_partially;
	}

	public Boolean getCan_ship_partially_item() {
		return can_ship_partially_item;
	}

	public void setCan_ship_partially_item(Boolean can_ship_partially_item) {
		this.can_ship_partially_item = can_ship_partially_item;
	}

	public Boolean getCustomer_is_guest() {
		return customer_is_guest;
	}

	public void setCustomer_is_guest(Boolean customer_is_guest) {
		this.customer_is_guest = customer_is_guest;
	}

	public Boolean getCustomer_note_notify() {
		return customer_note_notify;
	}

	public void setCustomer_note_notify(Boolean customer_note_notify) {
		this.customer_note_notify = customer_note_notify;
	}

	public Integer getBilling_address_id() {
		return billing_address_id;
	}

	public void setBilling_address_id(Integer billing_address_id) {
		this.billing_address_id = billing_address_id;
	}

	public Boolean getCustomer_group_id() {
		return customer_group_id;
	}

	public void setCustomer_group_id(Boolean customer_group_id) {
		this.customer_group_id = customer_group_id;
	}

	public Integer getEdit_increment() {
		return edit_increment;
	}

	public void setEdit_increment(Integer edit_increment) {
		this.edit_increment = edit_increment;
	}

	public Boolean getEmail_sent() {
		return email_sent;
	}

	public void setEmail_sent(Boolean email_sent) {
		this.email_sent = email_sent;
	}

	public Boolean getForced_shipment_with_invoice() {
		return forced_shipment_with_invoice;
	}

	public void setForced_shipment_with_invoice(
			Boolean forced_shipment_with_invoice) {
		this.forced_shipment_with_invoice = forced_shipment_with_invoice;
	}

	public Integer getGift_message_id() {
		return gift_message_id;
	}

	public void setGift_message_id(Integer gift_message_id) {
		this.gift_message_id = gift_message_id;
	}

	public Integer getPayment_auth_expiration() {
		return payment_auth_expiration;
	}

	public void setPayment_auth_expiration(Integer payment_auth_expiration) {
		this.payment_auth_expiration = payment_auth_expiration;
	}

	public Integer getPaypal_ipn_customer_notified() {
		return paypal_ipn_customer_notified;
	}

	public void setPaypal_ipn_customer_notified(
			Integer paypal_ipn_customer_notified) {
		this.paypal_ipn_customer_notified = paypal_ipn_customer_notified;
	}

	public Integer getQuote_address_id() {
		return quote_address_id;
	}

	public void setQuote_address_id(Integer quote_address_id) {
		this.quote_address_id = quote_address_id;
	}

	public Integer getQuote_id() {
		return quote_id;
	}

	public void setQuote_id(Integer quote_id) {
		this.quote_id = quote_id;
	}

	public Integer getShipping_address_id() {
		return shipping_address_id;
	}

	public void setShipping_address_id(Integer shipping_address_id) {
		this.shipping_address_id = shipping_address_id;
	}

	public Double getAdjustment_negative() {
		return adjustment_negative;
	}

	public void setAdjustment_negative(Double adjustment_negative) {
		this.adjustment_negative = adjustment_negative;
	}

	public Double getAdjustment_positive() {
		return adjustment_positive;
	}

	public void setAdjustment_positive(Double adjustment_positive) {
		this.adjustment_positive = adjustment_positive;
	}

	public Double getBase_adjustment_negative() {
		return base_adjustment_negative;
	}

	public void setBase_adjustment_negative(Double base_adjustment_negative) {
		this.base_adjustment_negative = base_adjustment_negative;
	}

	public Double getBase_adjustment_positive() {
		return base_adjustment_positive;
	}

	public void setBase_adjustment_positive(Double base_adjustment_positive) {
		this.base_adjustment_positive = base_adjustment_positive;
	}

	public Double getBase_shipping_discount_amount() {
		return base_shipping_discount_amount;
	}

	public void setBase_shipping_discount_amount(
			Double base_shipping_discount_amount) {
		this.base_shipping_discount_amount = base_shipping_discount_amount;
	}

	public Double getBase_subtotal_incl_tax() {
		return base_subtotal_incl_tax;
	}

	public void setBase_subtotal_incl_tax(Double base_subtotal_incl_tax) {
		this.base_subtotal_incl_tax = base_subtotal_incl_tax;
	}

	public Double getBase_total_due() {
		return base_total_due;
	}

	public void setBase_total_due(Double base_total_due) {
		this.base_total_due = base_total_due;
	}

	public Double getPayment_authorization_amount() {
		return payment_authorization_amount;
	}

	public void setPayment_authorization_amount(
			Double payment_authorization_amount) {
		this.payment_authorization_amount = payment_authorization_amount;
	}

	public Double getShipping_discount_amount() {
		return shipping_discount_amount;
	}

	public void setShipping_discount_amount(Double shipping_discount_amount) {
		this.shipping_discount_amount = shipping_discount_amount;
	}

	public Double getSubtotal_incl_tax() {
		return subtotal_incl_tax;
	}

	public void setSubtotal_incl_tax(Double subtotal_incl_tax) {
		this.subtotal_incl_tax = subtotal_incl_tax;
	}

	public Double getTotal_due() {
		return total_due;
	}

	public void setTotal_due(Double total_due) {
		this.total_due = total_due;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Date getCustomer_dob() {
		return customer_dob;
	}

	public void setCustomer_dob(Date customer_dob) {
		this.customer_dob = customer_dob;
	}

	public String getIncrement_id() {
		return increment_id;
	}

	public void setIncrement_id(String increment_id) {
		this.increment_id = increment_id;
	}

	public String getApplied_rule_ids() {
		return applied_rule_ids;
	}

	public void setApplied_rule_ids(String applied_rule_ids) {
		this.applied_rule_ids = applied_rule_ids;
	}

	public String getBase_currency_code() {
		return base_currency_code;
	}

	public void setBase_currency_code(String base_currency_code) {
		this.base_currency_code = base_currency_code;
	}

	public String getCustomer_email() {
		return customer_email;
	}

	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}

	public String getCustomer_firstname() {
		return customer_firstname;
	}

	public void setCustomer_firstname(String customer_firstname) {
		this.customer_firstname = customer_firstname;
	}

	public String getCustomer_lastname() {
		return customer_lastname;
	}

	public void setCustomer_lastname(String customer_lastname) {
		this.customer_lastname = customer_lastname;
	}

	public String getCustomer_middlename() {
		return customer_middlename;
	}

	public void setCustomer_middlename(String customer_middlename) {
		this.customer_middlename = customer_middlename;
	}

	public String getCustomer_prefix() {
		return customer_prefix;
	}

	public void setCustomer_prefix(String customer_prefix) {
		this.customer_prefix = customer_prefix;
	}

	public String getCustomer_suffix() {
		return customer_suffix;
	}

	public void setCustomer_suffix(String customer_suffix) {
		this.customer_suffix = customer_suffix;
	}

	public String getCustomer_taxvat() {
		return customer_taxvat;
	}

	public void setCustomer_taxvat(String customer_taxvat) {
		this.customer_taxvat = customer_taxvat;
	}

	public String getDiscount_description() {
		return discount_description;
	}

	public void setDiscount_description(String discount_description) {
		this.discount_description = discount_description;
	}

	public String getExt_customer_id() {
		return ext_customer_id;
	}

	public void setExt_customer_id(String ext_customer_id) {
		this.ext_customer_id = ext_customer_id;
	}

	public String getExt_order_id() {
		return ext_order_id;
	}

	public void setExt_order_id(String ext_order_id) {
		this.ext_order_id = ext_order_id;
	}

	public String getGlobal_currency_code() {
		return global_currency_code;
	}

	public void setGlobal_currency_code(String global_currency_code) {
		this.global_currency_code = global_currency_code;
	}

	public String getHold_before_state() {
		return hold_before_state;
	}

	public void setHold_before_state(String hold_before_state) {
		this.hold_before_state = hold_before_state;
	}

	public String getHold_before_status() {
		return hold_before_status;
	}

	public void setHold_before_status(String hold_before_status) {
		this.hold_before_status = hold_before_status;
	}

	public String getOrder_currency_code() {
		return order_currency_code;
	}

	public void setOrder_currency_code(String order_currency_code) {
		this.order_currency_code = order_currency_code;
	}

	public String getOriginal_increment_id() {
		return original_increment_id;
	}

	public void setOriginal_increment_id(String original_increment_id) {
		this.original_increment_id = original_increment_id;
	}

	public String getRelation_child_id() {
		return relation_child_id;
	}

	public void setRelation_child_id(String relation_child_id) {
		this.relation_child_id = relation_child_id;
	}

	public String getRelation_child_real_id() {
		return relation_child_real_id;
	}

	public void setRelation_child_real_id(String relation_child_real_id) {
		this.relation_child_real_id = relation_child_real_id;
	}

	public String getRelation_parent_id() {
		return relation_parent_id;
	}

	public void setRelation_parent_id(String relation_parent_id) {
		this.relation_parent_id = relation_parent_id;
	}

	public String getRelation_parent_real_id() {
		return relation_parent_real_id;
	}

	public void setRelation_parent_real_id(String relation_parent_real_id) {
		this.relation_parent_real_id = relation_parent_real_id;
	}

	public String getRemote_ip() {
		return remote_ip;
	}

	public void setRemote_ip(String remote_ip) {
		this.remote_ip = remote_ip;
	}

	public String getShipping_method() {
		return shipping_method;
	}

	public void setShipping_method(String shipping_method) {
		this.shipping_method = shipping_method;
	}

	public String getStore_currency_code() {
		return store_currency_code;
	}

	public void setStore_currency_code(String store_currency_code) {
		this.store_currency_code = store_currency_code;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getX_forwarded_for() {
		return x_forwarded_for;
	}

	public void setX_forwarded_for(String x_forwarded_for) {
		this.x_forwarded_for = x_forwarded_for;
	}

	public String getCustomer_note() {
		return customer_note;
	}

	public void setCustomer_note(String customer_note) {
		this.customer_note = customer_note;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public Boolean getTotal_item_count() {
		return total_item_count;
	}

	public void setTotal_item_count(Boolean total_item_count) {
		this.total_item_count = total_item_count;
	}

	public Integer getCustomer_gender() {
		return customer_gender;
	}

	public void setCustomer_gender(Integer customer_gender) {
		this.customer_gender = customer_gender;
	}

	public Double getBase_custbalance_amount() {
		return base_custbalance_amount;
	}

	public void setBase_custbalance_amount(Double base_custbalance_amount) {
		this.base_custbalance_amount = base_custbalance_amount;
	}

	public Integer getCurrency_base_id() {
		return currency_base_id;
	}

	public void setCurrency_base_id(Integer currency_base_id) {
		this.currency_base_id = currency_base_id;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public Double getCurrency_rate() {
		return currency_rate;
	}

	public void setCurrency_rate(Double currency_rate) {
		this.currency_rate = currency_rate;
	}

	public Double getCustbalance_amount() {
		return custbalance_amount;
	}

	public void setCustbalance_amount(Double custbalance_amount) {
		this.custbalance_amount = custbalance_amount;
	}

	public Integer getIs_hold() {
		return is_hold;
	}

	public void setIs_hold(Integer is_hold) {
		this.is_hold = is_hold;
	}

	public Integer getIs_multi_payment() {
		return is_multi_payment;
	}

	public void setIs_multi_payment(Integer is_multi_payment) {
		this.is_multi_payment = is_multi_payment;
	}

	public String getReal_order_id() {
		return real_order_id;
	}

	public void setReal_order_id(String real_order_id) {
		this.real_order_id = real_order_id;
	}

	public Double getTax_percent() {
		return tax_percent;
	}

	public void setTax_percent(Double tax_percent) {
		this.tax_percent = tax_percent;
	}

	public String getTracking_numbers() {
		return tracking_numbers;
	}

	public void setTracking_numbers(String tracking_numbers) {
		this.tracking_numbers = tracking_numbers;
	}

	public Double getHidden_tax_amount() {
		return hidden_tax_amount;
	}

	public void setHidden_tax_amount(Double hidden_tax_amount) {
		this.hidden_tax_amount = hidden_tax_amount;
	}

	public Double getBase_hidden_tax_amount() {
		return base_hidden_tax_amount;
	}

	public void setBase_hidden_tax_amount(Double base_hidden_tax_amount) {
		this.base_hidden_tax_amount = base_hidden_tax_amount;
	}

	public Double getShipping_hidden_tax_amount() {
		return shipping_hidden_tax_amount;
	}

	public void setShipping_hidden_tax_amount(Double shipping_hidden_tax_amount) {
		this.shipping_hidden_tax_amount = shipping_hidden_tax_amount;
	}

	public Double getBase_shipping_hidden_tax_amnt() {
		return base_shipping_hidden_tax_amnt;
	}

	public void setBase_shipping_hidden_tax_amnt(
			Double base_shipping_hidden_tax_amnt) {
		this.base_shipping_hidden_tax_amnt = base_shipping_hidden_tax_amnt;
	}

	public Double getHidden_tax_invoiced() {
		return hidden_tax_invoiced;
	}

	public void setHidden_tax_invoiced(Double hidden_tax_invoiced) {
		this.hidden_tax_invoiced = hidden_tax_invoiced;
	}

	public Double getBase_hidden_tax_invoiced() {
		return base_hidden_tax_invoiced;
	}

	public void setBase_hidden_tax_invoiced(Double base_hidden_tax_invoiced) {
		this.base_hidden_tax_invoiced = base_hidden_tax_invoiced;
	}

	public Double getHidden_tax_refunded() {
		return hidden_tax_refunded;
	}

	public void setHidden_tax_refunded(Double hidden_tax_refunded) {
		this.hidden_tax_refunded = hidden_tax_refunded;
	}

	public Double getBase_hidden_tax_refunded() {
		return base_hidden_tax_refunded;
	}

	public void setBase_hidden_tax_refunded(Double base_hidden_tax_refunded) {
		this.base_hidden_tax_refunded = base_hidden_tax_refunded;
	}

	public Double getShipping_incl_tax() {
		return shipping_incl_tax;
	}

	public void setShipping_incl_tax(Double shipping_incl_tax) {
		this.shipping_incl_tax = shipping_incl_tax;
	}

	public Double getBase_shipping_incl_tax() {
		return base_shipping_incl_tax;
	}

	public void setBase_shipping_incl_tax(Double base_shipping_incl_tax) {
		this.base_shipping_incl_tax = base_shipping_incl_tax;
	}

	public String getCoupon_rule_name() {
		return coupon_rule_name;
	}

	public void setCoupon_rule_name(String coupon_rule_name) {
		this.coupon_rule_name = coupon_rule_name;
	}

	public String getIncome_type() {
		return income_type;
	}

	public void setIncome_type(String income_type) {
		this.income_type = income_type;
	}

	public Double getAffiliateplus_discount() {
		return affiliateplus_discount;
	}

	public void setAffiliateplus_discount(Double affiliateplus_discount) {
		this.affiliateplus_discount = affiliateplus_discount;
	}

	public Double getBase_affiliateplus_discount() {
		return base_affiliateplus_discount;
	}

	public void setBase_affiliateplus_discount(
			Double base_affiliateplus_discount) {
		this.base_affiliateplus_discount = base_affiliateplus_discount;
	}

	public Double getBase_affiliate_credit() {
		return base_affiliate_credit;
	}

	public void setBase_affiliate_credit(Double base_affiliate_credit) {
		this.base_affiliate_credit = base_affiliate_credit;
	}

	public Double getAffiliate_credit() {
		return affiliate_credit;
	}

	public void setAffiliate_credit(Double affiliate_credit) {
		this.affiliate_credit = affiliate_credit;
	}

	public String getAffiliateplus_coupon() {
		return affiliateplus_coupon;
	}

	public void setAffiliateplus_coupon(String affiliateplus_coupon) {
		this.affiliateplus_coupon = affiliateplus_coupon;
	}

}
