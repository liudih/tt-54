package services.order;

import java.util.Date;
import java.util.List;

import dto.order.OrderDetail;

public interface IOrderDetailService {

	public abstract OrderDetail getOrderDetailByCid(String cid);

	public abstract List<OrderDetail> getDetailByOrderIds(List<Integer> orderids);

	public abstract boolean updateDetailCommentId(Integer commentId, String cid);

	/**
	 * sku销售报表分而用函数
	 * 
	 * @param p
	 * @param pagesize
	 * @param csku
	 * @param istatus
	 *            订单状态列表
	 * @param cvhosts
	 *            订单来源列表
	 * @param dpaymentstartdate
	 *            订单付款开始时间
	 * @param dpaymentenddate
	 *            订单付款结束时间
	 * @return
	 */
	public List<OrderDetail> getPageBySkuInfo(int p, int pagesize, List<String> csku,
			List<Integer> istatus, List<String> cvhosts,
			Date dpaymentstartdate, Date dpaymentenddate,Date createStartDate,Date createEndDate);

	/**
	 * 配合getPageBySkuInfo,使用
	 * 
	 * @param csku
	 * @param istatus
	 * @param cvhosts
	 * @param dpaymentstartdate
	 * @param dpaymentenddate
	 * @return
	 */
	public int getCountBySkuInfo( List<String> csku,
			List<Integer> istatus, List<String> cvhosts,
			Date dpaymentstartdate, Date dpaymentenddate,Date createStartDate,Date createEndDate);


}