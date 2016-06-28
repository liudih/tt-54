package mapper.wholesale;

import java.util.List;

import org.apache.ibatis.annotations.Insert;

import entity.wholesale.WholeSaleOrderProduct;

public interface WholeSaleOrderProductMapper {
	@Insert("insert into t_wholesale_order_product(iwebsiteid, csku, cemail, iqty, iorderid) "
			+ "values(#{iwebsiteid}, #{csku}, #{cemail}, #{iqty}, #{iorderid})")
	int insert(WholeSaleOrderProduct product);

	@Insert("<script>insert into t_wholesale_order_product(iwebsiteid, csku, cemail, iqty, iorderid) "
			+ "values <foreach collection =\"list\" item =\"item\" index =\"index\" separator =\",\">"
			+ "(#{item.iwebsiteid}, #{item.csku}, #{item.cemail}, #{item.iqty}, #{item.iorderid})"
			+ "</foreach></script>")
	int batchInsert(List<WholeSaleOrderProduct> list);
}
