package valueobjects.order_api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import dto.Country;
import dto.member.MemberAddress;

public class Address implements IOrderFragment,Serializable {

	private static final long serialVersionUID = 1L;
	
	final MemberAddress defaultAddress;
	final List<MemberAddress> memberAddresses;

	final transient List<Country> allCountries;
	final transient Map<Integer, Country> countryMap;

	public Address(MemberAddress defaultAddress,
			List<MemberAddress> memberAddresses,
			Map<Integer, Country> countryMap, List<Country> allCountries) {
		super();
		this.defaultAddress = defaultAddress;
		this.memberAddresses = memberAddresses;
		this.allCountries = allCountries;
		this.countryMap = countryMap;
	}

	public MemberAddress getDefaultAddress() {
		return defaultAddress;
	}

	public Map<Integer, Country> getCountryMap() {
		return countryMap;
	}

	public List<MemberAddress> getMemberAddresses() {
		return memberAddresses;
	}

	public List<Country> getAllCountries() {
		return allCountries;
	}
}