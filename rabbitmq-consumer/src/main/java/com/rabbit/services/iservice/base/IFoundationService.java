package com.rabbit.services.iservice.base;

import java.util.List;

import com.rabbit.dto.base.SimpleLanguage;

public interface IFoundationService {

	public int getSiteID();
	public List<SimpleLanguage> getAllLanguage();
}