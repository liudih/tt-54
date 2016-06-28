package com.rabbit.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("configInfo")
public class ConfigInfo {

	@Value("#{configProperties['remote.elasticsearch.url']}")
	private String remoteElaticsearchUrl;

	@Value("#{configProperties['guphotos.images']}")
	private String guphotosImages;

	@Value("#{configProperties['guphotos.listingImages']}")
	private String guphotosListingImages;

	@Value("#{configProperties['tomtop.images']}")
	private String tomtopImage;

	@Value("#{configProperties['category.cache.url']}")
	private String categoryCacheUrl;
	
	@Value("#{configProperties['www.elasticsearch.url']}")
	private String wwwElasticsearchUrl;

	
	public String getWwwElasticsearchUrl() {
		return wwwElasticsearchUrl;
	}

	public void setWwwElasticsearchUrl(String wwwElasticsearchUrl) {
		this.wwwElasticsearchUrl = wwwElasticsearchUrl;
	}

	public String getCategoryCacheUrl() {
		return categoryCacheUrl;
	}

	public void setCategoryCacheUrl(String categoryCacheUrl) {
		this.categoryCacheUrl = categoryCacheUrl;
	}

	public String getRemoteElaticsearchUrl() {
		return remoteElaticsearchUrl;
	}

	public void setRemoteElaticsearchUrl(String remoteElaticsearchUrl) {
		this.remoteElaticsearchUrl = remoteElaticsearchUrl;
	}

	public String getGuphotosImages() {
		return guphotosImages;
	}

	public void setGuphotosImages(String guphotosImages) {
		this.guphotosImages = guphotosImages;
	}

	public String getGuphotosListingImages() {
		return guphotosListingImages;
	}

	public void setGuphotosListingImages(String guphotosListingImages) {
		this.guphotosListingImages = guphotosListingImages;
	}

	public String getTomtopImage() {
		return tomtopImage;
	}

	public void setTomtopImage(String tomtopImage) {
		this.tomtopImage = tomtopImage;
	}

}