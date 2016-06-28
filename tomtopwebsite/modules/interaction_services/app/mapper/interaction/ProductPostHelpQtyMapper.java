package mapper.interaction;

import org.apache.ibatis.annotations.Select;

import dto.interaction.ProductPostHelpQty;

public interface ProductPostHelpQtyMapper {

	int deleteByPrimaryKey(Integer iid);

	int insert(ProductPostHelpQty record);

	int insertSelective(ProductPostHelpQty record);

	ProductPostHelpQty selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(ProductPostHelpQty record);

	int updateByPrimaryKey(ProductPostHelpQty record);

	@Select("select iid, ipostid, ihelpfulqty,inothelpfulqty,dcreatedate from t_interaction_product_post_help_qty where ipostid=#{ipostid}")
	ProductPostHelpQty selectByProductPostId(Integer ipostid);

}
