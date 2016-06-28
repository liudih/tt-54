package services.loyalty.collectshare;

import java.util.List;

import javax.inject.Inject;

import mapper.loyalty.CollectingCustomerShareMapper;
import dto.CustomerShareDto;

/**
 * 客户分享服务类
 * 
 * @author Administrator
 *
 */
public class CollectingCustomerShareService {

	@Inject
	CollectingCustomerShareMapper collectingCustomerShareMapper;

	/**
	 * 添加客户分享数据
	 * 
	 * @param customerShareDto
	 */
	public void addCustomerShare(CustomerShareDto customerShareDto) {
		collectingCustomerShareMapper.addCustomerShare(customerShareDto);
	}

	/**
	 * 根据条件查询客户分享信息
	 * 
	 * @param customerShareDto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<CustomerShareDto> queryCustomerShare(
			CustomerShareDto customerShareDto, int page, int pageSize) {
		return collectingCustomerShareMapper.queryCustomerShareInfo(page,
				pageSize, customerShareDto);
	}
	/**
	 * 导出客户分享信息
	 * 
	 * @param customerShareDto
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<CustomerShareDto> exportCustomerShare(
			CustomerShareDto customerShareDto) {
		return collectingCustomerShareMapper.exportCustomerShareInfo( customerShareDto);
	}
	/**
	 * 通过条件查询客户数据总数
	 * 
	 * @param customerShareDto
	 * @return
	 */
	public int queryCountCustomerShare(CustomerShareDto customerShareDto) {
		return collectingCustomerShareMapper
				.queryCountCustomerShareInfo(customerShareDto);
	}
}
