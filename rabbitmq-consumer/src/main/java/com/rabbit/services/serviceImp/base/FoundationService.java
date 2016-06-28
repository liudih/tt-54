package com.rabbit.services.serviceImp.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.rabbit.conf.basemapper.BaseMapper;
import com.rabbit.dto.base.SimpleLanguage;
import com.rabbit.services.iservice.base.IFoundationService;


@Scope("singleton")
@Service
public class FoundationService implements IFoundationService {
	public static final String COOKIE_LANG = "TT_LANG";
	public static final String COOKIE_DEVICE = "TT_DEVICE";
	final static String LOGIN_SESSION_NAME = "LOGIN_CONTEXT";
	public static final int DEFAULT_WEB_SITE=1;
	
	@Autowired
	BaseMapper mapper;
	
	public int getSiteID() {
		return DEFAULT_WEB_SITE;//使用默认站点
	}
	@Override
	public List<SimpleLanguage> getAllLanguage() {
		return mapper.getAllSimpleLanguages();
	}
}
