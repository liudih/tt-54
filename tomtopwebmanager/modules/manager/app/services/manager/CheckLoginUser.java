package services.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.inject.Singleton;

import mapper.manager.AdminUserMapper;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import session.ISessionService;
import base.util.md5.MD5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import forms.manager.login.PublicLoginForm;

@Singleton
public class CheckLoginUser{

	
	@Inject
	AdminUserMapper adminUserMapper;
	
	@Inject
	ISessionService sessionService;
	
	public Map<String,Object> validateLoginUser(PublicLoginForm publicLoginForm ){
		Map<String,Object> validateMap=Maps.newHashMap();
		if(publicLoginForm==null){
			Logger.info("validateLoginUser-------->publicLoginForm:"+publicLoginForm);
			validateMap.put("publicLoginForm", "publicLoginForm null");
			return validateMap;
		}
		
		
		String jobNumber=publicLoginForm.getJobNumber();
		String timestamp=publicLoginForm.getTimestamp();
		String sysName=publicLoginForm.getSysName();
		String signData=publicLoginForm.getSignData();
		String userSessionId=publicLoginForm.getSeId();
		
		validateParams(publicLoginForm.getJobNumber(),"jobNumber",validateMap);
		validateParams(publicLoginForm.getSysName(),"sysName",validateMap);
		//validateParams(times,"timestamp",validateMap);//时间戳校验
		validateParams(publicLoginForm.getSignData(),"signData",validateMap);
		validateParams(publicLoginForm.getSeId(),"seId",validateMap);
		//验证时间
		if(StringUtils.isNotEmpty(timestamp)){
			Logger.info("timestamp:"+timestamp);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");  
	        try {
				Date dt = sdf.parse(timestamp);
				String newStr=sdf.format(new Date());
				Logger.info("newStr:   "+newStr);
				
				Date newDt = sdf.parse(newStr);
				Long diffTm=(newDt.getTime()-dt.getTime())%1000!=0?((newDt.getTime()-dt.getTime())/1000+1):(newDt.getTime()-dt.getTime())/1000;
				Long limit=new Long(Play.application().configuration().getString("login.time.limit"));
				if(diffTm>limit){
					Logger.info("validateLoginUser diffTm:"+diffTm+"  limit:"+limit);
					validateMap.put("timestamp", "time out");
				}else{
					Logger.debug("validateLoginUser diffTm ok");
				}
				
			} catch (ParseException e) {
				validateMap.put("timestamp", e);
				Logger.error("validateLoginUser error",e);
			} 
		}
		//签名验证
		String encodeKey=Play.application().configuration().getString("login.encode.key");
		
		String temp=encodeKey+jobNumber+timestamp+sysName+userSessionId;
		Logger.debug("encodeKey:"+encodeKey+" jobNumber:"+jobNumber+" timestamp："+timestamp+"  sysName:"+sysName+"  userSessionId:"+userSessionId);
		
		String md5 = MD5.md5(temp);
		Logger.info("validateLoginUser encodeKey:"+encodeKey+"  temp:"+temp +"  md5:"+md5);
		if(StringUtils.isNotEmpty(signData) && !signData.equals(md5)){
			Logger.info("validateLoginUser sign fail ! md5:"+md5+"  signData:"+signData);
			validateMap.put("sign", "signData fail");
		}
		//如果参数通过，则验证用户
		if(MapUtils.isEmpty(validateMap)){
			int validateJobNumber = adminUserMapper.validateJobNumber(jobNumber);//验证用户存在
			if(validateJobNumber<=0){// 如何用户不存在
				Logger.info("validateLoginUser jobNumber:"+jobNumber+" not exist");
				validateMap.put("jobNumber", "jobNumber not exist");
			}
		}
		
		//用户通过则验证签名
		return validateMap;
	}
	
	private void validateParams(Object obj,String paramName,Map<String,Object> map){
		if(map==null || StringUtils.isEmpty(paramName)){
			Logger.info("CheckLoginUser validateParams map or paramName null paramName:"+paramName+"  map:"+map);
			return ;
		}
		if(obj instanceof Long){
			Long longParam=(Long)obj;
			if(longParam<=0){
				Logger.debug("validateParams longParam Less than 0");
				map.put(paramName, "Less than 0");
			}
			//检查时间间隔 //时间间隔不超过30s
			long nowTs = new Date().getTime();
			Logger.debug("validateParams nowTs:"+nowTs+"   longParam:"+longParam);
			if(nowTs-longParam>1000*60*2){//2分钟
				Logger.debug("validateParams obj :"+obj+"   paramName:"+paramName+"  times more than 1000*60*2");
				map.put(paramName, "times more than 1000*60*2");
			}
		}else if(obj instanceof String){
			if(StringUtils.isEmpty((String)obj)){
				Logger.debug("validateParams obj :"+obj+"   paramName:"+paramName);
				map.put(paramName, "param empty");
			}
			
		}
	}
	
	public Promise<Map> callBack(String token,String jobNumber,String timestamp,String sysName){
		
		ObjectMapper objectMapper=new ObjectMapper();
		String managermentHost=Play.application().configuration().getString("management.host.url");
		Logger.info("callBack token:"+token+" jobNumber:"+jobNumber+" timestamp:"+timestamp+" sysName:"+sysName+" managermentHost:"+managermentHost);
		return WS
			.client()
			.url(managermentHost+"/public/user/islogin?token="+token+"&jobNumber="+jobNumber+"&timestamp="+timestamp
					+ "&sysName="+sysName)
			.get()
			.map(resp -> objectMapper.convertValue(resp.asJson(),
					Map.class));
	}
}
