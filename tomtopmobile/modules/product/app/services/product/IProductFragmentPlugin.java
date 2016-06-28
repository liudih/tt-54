package services.product;

public interface IProductFragmentPlugin {

	String getName();

	IProductFragmentProvider getFragmentProvider();

	IProductFragmentRenderer getFragmentRenderer();

}
