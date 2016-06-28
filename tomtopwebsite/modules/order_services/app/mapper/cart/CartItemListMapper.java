package mapper.cart;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import dto.cart.CartItemList;

public interface CartItemListMapper {

	int insert(CartItemList record);

	@Select(" select m.* from t_cart_item_list m "
			+ " inner join t_cart_item a on m.ccartitemid=a.cid "
			+ " inner join t_cart_base b on a.ccartbaseid=b.cid "
			+ " where b.bgenerateorders=#{1} and b.cid=#{0}")
	List<CartItemList> getCartItemListByCartID(String cartID,
			boolean generateorders);

	@Select(" select m.* from t_cart_item_list m "
			+ " inner join t_cart_item a on m.ccartitemid=a.cid "
			+ " inner join t_cart_base b on a.ccartbaseid=b.cid and b.iwebsiteid=#{1} "
			+ " where b.cmemberemail is null and b.bgenerateorders=false and b.cuuid=#{0}")
	List<CartItemList> getCartItemListByUUID(String UUID, Integer siteID);

	@Delete("delete from t_cart_item_list l where l.ccartitemid = #{0}")
	int delAllItemListByCartItemID(String cartitemid);

	@Delete("delete from t_cart_item_list "
			+ "where ccartitemid in (select cid from t_cart_item "
			+ "where ccartbaseid=#{0})")
	int delByCartID(String id);

	@Delete("delete from t_cart_item_list where ccartitemid = #{0} and clistingid = #{1}")
	int deleteByItemIDAndListingID(String itemID, String listingID);

	@Select(" select * from t_cart_item_list where ccartitemid=#{0}")
	List<CartItemList> getCartItemListByCartItemId(String id);

	@Select("select count(*) from t_cart_item_list where ccartitemid=#{0}")
	int getCountByCartItemID(String itemID);

}