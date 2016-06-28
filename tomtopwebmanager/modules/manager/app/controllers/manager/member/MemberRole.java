package controllers.manager.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.category.recommend.CategoryProductRecommendService;
import services.member.IMemberRoleService;
import session.ISessionService;
import valueobjects.base.Page;
import dto.Website;
import dto.member.MemberRoleInfo;
import dto.member.role.MemberRoleBase;
import entity.manager.AdminUser;
import forms.MemberUserRoleForm;

public class MemberRole extends Controller {

	@Inject
	IMemberRoleService memberRoleService;
	@Inject
	ISessionService sessionService;
	@Inject
	CategoryProductRecommendService categoryProductRecommendService;

	public Result search(String email, String siteId, String memberRole,
			Integer page, Integer pageSize) {
		Integer isiteid = null;
		List<Integer> roleList = null;
		if (memberRole != null && !"".equals(memberRole)) {
			roleList = new ArrayList<Integer>();
			if (memberRole.indexOf(",") >= 0) {
				String[] roleids = memberRole.split("\\,");
				int len = roleids.length;
				for (int i = 0; i < len; i++) {
					roleList.add(Integer.parseInt(roleids[i]));
				}
			} else {
				roleList.add(Integer.parseInt(memberRole));
			}
		}
		if (siteId != null && !"".equals(siteId)) {
			isiteid = Integer.parseInt(siteId);
		}
		if ("".equals(email)) {
			email = null;
		}
		List<MemberRoleBase> memberRoleList = memberRoleService.getMemberRole();
		List<MemberRoleInfo> mrilist = memberRoleService.search(email, isiteid,
				roleList, page, pageSize);
		Integer total = memberRoleService.searchCount(email, isiteid, roleList);
		Page<MemberRoleInfo> p = new Page<MemberRoleInfo>(mrilist, total, page,
				pageSize);
		List<Website> website = categoryProductRecommendService.getWebsiteAll();
		return ok(views.html.manager.user.role.userrolelist.render(p, email,
				siteId, memberRole, memberRoleList, website));
	}

	public Result editForm(Integer iid, String email) {
		List<MemberRoleBase> mrlist = memberRoleService.getMemberRole();
		List<Integer> roleIds = memberRoleService.getRoleIdByUserId(iid);
		return ok(views.html.manager.user.role.user_role_edit.render(mrlist,
				roleIds, iid, email));
	}

	public Result updateMemberRole() {
		Form<MemberUserRoleForm> form = Form.form(MemberUserRoleForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			flash().put("error", Json.toJson(form.errorsAsJson()).toString());

		}
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		MemberUserRoleForm muform = form.get();
		Integer iid = muform.getIid();
		String email = muform.getSearchEmail();
		Integer siteid = muform.getSiteId();
		Integer memberRole = muform.getMemberRole();
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		String[] checkedVal = map.get("imemberroleid");
		List<Integer> roleIds = memberRoleService.getRoleIdByUserId(iid);
		if (checkedVal == null) {
			if (roleIds.size() > 0) {
				memberRoleService.deleteMemberRoleMap(iid);
			}
		} else {
			if (checkedVal.length != roleIds.size()) {
				memberRoleService.deleteMemberRoleMap(iid);
				for (int i = 0; i < checkedVal.length; i++) {
					memberRoleService.insertMemberRoleMap(iid,
							Integer.parseInt(checkedVal[i]),
							user.getCusername());
				}
			}
		}
		String websiteid = null;
		String searchrole = null;
		if (null != siteid) {
			websiteid = siteid.toString();
		}
		if (null != memberRole) {
			searchrole = memberRole.toString();
		}
		return redirect(controllers.manager.member.routes.MemberRole.search(
				email, websiteid, searchrole, 1, 10));
	}
}
