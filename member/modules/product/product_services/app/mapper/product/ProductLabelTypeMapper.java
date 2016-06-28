package mapper.product;

import java.util.List;
import java.util.Map;

import dto.product.ProductLabelType;

/**
 * @see t_product_label_type
 * @author lijun
 *
 */
public interface ProductLabelTypeMapper {
	
	/**
	 * 删除记录
	 * @param id
	 * @return
	 */
	public int delete(Map paras);
	
	/**
	 * 插入操作
	 * @param type
	 * @return
	 */
	public int insert(ProductLabelType type);
	
	/**
	 * 批量插入操作
	 * @param types
	 * @return
	 */
	public int batchInsert(List<ProductLabelType> types);
	
	/**
	 * 查询操作
	 * @param paras
	 * @return
	 */
	public List<ProductLabelType> select(Map paras);
}
