package services.shipping;

import java.util.Comparator;
import java.util.List;

import mapper.shipping.ShippingStorageMapper;
import services.base.CountryService;
import services.base.StorageService;
import services.base.WebsiteService;

import com.google.api.client.util.Lists;
import com.google.common.collect.Collections2;
import com.google.common.collect.Ordering;
import com.google.inject.Inject;

import dto.Country;
import dto.Storage;
import dto.Website;
import dto.shipping.ShippingStorage;

public class ShippingServices implements IShippingServices {

	@Inject
	ShippingStorageMapper shippingStorageMapper;

	@Inject
	CountryService countryEnquiryService;

	@Inject
	StorageService storageEnquiryService;

	@Inject
	WebsiteService websiteEnquiryService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingServices#getWebsiteLimitStorage(int)
	 */
	public Storage getWebsiteLimitStorage(int siteId) {
		Website website = websiteEnquiryService.getWebsite(siteId);
		if (website != null) {
			int defaultstoregeid = website.getIdefaultshippingcountry();
			return storageEnquiryService
					.getStorageForStorageId(defaultstoregeid);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingServices#getStorages(java.util.List)
	 */
	public List<ShippingStorage> getStorages(List<String> listingids) {
		// modify by lijun
		if (listingids == null || listingids.size() == 0) {
			return null;
		}
		return shippingStorageMapper.getShoppingStorageForListings(listingids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.shipping.IShippingServices#getCountryDefaultStorage(dto.Country)
	 */
	public Storage getCountryDefaultStorage(Country country) {
		if (country != null) {
			int defaultStorage = country.getIdefaultstorage();
			return storageEnquiryService.getStorageForStorageId(defaultStorage);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingServices#getShippingStorage(int,
	 * java.util.List)
	 */
	public Storage getShippingStorage(int siteId, List<String> listingids) {
		return getShippingStorage(siteId, null, listingids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.shipping.IShippingServices#getShippingStorage(int,
	 * dto.Country, java.util.List)
	 */
	public Storage getShippingStorage(int siteId, Country country,
			List<String> listingids) {
		Storage Websitestorage = getWebsiteLimitStorage(siteId);
		if (Websitestorage != null) {
			return Websitestorage;
		}
		// modify by lijun
		if (listingids == null || listingids.size() == 0) {
			throw new NullPointerException("listingids is null");
		}
		Storage overseasStorage = storageEnquiryService.getNotOverseasStorage();
		List<ShippingStorage> list = getStorages(listingids);
		// Storage storage = getCountryDefaultStorage(country);
		// if (list == null) {
		// return storage;
		// }

		List<ShippingStorage> newList = Lists
				.newArrayList(Collections2.filter(list, e -> !e.getIstorageid()
						.equals(overseasStorage.getIid())));
		Comparator<ShippingStorage> bycount = new Comparator<ShippingStorage>() {
			public int compare(final ShippingStorage p1,
					final ShippingStorage p2) {
				return p1.getIcount().compareTo(p2.getIcount());
			}
		};

		List<ShippingStorage> sortedCopy = Ordering.from(bycount).reverse()
				.sortedCopy(newList);
		if (sortedCopy.size() != 0) {
			if (sortedCopy.get(0).getIcount() == listingids.size()) {
				return storageEnquiryService.getStorageForStorageId(sortedCopy
						.get(0).getIstorageid());
			} else {
				return overseasStorage;
			}
		} else {
			return overseasStorage;
		}
	}

	@Override
	public boolean isSameStorage(List<String> listingids, String storageId) {
		if (listingids == null || listingids.size() == 0 || storageId == null
				|| storageId.length() == 0) {
			throw new NullPointerException();
		}
		List<String> sames = shippingStorageMapper.getSameStorageListings(
				listingids, storageId);
		return listingids.size() == sames.size();
	}
}
