package services.customerService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import services.base.utils.DateFormatUtils;
import services.manager.AdminUserService;
import valueobjects.base.Page;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import dao.manager.ICustomerServiceScheduleEnquiryDao;
import dao.manager.ICustomerServiceScheduleUpdateDao;
import dto.AdminUser;
import entity.manager.CustomerServiceSchedule;
import forms.CustomerServiceScheduleForm;
import forms.CustomerServiceScheduleSearchForm;

public class CustomerServiceScheduleService {
	@Inject
	ICustomerServiceScheduleEnquiryDao enquiryDao;
	@Inject
	ICustomerServiceScheduleUpdateDao updateDao;
	@Inject
	AdminUserService userService;

	private int pageSize = 15;

	public boolean insert(CustomerServiceSchedule schedule) {
		int i = updateDao.insert(schedule);
		return 1 == i ? true : false;
	}

	public boolean insert(CustomerServiceScheduleForm form) {
		if (!validate(form)) {
			return false;
		}
		CustomerServiceSchedule schedule = new CustomerServiceSchedule();
		schedule.setIuserid(form.getIuserid());
		schedule.setDstartdate(DateFormatUtils.dateTransformBetweenTimeZone(
				form.getDstartdate(), TimeZone.getTimeZone("Asia/Shanghai"),
				TimeZone.getDefault()));
		schedule.setDenddate(DateFormatUtils.dateTransformBetweenTimeZone(
				form.getDenddate(), TimeZone.getTimeZone("Asia/Shanghai"),
				TimeZone.getDefault()));
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(schedule.getDstartdate());
		SimpleDateFormat sdf = DateFormatUtils.getSimpleDateFormat("EEE");
		schedule.setCdayofweek(sdf.format(schedule.getDstartdate()));
		schedule.setIweekofyear(calendar.get(Calendar.WEEK_OF_YEAR));
		return insert(schedule);
	}

	private boolean validate(CustomerServiceScheduleForm form) {
		if (form.getDenddate().before(form.getDstartdate())) {
			return false;
		}
		GregorianCalendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(form.getDstartdate());
		GregorianCalendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(form.getDenddate());
		int d = endCalendar.get(Calendar.DAY_OF_YEAR)
				- startCalendar.get(Calendar.DAY_OF_YEAR);
		int y = endCalendar.get(Calendar.YEAR)
				- startCalendar.get(Calendar.YEAR);
		if (d != 0 || y != 0) {
			return false;
		}
		return true;
	}

	public boolean deleteByID(int id) {
		int i = updateDao.delete(id);
		return 1 == i ? true : false;
	}

	public List<CustomerServiceSchedule> getByWeekOfYear(int weekOfYear) {
		return enquiryDao.getByWeekOfYear(weekOfYear);
	}

	public Page<dto.CustomerServiceSchedule> getPage(int p) {
		List<CustomerServiceSchedule> list = enquiryDao.getPage(p, pageSize);
		int total = enquiryDao.getCount();
		Page<dto.CustomerServiceSchedule> page = transformToDTO(list, total, p);
		return page;
	}

	public Page<dto.CustomerServiceSchedule> searchPage(
			CustomerServiceScheduleSearchForm searchForm) {
		searchForm.setPageSize(pageSize);
		List<CustomerServiceSchedule> list = enquiryDao.searchPage(searchForm);
		int total = enquiryDao.getCount(searchForm);
		Page<dto.CustomerServiceSchedule> page = transformToDTO(list, total,
				searchForm.getP());
		return page;
	}

	private Page<dto.CustomerServiceSchedule> transformToDTO(
			List<CustomerServiceSchedule> list, int total, int p) {
		List<dto.CustomerServiceSchedule> dtoList = Lists
				.transform(
						list,
						s -> {
							try {
								dto.CustomerServiceSchedule dto = new dto.CustomerServiceSchedule();
								BeanUtils.copyProperties(dto, s);
								AdminUser user = userService.getAdminUser(s
										.getIuserid());
								if (null != user) {
									dto.setUserName(user.getCusername());
								}
								return dto;
							} catch (Exception e) {
								Logger.error("transform error:", e);
							}
							return null;
						});
		Collection<dto.CustomerServiceSchedule> temp = Collections2.filter(
				dtoList, e -> e != null);
		temp.forEach(c -> {
			c.setDstartdate(DateFormatUtils.dateTransformBetweenTimeZone(
					c.getDstartdate(), TimeZone.getDefault(),
					TimeZone.getTimeZone("Asia/Shanghai")));
			c.setDenddate(DateFormatUtils.dateTransformBetweenTimeZone(
					c.getDenddate(), TimeZone.getDefault(),
					TimeZone.getTimeZone("Asia/Shanghai")));
		});
		Page<dto.CustomerServiceSchedule> page = new Page<dto.CustomerServiceSchedule>(
				Lists.newArrayList(temp), total, p, pageSize);
		return page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
