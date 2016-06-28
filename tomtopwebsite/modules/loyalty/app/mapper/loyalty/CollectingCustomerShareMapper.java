package mapper.loyalty;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import dto.CustomerShareDto;

public interface CollectingCustomerShareMapper {
	/**
	 * 添加客户分享数据
	 * 
	 * @param customerShareDto
	 */
	@Insert("insert into t_collect_customer_share(cemail,curl,ctype,ccountry)"
			+ " values(#{cemail},#{curl},#{ctype},#{ccountry})")
	public void addCustomerShare(CustomerShareDto customerShareDto);

	/**
	 * 根据条件查询客服分享
	 * 
	 * @param customerShareDto
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<CustomerShareDto> queryCustomerShareInfo(
			@Param("page") int page, @Param("pageSize") int pageSize,
			@Param("customerShareDto") CustomerShareDto customerShareDto);
	/**
	 * 根据条件查询客服分享
	 * 
	 * @param customerShareDto
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<CustomerShareDto> exportCustomerShareInfo(@Param("customerShareDto") CustomerShareDto customerShareDto);
	/**
	 * 根据条件查询客服分享总数
	 * 
	 * @param customerShareDto
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public int queryCountCustomerShareInfo(
			@Param("customerShareDto") CustomerShareDto customerShareDto);
}
