package services.mobile.personal;

import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import mapper.ContactMapper;

import org.apache.commons.lang3.StringUtils;

import services.mobile.MobileService;
import services.mobile.member.LoginService;
import utils.ValidataUtils;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.member.MobileContext;
import entity.mobile.Contact;

public class ContactService {

	@Inject
	ContactMapper contactMapper;

	@Inject
	MobileService mobileService;

	@Inject
	LoginService loginService;

	public HashMap<String, Object> checkContact(String title, String content,
			String email) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		if (StringUtils.isEmpty(title)) {
			map.put("re", BaseResultType.CONTACT_TITLE_EMPTY_ERROR_CODE);
			map.put("msg", BaseResultType.CONTACT_TITLE_EMPTY_ERROR_MSG);
			return map;
		}
		if (StringUtils.isEmpty(content)) {
			map.put("re", BaseResultType.CONTACT_CONTENT_EMPTY_ERROR_CODE);
			map.put("msg", BaseResultType.CONTACT_CONTENT_EMPTY_ERROR_MSG);
			return map;
		}
		if (StringUtils.isEmpty(email)) {
			map.put("re", BaseResultType.CONTACT_EMAIL_EMPTY_ERROR_CODE);
			map.put("msg", BaseResultType.CONTACT_EMAIL_EMPTY_ERROR_MSG);
			return map;
		}
		if (!ValidataUtils.checkEmail(email)) {
			map.put("re", BaseResultType.CONTACT_EMAIL_FORMAT_ERROR_CODE);
			map.put("msg", BaseResultType.CONTACT_EMAIL_FORMAT_ERROR_MSG);
			return map;
		}
		if (title.length() > 50) {
			map.put("re", BaseResultType.CONTACT_TITLE_LENGTH_OVER_ERROR_CODE);
			map.put("msg", BaseResultType.CONTACT_TITLE_LENGTH_OVER_ERROR_MSG);
			return map;
		}
		if (content.length() > 200) {
			map.put("re", BaseResultType.CONTACT_CONTENT_LENGTH_OVER_ERROR_CODE);
			map.put("msg", BaseResultType.CONTACT_CONTENT_LENGTH_OVER_ERROR_MSG);
			return map;
		}
		if (email.length() > 50) {
			map.put("re", BaseResultType.CONTACT_EMAIL_LENGTH_OVER_ERROR_CODE);
			map.put("msg", BaseResultType.CONTACT_EMAIL_LENGTH_OVER_ERROR_MSG);
			return map;
		}
		map.put("re", BaseResultType.SUCCESS);
		return map;
	}

	/**
	 * 保存APP设置
	 * 
	 * @param setting
	 * @return
	 */
	public boolean saveContact(String title, String content, String email) {
		String uuid = mobileService.getUUID();
		Contact contact = new Contact();
		contact.setUuid(uuid);
		contact.setTitle(title);
		contact.setContent(content);
		contact.setCreatedate(new Date());
		contact.setDevice(mobileService.getMobileContext().getIplatform() + "");
		MobileContext mobileContext = mobileService.getMobileContext();
		contact.setSysversion(mobileContext.getCsysversion());
		contact.setImei(mobileContext.getCimei());
		contact.setPhonename(mobileContext.getCphonename());
		contact.setLanguageid(mobileContext.getIlanguageid() + "");
		contact.setCurrentversion(mobileContext.getCurrentversion() + "");
		contact.setStatus(1); // 未处理
		contact.setMemberemail(email);
		int result = contactMapper.insert(contact);
		return result > 0 ? true : false;
	}

}
