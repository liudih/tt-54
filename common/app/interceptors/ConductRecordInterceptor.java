package interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.joda.time.DateTime;

public class ConductRecordInterceptor implements MethodInterceptor {

	public Object invoke(MethodInvocation invocation) throws Throwable {
		String dtime = new DateTime().toString();
		String action = invocation.getMethod().getName();
		play.Logger.debug(dtime+":"+action);
		Object result = invocation.proceed();
		return result;
	}

}
