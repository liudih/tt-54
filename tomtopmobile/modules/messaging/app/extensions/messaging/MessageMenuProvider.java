package extensions.messaging;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import play.twirl.api.Html;
import services.messaging.IMessageService;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class MessageMenuProvider implements IMemberAccountHomeFragmentProvider {
    
	@Inject
	IMessageService messageService;
	
	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.PROFILE);
	}

	@Override
	public int getDisplayOrder() {
		return 1;
	}

	@Override
	public Html getFragment(String email, Position position) {
		
		Integer messageCount=messageService.getMyMessageTotal();
		
		return views.html.messaging.menu.message_fragment.render(messageCount.toString());
	}

}
