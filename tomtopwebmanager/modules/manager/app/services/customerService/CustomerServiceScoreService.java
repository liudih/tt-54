package services.customerService;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import valueobjects.base.Page;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dao.manager.ICustomerServiceScoreEnquiryDao;
import dto.CustomerServiceScoreDTO;
import entity.manager.CustomerServiceScore;
import entity.manager.CustomerServiceScoreType;

public class CustomerServiceScoreService {
	@Inject
	ICustomerServiceScoreEnquiryDao enquiryDao;
	@Inject
	CustomerServiceScoreTypeService typeService;

	private Integer pageSize = 15;

	public Page<CustomerServiceScoreDTO> getPage(int page) {
		List<CustomerServiceScore> list = enquiryDao.getPage(page, pageSize);
		int count = enquiryDao.count();
		return transformToPage(list, count, page);
	}

	public Page<CustomerServiceScoreDTO> searchPage(String name,
			Integer typeID, int page) {
		List<CustomerServiceScore> list = enquiryDao.searchPage(name, typeID,
				page, pageSize);
		int count = enquiryDao.searchCount(name, typeID);
		return transformToPage(list, count, page);
	}

	private Page<CustomerServiceScoreDTO> transformToPage(
			List<CustomerServiceScore> list, int count, int page) {
		List<CustomerServiceScoreType> types = typeService.getAll();
		Map<Integer, CustomerServiceScoreType> typeMap = Maps.uniqueIndex(
				types, t -> t.getIid());
		List<CustomerServiceScoreDTO> dtoList = Lists
				.transform(
						list,
						s -> {
							CustomerServiceScoreDTO dto = new CustomerServiceScoreDTO();
							try {
								BeanUtils.copyProperties(dto, s);
							} catch (Exception e) {
								Logger.error(
										"BeanUtils.copyProperties error: ", e);
								return null;
							}
							dto.setCtype(typeMap.get(s.getItype()) != null ? typeMap
									.get(s.getItype()).getCname() : null);
							return dto;
						});
		dtoList = Lists.newArrayList(Collections2.filter(dtoList,
				d -> d != null));
		return new Page<CustomerServiceScoreDTO>(dtoList, count, page, pageSize);
	}

	int count() {
		return enquiryDao.count();
	}

	int searchCount(String name, Integer typeID) {
		return enquiryDao.searchCount(name, typeID);
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
