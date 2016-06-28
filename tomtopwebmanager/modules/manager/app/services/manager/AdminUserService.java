package services.manager;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.manager.AdminUserMapper;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.Play;
import play.libs.Json;
import play.mvc.Result;
import session.ISessionService;
import valueobjects.base.Page;
import base.util.md5.MD5;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import entity.manager.AdminUser;
import extensions.InjectorInstance;

public class AdminUserService {

	final static int PAGE_SIZE = 10;

	@Inject
	AdminUserMapper adminUserMapper;

	@Inject
	ISessionService sessionService;

	public static AdminUserService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				AdminUserService.class);
	}

	public List<dto.AdminUser> getadminUserList() {
		List<AdminUser> list = adminUserMapper.getAdminUserAll();
		Collection<dto.AdminUser> voList = Collections2.transform(list,
				user -> {
					dto.AdminUser userVo = new dto.AdminUser();
					BeanUtils.copyProperties(user, userVo);
					return userVo;
				});
		return new ArrayList<dto.AdminUser>(voList);
	}

	public Page<dto.AdminUser> getadminUserPage(int page) {
		List<AdminUser> list = adminUserMapper
				.getAdminUserPage(page, PAGE_SIZE);
		Collection<dto.AdminUser> coll = Collections2.transform(list, user -> {
			dto.AdminUser userVo = new dto.AdminUser();
			BeanUtils.copyProperties(user, userVo);
			return userVo;
		});
		int total = adminUserMapper.getAdminUserCount();
		List<dto.AdminUser> voList = new ArrayList<dto.AdminUser>(coll);
		Page<dto.AdminUser> p = new Page<dto.AdminUser>(voList, total, page,
				PAGE_SIZE);
		return p;
	}

	public boolean addAdminUser(AdminUser adminUser) {

		int result = adminUserMapper.insert(adminUser);
		return result > 0 ? true : false;
	}

	public dto.AdminUser getAdminUser(Integer iid) {

		AdminUser adminUser = adminUserMapper.selectByPrimaryKey(iid);
		dto.AdminUser adminUserVo = null;
		if (null != adminUser) {
			adminUserVo = new dto.AdminUser();
			BeanUtils.copyProperties(adminUser, adminUserVo);
		}
		return adminUserVo;
	}
	
	public List<dto.AdminUser> getAdminUsers(List<Integer> iids) {
		List<AdminUser> aus = adminUserMapper.getAdminUserList(iids);
		List<dto.AdminUser> auList = new ArrayList<dto.AdminUser>();
		dto.AdminUser adminUserVo = null;
		for(AdminUser au : aus){
			if (null != au) {
				adminUserVo = new dto.AdminUser();
				BeanUtils.copyProperties(au, adminUserVo);
				auList.add(adminUserVo);
			}
		}
		return auList;
	}

	public boolean deleteAdminUser(Integer iid) {
		int result = adminUserMapper.deleteByPrimaryKey(iid);
		return result > 0 ? true : false;
	}

	public boolean updateAdminUser(AdminUser adminUser) {
		int result = adminUserMapper.updateByPrimaryKeySelective(adminUser);
		return result > 0 ? true : false;
	}

	public AdminUser selectByPrimaryKey(Integer iid) {
		AdminUser adminUser = adminUserMapper.selectByPrimaryKey(iid);

		return adminUser;
	}

	public AdminUser getCuerrentUser() {
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");

		return user;
	}

	public Page<dto.AdminUser> searchAdminUserPage(int page, String cusername,
			String cjobnumber, Integer iroleid) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("page", page);
		param.put("pageSize", PAGE_SIZE);
		param.put("cusername", cusername);
		param.put("cjobnumber", cjobnumber);
		param.put("iroleid", iroleid);
		List<AdminUser> list = adminUserMapper.searchAdminUserPage(param);

		Collection<dto.AdminUser> coll = Collections2.transform(list, user -> {
			dto.AdminUser userVo = new dto.AdminUser();
			BeanUtils.copyProperties(user, userVo);
			return userVo;
		});
		Integer total = adminUserMapper.searchAdminUserCount(param);
		List<dto.AdminUser> voList = Lists.newArrayList(coll);
		Page<dto.AdminUser> p = new Page<dto.AdminUser>(voList, total, page,
				PAGE_SIZE);

		return p;
	}

	/**
	 * For a specified length of the random password
	 * 
	 * @param length
	 * @return
	 */
	public String generatePassword(int length) {
		StringBuilder sBuilder = new StringBuilder("abcdefghigklmnopqrstuvwxyz");
		sBuilder.append("ABCDEFGHIGKLMNOPQRSTUVWXYZ");
		sBuilder.append("~@#$%^&*()_+|.");
		sBuilder.append("1234567890");

		ImmutableList<Character> characters = Lists.charactersOf(sBuilder
				.toString());

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			int number = random.nextInt(characters.size());
			sb.append(characters.get(number));
		}

		return sb.toString();
	}

	public List<AdminUser> getUsers(List<Integer> userids) {
		return adminUserMapper.getAdminUserList(userids);
	}

	public List<dto.AdminUser> getAllAdminUser() {
		List<AdminUser> users = adminUserMapper.getAllAdminUser();
		return Lists.transform(users, u -> {
			dto.AdminUser admin = new dto.AdminUser();
			BeanUtils.copyProperties(u, admin);
			return admin;

		});
	}

	
	public boolean validateJobNumber(String cjobnumber) {
		int result = adminUserMapper.validateJobNumber(cjobnumber);
		Logger.debug(result+"=========cjobnumber==========用户count");
		return result == 0 ? false : true;
	}
	
	public boolean validateUserName(String cusername) {
		int result = adminUserMapper.validateUserName(cusername);
		Logger.debug(result+"===================用户count");
		return result == 0 ? false : true;
	}
	
	public dto.AdminUser getAdminUserByJobnumber(String num){
		return adminUserMapper.getAdminUserByJobnumber(num);
	}
	public String getNewWebUrl(){
		//先验证工号是否已经添加
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");
		String cjobnumber = user.getCjobnumber();
		String encodeKey=Play.application().configuration().getString("login.encode.key");
		String sysName=Play.application().configuration().getString("management.sysName");
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		String timestamp = sdf.format(new Date());
		
		String temp=encodeKey+cjobnumber+timestamp+sysName;
		String md5 = MD5.md5(temp);
		String managermentHost=Play.application().configuration().getString("management.host.url");
		Logger.debug("getNewWebUrl managermentHost:"+managermentHost);
		String link=managermentHost+"/public/login/user?jobNumber="+cjobnumber+"&ts="+java.net.URLEncoder.encode(timestamp)+"&signData="+md5+"&sysName="+sysName;
		return link;
	}
}
