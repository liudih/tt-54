package controllers.manager.crm;

import java.util.List;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.manager.AdminUserService;
import services.messaging.IBroadcastService;
import session.ISessionService;
import valueobjects.base.Page;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import entity.manager.AdminUser;
import entity.messaging.Broadcast;
import enums.messaging.SendMethod;
import enums.messaging.Type;
import forms.MessageForm;

/**
 * CRM->Message Manage
 * 
 * @author lijun
 *
 */
public class Message extends Controller {
	@Inject
	private IBroadcastService service;
	@Inject
	ISessionService sessionService;
	@Inject
	FoundationService fService;
	@Inject
	AdminUserService userService;

	/**
	 * 消息列表展示
	 * 
	 * @author lijun
	 * @param page
	 *            当前页
	 * @param pageSize
	 *            每页数据大小
	 * @return
	 */
	public Result showMessageList(int page, int pageSize) {
		Type[] types = Type.values();
		Page<Broadcast> result = service
				.selectBroadcastsForPage(page, pageSize);
		List<dto.AdminUser> users = userService.getadminUserList();
		for(Broadcast cell : result.getList()){
			for(dto.AdminUser user : users){
				if(user.getIid() == cell.getSendId()){
					cell.setCreater(user.getCusername());
				}
			}
		}
		return ok(views.html.manager.crm.message.list.render(result, types));
	}

	/**
	 * 新增 or修改 消息
	 * 
	 * @return
	 */
	public Result addOrUpdateMessage() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");

		Form<MessageForm> form = Form.form(MessageForm.class).bindFromRequest(
				"id", "subject", "content", "type");
		
		if(form.hasErrors()){
			return ok(views.html.manager.user.error.render());
		}

		entity.messaging.Broadcast message = new entity.messaging.Broadcast();
		MessageForm mform = form.get();
		
		BeanUtils.copyProperties(mform, message);

		String id = mform.getId();
		String type = mform.getType();
		int typeInt = Integer.parseInt(type);
		message.setType(typeInt);

		int site = fService.getSiteID();
		message.setWebsiteId(site);
		message.setSendMethod(SendMethod.MANUAL.getCode());
		message.setFrom("TOMTOP Team");
		if (StringUtils.isEmpty(id)) {
			message.setSendId(user.getIid());
			this.service.add(message);
		} else {
			int idInt = Integer.parseInt(id);
			message.setId(idInt);
			message.setModifierId(user.getIid());
			this.service.update(message);
		}

		return redirect(controllers.manager.crm.routes.Message.showMessageList(
				1, 15));
	}

	/**
	 * 推送消息
	 * 
	 * @return
	 */
	public Result publish(int id) {
		ObjectNode result = Json.newObject();
		try {
			entity.messaging.Broadcast message = new entity.messaging.Broadcast();
			message.setId(id);

			AdminUser user = (AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			message.setModifierId(user.getIid());

			message.setStatus(enums.messaging.Status.PUBLISH.getCode());
			this.service.publish(message);
			result.put("succeed", true);
		} catch (Exception e) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("CRM->消息推送操作失败", e);
			}
			result.put("succeed", false);
		}
		return ok(result).as("text/json");
	}

	/**
	 * 删除操作
	 * 
	 * @param i
	 * @return
	 */
	public Result delete(int id) {
		this.service.delete(id);
		return redirect(controllers.manager.crm.routes.Message.showMessageList(
				1, 15));
	}
}
