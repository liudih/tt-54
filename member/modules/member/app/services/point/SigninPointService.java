package services.point;
import java.util.Set;
import javax.inject.Inject;
import extension.point.ISigninProvider;

public class SigninPointService {

	@Inject
	Set<ISigninProvider> iSigninProvider;

	private ISigninProvider getPointService() {
		if (null != iSigninProvider && iSigninProvider.size() > 0) {
			return (ISigninProvider) iSigninProvider.toArray()[0];
		}
		return null;
	}
	
	public boolean checkMemberSignToday(String email, int siteId){	
		return getPointService().checkMemberSignToday(email, siteId);		
	}

}
