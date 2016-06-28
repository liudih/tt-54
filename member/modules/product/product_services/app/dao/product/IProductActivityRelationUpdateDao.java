package dao.product;

import java.util.Map;

import dao.IProductUpdateDao;

public interface IProductActivityRelationUpdateDao extends IProductUpdateDao{
	
	public int addProductRelation(Map<String, Object> param);

}
