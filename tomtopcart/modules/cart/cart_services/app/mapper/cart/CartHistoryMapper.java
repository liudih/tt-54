package mapper.cart;

import dto.cart.CartHistory;

public interface CartHistoryMapper {
    int deleteByPrimaryKey(Integer iid);

    int insert(CartHistory record);

    int insertSelective(CartHistory record);

    CartHistory selectByPrimaryKey(Integer iid);

    int updateByPrimaryKeySelective(CartHistory record);

    int updateByPrimaryKey(CartHistory record);
}