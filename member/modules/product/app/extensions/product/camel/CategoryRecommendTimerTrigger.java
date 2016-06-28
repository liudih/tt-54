package extensions.product.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.product.CategoryRecommendEvent;
import extensions.InjectorInstance;

public class CategoryRecommendTimerTrigger extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("quartz2://categoryRecommend?cron=0+0+17+*+*+?").bean(this,
				"selectCategoryRecommend");
	}

	public void selectCategoryRecommend() {
		InjectorInstance.getInstance(EventBus.class).post(
				new CategoryRecommendEvent());
	}
}
