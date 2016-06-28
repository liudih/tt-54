package dao.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapper.product.ProductLabelTypeMapper;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import dao.product.IProductLabelTypeDao;
import dto.product.ProductLabelType;

/**
 * @author lijun
 *
 */
public class ProductLabelTypeDao implements IProductLabelTypeDao {
	@Inject
	ProductLabelTypeMapper mapper;

	@Override
	public int deleteById(Integer id) {
		if (id == null || id == 0) {
			return 0;
		}
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("id", id);
		return this.mapper.delete(paras);
	}

	@Override
	public int deleteByCreater(String creater) {
		if (StringUtils.isEmpty(creater)) {
			return 0;
		}
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("creater", creater);
		return this.mapper.delete(paras);
	}

	@Override
	public int insert(ProductLabelType type) {
		if (type == null || StringUtils.isEmpty(type.getType())) {
			return 0;
		}
		return this.mapper.insert(type);
	}

	@Override
	public int batchInsert(List<ProductLabelType> types) {
		if (types == null || types.size() == 0) {
			return 0;
		}
		return this.mapper.batchInsert(types);
	}

	@Override
	public ProductLabelType selectById(int id) {
		if (0 == id) {
			return null;
		}
		Map<String, Object> paras = new HashMap<String, Object>();
		paras.put("id", id);
		List<ProductLabelType> result = this.mapper.select(paras);
		if (result == null || result.size() == 0) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public List<ProductLabelType> selectAll() {
		return this.mapper.select(null);
	}

}
