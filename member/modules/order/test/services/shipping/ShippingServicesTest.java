package services.shipping;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import services.base.CountryService;

import com.google.inject.Inject;
import common.test.ModuleTest;

import dto.Country;
import dto.Storage;
import extensions.IModule;
import extensions.order.OrderModule;
import extensions.search.SearchModule;

public class ShippingServicesTest extends ModuleTest {

	@Inject
	ShippingServices ship;

	@Inject
	CountryService country;

	@Test
	public void testGetShippingStorageForListings() {
		run(() -> {
			List<String> listingids = new ArrayList<String>();
			listingids.add("12340");
			listingids.add("12341");
			listingids.add("12342");
			listingids.add("12343");
			Country us = country.getCountryByShortCountryName("US");
			Storage t = ship.getShippingStorage(1, us, listingids);
			int storageid = t.getIid();
			System.out.print(storageid);
			assertEquals(2, storageid);
		});
	}

	@Override
	public String[] mybatisNames() {
		return new String[] { "order", "cart", "product", "member", "base",
				"search" };
	}

	@Override
	public Class<? extends IModule>[] moduleClasses() {
		return new Class[] { OrderModule.class, SearchModule.class };
	}

}
