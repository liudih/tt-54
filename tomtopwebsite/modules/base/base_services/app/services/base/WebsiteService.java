package services.base;

import interceptors.CacheResult;

import java.util.List;

import javax.inject.Inject;

import mapper.base.PlatformMapper;
import mapper.base.WebsiteMapper;
import play.Logger;
import services.IWebsiteService;
import services.base.geoip.GeoIPService;
import dto.Platform;
import dto.Website;

public class WebsiteService implements IWebsiteService {

	@Inject
	WebsiteMapper wsmapper;

	@Inject
	GeoIPService geoip;

	@Inject
	PlatformMapper pfmapper;

	/* (non-Javadoc)
	 * @see services.base.IWebsiteService#getAll()
	 */
	@Override
	@CacheResult
	public List<Website> getAll() {
		List<Website> list = wsmapper.getAll();
		return list;
	}

	/* (non-Javadoc)
	 * @see services.base.IWebsiteService#getWebsite(int)
	 */
	@Override
	@CacheResult
	public Website getWebsite(int websiteID) {
		return wsmapper.selectByPrimaryKey(websiteID);
	}

	/* (non-Javadoc)
	 * @see services.base.IWebsiteService#getWebsite(java.lang.String)
	 */
	@Override
	@CacheResult
	public Website getWebsite(String vhost) {
		return wsmapper.findByHostname(vhost);
	}

	/* (non-Javadoc)
	 * @see services.base.IWebsiteService#getDefaultWebsite()
	 */
	@Override
	@CacheResult
	public Website getDefaultWebsite() {
		return wsmapper.selectDefaultWebsite();
	}

	/* (non-Javadoc)
	 * @see services.base.IWebsiteService#getDefaultWebsite(java.lang.String, java.lang.String)
	 */
	@Override
	@CacheResult
	public Website getDefaultWebsite(String vhost, String clientIP) {
		try {
			Website website = wsmapper.findByHostname(vhost);
			if (website != null) {
				// hit, exit
				return website;
			}
			Platform platform = findNearestDomainPlatform(vhost);
			String countryCode = geoip.getCountryCode(clientIP);
			Logger.debug("Client IP's Country: {} -> {}", clientIP, countryCode);
			if (countryCode != null && platform != null) {
				Website countryDefault = wsmapper.findCountryDefault(
						countryCode, platform.getIid());
				if (countryDefault != null) {
					Logger.debug("Country Default Website: "
							+ countryDefault.getCurl());
					return countryDefault;
				}
			}
		} catch (Exception e) {
			Logger.error("Cannot find default website", e);
		}
		return null;
	}

	/**
	 * Search the nearest platform for the given hostname: e.g. www.tomtop.com,
	 * we search each level one by one (www.tomtop.com, tomtop.com, com) until
	 * an exact match.
	 *
	 * @param hostname
	 * @return
	 */
	@CacheResult
	Platform findNearestDomainPlatform(String hostname) {
		Platform platform = pfmapper.findByDomain(hostname);
		if (platform != null)
			return platform;
		int dotPos = hostname.indexOf('.');
		if (dotPos < 0) {
			return null;
		}
		String subhostname = hostname.substring(dotPos + 1, hostname.length());
		return findNearestDomainPlatform(subhostname);
	}

	/* (non-Javadoc)
	 * @see services.base.IWebsiteService#getWebsitesByWebsiteId(java.util.List)
	 */
	@Override
	public List<Website> getWebsitesByWebsiteId(List<Integer> websiteIds) {
		return wsmapper.getWebsitesByWebsiteIds(websiteIds);
	}

}
