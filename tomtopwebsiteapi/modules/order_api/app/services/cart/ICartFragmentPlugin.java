package services.cart;

public interface ICartFragmentPlugin {

	String getName();

	ICartFragmentProvider getFragmentProvider();

	ICartFragmentRenderer getFragmentRenderer();
}
