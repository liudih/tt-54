package handlers.mobile;

import java.util.Date;

import javax.inject.Inject;

import mapper.interaction.ProductBrowseMapper;

import com.google.common.eventbus.Subscribe;

import dto.interaction.ProductBrowse;
import events.mobile.ViewProductEvent;

public class ViewProductHandler {

	@Inject
	ProductBrowseMapper mapper;

	@Subscribe
	public void visitProduct(ViewProductEvent event) {
		ProductBrowse log = new ProductBrowse();
		log.setClistingid(event.getGid());
		log.setCsku(event.getSku());
		log.setIplatformid(event.getPlatformid());
		log.setIwebsiteid(event.getSitid());
		log.setCltc(event.getUuid());
		log.setCstc(event.getUuid());
		log.setImemberid(event.getMemberid());
		log.setDcreatedate(new Date());
		mapper.insert(log);
	}

}
