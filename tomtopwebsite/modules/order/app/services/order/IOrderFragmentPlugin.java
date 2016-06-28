package services.order;

public interface IOrderFragmentPlugin {

	String getName();

	IOrderFragmentProvider getFragmentProvider();

	IOrderFragmentRenderer getFragmentRenderer();

	IOrderContextPretreatment getContextPretreatment();

}