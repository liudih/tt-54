package dao.product;

import java.util.List;

import dto.product.ProductLabelType;

/**
 * 
 * @author lijun
 *
 */
public interface IProductLabelTypeDao {
	/**
	 * 通过id来删除记录
	 * 
	 * @param id
	 * @return
	 */
	public int deleteById(Integer id);

	/**
	 * 通过creater来删除记录
	 * 
	 * @param creater
	 * @return
	 */
	public int deleteByCreater(String creater);

	/**
	 * 插入操作
	 * 
	 * @param type
	 * @return
	 */
	public int insert(ProductLabelType type);

	/**
	 * 批量插入操作
	 * 
	 * @param types
	 * @return
	 */
	public int batchInsert(List<ProductLabelType> types);

	/**
	 * 查询操作
	 * 
	 * @param paras
	 * @return maybe return null
	 */
	public ProductLabelType selectById(int id);

	/**
	 * 查询出所有的记录
	 * 
	 * @return
	 */
	public List<ProductLabelType> selectAll();
}
