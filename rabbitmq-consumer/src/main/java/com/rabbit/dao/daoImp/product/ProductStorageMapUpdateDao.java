package  com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductStorageMapMapper;
import com.rabbit.dao.idao.product.IProductStorageMapUpdateDao;
import com.rabbit.dto.product.ProductStorageMap;
@Component
public class ProductStorageMapUpdateDao implements IProductStorageMapUpdateDao {

	@Autowired
	ProductStorageMapMapper productStorageMapMapper;
	
	@Override
	public int addProductStorageList(List<ProductStorageMap> list) {
		return this.productStorageMapMapper.addProductStorageList(list);
	}

	@Override
	public int deleteByListingId(String listingId) {
		return this.productStorageMapMapper.deleteByListingId(listingId);
	}

	@Override
	public int deleteProductStorageList(String listingId, List<Integer> storageIds) {
		return this.productStorageMapMapper.deleteProductStorageList(listingId, storageIds);
	}


}
