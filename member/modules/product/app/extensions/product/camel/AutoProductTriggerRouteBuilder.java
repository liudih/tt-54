package extensions.product.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.product.AutoMerchantProductByCategoryEvent;
import extensions.InjectorInstance;

public class AutoProductTriggerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {//每天 凌晨2点半自动同步上传新品数据
		//from("quartz2://autoPublishGoogleProductByCategorys?cron=0+30+2+*+*+?").bean(this,
		//		"autoProductEventService");
	}

	public void autoProductEventService() {
		InjectorInstance.getInstance(EventBus.class).post(
				new AutoMerchantProductByCategoryEvent());
	}

}
