package services.member.findpassword;

import context.WebContext;
import play.libs.F;
import play.libs.F.Promise;

public interface IFindPasswordService {

	public abstract Promise<F.Tuple<Integer, Integer>> asyncFindPass(
			String toemail, String url, WebContext context);

	public abstract F.Tuple3<Integer, Integer, Integer> resetPassValide(
			String cid, WebContext context);

	public abstract boolean asyncFindPassword(final String toemail, String url,
			WebContext webContext);

	public abstract int resetPasswordValide(String cid, WebContext context);
}