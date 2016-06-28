package services.order;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import play.Logger;

import com.google.common.collect.Lists;
import common.test.ModuleTest;

import extensions.IModule;
import extensions.order.OrderModule;

public class OrderTaggingServiceTest extends ModuleTest {

	@Inject
	OrderTaggingService tagService;

	@Test
	public void testTag() {
		run(() -> {
			tagService.tag(1, Lists.newArrayList("tag1", "tag2"));
			List<String> tags = tagService.getOrderTags(1);
			Logger.debug("Tags: {}", tags);
			assertEquals(2, tags.size());

			List<Integer> orderId1 = tagService.findContainAllTags(Lists
					.newArrayList("tag1", "tag2"));
			assertEquals(1, orderId1.size());

			List<Integer> orderId2 = tagService.findContainAllTags(Lists
					.newArrayList("tag1", "tag3"));
			assertEquals(0, orderId2.size());

			List<Integer> orderId3 = tagService.findContainAnyTags(Lists
					.newArrayList("tag3", "tag2"));
			assertEquals(1, orderId3.size());

			List<Integer> orderId4 = tagService.findContainAnyTags(Lists
					.newArrayList("tag3", "tag4"));
			assertEquals(0, orderId4.size());
		});
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

}
