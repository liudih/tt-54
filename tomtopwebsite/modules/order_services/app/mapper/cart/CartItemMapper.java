package mapper.cart;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.cart.CartItem;

public interface CartItemMapper {
	int insertSelective(CartItem record);

	CartItem selectByPrimaryKey(String cid);

	int updateByPrimaryKeySelective(CartItem record);

	@Select("SELECT t.* from t_cart_item t where t.ccartbaseid=#{0}")
	List<CartItem> getCartItemsByCid(String cid);

	@Select("SELECT t.* from t_cart_item t"
			+ " inner join t_cart_base b on b.cid = t.ccartbaseid and b.iwebsiteid=#{1} "
			+ " where b.cmemberemail is null and b.cuuid=#{0}")
	List<CartItem> getCartItemsByUUID(String uuid, Integer siteID);

	@Update("update t_cart_item  set ccartbaseid=#{1} where "
			+ " exists(select a.cid from t_cart_base a where a.cmemberemail is null and "
			+ " a.bgenerateorders=false and a.cuuid=#{0} and a.cid = ccartbaseid)")
	int updateEmailByUuid(String uuid, String cartbaseid);

	@Update("update t_cart_item  set ccartbaseid=#{1} where cid = #{0}")
	int updateItemForTransCart(String cid, String cartbaseid);

	CartItem getById(@Param("iid") Integer iid, @Param("email") String email,
			@Param("uuid") String uuid);

	@Delete("delete from t_cart_item l where l.cid = #{0}")
	int delItemByCartItemID(String cartitemid);

	@Delete("delete from t_cart_item where ccartbaseid=#{0}")
	int delByCartID(String id);

	@Update("update t_cart_item set iitemtype = #{1} where cid = #{0}")
	int updateTypeByID(String id, int type);
}