package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.product.ProductExplainType;

public interface ProductExplainTypeMapper {
	@Select("select * from t_all_product_explain_type")
	List<ProductExplainType> getAllProductExplainType();
}
