package dto;

import java.util.List;

import valueobjects.order_api.ShippingMethodInformations;
import dto.order.dropShipping.DropShippingOrder;
import dto.order.dropShipping.DropShippingOrderDetail;

public class DropShippingOrderDTO extends DropShippingOrder {
	private List<DropShippingOrderDetail> details;
	private ShippingMethodInformations shippingMethods;

	public DropShippingOrderDTO(DropShippingOrder o) {
		if (o != null) {
			setIid(o.getIid());
			setCdropshippingid(o.getCdropshippingid());
			setCuseremail(o.getCuseremail());
			setCuserorderid(o.getCuserorderid());
			setIwebsiteid(o.getIwebsiteid());
			setCcountrysn(o.getCcountrysn());
			setCcountry(o.getCcountry());
			setCstreetaddress(o.getCstreetaddress());
			setCcity(o.getCcity());
			setCprovince(o.getCprovince());
			setCpostalcode(o.getCpostalcode());
			setCtelephone(o.getCtelephone());
			setCfirstname(o.getCfirstname());
			setCcnote(o.getCcnote());
			setCerrorlog(o.getCerrorlog());
			setFtotal(o.getFtotal());
			setDcreatedate(o.getDcreatedate());
			setDuserdate(o.getDuserdate());
		}
	}

	public List<DropShippingOrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<DropShippingOrderDetail> details) {
		this.details = details;
	}

	public ShippingMethodInformations getShippingMethods() {
		return shippingMethods;
	}

	public void setShippingMethods(ShippingMethodInformations shippingMethods) {
		this.shippingMethods = shippingMethods;
	}

}
