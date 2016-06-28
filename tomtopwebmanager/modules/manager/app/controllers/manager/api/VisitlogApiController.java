package controllers.manager.api;

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
import services.manager.TrafficService;
import valueobjects.base.Page;

import com.google.inject.Inject;

import dto.api.VisitLogApiVo;
import forms.manager.api.VisitLogForm;

public class VisitlogApiController extends Controller {
	
	@Inject
	TrafficService trafficService;
	
	public Result showVisitLog(){
		Map<String, Object> mjson = new HashMap<String, Object>();
		Form<VisitLogForm> form = Form.form(VisitLogForm.class)
				.bindFromRequest();
		if(form.hasErrors()){
			Iterator err = form.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry= (Entry<String, List<ValidationError>>) err.next();  
			mjson.put("result", entry.getKey()+":  "+entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}
		VisitLogForm ff = form.get();
		Page<VisitLogApiVo> list = trafficService.getVisitLogPageForApi(ff.getPage(),ff.getPageSize(),
				ff.getStartDate(),ff.getEndDate()); 
		mjson.put("list", list.getList());
		mjson.put("totalPages", list.totalPages());
		mjson.put("totalCount", list.totalCount());
		return ok(Json.toJson(mjson));
	}
}
