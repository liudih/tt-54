package dao.interaction.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.interaction.DropshipProductMapper;
import dao.interaction.IDropshipProductDao;
import dto.interaction.DropshipProduct;

public class DropshipProductDao implements IDropshipProductDao {

	@Inject
	DropshipProductMapper dropshipProductMapper;

	@Override
	public List<String> getDropshipProductSkusByEmailAndState(String email,
			Boolean state, Integer limit, Integer websiteid) {
		return dropshipProductMapper.getDropshipProductSkusByEmailAndState(
				email, state, limit, websiteid);
	}

	@Override
	public Integer setDropshipProductState(String sku, String email,
			Boolean state) {
		return dropshipProductMapper.setDropshipProductState(sku, email, state);
	}

	@Override
	public void batchSetDropshipProductState(List<String> skus, String email,
			Boolean state, Integer siteId) {
		dropshipProductMapper.batchSetDropshipProductState(skus, email, state,
				siteId);
	}

	@Override
	public int getCountDropShipSkuByEmail(String email, Integer siteId) {
		return dropshipProductMapper.getCountDropShipSkuByEmail(email, siteId);
	}

	@Override
	public boolean addDropshipProduct(DropshipProduct dropship) {
		int result = dropshipProductMapper.insertSelective(dropship);
		return result > 0 ? true : false;
	}

	@Override
	public DropshipProduct getDropshipProductByEmailAndSku(String email,
			String sku, Integer siteId) {
		return dropshipProductMapper.getDropshipProductByEmailAndSku(email,
				sku, siteId);
	}

	@Override
	public List<DropshipProduct> getDropshipProductsByEmailAndSkus(
			String email, List<String> skus, Integer siteId) {
		return dropshipProductMapper.getDropshipProductsByEmailAndSkus(email,
				skus, siteId);
	}

	@Override
	public int batchDeleteDropshipProduct(List<Integer> ids) {
		return dropshipProductMapper.batchDeleteDropshipProduct(ids);
	}

	@Override
	public int deleteDropshipProduct(Integer id) {
		return dropshipProductMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int batchSetDropshipProductStatus(String email, List<Integer> ids,
			Boolean state) {
		return dropshipProductMapper.batchSetDropshipProductStatus(email, ids,
				state);
	}

	@Override
	public int setDropShipStatus(Integer id, String email, Boolean status) {
		return dropshipProductMapper.setDropShipStatus(id, email, status);
	}

}
