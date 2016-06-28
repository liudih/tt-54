package com.rabbit.dao.idao.product;

import com.rabbit.dao.idao.IProductUpdateDao;

public interface IProductRecommendUpdateDao extends IProductUpdateDao {

	int insert(String clistinid, String crecommendlisting, String ccreateuser);
}
