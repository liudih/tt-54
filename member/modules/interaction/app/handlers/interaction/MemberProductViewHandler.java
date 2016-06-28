package handlers.interaction;

import java.util.Date;

import javax.inject.Inject;

import mapper.interaction.ProductBrowseMapper;
import play.Logger;
import valueobjects.member.MemberInSession;

import com.google.common.eventbus.Subscribe;

import dto.interaction.ProductBrowse;
import events.product.ProductViewEvent;

public class MemberProductViewHandler {

	@Inject
	ProductBrowseMapper mapper;

	@Subscribe
	public void onView(ProductViewEvent event) {
		Logger.trace(
				"MemberProductViewHandler(ProductViewEvent: Login={}, Product={})",
				event.getLoginContext(), event.getProductContext());
		ProductBrowse log = new ProductBrowse();
		log.setClistingid(event.getProductContext().getListingID());
		log.setCsku(event.getProductContext().getSku());
		log.setIwebsiteid(event.getProductContext().getSiteID());
		log.setCltc(event.getLoginContext().getLTC());
		log.setCstc(event.getLoginContext().getSTC());
		MemberInSession mis = (MemberInSession) event.getLoginContext()
				.getPayload();
		if (mis != null) {
			log.setImemberid(mis.getMemberId());
		}
		log.setDcreatedate(new Date());
		mapper.insert(log);
	}

}
