package services.shipping;

import java.util.List;

import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import dto.ShippingMethodDetail;
import dto.shipping.ShippingMethod;

public interface IShippingMethodService {

	public abstract ShippingMethodInformations getShippingMethodInformations(
			ShippingMethodRequst requst);

	public abstract List<ShippingMethodInformation> processingInPlugin(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst);

	public abstract boolean isSpecial(List<String> listingIds);

	public abstract int add(List<ShippingMethod> methods);

	public abstract List<ShippingMethodDetail> getShippingMethods(
			Integer storageId, String country, Double weight, Integer lang,
			Double subTotal, Boolean isSpecial);

	public abstract ShippingMethod getShippingMethodById(Integer id);

	public abstract ShippingMethodDetail getShippingMethodDetail(Integer id,
			Integer lang);

	public abstract List<ShippingMethodDetail> getShippingMethodDetailByLanguageId(
			Integer languageId);

	public abstract Boolean updateRule(ShippingMethod rule);

	public abstract ShippingMethodDetail getShippingMethodDetailByCode(
			String code, Integer lang);

}