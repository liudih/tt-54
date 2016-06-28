package dao.wholesale;

import java.util.List;

import dao.IWholeSaleEnquiryDao;
import entity.wholesale.WholeSaleCategory;

public interface IWholeSaleCategoryEnquiryDao extends IWholeSaleEnquiryDao {
	public List<WholeSaleCategory> getWholeSaleCategoryByWholeSaleId(
			Integer wholeSaleId);
}
