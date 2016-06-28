package services.search;

import org.junit.Test;

import valueobjects.product.index.ProductIndexDocument;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class IndexingServiceTest {

	@Test
	public void testGenerateMapping() {
		ObjectNode o = new IndexingService()
				.generateMapping(ProductIndexDocument.class);
		System.out.println("Mapping: " + o);
	}

}
