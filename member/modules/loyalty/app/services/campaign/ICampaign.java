package services.campaign;

import java.util.List;

import com.google.common.base.Optional;

/**
 * 因应Payload类型做的市场活动：
 * <ul>
 * <li>假如某活动是因为在购物车里输入了一个推广码，Payload可以定义为CartPromotionCode（包括Cart和一个String）</li>
 * <li>假如某活动是因为登记了邮件地址接收EDM，Payload可以定义为EdmRegistrationEvent</li>
 * </ul>
 * 
 * @author kmtong
 *
 */
public interface ICampaign {

	String getId();

	Class<?> getPayloadClass();

	CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance);

	ICampaignInstance createCampaignInstance();

	List<ICampaignInstance> getActiveInstances(Object payload);

	Optional<ICampaignInstance> getInstance(String instanceId);

	List<IActionRule> getPossibleActionRules();

	List<IAction> getPossibleActions();

}
