package mapper.test;

import org.apache.ibatis.annotations.Insert;


public interface OrderMapper {

	@Insert("insert into t_order_num_test(number) values(#{0})")
	int insert(String num);
}
