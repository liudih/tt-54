package mapper.interaction;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.interaction.ProductPostEvaluate;
import valueobjects.interaction.ReviewHelpEvaluateCount;

public interface ProductPostEvaluateMapper {

	int insertSelective(ProductPostEvaluate record);

	int insert(ProductPostEvaluate record);

	List<ReviewHelpEvaluateCount> getHelpfulCount(List<Integer> list);

	@Select("select  ipostid, ihelpfulcode, cmemberemail, dcreatedate from t_interaction_product_post_evaluate "
			+ "where ipostid = #{0} and cmemberemail = #{1}")
	ProductPostEvaluate getProductEvaluteOne(Integer ipostid,
			String cmemberemail);
}