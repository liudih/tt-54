package dao.product;

import dao.IProductUpdateDao;

public interface IProductRecommendUpdateDao extends IProductUpdateDao {

	int insert(String clistinid, String crecommendlisting, String ccreateuser);
}
