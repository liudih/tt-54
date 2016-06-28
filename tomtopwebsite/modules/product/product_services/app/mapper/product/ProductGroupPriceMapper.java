package mapper.product;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.ProductGroupPriceLite;

public interface ProductGroupPriceMapper {
	@Select("  SELECT m.icustomergroupid,m.iqty,m.fdiscount,a.fprice FROM t_product_group_price m "
			+ " inner join t_product_base a on m.clistingid=a.clistingid "
			+ " WHERE m.clistingid = #{0} ")
	List<ProductGroupPriceLite> getProductGroupPriceByListingId(
			String listingId, String currency);

}