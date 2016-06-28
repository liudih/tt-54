package valueobjects.member;

import java.util.List;
import java.util.Map;

import dto.Country;

public class MemberAddress {

	final dto.member.MemberAddress defaultAddress;
	final List<dto.member.MemberAddress> memberAddresses;
	final List<Country> allCountries;
	final Map<Integer, Country> countryMap;

	public MemberAddress(dto.member.MemberAddress defaultAddress,
			List<dto.member.MemberAddress> memberAddresses,
			List<Country> allCountries, Map<Integer, Country> countryMap) {
		super();
		this.defaultAddress = defaultAddress;
		this.memberAddresses = memberAddresses;
		this.allCountries = allCountries;
		this.countryMap = countryMap;
	}

	public List<Country> getAllCountries() {
		return allCountries;
	}

	public Map<Integer, Country> getCountryMap() {
		return countryMap;
	}

	public List<dto.member.MemberAddress> getMemberAddresses() {
		return memberAddresses;
	}

	public dto.member.MemberAddress getDefaultAddress() {
		return defaultAddress;
	}

}
