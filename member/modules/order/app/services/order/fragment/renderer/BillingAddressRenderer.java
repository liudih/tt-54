package services.order.fragment.renderer;

import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;

import dto.Country;
import dto.member.MemberAddress;
import play.twirl.api.Html;
import services.ICountryService;
import services.order.ExistingOrderRenderContext;
import services.order.IOrderFragmentRenderer;
import services.order.OrderRenderContext;
import valueobjects.order_api.Address;
import valueobjects.order_api.IOrderFragment;

public class BillingAddressRenderer implements IOrderFragmentRenderer {

	@Inject
	ICountryService countryService;
	
	@Override
	public Html render(IOrderFragment fragment, OrderRenderContext context) {
		Address address = (Address) fragment;
		if(address == null){
			List<Country> Countries = countryService.getAllCountries();
			address = new Address(null,Lists.newLinkedList(),null,Countries);
		}
		MemberAddress ma = new MemberAddress();
		ma.setIid(0);
		ma.setBdefault(false);
		
		address.getMemberAddresses().add(0, ma);
		
		return views.html.order.billing_address.render(address,
				controllers.member.routes.Address.modifyMemberAddress().url(),
				controllers.member.routes.Address.addNewMemberAddress().url());
	}

	@Override
	public Html renderExisting(IOrderFragment fragment,
			ExistingOrderRenderContext context) {
		return views.html.order.billing_address.render((Address) fragment,
				controllers.member.routes.Address.modifyMemberAddress().url(),
				controllers.member.routes.Address.addNewMemberAddress().url());
	}

}
