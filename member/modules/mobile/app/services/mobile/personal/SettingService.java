package services.mobile.personal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.SettingMapper;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.lang.LanguageService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import valuesobject.mobile.member.MobileContext;

import com.twelvemonkeys.lang.StringUtil;

import dto.Country;
import dto.Currency;
import dto.Language;
import entity.mobile.Setting;

public class SettingService {

	@Inject
	SettingMapper settingMapper;

	@Inject
	MobileService mobileService;

	@Inject
	CountryService countryService;

	@Inject
	CurrencyService currencyService;

	@Inject
	LanguageService languageService;

	@Inject
	LoginService loginService;

	public boolean checkSetting(Integer type, String val) {
		List<Integer> typeList = new ArrayList<>();
		typeList.add(1);
		typeList.add(2);
		typeList.add(3);
		if (StringUtil.isEmpty(val) || type == null || !typeList.contains(type)) {
			return false;
		}
		return true;
	}

	/**
	 * 修改或保存APP设置
	 * 
	 * @param setting
	 * @return
	 */
	public boolean updateSetting(Integer type, String val) {
		String uuid = mobileService.getUUID();
		Setting setting = settingMapper.getSettingBymember(uuid);
		boolean isadd = false;
		if (setting == null) {
			setting = new Setting();
			isadd = true;
			setting.setUuid(uuid);
			setting.setModifydate(new Date());
			MobileContext mobileContext = mobileService.getMobileContext();
			setting.setImei(mobileContext.getCimei());
			setting.setCurrentversion(mobileContext.getCurrentversion() + "");
			if (loginService.isLogin()) {
				setting.setMemberemail(loginService.getLoginMemberEmail());
			}
		}
		setting.setDevice(mobileService.getMobileContext().getIplatform() + "");
		switch (type) {
		case 1:
			Country country = countryService.getCountryByCountryId(Integer
					.valueOf(val));
			setting.setCountryid(val);
			setting.setCountry(country.getCname());
			break;
		case 2:
			Language language = languageService.getLanguage(Integer
					.valueOf(val));
			setting.setLanguageid(val);
			setting.setLanguage(language.getCname());
			break;
		case 3:
			Currency currency = currencyService.getCurrencyById(Integer
					.valueOf(val));
			setting.setCurrencyid(val);
			setting.setCurrency(currency.getCcode());
			break;
		}

		int result = 0;
		if (isadd) {
			result = settingMapper.insert(setting);
		} else {
			result = settingMapper.updateByMember(setting);
		}
		return result > 0 ? true : false;
	}

	public Setting getSetting() {
		String uuid = mobileService.getUUID();
		Setting setting = settingMapper.getSettingBymember(uuid);
		// SettingInfo settingInfo = new SettingInfo();
		// BeanUtils.copyProperties(settingInfo, setting);
		return setting;
	}

}
