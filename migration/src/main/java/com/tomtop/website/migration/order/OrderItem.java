package com.tomtop.website.migration.order;

import java.util.Date;

public class OrderItem {
	Integer item_id;
	Integer order_id;
	Integer parent_item_id;
	Integer quote_item_id;
	Integer store_id;
	Date created_at;
	Date updated_at;
	Integer product_id;
	String product_type;
	String product_options;
	Double weight;
	Boolean is_virtual;
	String sku;
	String name;
	String description;
	String applied_rule_ids;
	String additional_data;
	Boolean free_shipping;
	Boolean is_qty_decimal;
	Boolean no_discount;
	Double qty_backordered;
	Double qty_canceled;
	Double qty_invoiced;
	Double qty_ordered;
	Double qty_refunded;
	Double qty_shipped;
	Double base_cost;
	Double price;
	Double base_price;
	Double original_price;
	Double base_original_price;
	Double tax_percent;
	Double tax_amount;
	Double base_tax_amount;
	Double tax_invoiced;
	Double base_tax_invoiced;
	Double discount_percent;
	Double discount_amount;
	Double base_discount_amount;
	Double discount_invoiced;
	Double base_discount_invoiced;
	Double amount_refunded;
	Double base_amount_refunded;
	Double row_total;
	Double base_row_total;
	Double row_invoiced;
	Double base_row_invoiced;
	Double row_weight;
	Integer gift_message_id;
	Integer gift_message_available;
	Double base_tax_before_discount;
	Double tax_before_discount;
	String ext_order_item_id;
	Boolean locked_do_invoice;
	Boolean locked_do_ship;
	Double price_incl_tax;
	Double base_price_incl_tax;
	Double row_total_incl_tax;
	Double base_row_total_incl_tax;
	String weee_tax_applied;
	Double weee_tax_applied_amount;
	Double weee_tax_applied_row_amount;
	Double base_weee_tax_applied_amount;
	Double base_weee_tax_applied_row_amnt;
	Double weee_tax_disposition;
	Double weee_tax_row_disposition;
	Double base_weee_tax_disposition;
	Double base_weee_tax_row_disposition;
	Double hidden_tax_amount;
	Double base_hidden_tax_amount;
	Double hidden_tax_invoiced;
	Double base_hidden_tax_invoiced;
	Double hidden_tax_refunded;
	Double base_hidden_tax_refunded;
	Integer is_nominal;
	Double tax_canceled;
	Double hidden_tax_canceled;
	Double tax_refunded;
	Double base_tax_refunded;
	Double discount_refunded;
	Double base_discount_refunded;
	Double affiliateplus_amount;
	Double base_affiliateplus_amount;
	Double affiliateplus_commission;

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public Integer getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}

	public Integer getParent_item_id() {
		return parent_item_id;
	}

	public void setParent_item_id(Integer parent_item_id) {
		this.parent_item_id = parent_item_id;
	}

	public Integer getQuote_item_id() {
		return quote_item_id;
	}

	public void setQuote_item_id(Integer quote_item_id) {
		this.quote_item_id = quote_item_id;
	}

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
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

	public Integer getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getProduct_options() {
		return product_options;
	}

	public void setProduct_options(String product_options) {
		this.product_options = product_options;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Boolean getIs_virtual() {
		return is_virtual;
	}

	public void setIs_virtual(Boolean is_virtual) {
		this.is_virtual = is_virtual;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApplied_rule_ids() {
		return applied_rule_ids;
	}

	public void setApplied_rule_ids(String applied_rule_ids) {
		this.applied_rule_ids = applied_rule_ids;
	}

	public String getAdditional_data() {
		return additional_data;
	}

	public void setAdditional_data(String additional_data) {
		this.additional_data = additional_data;
	}

	public Boolean getFree_shipping() {
		return free_shipping;
	}

	public void setFree_shipping(Boolean free_shipping) {
		this.free_shipping = free_shipping;
	}

	public Boolean getIs_qty_decimal() {
		return is_qty_decimal;
	}

	public void setIs_qty_decimal(Boolean is_qty_decimal) {
		this.is_qty_decimal = is_qty_decimal;
	}

	public Boolean getNo_discount() {
		return no_discount;
	}

	public void setNo_discount(Boolean no_discount) {
		this.no_discount = no_discount;
	}

	public Double getQty_backordered() {
		return qty_backordered;
	}

	public void setQty_backordered(Double qty_backordered) {
		this.qty_backordered = qty_backordered;
	}

	public Double getQty_canceled() {
		return qty_canceled;
	}

	public void setQty_canceled(Double qty_canceled) {
		this.qty_canceled = qty_canceled;
	}

	public Double getQty_invoiced() {
		return qty_invoiced;
	}

	public void setQty_invoiced(Double qty_invoiced) {
		this.qty_invoiced = qty_invoiced;
	}

	public Double getQty_ordered() {
		return qty_ordered;
	}

	public void setQty_ordered(Double qty_ordered) {
		this.qty_ordered = qty_ordered;
	}

	public Double getQty_refunded() {
		return qty_refunded;
	}

	public void setQty_refunded(Double qty_refunded) {
		this.qty_refunded = qty_refunded;
	}

	public Double getQty_shipped() {
		return qty_shipped;
	}

	public void setQty_shipped(Double qty_shipped) {
		this.qty_shipped = qty_shipped;
	}

	public Double getBase_cost() {
		return base_cost;
	}

	public void setBase_cost(Double base_cost) {
		this.base_cost = base_cost;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getBase_price() {
		return base_price;
	}

	public void setBase_price(Double base_price) {
		this.base_price = base_price;
	}

	public Double getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(Double original_price) {
		this.original_price = original_price;
	}

	public Double getBase_original_price() {
		return base_original_price;
	}

	public void setBase_original_price(Double base_original_price) {
		this.base_original_price = base_original_price;
	}

	public Double getTax_percent() {
		return tax_percent;
	}

	public void setTax_percent(Double tax_percent) {
		this.tax_percent = tax_percent;
	}

	public Double getTax_amount() {
		return tax_amount;
	}

	public void setTax_amount(Double tax_amount) {
		this.tax_amount = tax_amount;
	}

	public Double getBase_tax_amount() {
		return base_tax_amount;
	}

	public void setBase_tax_amount(Double base_tax_amount) {
		this.base_tax_amount = base_tax_amount;
	}

	public Double getTax_invoiced() {
		return tax_invoiced;
	}

	public void setTax_invoiced(Double tax_invoiced) {
		this.tax_invoiced = tax_invoiced;
	}

	public Double getBase_tax_invoiced() {
		return base_tax_invoiced;
	}

	public void setBase_tax_invoiced(Double base_tax_invoiced) {
		this.base_tax_invoiced = base_tax_invoiced;
	}

	public Double getDiscount_percent() {
		return discount_percent;
	}

	public void setDiscount_percent(Double discount_percent) {
		this.discount_percent = discount_percent;
	}

	public Double getDiscount_amount() {
		return discount_amount;
	}

	public void setDiscount_amount(Double discount_amount) {
		this.discount_amount = discount_amount;
	}

	public Double getBase_discount_amount() {
		return base_discount_amount;
	}

	public void setBase_discount_amount(Double base_discount_amount) {
		this.base_discount_amount = base_discount_amount;
	}

	public Double getDiscount_invoiced() {
		return discount_invoiced;
	}

	public void setDiscount_invoiced(Double discount_invoiced) {
		this.discount_invoiced = discount_invoiced;
	}

	public Double getBase_discount_invoiced() {
		return base_discount_invoiced;
	}

	public void setBase_discount_invoiced(Double base_discount_invoiced) {
		this.base_discount_invoiced = base_discount_invoiced;
	}

	public Double getAmount_refunded() {
		return amount_refunded;
	}

	public void setAmount_refunded(Double amount_refunded) {
		this.amount_refunded = amount_refunded;
	}

	public Double getBase_amount_refunded() {
		return base_amount_refunded;
	}

	public void setBase_amount_refunded(Double base_amount_refunded) {
		this.base_amount_refunded = base_amount_refunded;
	}

	public Double getRow_total() {
		return row_total;
	}

	public void setRow_total(Double row_total) {
		this.row_total = row_total;
	}

	public Double getBase_row_total() {
		return base_row_total;
	}

	public void setBase_row_total(Double base_row_total) {
		this.base_row_total = base_row_total;
	}

	public Double getRow_invoiced() {
		return row_invoiced;
	}

	public void setRow_invoiced(Double row_invoiced) {
		this.row_invoiced = row_invoiced;
	}

	public Double getBase_row_invoiced() {
		return base_row_invoiced;
	}

	public void setBase_row_invoiced(Double base_row_invoiced) {
		this.base_row_invoiced = base_row_invoiced;
	}

	public Double getRow_weight() {
		return row_weight;
	}

	public void setRow_weight(Double row_weight) {
		this.row_weight = row_weight;
	}

	public Integer getGift_message_id() {
		return gift_message_id;
	}

	public void setGift_message_id(Integer gift_message_id) {
		this.gift_message_id = gift_message_id;
	}

	public Integer getGift_message_available() {
		return gift_message_available;
	}

	public void setGift_message_available(Integer gift_message_available) {
		this.gift_message_available = gift_message_available;
	}

	public Double getBase_tax_before_discount() {
		return base_tax_before_discount;
	}

	public void setBase_tax_before_discount(Double base_tax_before_discount) {
		this.base_tax_before_discount = base_tax_before_discount;
	}

	public Double getTax_before_discount() {
		return tax_before_discount;
	}

	public void setTax_before_discount(Double tax_before_discount) {
		this.tax_before_discount = tax_before_discount;
	}

	public String getExt_order_item_id() {
		return ext_order_item_id;
	}

	public void setExt_order_item_id(String ext_order_item_id) {
		this.ext_order_item_id = ext_order_item_id;
	}

	public Boolean getLocked_do_invoice() {
		return locked_do_invoice;
	}

	public void setLocked_do_invoice(Boolean locked_do_invoice) {
		this.locked_do_invoice = locked_do_invoice;
	}

	public Boolean getLocked_do_ship() {
		return locked_do_ship;
	}

	public void setLocked_do_ship(Boolean locked_do_ship) {
		this.locked_do_ship = locked_do_ship;
	}

	public Double getPrice_incl_tax() {
		return price_incl_tax;
	}

	public void setPrice_incl_tax(Double price_incl_tax) {
		this.price_incl_tax = price_incl_tax;
	}

	public Double getBase_price_incl_tax() {
		return base_price_incl_tax;
	}

	public void setBase_price_incl_tax(Double base_price_incl_tax) {
		this.base_price_incl_tax = base_price_incl_tax;
	}

	public Double getRow_total_incl_tax() {
		return row_total_incl_tax;
	}

	public void setRow_total_incl_tax(Double row_total_incl_tax) {
		this.row_total_incl_tax = row_total_incl_tax;
	}

	public Double getBase_row_total_incl_tax() {
		return base_row_total_incl_tax;
	}

	public void setBase_row_total_incl_tax(Double base_row_total_incl_tax) {
		this.base_row_total_incl_tax = base_row_total_incl_tax;
	}

	public String getWeee_tax_applied() {
		return weee_tax_applied;
	}

	public void setWeee_tax_applied(String weee_tax_applied) {
		this.weee_tax_applied = weee_tax_applied;
	}

	public Double getWeee_tax_applied_amount() {
		return weee_tax_applied_amount;
	}

	public void setWeee_tax_applied_amount(Double weee_tax_applied_amount) {
		this.weee_tax_applied_amount = weee_tax_applied_amount;
	}

	public Double getWeee_tax_applied_row_amount() {
		return weee_tax_applied_row_amount;
	}

	public void setWeee_tax_applied_row_amount(
			Double weee_tax_applied_row_amount) {
		this.weee_tax_applied_row_amount = weee_tax_applied_row_amount;
	}

	public Double getBase_weee_tax_applied_amount() {
		return base_weee_tax_applied_amount;
	}

	public void setBase_weee_tax_applied_amount(
			Double base_weee_tax_applied_amount) {
		this.base_weee_tax_applied_amount = base_weee_tax_applied_amount;
	}

	public Double getBase_weee_tax_applied_row_amnt() {
		return base_weee_tax_applied_row_amnt;
	}

	public void setBase_weee_tax_applied_row_amnt(
			Double base_weee_tax_applied_row_amnt) {
		this.base_weee_tax_applied_row_amnt = base_weee_tax_applied_row_amnt;
	}

	public Double getWeee_tax_disposition() {
		return weee_tax_disposition;
	}

	public void setWeee_tax_disposition(Double weee_tax_disposition) {
		this.weee_tax_disposition = weee_tax_disposition;
	}

	public Double getWeee_tax_row_disposition() {
		return weee_tax_row_disposition;
	}

	public void setWeee_tax_row_disposition(Double weee_tax_row_disposition) {
		this.weee_tax_row_disposition = weee_tax_row_disposition;
	}

	public Double getBase_weee_tax_disposition() {
		return base_weee_tax_disposition;
	}

	public void setBase_weee_tax_disposition(Double base_weee_tax_disposition) {
		this.base_weee_tax_disposition = base_weee_tax_disposition;
	}

	public Double getBase_weee_tax_row_disposition() {
		return base_weee_tax_row_disposition;
	}

	public void setBase_weee_tax_row_disposition(
			Double base_weee_tax_row_disposition) {
		this.base_weee_tax_row_disposition = base_weee_tax_row_disposition;
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

	public Integer getIs_nominal() {
		return is_nominal;
	}

	public void setIs_nominal(Integer is_nominal) {
		this.is_nominal = is_nominal;
	}

	public Double getTax_canceled() {
		return tax_canceled;
	}

	public void setTax_canceled(Double tax_canceled) {
		this.tax_canceled = tax_canceled;
	}

	public Double getHidden_tax_canceled() {
		return hidden_tax_canceled;
	}

	public void setHidden_tax_canceled(Double hidden_tax_canceled) {
		this.hidden_tax_canceled = hidden_tax_canceled;
	}

	public Double getTax_refunded() {
		return tax_refunded;
	}

	public void setTax_refunded(Double tax_refunded) {
		this.tax_refunded = tax_refunded;
	}

	public Double getBase_tax_refunded() {
		return base_tax_refunded;
	}

	public void setBase_tax_refunded(Double base_tax_refunded) {
		this.base_tax_refunded = base_tax_refunded;
	}

	public Double getDiscount_refunded() {
		return discount_refunded;
	}

	public void setDiscount_refunded(Double discount_refunded) {
		this.discount_refunded = discount_refunded;
	}

	public Double getBase_discount_refunded() {
		return base_discount_refunded;
	}

	public void setBase_discount_refunded(Double base_discount_refunded) {
		this.base_discount_refunded = base_discount_refunded;
	}

	public Double getAffiliateplus_amount() {
		return affiliateplus_amount;
	}

	public void setAffiliateplus_amount(Double affiliateplus_amount) {
		this.affiliateplus_amount = affiliateplus_amount;
	}

	public Double getBase_affiliateplus_amount() {
		return base_affiliateplus_amount;
	}

	public void setBase_affiliateplus_amount(Double base_affiliateplus_amount) {
		this.base_affiliateplus_amount = base_affiliateplus_amount;
	}

	public Double getAffiliateplus_commission() {
		return affiliateplus_commission;
	}

	public void setAffiliateplus_commission(Double affiliateplus_commission) {
		this.affiliateplus_commission = affiliateplus_commission;
	}

}
