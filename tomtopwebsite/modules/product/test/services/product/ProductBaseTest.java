package services.product;

import java.util.Set;

import common.test.ModuleTest;

import extensions.IModule;
import extensions.product.ProductModule;

public class ProductBaseTest extends ModuleTest {

	@Override
	public String[] mybatisNames() {
		return new String[] { "product", "base", "image", "search" };
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends IModule>[] moduleClasses() {
		return new Class[] { ProductWithoutSearchModule.class };
	}

	public static class ProductWithoutSearchModule extends ProductModule {

		@Override
		public Set<Class<? extends IModule>> getDependentModules() {
			Set<Class<? extends IModule>> orig = super.getDependentModules();
			// XXX seems cannot remove search module for testing
			// orig.remove(SearchModule.class);
			return orig;
		}
	}
}
