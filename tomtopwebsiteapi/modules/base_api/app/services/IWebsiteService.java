package services;

import java.util.List;

import dto.Website;

public interface IWebsiteService {

	public abstract List<Website> getAll();

	public abstract Website getWebsite(int websiteID);

	public abstract Website getWebsite(String vhost);

	public abstract Website getDefaultWebsite();

	/**
	 * Given a hostname and a client IP, find the most appropriate default
	 * website.
	 *
	 * @param vhost
	 * @param clientIP
	 * @return Most appropriate Website
	 */
	public abstract Website getDefaultWebsite(String vhost, String clientIP);

	public abstract List<Website> getWebsitesByWebsiteId(
			List<Integer> websiteIds);

}