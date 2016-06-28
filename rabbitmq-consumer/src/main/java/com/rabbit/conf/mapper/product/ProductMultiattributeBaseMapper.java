package com.rabbit.conf.mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.rabbit.dto.product.ProductMultiattributeBase;

public interface ProductMultiattributeBaseMapper {
    int batchInsert(List<ProductMultiattributeBase> list);
    
    @Select("select cparentsku,iwebsiteid,iid from t_product_multiattribute_base where cparentsku=#{0} and iwebsiteid=#{1} ")
    ProductMultiattributeBase getMultiattributeBase(String parentSku,int websiteId);
    
    @Delete("delete from t_product_multiattribute_base where cparentsku=#{0} and iwebsiteid=#{1} ")
    Integer delete(String parentSku,int websiteId);
}