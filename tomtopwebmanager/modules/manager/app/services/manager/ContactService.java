package services.manager;

import java.util.List;

import javax.inject.Inject;

import dto.mobile.ContactDto;
import mapper.ContactMapper;


public class ContactService {

	@Inject
	ContactMapper contactMapper;

	public List<ContactDto> getAllAppVersion() {
		return contactMapper.getAllContact();
	}

}
