package com.website;

import junit.framework.TestCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.website.dto.product.Product;

public class JsonTest extends TestCase {

	public void testProduct() throws Exception {
		ObjectMapper om = new ObjectMapper();
		Product product = new Product();
		System.out.println(om.writeValueAsString(product));

		ObjectNode on = om.convertValue(product, ObjectNode.class);
		System.out.println(on);
	}
}
