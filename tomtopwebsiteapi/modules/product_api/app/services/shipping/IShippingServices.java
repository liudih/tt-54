package services.shipping;

import java.util.List;

import dto.Country;
import dto.Storage;
import dto.shipping.ShippingStorage;

public interface IShippingServices {

	/**
	 * 当站点有默认发货仓库时，忽略其它，直接按默认仓库发货
	 * 
	 * @param siteId
	 * @return
	 */
	public Storage getWebsiteLimitStorage(int siteId);

	/**
	 * 获取listing的所有仓库
	 * 
	 * @param listingids
	 * @return
	 */
	public List<ShippingStorage> getStorages(List<String> listingids);

	/**
	 * 根据目的发货地国家，获取这个国家优先从那个仓库发货 1.如果站点有默认发货仓库,那么默认仓库，这是为了一些站点会让一些海外子公司自主经营
	 * 2.如果所有商品未设置发货仓库，那么根据目的地国家取相应的发货仓库
	 * 
	 * @param country
	 *            目的发货国家
	 * @return 仓库
	 */
	public Storage getCountryDefaultStorage(Country country);

	public Storage getShippingStorage(int siteId, List<String> listingids);

	public Storage getShippingStorage(int siteId, Country country,
			List<String> listingids);

	/**
	 * 判断是否同仓库
	 * 
	 * @author lijun
	 * @param listingids
	 * @param storageId
	 * @return
	 */
	public boolean isSameStorage(List<String> listingids, String storageId);

}