package dao.wholesale;

import java.util.List;

import dao.IWholeSaleEnquiryDao;
import entity.wholesale.WholeSaleBase;
import form.wholesale.WholeSaleSearchForm;


public interface IWholeSaleBaseEnquiryDao extends IWholeSaleEnquiryDao {
	public WholeSaleBase getWholeSaleBaseByEmail(String email,
			Integer iwebsiteId);

	public List<WholeSaleBase> getWholeSaleBasesBySearchForm(
			WholeSaleSearchForm wholeSaleSearchForm);

	public Integer getWholeSaleBaseCount(WholeSaleSearchForm wholeSaleSearchForm);
	
	public WholeSaleBase getWholeSaleBaseByIid(Integer iid);
}
