package controllers.manager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import services.base.FoundationService;
import services.customerService.CustomerServiceScheduleService;
import services.customerService.CustomerServiceScoreService;
import services.customerService.CustomerServiceScoreTypeService;
import services.customerService.ProfessionSkillService;
import services.customerService.ProfessionSkillTopicService;
import services.customerService.UserSkillMapService;
import services.customerService.UserSkillTypeEnum;
import services.manager.AdminUserService;
import services.messaging.IMessageService;
import services.messaging.impl.MessageService;
import session.ISessionService;
import valueobjects.base.Page;
import dto.CustomerServiceScoreDTO;
import dto.ProfessionSkillTopicDTO;
import dto.SimpleLanguage;
import entity.manager.AdminUser;
import entity.manager.CustomerServiceScoreType;
import entity.manager.ProfessionSkill;
import entity.manager.ProfessionSkillTopic;
import entity.manager.UserSkillMap;
import entity.messaging.Broadcast;
import entity.messaging.MessageInfo;
import forms.CustomerServiceScheduleForm;
import forms.CustomerServiceScheduleSearchForm;
import forms.CustomerServiceScoreForm;
import forms.CustomerServiceScoreTypeForm;
import forms.MessageForm;
import forms.ProfessionSkillForm;
import forms.ProfessionSkillTopicForm;
import forms.ProfessionSkillTopicSearchForm;
import forms.UserSkillEditForm;

public class CustomerService extends Controller {
	@Inject
	AdminUserService adminUserService;
	@Inject
	ILanguageService languageService;
	@Inject
	UserSkillMapService mapService;
	@Inject
	ProfessionSkillService skillService;
	@Inject
	CustomerServiceScheduleService scheduleService;
	@Inject
	ProfessionSkillTopicService topicService;
	@Inject
	ISessionService sessionService;
	@Inject
	CustomerServiceScoreTypeService typeService;
	@Inject
	CustomerServiceScoreService scoreService;
	@Inject
	IMessageService iMessageInfo;
	
	@Inject
	MessageService messageInfoServer;
	@Inject
	FoundationService fService;

	public Result manage(int page) {
		Page<dto.AdminUser> p = adminUserService.getadminUserPage(page);
		return ok(views.html.manager.customerservice.manage.render(p));
	}

	public Result edit(int userID) {
		List<SimpleLanguage> langList = languageService.getAllSimpleLanguages();
		List<ProfessionSkill> skillList = skillService.getAll();
		List<UserSkillMap> langSkillList = mapService.getListByUserID(userID,
				UserSkillTypeEnum.LANGUAGE);
		List<UserSkillMap> proSkillList = mapService.getListByUserID(userID,
				UserSkillTypeEnum.PROFESSION);
		List<Integer> langSkillIDs = Lists.transform(langSkillList,
				s -> s.getIskillid());
		List<Integer> proSkillIDs = Lists.transform(proSkillList,
				s -> s.getIskillid());
		return ok(views.html.manager.customerservice.edit_user_skill.render(
				langList, langSkillIDs, skillList, proSkillIDs, userID));
	}

	public Result save() {
		Form<UserSkillEditForm> form = Form.form(UserSkillEditForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		UserSkillEditForm editForm = form.get();
		List<UserSkillMap> list = mapService.convertToList(editForm);
		mapService.insertList(list, editForm.getUserID());
		return redirect(controllers.manager.routes.CustomerService
				.manage(editForm.getP()));
	}

	public Result skillManage(int page) {
		Page<ProfessionSkill> p = skillService.getPage(page);
		return ok(views.html.manager.customerservice.skill_manage.render(p));
	}

	public Result skillEdit(int id) {
		ProfessionSkill skill = skillService.getByID(id);
		if (null == skill) {
			return badRequest("can't found ProfessionSkill where iid = " + id);
		}
		return ok(views.html.manager.customerservice.edit_profession_skill
				.render(skill));
	}

	public Result skillSave() {
		Form<ProfessionSkillForm> form = Form.form(ProfessionSkillForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ProfessionSkillForm editForm = form.get();
		if (skillService.updateNameByID(editForm.getCskillname(),
				editForm.getIid())) {
			return redirect(controllers.manager.routes.CustomerService
					.skillManage(editForm.getP()));
		} else {
			return badRequest("update error where id = " + editForm.getIid());
		}
	}

	public Result skillDelete(int id, int p) {
		if (skillService.deleteByID(id)) {
			return redirect(controllers.manager.routes.CustomerService
					.skillManage(p));
		} else {
			return badRequest("delete error where id = " + id);
		}
	}

	public Result skillAdd() {
		Form<ProfessionSkillForm> form = Form.form(ProfessionSkillForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ProfessionSkillForm editForm = form.get();
		if (skillService.insert(editForm.getCskillname())) {
			return redirect(controllers.manager.routes.CustomerService
					.skillManage(editForm.getP()));
		} else {
			return badRequest("add error where id = " + editForm.getIid());
		}
	}

	public Result scheduleManage(int page) {
		Page<dto.CustomerServiceSchedule> p = scheduleService.getPage(page);
		List<dto.AdminUser> userList = adminUserService.getadminUserList();
		GregorianCalendar calendar = new GregorianCalendar();
		return ok(views.html.manager.customerservice.schedule_manage.render(p,
				userList, calendar.get(Calendar.WEEK_OF_YEAR)));
	}

	public Result scheduleDelete(int id, int p) {
		if (scheduleService.deleteByID(id)) {
			return redirect(controllers.manager.routes.CustomerService
					.scheduleManage(p));
		} else {
			return badRequest("delete error where id = " + id);
		}
	}

	public Result scheduleAdd() {
		Form<CustomerServiceScheduleForm> form = Form.form(
				CustomerServiceScheduleForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		CustomerServiceScheduleForm addForm = form.get();
		if (scheduleService.insert(addForm)) {
			return redirect(controllers.manager.routes.CustomerService
					.scheduleManage(addForm.getP()));
		} else {
			return badRequest("validate time error");
		}
	}

	public Result scheduleSearch() {
		Form<CustomerServiceScheduleSearchForm> form = Form.form(
				CustomerServiceScheduleSearchForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		CustomerServiceScheduleSearchForm searchForm = form.get();
		Page<dto.CustomerServiceSchedule> p = scheduleService
				.searchPage(searchForm);
		List<dto.AdminUser> userList = adminUserService.getadminUserList();
		GregorianCalendar calendar = new GregorianCalendar();
		return ok(views.html.manager.customerservice.schedule_search.render(p,
				userList, calendar.get(Calendar.WEEK_OF_YEAR), searchForm));
	}

	public Result topicManage(int p) {
		Page<ProfessionSkillTopicDTO> page = topicService.getPage(p);
		List<ProfessionSkill> skillList = skillService.getAll();
		return ok(views.html.manager.customerservice.topic_manage.render(page,
				skillList));
	}

	public Result topicAdd() {
		Form<ProfessionSkillTopicForm> form = Form.form(
				ProfessionSkillTopicForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null != user) {
			ProfessionSkillTopicForm addForm = form.get();
			if (topicService.insert(addForm, user)) {
				return redirect(controllers.manager.routes.CustomerService
						.topicManage(addForm.getP()));
			}
		}
		return badRequest("validate data error");
	}

	public Result topicSearch() {
		Form<ProfessionSkillTopicSearchForm> form = Form.form(
				ProfessionSkillTopicSearchForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ProfessionSkillTopicSearchForm searchForm = form.get();
		Page<ProfessionSkillTopicDTO> page = topicService.getPage(
				searchForm.getP(), searchForm.getSkillID());
		List<ProfessionSkill> skillList = skillService.getAll();
		return ok(views.html.manager.customerservice.topic_search.render(page,
				skillList, searchForm));
	}

	public Result topicEdit(int id) {
		ProfessionSkillTopic topic = topicService.getByID(id);
		List<ProfessionSkill> skillList = skillService.getAll();
		if (topic != null) {
			return ok(views.html.manager.customerservice.topic_edit.render(
					topic, skillList));
		} else {
			return badRequest("edit error where id = " + id);
		}
	}

	public Result topicSave() {
		Form<ProfessionSkillTopicForm> form = Form.form(
				ProfessionSkillTopicForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ProfessionSkillTopicForm saveForm = form.get();
		if (saveForm.getIid() != null && topicService.update(saveForm)) {
			return redirect(controllers.manager.routes.CustomerService
					.topicManage(saveForm.getP()));
		} else {
			return badRequest("save error where id = " + saveForm.getIid());
		}
	}

	public Result scoreType(int p) {
		Page<CustomerServiceScoreType> page = typeService.getPage(p);
		return ok(views.html.manager.customerservice.score_type.render(page));
	}

	public Result scoreTypeAdd() {
		Form<CustomerServiceScoreTypeForm> form = Form.form(
				CustomerServiceScoreTypeForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		CustomerServiceScoreTypeForm typeForm = form.get();
		boolean flag = typeService.insert(typeForm);
		if (flag) {
			return redirect(controllers.manager.routes.CustomerService
					.scoreType(typeForm.getP()));
		} else {
			return badRequest("add error");
		}
	}

	public Result scoreTypeDelete(int id, int p) {
		boolean flag = typeService.deleteByID(id);
		if (flag) {
			return redirect(controllers.manager.routes.CustomerService
					.scoreType(p));
		} else {
			return badRequest("delete error");
		}
	}

	public Result scoreTypeEdit(int id) {
		CustomerServiceScoreType type = typeService.getByID(id);
		if (null != type) {
			return ok(views.html.manager.customerservice.edit_score_type
					.render(type));
		} else {
			return badRequest("edit error");
		}
	}

	public Result scoreTypeSave() {
		Form<CustomerServiceScoreTypeForm> form = Form.form(
				CustomerServiceScoreTypeForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		CustomerServiceScoreTypeForm typeForm = form.get();
		boolean flag = typeService.update(typeForm);
		if (flag) {
			return redirect(controllers.manager.routes.CustomerService
					.scoreType(typeForm.getP()));
		} else {
			return badRequest("add error");
		}
	}

	public Result scoreManage(int p) {
		Page<CustomerServiceScoreDTO> page = scoreService.getPage(p);
		List<CustomerServiceScoreType> typeList = typeService.getAll();
		return ok(views.html.manager.customerservice.score_manage.render(page,
				typeList, null));
	}

	public Result scoreSearch() {
		Form<CustomerServiceScoreForm> form = Form.form(
				CustomerServiceScoreForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		CustomerServiceScoreForm searchForm = form.get();
		Page<CustomerServiceScoreDTO> page = scoreService
				.searchPage(searchForm.getName(), searchForm.getTypeID(),
						searchForm.getP());
		List<CustomerServiceScoreType> typeList = typeService.getAll();
		return ok(views.html.manager.customerservice.score_manage.render(page,
				typeList, searchForm));
	}
	
	public Result messageIndex(int page) {		
		
		//获取当前登录的用户（id）
		int adminUserId = 0;
		AdminUser adminUser = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null != adminUser) {
			adminUserId = adminUser.getIid();
		}
		
		Page<MessageInfo> list = messageInfoServer.getAllPersonalMessages(page,adminUserId);
		
		//获取后台用户
		List<dto.AdminUser> users = adminUserService.getadminUserList();
		for(MessageInfo l : list.getList()){
			for(dto.AdminUser user : users){
				if(user.getIid() == l.getIsendid()){
					l.setCsendname(user.getCusername());
				}
			}
		}		
		return ok(views.html.manager.customerservice.send_message.render(list));
	}
	
	public Result sendMessage() {
		
		Form<MessageForm> form = Form.form(MessageForm.class).bindFromRequest();		
		
		if (form.hasErrors()) {
			flash().put("error", "error");
			return redirect(controllers.manager.routes.CustomerService.messageIndex(1));
		}

		MessageForm mesForm = form.get();		
		
		//发送用户（id）
		int sendId = 0;
		String adminUser = null;
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null != user) {
			sendId = user.getIid();
			adminUser = user.getCemail();
		}
		
		int websiteId = fService.getSiteID();
		
		
		Broadcast info = new Broadcast();
		info.setContent(mesForm.getContent());
		info.setSubject(mesForm.getSubject());	
		info.setEmail(mesForm.getCemail());
		info.setSendId(sendId);
		info.setFrom("TOMTOP Team");		
		info.setType(enums.messaging.Type.PERSONAL.getCode());
		info.setStatus(enums.messaging.Status.unread.getCode());
		info.setSendMethod(enums.messaging.SendMethod.MANUAL.getCode());
		info.setWebsiteId(websiteId);
		
		if (messageInfoServer.sendPersonalMessage(info)) {
			return redirect(controllers.manager.routes.CustomerService.messageIndex(1));
		}
		return redirect(controllers.manager.routes.CustomerService.sendMessage());
	}
}
