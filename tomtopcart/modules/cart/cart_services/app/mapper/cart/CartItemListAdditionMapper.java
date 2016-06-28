package mapper.cart;

import org.apache.ibatis.annotations.Select;

import dto.cart.CartItemListAddition;

public interface CartItemListAdditionMapper {
    @Select("select * from t_cart_item_list_addition where ccartitemid=#{0} and clistingid=#{1} limit 1")
    CartItemListAddition getCartItemListAdditionByItemIdAndListingId(String itemId,String listingId);
}