package mapper.cart;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.cart.CartBase;

public interface CartBaseMapper {
	int insertSelective(CartBase record);

	CartBase selectByPrimaryKey(String cartId);

	@Select("select * from t_cart_base where cmemberemail=#{0} and bgenerateorders=#{1} "
			+ "and iwebsiteid=#{2} limit 1")
	CartBase selectByEmail(String email, Boolean bgenerateorders, Integer siteID);

	@Select("select * from t_cart_base where cmemberemail is null "
			+ "and cuuid=#{0} and bgenerateorders=#{1} "
			+ "and iwebsiteid=#{2} limit 1")
	CartBase selectByUuid(String uuid, Boolean bgenerateorders, Integer siteID);

	@Select("select tl.cid  from t_cart_item tl where exists(select * from t_cart_base t "
			+"where t.bgenerateorders = 'false' and t.cid=tl.ccartbaseid and t.cuuid = #{0})")
	List<Integer> getCartItemIdByCuuid(String uuid);

	@Select("select tl.cid  from t_cart_item tl where exists(select * from t_cart_base t "
			+"where t.bgenerateorders = 'false' and t.cid=tl.ccartbaseid and t.cmemberemail = #{0})")
	List<Integer> getCartItemIdByCmemberemail(String cmemberemail);

	@Update("update t_cart_base set bgenerateorders=true where cid=#{cid}")
	int updateCartStatusByCartId(String cid);

	@Select("SELECT COUNT(cid) FROM t_cart_base WHERE cmemberemail = #{0} AND cid = #{1} AND bgenerateorders = false")
	int validByEmailAndId(String email, String id);
}