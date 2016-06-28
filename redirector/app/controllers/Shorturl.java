package controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.ShorturlService;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import entity.base.VisitLog;
import events.base.VisitEvent;
import form.base.ShorturlForm;
import form.base.VisitForm;

public class Shorturl extends Controller {
	@Inject
	ShorturlService shorturlService;
	@Inject
	EventBus eventBus;
	
	public Result addShorturl(){
		Map<String,Object> mjson = new HashMap<String,Object>();
		play.data.Form<ShorturlForm> userForm = Form.form(ShorturlForm.class).bindFromRequest();
		mjson.put("result", "error");
		if(userForm.hasErrors()){
			Iterator err = userForm.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry= (Entry<String, List<ValidationError>>) err.next();  
			mjson.put("result", entry.getKey()+":  "+entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}
		String host = "http://"+ctx().request().host();
		String surl = shorturlService.addShorturl(userForm.get(),host);
		if(surl!=null){
			mjson.put("shorturl", surl);
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}
	
	public Result transShorturl(final String cpath){
		if(cpath!=null && !"".equals(cpath)){
			entity.base.Shorturl url = shorturlService.getLongurl(cpath);
			if(url==null){
				return badRequest();
			}
			String referrer = request().getHeader("Referer");
			String ip = request().remoteAddress();
			eventBus.post(new VisitEvent(url.getCurl(),url.getCshorturlcode(),
					url.getCaid(),referrer,ip,url.getCeid(),url.getItasktype()));
			String redurl = url.getCurl();
			if(redurl.indexOf("http://")==-1){
				redurl = "http://" + redurl;
			}
			return redirect(redurl);
		}
		return badRequest();
	}
	
	public Result getVisit(){
		Map<String,Object> mjson = new HashMap<String,Object>();
		play.data.Form<VisitForm> vform = Form.form(VisitForm.class).bindFromRequest();
		mjson.put("result", "error");
		if(vform.hasErrors()){
			Iterator err = vform.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry= (Entry<String, List<ValidationError>>) err.next();  
			mjson.put("result", entry.getKey()+":  "+entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}
		List<dto.VisitLog> vlist = shorturlService.getVisit(vform.get());
		return ok(Json.toJson(vlist));
	}
}
