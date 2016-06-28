package extensions.product.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.product.PullMerchantProductEvent;
import extensions.InjectorInstance;

public class PullProductTriggerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {//每周一 凌晨1点半同步一次数据(拉取)
		//from("quartz2://pullMerchantProductData?cron=0+30+1+?+*+MON").bean(this,
		//		"pullProductEventService");
	}

	public void pullProductEventService() {
		InjectorInstance.getInstance(EventBus.class).post(
				new PullMerchantProductEvent());
	}

}
