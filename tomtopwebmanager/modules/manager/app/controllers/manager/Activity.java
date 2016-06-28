package controllers.manager;

import play.mvc.Controller;
import play.mvc.Result;

public class Activity extends Controller {

	
	public Result activitylist() {
		
		return ok(views.html.manager.activity.activity_list.render());
	}
	
	public Result addRuleRelation() {
		
		return ok();
	}
	
	public Result deleteRuleRelation(Integer iid) {
		
		return ok();
	}
	 
}
