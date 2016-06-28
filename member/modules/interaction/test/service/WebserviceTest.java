package service;

import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.route;

import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;

import play.libs.Json;
import play.mvc.Result;
import play.test.FakeRequest;

import common.test.ModuleTest;

import controllers.api.EbayComment;
import extensions.IModule;
import extensions.order.OrderModule;

public class WebserviceTest extends ModuleTest {

	@Inject
	EbayComment controller;

	@Test
	public void run() {
		run(() -> {
			// -- single test
			testWSclient();
		});
	}

	public void testWSclient() {
		FakeRequest req = fakeRequest(
				controllers.api.routes.EbayComment.getEbayCommentByERP())
				.withJsonBody(Json.parse("{\"result\": []}"));
		Result r = route(req);
		// assert some results?
	}

	@Override
	public String[] mybatisNames() {
		return new String[] { "base", "member", "product", "image", "search",
				"cart", "order" };
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends IModule>[] moduleClasses() {
		return new Class[] { OrderModule.class };
	}

	@Override
	protected Map<String, String> additionalConfig(Map<String, String> config) {
		config.put("application.router", "interaction.Routes");
		return config;
	}

}
