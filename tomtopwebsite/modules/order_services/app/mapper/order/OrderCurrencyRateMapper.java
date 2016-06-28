package mapper.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import dto.order.OrderCurrencyRate;

public interface OrderCurrencyRateMapper {
	@Select("select * from t_order_currency_rate where cordernumber = #{0} limit 1")
	OrderCurrencyRate getByOrderNumber(String orderNumber);

	@Insert("insert into t_order_currency_rate (cordernumber, ccurrency, frate) "
			+ "values (#{cordernumber}, #{ccurrency}, #{frate})")
	int insert(OrderCurrencyRate rate);
	
	
	@Select({"<script>",
		"select * from t_order_currency_rate where cordernumber in ",
		"<foreach item='item' index='index' collection='list' open='(' separator=',' close=')'>#{item}</foreach> ",
		"</script>"})
	List<OrderCurrencyRate> getRateByOrderNumbers(@Param("list")List<String> olist);
}
