package services.order;

import java.util.List;

import javax.inject.Inject;

import mapper.order.OrderTagMapper;
import valueobjects.order_api.OrderTag;
import valueobjects.order_api.OrderTagCount;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

/**
 * 为订单增加标签，所谓标签就是简单的一个字符串。
 * 
 * @author kmtong
 *
 */
public class OrderTaggingService implements IOrderTaggingService {

	@Inject
	OrderTagMapper mapper;

	/* (non-Javadoc)
	 * @see services.order.IOrderTaggingService#tag(int, java.util.List)
	 */
	@Override
	public void tag(int orderId, List<String> tags) {
		List<String> existing = getOrderTags(orderId);
		List<String> inop = Lists.newArrayList(tags);
		inop.removeAll(existing);
		List<OrderTag> orderTags = Lists.transform(inop, (String t) -> {
			OrderTag ot = new OrderTag();
			ot.setIorderid(orderId);
			ot.setCtag(t);
			return ot;
		});
		for (OrderTag ot : orderTags) {
			mapper.insert(ot);
		}
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderTaggingService#untag(int, java.util.List)
	 */
	@Override
	public void untag(int orderId, List<String> tags) {
		List<OrderTag> orderTags = Lists.transform(tags, (String t) -> {
			OrderTag ot = new OrderTag();
			ot.setIorderid(orderId);
			ot.setCtag(t);
			return ot;
		});
		for (OrderTag ot : orderTags) {
			mapper.delete(ot);
		}
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderTaggingService#getOrderTags(int)
	 */
	@Override
	public List<String> getOrderTags(int orderId) {
		List<OrderTag> tags = mapper.findTags(orderId);
		return Lists.transform(tags, t -> t.getCtag());
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderTaggingService#findContainAllTags(java.util.List)
	 */
	@Override
	public List<Integer> findContainAllTags(List<String> tags) {
		int tagSize = tags.size();
		if (tagSize == 0) {
			return Lists.newArrayList();
		}
		List<OrderTagCount> count = mapper.findOrderCountByTags(tags);
		return FluentIterable.from(count).filter(c -> c.getIcount() == tagSize)
				.transform(c -> c.getIorderid()).toList();
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderTaggingService#findContainAnyTags(java.util.List)
	 */
	@Override
	public List<Integer> findContainAnyTags(List<String> tags) {
		List<OrderTagCount> count = mapper.findOrderCountByTags(tags);
		return FluentIterable.from(count).filter(c -> c.getIcount() > 0)
				.transform(c -> c.getIorderid()).toList();
	}
}
