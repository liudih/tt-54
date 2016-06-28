package controllers.messaging.member;

import java.util.Iterator;

import org.json.simple.JSONObject;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.messaging.IBroadcastService;
import services.messaging.IMessageService;
import validates.messaging.DefaultValidate;
import valueobjects.base.LoginContext;
import valueobjects.base.Page;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import entity.messaging.Broadcast;

/**
 * 该类提供[我的消息列表，详情]服务
 * 
 * @author lijun
 *
 */
public class MyMessage extends Controller {
	@Inject
	private IBroadcastService bService;
	@Inject
	private IMessageService mService;
	@Inject
	private FoundationService fservice;
	
	/**
	 * 消息列表
	 * 
	 * @return
	 */

	public Result listMyMessage(int page, int pageSize) {
		LoginContext lc = fservice.getLoginContext();
		if (lc == null) {
			return redirect(controllers.member.routes.Login.loginForm("/"));
		}
		Page<Broadcast> messages = mService.getMyMessageForPage(page, pageSize);

		return ok(views.html.messaging.member.list.render(messages));
	}

	/**
	 * my message->delete
	 * 
	 * @author lijun
	 * @param ids
	 * @return
	 */
	public Result delete() {
		String ids = request().getQueryString("ids");
		JsonNode idJson = Json.parse(ids);
		if (idJson.isArray()) {
			Iterator<JsonNode> iterator = idJson.iterator();
			while (iterator.hasNext()) {
				JsonNode node = iterator.next();
				int id = node.get("id").asInt();
				String table = node.get("t").asText();
				if ("b".equals(table)) {
					boolean isExisted = this.mService.isExistedByBroadcastId(id
							+ "");
					if (!isExisted) {
						// 把消息设置为删除
						this.bService.deleteMyBroadcastMessage(id);
					}

				} else if ("i".equals(table)) {
					this.mService.deleteMessage(id + "");
				}
			}
		} else {
			return internalServerError();
		}
		return redirect(controllers.messaging.member.routes.MyMessage
				.listMyMessage(1, 15));
	}

	/**
	 * 我的消息详情
	 * 
	 * @return
	 */
	public Result detail() {
		String id = request().getQueryString("id");
		String table = request().getQueryString("t");
		if (StringUtils.isEmpty(id)) {
			return internalServerError();
		}
		Broadcast result = null;
		// b=t_message_broadcast i=t_message_info
		// 用t参数区分消息是哪个表的,分别取不同的表中取xiangq
		if ("b".equals(table)) {
			boolean isExisted = this.mService.isExistedByBroadcastId(id);
			if (!isExisted) {
				// 把消息设置为已阅
				this.bService.readMyBroadcastMessage(id);
			}
			// 获取详情
			result = this.bService.getDetail(id);
		} else if ("i".equals(table)) {
			mService.readMessage(id);
			result = this.mService.getDetail(id);
		}

		return ok(views.html.messaging.member.detail.render(result));
	}
	
	/**
	 * 消息接口
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result add() {
		int site = fservice.getSiteID();
		JSONObject feedback = new JSONObject();

		JsonNode paras = request().body().asJson();
		if (paras == null) {
			feedback.put("succeed", false);
			feedback.put("exception", "data not json format");
			return ok(feedback.toJSONString()).as("application/json");
		}
		// 验证数据完整行
		JSONObject exception = this.valid(paras);
		if (exception != null) {
			return ok(exception.toJSONString()).as("application/json");
		}

		if (paras.isArray()) {
			Iterator<JsonNode> iterator = paras.iterator();
			while (iterator.hasNext()) {
				JsonNode cell = iterator.next();
				Broadcast bc = Json.fromJson(cell, Broadcast.class);
				bc.setFrom("TOMTOP Team");
				bc.setSendId(0);
				bc.setWebsiteId(site);
				bc.setStatus(enums.messaging.Status.unread.getCode());
				bc.setSendMethod(enums.messaging.SendMethod.SYSTEM.getCode());
				this.mService.insert(bc);
			}
		} else {
			Broadcast bc = Json.fromJson(paras, Broadcast.class);
			Logger.debug(bc.getContent());
			bc.setFrom("TOMTOP Team");
			bc.setSendId(0);
			bc.setWebsiteId(site);
			bc.setStatus(enums.messaging.Status.unread.getCode());
			bc.setSendMethod(enums.messaging.SendMethod.SYSTEM.getCode());
			this.mService.insert(bc);
		}
		feedback.put("succeed", true);
		return ok(feedback.toJSONString()).as("application/json");
	}
	
	/**
	 * 验证数据完整性
	 * @param paras
	 * @return
	 */
	private JSONObject valid(JsonNode paras) {
		DefaultValidate vd = new DefaultValidate();
		if (paras.isArray()) {
			Iterator<JsonNode> iterator = paras.iterator();
			while (iterator.hasNext()) {
				JsonNode cell = iterator.next();
				JSONObject feedback = vd.valid(cell);
				if (feedback != null) {
					return feedback;
				}
				return null;
			}
		} else {
			JSONObject feedback = vd.valid(paras);
			return feedback;
		}
		return null;
	}
	
	public Result markedAsRead(){
		String ids = request().getQueryString("ids");
		JsonNode idJson = Json.parse(ids);
		if(idJson.isArray()){
			Iterator<JsonNode> iterator = idJson.iterator();
			while (iterator.hasNext()) {
				JsonNode node = iterator.next();
				int id = node.get("id").asInt();
				String table = node.get("t").asText();
				if ("b".equals(table)) {
					boolean isExisted = this.mService.isExistedByBroadcastId(id
							+ "");
					if (!isExisted) {
						// 把消息设置为删除
						this.bService.readMyBroadcastMessage(id + "");
					}

				} else if ("i".equals(table)) {
					mService.readMessage(id + "");
				}
			}
		}else{
			return internalServerError();
		}
		return redirect(controllers.messaging.member.routes.MyMessage
				.listMyMessage(1, 15));
	}
}
