package extensions.livechat;

import java.util.Set;

import javax.inject.Inject;

import extensions.livechat.role.ILiveChatOnDutyCustomerServiceProvider;

public class LiveChatOnDutyCustomerServiceProvider implements
		ILiveChatOnDutyCustomerServiceProvider {

	@Inject
	CustomerServiceFilter customerServiceFilter;

	@Override
	public Set<String> getCustomerService() {
		return customerServiceFilter.getScheduleUserNames();
	}

}
