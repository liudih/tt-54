package services.loyalty;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import mapper.loyalty.MemberEdmMapper;


import play.Configuration;
import play.Logger;
import play.Play;
import services.product.CategoryEnquiryService;
import services.webservice.rspread.DoubleOptIn;
import services.webservice.rspread.ServiceSoapProxy;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import entity.loyalty.MemberEdm;

/** 
* @ClassName: MemberEdmService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author liudi
* @date 2015年3月6日 下午5:29:19   
*/
public class MemberEdmService {

	@Inject
	MemberEdmMapper memberEdmMapper;
	
	@Inject
	CategoryEnquiryService categoryEnquiryService;
	
	public List<String> addMemberEdm(String[] categoryArr,String email,int websiteId){
		List<MemberEdm> existList = memberEdmMapper.getEdmByEmail(email);
		List<String> existCategory = Lists.transform(existList, list->list.getCcategory());
		Set<String> arrset = Sets.newHashSet(categoryArr);
		if(existCategory.size()>0){
			arrset = Sets.filter(arrset, cate -> !existCategory.contains(cate));
		}
		for(String cate : arrset){
			MemberEdm m = new MemberEdm();
			m.setCcategory(cate);
			m.setCemail(email);
			m.setDcreatedate(new Date());
			m.setBenabled(true);
			m.setIwebsiteid(websiteId);
			memberEdmMapper.insertSelective(m);
		}
		List<String> allCategory = Lists.newArrayList();
		allCategory.addAll(existCategory);
		allCategory.addAll(arrset);
		return allCategory;
	}
	
	/**
	  * @param arr
	  * @param email void
	  * @Title: callWebservice
	  * @Description: TODO(同步到思齐第三方服务器)
	  * @author liudi
	  */
	public void callWebservice(String[] categoryArr , String email,int languageid,
			int websiteid){
		Configuration config = Play.application().configuration()
				.getConfig("rspread");
		String url = config.getString("url");
		String account = config.getString("account");
		String password = config.getString("password");
		
		ServiceSoapProxy ws = new ServiceSoapProxy();
		ws.setEndpoint(url);
		DoubleOptIn addOption = DoubleOptIn.Off;
		
		for(String cn : categoryArr){
			try {
				boolean flag = ws.createSubscription(account,password,
						cn,cn);
				boolean flag2 = ws.addSubscriberByEmail(account,password,
						email,cn, addOption);
			}catch (RemoteException e) {
				Logger.debug(e.toString());
				if(e.toString().indexOf("\""+cn+"\" already exist")>0){
					try {
						boolean flag3 = ws.addSubscriberByEmail(account,password,
							email,cn, addOption);
					}catch (RemoteException e1) {
						Logger.debug(e1.toString());
					}
				}
			}
		}
	}
}
