package services.interaction;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dto.interaction.WholesaleInquiry;
import mapper.interaction.WholesaleInquiryMapper;
import forms.interaction.WholesaleInquiryBaseForm;
import forms.interaction.WholesaleInquiryForm;

public class WholesaleService {

	@Inject
	WholesaleInquiryMapper wholesaleInquiryMapper;

	public boolean addWholesaleInquiry(WholesaleInquiryForm form) {
		WholesaleInquiry p = new WholesaleInquiry();
		p.setCemail(form.getCemail().toLowerCase());
		p.setCinquiry(form.getCinquiry());
		p.setClistingid(form.getClistingid());
		p.setCsku(form.getCsku());
		p.setIquantity(form.getIquantity());
		p.setDcreatedate(new Date());
		p.setFtargetprice(form.getFtargetprice());
		p.setCcountrystate(form.getCcountrystate());
		p.setCcompany(form.getCcompany());
		p.setCphone(form.getCphone());
		p.setCname(form.getCname());
		int flag = wholesaleInquiryMapper.insertSelective(p);
		return flag > 0;
	}

	/**
	 * 计算客户批发记录(WholesaleInquiry)的数据总条数
	 * @param WholesaleInquiryForm
	 * @return
	 */
	public Integer getCount(WholesaleInquiryBaseForm WholesaleInquiryForm){
		return wholesaleInquiryMapper.getCount(				
				WholesaleInquiryForm.getStartDate(),
				WholesaleInquiryForm.getEndDate());
	};
	
	/**
	 * 通过创建时间，获取客户批发记录的所有调查数据信息
	 * 
	 * @return
	 */
	public List<WholesaleInquiry> getWholesaleInquiries(
			WholesaleInquiryBaseForm wholesaleInquiryForm) {	

		return wholesaleInquiryMapper.getWholesaleInquiries(
				wholesaleInquiryForm.getStartDate(),
				wholesaleInquiryForm.getEndDate(),
				wholesaleInquiryForm.getPageSize(),
				wholesaleInquiryForm.getPageNum());
	};

}
