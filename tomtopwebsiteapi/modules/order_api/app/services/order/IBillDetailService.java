package services.order;

import java.util.List;
import java.util.Map;

import dto.order.BillDetail;

public interface IBillDetailService {

	public static final String TYPE_PRODUCT = "product";
	public static final String TYPE_SHIPPING_METHOD = "shipping_method";
	public static final String TYPE_COUPON = "coupon";
	public static final String TYPE_POINTS = "tomtop.points";
	public static final String TYPE_PROMO_CODE = "promoCode";

	public abstract boolean insert(BillDetail bill);

	public abstract boolean batchInsert(List<BillDetail> list);

	public abstract Double saveTotal(Integer orderId);

	public abstract List<BillDetail> getExtraBill(Integer orderId);

	public abstract Map<String, BillDetail> getMapExceptProduct(Integer orderId);

	public abstract List<BillDetail> getBillByOrderAndType(Integer orderId,
			String type);

}