package controllers.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;

import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.WebsiteService;
import services.base.utils.ExcelUtils;
import services.base.utils.StringUtils;
import services.loyalty.bulk.BulkRateService;
import services.member.IMemberBlackUserService;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.MemberGroupService;
import services.member.login.CryptoUtils;
import util.AppsUtil;
import valueobjects.base.Page;
import base.util.md5.MD5;
import context.ContextUtils;
import controllers.InterceptActon;
import dto.member.BlackUser;
import dto.member.MemberBase;
import dto.member.MemberGroup;
import entity.loyalty.Bulk;
import entity.loyalty.BulkRate;
import forms.loyalty.BulkForm;
import forms.loyalty.BulkRateForm;
import forms.member.memberSearch.MemberSearchForm;
import forms.member.register.RegisterUpdateForm;

@With(InterceptActon.class)
public class Member extends Controller {
	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	CryptoUtils crypto;

	@Inject
	IMemberUpdateService mService;

	@Inject
	WebsiteService websiteEnquiryService;

	@Inject
	MemberGroupService memberGroupService;

	@Inject
	BulkRateService bulkRateService;

	@Inject
	IMemberBlackUserService blackUserService;

	@Inject
	FoundationService foundation;

	public Result memberManager() {
		MemberSearchForm memberSearchForm = new MemberSearchForm();
		return ok(views.html.manager.member.message.member_manager
				.render(getMemberList(memberSearchForm)));
	}

	public Result search() {
		Form<MemberSearchForm> memberSearchForm = Form.form(
				MemberSearchForm.class).bindFromRequest();
		return ok(getMemberList(memberSearchForm.get()));
	}

	public Html getMemberList(MemberSearchForm memberSearchForm) {

		List<MemberBase> memberBases = memberEnquiryService
				.searchMemberMessageForDate(memberSearchForm);

		List<String> emails = null;
		if (null != memberBases && memberBases.size() > 0) {
			emails = Lists.transform(memberBases, i -> i.getCemail());
		}
		Map<String, Boolean> emailAndStatusMap = Maps.newHashMap();
		List<BlackUser> blackUserList = blackUserService.getBlackUser(
				memberSearchForm.getSiteId() == null ? foundation.getSiteID()
						: memberSearchForm.getSiteId(), emails);
		if (null != blackUserList && blackUserList.size() > 0) {
			for (BlackUser blackUser : blackUserList) {
				boolean istatus = blackUser.getIstatus() == 0 ? true : false;
				emailAndStatusMap.put(blackUser.getCemail(), istatus);
			}
		}

		if (memberBases.isEmpty()) {
			return views.html.manager.member.message.member_table_list.render(
					memberBases, 0, memberSearchForm.getPageNum(), 0,
					emailAndStatusMap);
		}

		Integer count = memberEnquiryService
				.searchMemberCount(memberSearchForm);
		Integer pageTotal = count / memberSearchForm.getPageSize()
				+ ((count % memberSearchForm.getPageSize() > 0) ? 1 : 0);

		return views.html.manager.member.message.member_table_list.render(
				memberBases, count, memberSearchForm.getPageNum(), pageTotal,
				emailAndStatusMap);
	}

	public Result updatePassword() {
		Form<RegisterUpdateForm> userupdateForm = Form.form(
				RegisterUpdateForm.class).bindFromRequest();
		RegisterUpdateForm registerUpdateForm = userupdateForm.get();
		MemberBase mbase = mService.getMemberById(registerUpdateForm.getIid());
		boolean savaMember = false;
		if (mbase != null) {
			mbase.setCpasswd(MD5.md5(registerUpdateForm.getCnewpassword()));
			savaMember = mService.updateMember(mbase);
		}
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", savaMember);
		return ok(Json.toJson(result));
	}

	public Result showBulk(int page, int pageSize) {
		List<dto.Website> websites = websiteEnquiryService.getAll();
		if (websites.size() == 0)
			return badRequest();
		List<MemberGroup> glist = memberGroupService
				.getMemberGroupsBySiteId(websites.get(0).getIid());
		Map<Integer, dto.Website> webmap = Maps.uniqueIndex(websites,
				w -> w.getIid());
		Map<Integer, MemberGroup> gmap = Maps.uniqueIndex(glist,
				g -> g.getIid());
		Page<Bulk> bulkList = bulkRateService.getBulksPage(page, pageSize);
		for (Bulk b : bulkList.getList()) {
			b.setWebsiteName(webmap.get(b.getIwebsiteid()).getCurl());
			b.setGroupName(gmap.get(b.getIgroupid()).getCgroupname());
		}
		return ok(views.html.manager.member.bulk_manager.render(websites,
				glist, bulkList));
	}

	public Result addBulk() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		play.data.Form<BulkForm> userForm = Form.form(BulkForm.class)
				.bindFromRequest();
		if (userForm.hasErrors()) {
			Iterator err = userForm.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry = (Entry<String, List<ValidationError>>) err
					.next();
			mjson.put("result", entry.getKey() + ":  "
					+ entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}

		boolean flag = bulkRateService.addBulk(userForm.get());
		if (flag) {
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result toEditBulk(Integer id) {
		List<dto.Website> websites = websiteEnquiryService.getAll();
		if (websites.size() == 0)
			return badRequest();
		List<MemberGroup> glist = memberGroupService
				.getMemberGroupsBySiteId(websites.get(0).getIid());
		Bulk b = bulkRateService.getBulkById(id);
		return ok(views.html.manager.member.bulk_edit
				.render(websites, glist, b));
	}

	public Result editBulk() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		play.data.Form<BulkForm> userForm = Form.form(BulkForm.class)
				.bindFromRequest();
		if (userForm.hasErrors()) {
			Iterator err = userForm.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry = (Entry<String, List<ValidationError>>) err
					.next();
			mjson.put("result", entry.getKey() + ":  "
					+ entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}

		boolean flag = bulkRateService.editBulk(userForm.get());
		if (flag) {
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result delBulk(Integer id) {
		if (id == null)
			return badRequest();
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");

		boolean flag = bulkRateService.delBulk(id);
		if (flag) {
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result showBulkrate(Integer bulkid, int page, int pageSize) {
		Page<BulkRate> bulkList = bulkRateService.getBulkRatesPage(bulkid,
				page, pageSize);
		return ok(views.html.manager.member.bulkrate_manager.render(bulkList,
				bulkid));
	}

	public Result addBulkrate() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		play.data.Form<BulkRateForm> userForm = Form.form(BulkRateForm.class)
				.bindFromRequest();
		if (userForm.hasErrors()) {
			Iterator err = userForm.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry = (Entry<String, List<ValidationError>>) err
					.next();
			mjson.put("result", entry.getKey() + ":  "
					+ entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}
		boolean flag = bulkRateService.addBulkRate(userForm.get());
		if (flag) {
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result toEditBulkrate(Integer id) {
		BulkRate b = bulkRateService.getBulkRateById(id);
		return ok(views.html.manager.member.bulkrate_edit.render(b));
	}

	public Result editBulkrate() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		play.data.Form<BulkRateForm> userForm = Form.form(BulkRateForm.class)
				.bindFromRequest();
		if (userForm.hasErrors()) {
			Iterator err = userForm.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry = (Entry<String, List<ValidationError>>) err
					.next();
			mjson.put("result", entry.getKey() + ":  "
					+ entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}

		boolean flag = bulkRateService.editBulkRate(userForm.get());
		if (flag) {
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result delBulkrate(Integer id) {
		if (id == null)
			return badRequest();
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		boolean flag = bulkRateService.delBulkRate(id);
		if (flag) {
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result showMemberGroup(Integer siteid) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		mjson.put("result", "error");
		if (siteid != null) {
			List<MemberGroup> glist = memberGroupService
					.getMemberGroupsBySiteId(siteid);
			mjson.put("list", glist);
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result downloadMemberList(String email, Integer blackUserStatus,
			String contry, Integer siteid, String vhost, String bactivated,
			String bnewsletter, String start, String end, Integer pageSize,
			Integer pageNum) {
		MemberSearchForm memberSearchForm = new MemberSearchForm();
		if (StringUtils.notEmpty(email)) {
			memberSearchForm.setEmail(email);
		}
		memberSearchForm.setBlackUserStatus(blackUserStatus);
		if (StringUtils.notEmpty(contry))
			memberSearchForm.setContry(contry);
		if (StringUtils.notEmpty(start))
			memberSearchForm.setStart(start);
		if (StringUtils.notEmpty(end))
			memberSearchForm.setEnd(end);
		if (StringUtils.notEmpty(bactivated)) {
			memberSearchForm.setBactivated("true".equals(bactivated) ? true
					: false);
		}
		if (StringUtils.notEmpty(bnewsletter)) {
			memberSearchForm.setBnewsletter("true".equals(bnewsletter) ? true
					: false);
		}
		if (StringUtils.notEmpty(vhost)) {
			memberSearchForm.setVhost(vhost);
		}
		memberSearchForm.setSiteId(siteid);
		memberSearchForm.setPageSize(pageSize);
		memberSearchForm.setPageNum(pageNum);

		List<MemberBase> memberBases = memberEnquiryService
				.searchMemberMessageForDate(memberSearchForm);
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> title = new ArrayList<Object>();
		title.add("邮箱");
		title.add("国家");
		title.add("来源");
		title.add("是否激活");
		title.add("是否可发推广邮件");
		title.add("注册时间");

		data.add(title);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		ArrayList<Object> obj = null;
		for (MemberBase mb : memberBases) {
			obj = new ArrayList<Object>();
			obj.add(mb.getCemail());
			obj.add(mb.getCcountry());
			obj.add(mb.getCvhost());
			obj.add(mb.isBactivated());
			obj.add(mb.isBnewsletter());
			if (null != mb.getDcreatedate()) {
				obj.add(sdf.format(mb.getDcreatedate()));
			} else {
				obj.add("");
			}
			data.add(obj);
		}
		String filename = "order-list-"
				+ AppsUtil.getTodayStrFormat("yyyyMMddhhmmss") + ".xlsx";
		ExcelUtils excel = new ExcelUtils();
		byte[] tmpFile = excel.arrayToXLSX(data);
		response().setHeader("Content-disposition",
				"attachment; filename=" + filename);
		return ok(tmpFile).as("application/vnd.ms-excel");
	}
}
