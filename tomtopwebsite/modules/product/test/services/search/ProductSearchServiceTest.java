package services.search;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import valueobjects.product.ProductBaseTranslate;
import valueobjects.product.index.ProductIndexDocument;
import valueobjects.search.ProductIndexingContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

public class ProductSearchServiceTest {

	@Test
	public void testMakeIndexSourceDocument() throws JsonProcessingException {
		ProductBaseTranslate base = new ProductBaseTranslate();
		base.setIwebsiteid(1);
		base.setIlanguageid(1);
		base.setCtitle("Testing Indexing Title");
		base.setCkeyword("Testing Indexing Keywords");
		base.setIstatus(0);
		ProductIndexingContext ctx = new ProductIndexingContext(1,
				"LISTING-ID", base, Lists.newArrayList(), Lists.newArrayList(),
				Lists.newArrayList(), Lists.newArrayList(),
				Lists.newArrayList(), null, 0, Lists.newArrayList());
		ProductIndexDocument doc = new ProductIndexDocument(ctx);
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(doc);
		System.out.println("JSON: " + json);
		assertNotNull(json);
	}
}
