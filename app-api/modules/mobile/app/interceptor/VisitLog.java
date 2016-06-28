package interceptor;

import javax.inject.Inject;

import play.libs.F.Promise;
import play.mvc.Action.Simple;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.mobile.MobileService;

import com.google.common.eventbus.EventBus;

import events.mobile.VisitEvent;

public class VisitLog extends Simple {

	@Inject
	EventBus eventBus;

	@Inject
	MobileService mobileService;

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		long beginTime = System.currentTimeMillis();
		Promise<Result> result = delegate.call(ctx);
		long endTime = System.currentTimeMillis();
		int consumeTime = (int) (endTime - beginTime);

		eventBus.post(new VisitEvent(consumeTime, ctx.request().host()
				+ ctx.request().path(), ctx.request().remoteAddress(),
				mobileService.getTokenKey(), mobileService.getLanguageID(), ctx));

		return result;
	}

}
