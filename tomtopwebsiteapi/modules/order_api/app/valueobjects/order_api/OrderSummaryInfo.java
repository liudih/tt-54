package valueobjects.order_api;

import java.io.Serializable;
import java.util.List;

/**
 * Order 进行结算时的账目信息
 *
 * @ClassName: OrderSummaryInfo
 * @Description: TODO
 * @author luojiaheng
 * @date 2015年1月16日 上午9:28:39
 *
 */
public class OrderSummaryInfo implements IOrderFragment, Serializable {
	private static final long serialVersionUID = -5861316639590447465L;

	private Double total;
	private List<ExtraLineView> list;
	private Double discount;
	// add by lijun
	private Double subTotal;
	private Double ShippingCost;

	public OrderSummaryInfo(Double baseTotal, List<ExtraLineView> list,
			Double discount) {
		this.total = baseTotal;
		this.list = list;
		this.discount = discount;
	}

	public OrderSummaryInfo(Double baseTotal, List<ExtraLineView> list) {
		this.total = baseTotal;
		this.list = list;
	}

	/**
	 * @author lijun
	 * @param total
	 * @param discountDetail
	 * @param discount
	 * @param subTotal
	 * @param shippingCost
	 */
	public OrderSummaryInfo(Double total, List<ExtraLineView> discountDetail,
			Double discount, Double subTotal, Double shippingCost) {
		this.total = total;
		this.list = discountDetail;
		this.discount = discount;
		this.subTotal = subTotal;
		ShippingCost = shippingCost;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public List<ExtraLineView> getList() {
		return list;
	}

	public void setList(List<ExtraLineView> list) {
		this.list = list;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public Double getShippingCost() {
		return ShippingCost;
	}

}
