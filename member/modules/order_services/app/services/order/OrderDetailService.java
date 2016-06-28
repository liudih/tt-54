package services.order;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.order.DetailMapper;
import dto.order.OrderDetail;

public class OrderDetailService implements IOrderDetailService {

	@Inject
	DetailMapper detailMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderDetailService#getOrderDetailByCid(java.lang.String)
	 */
	@Override
	public OrderDetail getOrderDetailByCid(String cid) {
		return detailMapper.getOrderDetailByCid(cid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderDetailService#getDetailByOrderIds(java.util.List)
	 */
	@Override
	public List<OrderDetail> getDetailByOrderIds(List<Integer> orderids) {
		return detailMapper.getDetailByOrderIds(orderids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.order.IOrderDetailService#updateDetailCommentId(java.lang.Integer
	 * , java.lang.String)
	 */
	@Override
	public boolean updateDetailCommentId(Integer commentId, String cid) {
		int flag = detailMapper.updateDetailCommentId(commentId, cid);
		return flag > 0;
	}

	@Override
	public List<OrderDetail> getPageBySkuInfo(int p, int pagesize,
			List<String> csku, List<Integer> istatus, List<String> cvhosts,
			Date dpaymentstartdate, Date dpaymentenddate, Date createStartDate,
			Date createEndDate) {
		// TODO Auto-generated method stub
		return detailMapper.getPageBySkuInfo(p, pagesize, csku, istatus,
				cvhosts, dpaymentstartdate, dpaymentenddate, createStartDate,
				createEndDate);
	}

	@Override
	public int getCountBySkuInfo(List<String> csku, List<Integer> istatus,
			List<String> cvhosts, Date dpaymentstartdate, Date dpaymentenddate,
			Date createStartDate, Date createEndDate) {
		// TODO Auto-generated method stub
		return detailMapper.getCountBySkuInfo(csku, istatus, cvhosts,
				dpaymentstartdate, dpaymentenddate, createStartDate,
				createEndDate);
	}
}
