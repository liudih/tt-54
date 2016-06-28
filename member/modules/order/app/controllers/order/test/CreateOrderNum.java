package controllers.order.test;

import mapper.test.OrderMapper;

import com.google.inject.Inject;

import play.mvc.Result;
import play.mvc.Controller;
import services.order.IOrderService;

public class CreateOrderNum extends Controller{
	@Inject
	IOrderService service;
	@Inject
	OrderMapper mapper;
	
	public Result create(){
		String num = service.createGuestOrderNumberV2(null);
		mapper.insert(num);
		return ok();
	}
}
