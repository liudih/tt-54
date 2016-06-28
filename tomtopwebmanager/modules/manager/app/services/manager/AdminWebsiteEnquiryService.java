package services.manager;

import java.util.List;

import mapper.manager.AdminWebsiteMapper;

import com.google.inject.Inject;

import entity.manager.AdminWebsite;

public class AdminWebsiteEnquiryService {
	
	@Inject
	AdminWebsiteMapper websiteMapper;
	
	public List<AdminWebsite> getAdminWebsites()
	{
		return websiteMapper.getWebsite();
	}
	

}
