package services.interaction;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import mapper.interaction.PriceMatchMapper;
import services.base.FoundationService;
import services.member.login.ILoginService;
import valueobjects.base.Page;

import com.google.api.client.util.Lists;

import dto.interaction.PriceMatch;
import forms.interaction.PriceMatchForm;

public class PriceMatchService {

	@Inject
	PriceMatchMapper priceMatchMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	ILoginService loginService;

	public boolean addPriceMatch(PriceMatchForm form) {
		PriceMatch p = new PriceMatch();
		int siteId = foundationService.getSiteID();
		String userEmail = loginService.getLoginEmail();
		p.setCemail(form.getCemail());
		p.setCinquiry(form.getCinquiry());
		p.setClistingid(form.getClistingid());
		p.setCsku(form.getCsku());
		p.setCsourceurl(form.getCsourceurl());
		p.setDcreatedate(new Date());
		p.setFlowerprice(form.getFlowerprice());
		p.setIwebsiteid(siteId);
		p.setCuseremail(userEmail);
		int flag = priceMatchMapper.insertSelective(p);
		return flag > 0;
	}

	public Page<PriceMatch> getPriceMatchByEmail(String email,
			Integer iwebsiteid, Integer pageSize, Integer page, String filter,
			Integer da) {

		Date start = null;
		Date end = null;
		if (da != 0) {
			end = new Date();
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, (da * (-1)));
			start = calendar.getTime();
		}
		List<String> listings = priceMatchMapper.selectClistingidByfilter(
				filter, start, end);
		if (listings.size() == 0) {
			return new Page<PriceMatch>(Lists.newArrayList(), 0, page, pageSize);
		} else {
			int pageIndex = (page - 1) * pageSize;
			List<PriceMatch> list = priceMatchMapper.getPriceMatchByEmail(
					email, iwebsiteid, pageSize, pageIndex, listings, filter,
					start, end);
			if (list.size() != 0) {
				int count = priceMatchMapper.getCountByEmail(email);
				return new Page<PriceMatch>(list, count, page, pageSize);
			}
			return new Page<PriceMatch>(Lists.newArrayList(), 0, page, pageSize);
		}
	}

}
