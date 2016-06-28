/****
 * dropship相关操作
 */
package services.order.dropShipping;

import java.util.List;

import dto.product.ProductDropShip;

public interface IDropshipService {
	/***
	 * 设置单个sku为此email用户的dropship产品
	 * 
	 * @param sku
	 * @param email
	 * @return TODO
	 */
	public boolean setSkuToDropShip(String sku, String email);

	/***
	 * 取消单个sku为此email用户的dropship产品
	 * 
	 * @param sku
	 * @param email
	 * @return TODO
	 */
	public boolean cancelSkuToDropShip(String sku, String email);

	/***
	 * 设置多个sku为此email用户的dropship产品
	 * @param email
	 * @param siteId TODO
	 * @param sku
	 */
	public void setSkusToDropShip(List<String> skus, String email, Integer siteId);

	/***
	 * 批量取消多个sku为此email用户的dropship产品
	 * @param email
	 * @param siteId TODO
	 * @param sku
	 */
	public void cancelSkusToDropShip(List<String> skus, String email, Integer siteId);

	/***
	 * 获取该用户的dropship sku个数
	 * 
	 * @param email
	 * @param siteId TODO
	 * @return
	 */
	public int getCountDropShipSkuByEmail(String email, Integer siteId);

	/***
	 * 根据邮箱、语言获取该账户做的drop ship产品信息
	 * 
	 * @param email
	 * @param siteid
	 *            TODO
	 * @param currency
	 *            TODO
	 * @return
	 */
	public List<ProductDropShip> getDropshipProductDownloadInfoByEmail(
			String email, Integer languageid, Integer siteid, String currency);

	/**
	 * 根据email和站点获取用户可用的sku
	 * 
	 * @param email
	 * @param site
	 * @return
	 */
	public List<String> getDropShipSkusByEmailAndSite(String email, int site);
}
