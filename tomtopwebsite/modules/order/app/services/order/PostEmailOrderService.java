package services.order;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import dto.order.Order;
import mapper.order.PostEmailOrderMapper;

public class PostEmailOrderService {
	@Inject
	PostEmailOrderMapper postEmailOrderMapper;

	public List<Integer> findUnpostEmailOrder(Integer status) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.HOUR_OF_DAY, -3);
		Date end = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		Date start = calendar.getTime();
		return postEmailOrderMapper
				.selectUnpostEmailOrderId(start, end, status);
	}

	public boolean insert(Order order) {
		int i = postEmailOrderMapper.insert(order);
		if (1 == i) {
			return true;
		}
		return false;
	}
}
