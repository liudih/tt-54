package mapper.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.interaction.ProductPostType;

public interface ProductPostTypeMapper {

	int deleteByPrimaryKey(Integer iid);

	int insert(ProductPostType record);

	int insertSelective(ProductPostType record);

	ProductPostType selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(ProductPostType record);

	int updateByPrimaryKey(ProductPostType record);

	@Select("select iid, ccode, dcreatedate from t_interaction_product_post_type")
	List<ProductPostType> selectAll();

}
