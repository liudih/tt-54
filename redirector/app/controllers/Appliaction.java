package controllers;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;



public class Appliaction extends Controller {

	
	public Result index(){
		return ok(views.html.index.render());
	}
}
