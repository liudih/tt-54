package services.base;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mapper.base.ShorturlMapper;
import mapper.base.VisitLogMapper;
import base.util.DateFormatUtils;
import base.util.ShortUrlGenerator;

import com.google.inject.Inject;

import entity.base.Shorturl;
import entity.base.VisitLog;
import events.base.VisitEvent;
import form.base.ShorturlForm;
import form.base.VisitForm;

public class ShorturlService {
	@Inject
	ShorturlMapper shortMapper;
	@Inject
	VisitLogMapper visitLogMapper;
	
	public String addShorturl(ShorturlForm form,String host){
		Shorturl s = new Shorturl();
		String uniqueurl = form.getUrl();
		if(uniqueurl.indexOf("?")==-1){
			uniqueurl += "?aid="+form.getAid()+"&taskid="+form.getTaskid();
		}else{
			uniqueurl += "&aid="+form.getAid()+"&taskid="+form.getTaskid();
		}
		String code = ShortUrlGenerator.shortUrl(uniqueurl)[0];
		Shorturl ss = shortMapper.getShorturlBySurl(code);
		if(ss!=null && form.getUrl().equals(ss.getCurl())){
			return ss.getCshorturl();
		}
		s.setCurl(form.getUrl());
		s.setCshorturl(host+"/"+code);
		s.setCshorturlcode(code);
		s.setCaid(form.getAid());
		s.setCeid(form.getTaskid());
		s.setItasktype(form.getTasktype());
		s.setDcreatedate(new Date());
		int flag = shortMapper.addShorturl(s);
		if(flag>0){
			return s.getCshorturl();
		}else{
			return null;
		}
	}
	
	public Shorturl getLongurl(String surl){
		Shorturl s = shortMapper.getShorturlBySurl(surl);
		if(s!=null){
			return s;
		}else{
			return null;
		}
	}
	
	public boolean addVisit(VisitEvent event){
		VisitLog v = new VisitLog();
		v.setCaid(event.getCaid());
		v.setCip(event.getCip());
		v.setCshorturlcode(event.getCshorturlcode());
		v.setCsource(event.getCsource()==null?"":event.getCsource());
		v.setDcreatedate(new Date());
		v.setCurl(event.getCurl());
		v.setCtaskid(event.getCtaskid());
		v.setItasktype(event.getItasktype());
		int flag = visitLogMapper.insertSelective(v);
		return flag>0;
	}
	
	public List<dto.VisitLog> getVisit(VisitForm v){
		Date sd = DateFormatUtils.getDateByTimestamp(v.getStartdate());
		Date ed = DateFormatUtils.getDateByTimestamp(v.getEnddate());
		//默认在一个月范围
		if(sd==null){
			sd = DateFormatUtils.getNowBeforeByDay(Calendar.MONTH, -1);
		}
		return visitLogMapper.getVisitLogs(v.getAid(),sd,ed);
	}
	
}
