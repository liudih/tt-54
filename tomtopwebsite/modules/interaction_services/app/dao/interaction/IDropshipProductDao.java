package dao.interaction;

import java.util.List;

import dto.interaction.DropshipProduct;

public interface IDropshipProductDao {

	List<String> getDropshipProductSkusByEmailAndState(String email, Boolean state, Integer limit, Integer websiteid);

	Integer setDropshipProductState(String sku, String email, Boolean state);

	void batchSetDropshipProductState(List<String> skus, String email, Boolean state, Integer siteId);

	int getCountDropShipSkuByEmail(String email, Integer siteId);

	boolean addDropshipProduct(DropshipProduct dropship);

	DropshipProduct getDropshipProductByEmailAndSku(String email, String sku, Integer siteId);

	List<DropshipProduct> getDropshipProductsByEmailAndSkus(String email,
			List<String> skus, Integer siteId);

	int batchDeleteDropshipProduct(List<Integer> ids);

	int deleteDropshipProduct(Integer id);

	int batchSetDropshipProductStatus(String email, List<Integer> ids,
			Boolean state);

	int setDropShipStatus(Integer id, String email, Boolean status);
}
