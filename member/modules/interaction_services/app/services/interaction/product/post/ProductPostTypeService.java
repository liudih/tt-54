package services.interaction.product.post;

import java.util.List;

import org.springframework.beans.BeanUtils;

import mapper.interaction.ProductPostTypeMapper;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dto.interaction.ProductPostType;

public class ProductPostTypeService {

	@Inject
	ProductPostTypeMapper mapper;

	public List<dto.ProductPostType> getAll() {
		List<ProductPostType> list = mapper.selectAll();
		return Lists.transform(list, o -> {

			dto.ProductPostType vo = new dto.ProductPostType();
			BeanUtils.copyProperties(o, vo);
			return vo;

		});
	}

	public int insertPorductPostType(ProductPostType type) {
		return mapper.insert(type);

	}

	public int updatePrimaryKeySelective(ProductPostType type) {
		return mapper.updateByPrimaryKeySelective(type);
	}

}
