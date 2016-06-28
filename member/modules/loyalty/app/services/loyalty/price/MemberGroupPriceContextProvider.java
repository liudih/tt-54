package services.loyalty.price;

import javax.inject.Inject;

import services.member.login.LoginService;
import services.price.IPriceCalculationContextProvider;
import valueobjects.member.MemberInSession;
import valueobjects.price.PriceCalculationContext;

public class MemberGroupPriceContextProvider implements
		IPriceCalculationContextProvider {

	public static final String MEMBER_IN_SESSION = "member";

	@Inject
	LoginService loginService;

	@Override
	public void contributeTo(PriceCalculationContext context) {
		try {
			MemberInSession mis = loginService.getLoginData();
			if (mis != null) {
				context.put(MEMBER_IN_SESSION, mis);
			}
		} catch (Exception e) {
			// Logger.warn("MemberGroupPriceContextProvider", e);
		}
	}

}
