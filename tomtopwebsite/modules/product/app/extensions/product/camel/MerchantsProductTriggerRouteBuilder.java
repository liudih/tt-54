package extensions.product.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.product.MerchantsProductEvent;
import extensions.InjectorInstance;

public class MerchantsProductTriggerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {//每周一 凌晨3点半更新一次数据(更新)
		//from("quartz2://pushMerchantsProductData?cron=0+30+3+?+*+MON").bean(this,
		//		"merchantsProductService");
	}

	public void merchantsProductService() {
		InjectorInstance.getInstance(EventBus.class).post(
				new MerchantsProductEvent());
	}

}
