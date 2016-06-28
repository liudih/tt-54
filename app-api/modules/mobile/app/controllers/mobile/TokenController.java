package controllers.mobile;

import interceptor.VisitLog;
import interceptor.auth.TokenAuth;
import play.mvc.Controller;
import play.mvc.With;

@With({ VisitLog.class, TokenAuth.class })
public class TokenController extends Controller{

}
