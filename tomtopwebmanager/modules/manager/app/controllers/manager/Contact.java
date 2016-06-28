package controllers.manager;

import java.util.List;

import javax.inject.Inject;

import dto.mobile.ContactDto;
import play.mvc.Controller;
import play.mvc.Result;
import services.manager.ContactService;

@controllers.AdminRole(menuName = "ContactMgr")
public class Contact extends Controller {

	@Inject
	ContactService contactService;

	public Result list() {
		List<ContactDto> contactList = contactService.getAllAppVersion();
		return ok(views.html.manager.contact.contact_manager
				.render(contactList));
	}

}
