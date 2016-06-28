package controllers.messaging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import entity.messaging.Broadcast;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.utils.StringUtils;
import services.messaging.IBroadcastService;
import services.messaging.IMessageService;
import valueobjects.base.Page;

public class MessageController extends Controller{
    
	@Inject
    IBroadcastService broadcastService;
	
	@Inject
	IMessageService messageService;
	
	private static final int PAGESIZE=15;
	
	/**
	 * 消息列表
	 * @return
	 */
	public Result getMessage(){
	     
		Page<Broadcast> messages = messageService.getMyMessageForPage(1, PAGESIZE);
		return ok(views.html.messaging.member.account_message.render(messages));
	}
	
	/**
	 * 异步加载消息列表
	 * @return
	 */
	public Result messageMore(int p){
		Page<Broadcast> messages = messageService.getMyMessageForPage(p, PAGESIZE);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", messages.pageNo()+1);
		map.put("totlePage", messages.totalPages());
		map.put("html", views.html.messaging.member.messaging_badge.render(messages)
				.toString());
		return ok(Json.toJson(map));
	}
	
	/**
	 * 消息详情
	 * @return
	 */
	public Result getMessageDetail(){
		String id = request().getQueryString("id");
		String table = request().getQueryString("t");
		if (StringUtils.isEmpty(id)) {
			return internalServerError();
		}
		Broadcast result = null;
	
		// 用t参数区分消息是哪个表的,分别取不同的表中取xiangq
		if ("b".equals(table)) {
				// 把消息设置为已阅
				this.broadcastService.readMyBroadcastMessage(id);
			// 获取详情
			result = this.broadcastService.getDetail(id);
		} else if ("i".equals(table)) {
			messageService.readMessage(id);
			result = this.messageService.getDetail(id);
		}
		
		return ok(views.html.messaging.member.account_message_detial.render(result));
	}
	
	/**
	 * 标记阅读
	 * @return
	 */
	public Result messagedAsRead(){
		String ids = request().getQueryString("ids");
		JsonNode idJson = Json.parse(ids);
		if(idJson.isArray()){
			Iterator<JsonNode> iterator = idJson.iterator();
			while (iterator.hasNext()) {
				JsonNode node = iterator.next();
				int id = node.get("id").asInt();
				String table = node.get("t").asText();
				if ("b".equals(table)) {
						// 把消息设置为删除
						this.broadcastService.readMyBroadcastMessage(id + "");

				} else if ("i".equals(table)) {
					messageService.readMessage(id + "");
				}
			}
		}else{
			return internalServerError();
		}
		return redirect(controllers.messaging.routes.MessageController
				.getMessage());
	}
	
	/**
	 * 删除消息
	 * @return
	 */
	public Result messageDelete(){
		String ids = request().getQueryString("ids");
		JsonNode idJson = Json.parse(ids);
		if (idJson.isArray()) {
			Iterator<JsonNode> iterator = idJson.iterator();
			while (iterator.hasNext()) {
				JsonNode node = iterator.next();
				int id = node.get("id").asInt();
				String table = node.get("t").asText();
				if ("b".equals(table)) {
						// 把消息设置为删除
						this.broadcastService.deleteMyBroadcastMessage(id);
				} else if ("i".equals(table)) {
					this.messageService.deleteMessage(id + "");
				}
			}
		} else {
			return internalServerError();
		}
		return redirect(controllers.messaging.routes.MessageController
				.getMessage());
	}
	
}
