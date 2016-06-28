package mapper.cart;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.CartItemHistoryLite;
import dto.cart.CartItemHistory;

public interface CartItemHistoryMapper {
    int insert(CartItemHistory record);

    @Select("select * from t_cart_item_history where cuuid = #{0} and clistingid = #{1} limit 1")
    CartItemHistory getCartItemHistoryByCuuid(String cuuid, String clistingid);
    
    @Select("select * from t_cart_item_history where cmemberemail = #{0} and clistingid = #{1} limit 1")
    CartItemHistory getCartItemHistoryByCmemberemail(String cmemberemail, String clistingid);
    
    @Select("select tl.icartitemid ,tl.clistingid, ti.dcreatedate "
    		+ "from t_cart_item ti inner join t_cart_item_list tl "
    		+ "on ti.iid = tl.icartitemid and tl.icartitemid = #{0}")
    List<CartItemHistoryLite> getCartItemHistorysByCartItemId(Integer cartItemId);
    
}