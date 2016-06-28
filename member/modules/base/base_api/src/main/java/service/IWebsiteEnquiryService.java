package service;

import java.util.List;

import dto.Platform;
import dto.Website;


public interface IWebsiteEnquiryService {
	public List<Website> getAll();
	public Website getWebsite(int websiteID);
	public Website getWebsite(String vhost);
	public Website getDefaultWebsite();
	public Website getDefaultWebsite(String vhost, String clientIP);
	public Platform findNearestDomainPlatform(String hostname);
	public List<Website> getWebsitesByWebsiteId(List<Integer> websiteIds);
}
