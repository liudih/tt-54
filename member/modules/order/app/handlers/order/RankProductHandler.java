package handlers.order;

import java.util.List;

import javax.inject.Inject;

import services.member.IMemberUpdateService;
import services.product.CategoryEnquiryService;
import services.product.CategoryUpdateService;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import dto.member.MemberByStatistics;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.product.ProductCategoryRank;
import events.order.PaymentConfirmationEvent;
import events.product.SaleEvent;

public class RankProductHandler {
	@Inject
	CategoryEnquiryService enquiryService;
	@Inject
	CategoryUpdateService updateService;
	@Inject
	IMemberUpdateService memberUpdateService;
	@Inject
	EventBus eventBus;

	@Subscribe
	public void onPaySuccess(PaymentConfirmationEvent event) {

		Order order = event.getOrderValue().getOrder();
		if (order != null) {
			MemberByStatistics memberByStatistics = new MemberByStatistics();
			memberByStatistics.setCemail(order.getCemail());
			memberByStatistics.setFamount(order.getFgrandtotal());
			memberUpdateService.SaveBuyStatistics(memberByStatistics);
		}

		List<OrderDetail> list = event.getOrderValue().getDetails();
		for (OrderDetail orderDetail : list) {
			if (!updateService.updateRank(orderDetail.getIqty(),
					orderDetail.getClistingid())) {
				Integer categoryId = enquiryService
						.getLastCategoryId(orderDetail.getClistingid());
				ProductCategoryRank rank = new ProductCategoryRank();
				rank.setClistingid(orderDetail.getClistingid());
				rank.setCsku(orderDetail.getCsku());
				rank.setIcategoryid(categoryId);
				rank.setIsales(orderDetail.getIqty());
				rank.setIwebsiteid(event.getOrderValue().getOrder()
						.getIwebsiteid());
				updateService.insertRank(rank);
				eventBus.post(new SaleEvent(rank.getClistingid(), rank
						.getIwebsiteid()));
			}
		}
	}
}
