package handlers.manager;

import javax.inject.Inject;

import services.manager.livechat.LiveChatMsgInfoService;
import valueobjects.livechat.data.LiveChatMessage;

import com.google.common.eventbus.Subscribe;

import entity.manager.LivechatMsgInfo;

public class LiveChatMessageHandler {
	@Inject
	LiveChatMsgInfoService infoService;

	@Subscribe
	public void onPostMessage(LiveChatMessage event) throws Exception {
		if (event != null) {
			LivechatMsgInfo info = new LivechatMsgInfo();
			info.setCcontent(event.getText());
			info.setCfromalias(event.getFromAlias());
			info.setCfromltc(event.getFrom());
			info.setCsessionid(event.getChatSessionID());
			info.setCtoalias(event.getToAlias());
			info.setCtoltc(event.getTo());
			info.setCtopic(event.getTopic());
			infoService.insert(info);
		}
	}
}
