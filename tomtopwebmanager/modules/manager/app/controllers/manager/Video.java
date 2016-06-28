package controllers.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import dto.interaction.InteractionProductMemberVideo;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.interaciton.review.ProductReviewsService;
import services.loyalty.IPointsService;
import services.manager.AdminUserService;
import valueobjects.base.Page;

public class Video extends Controller {

	@Inject
	ProductReviewsService productReviewsService;
	@Inject
	AdminUserService userService;
	@Inject
	IPointsService pointService;
	
	public Result videohome(int page,int pageSize,int status){
		Page<InteractionProductMemberVideo> vlist = productReviewsService.getVideoPage(page, pageSize,status);
		return ok(views.html.manager.review.video_manager.render(vlist,status));
	}
	
	public Result verify(){
		Map<String,Object> mjson = new HashMap<String,Object>();
		mjson.put("result", "error");
		String iid = request().body().asFormUrlEncoded().get("iid")[0];
		String email = request().body().asFormUrlEncoded().get("email")[0];
		String siteid = request().body().asFormUrlEncoded().get("siteid")[0];
		String istatus = request().body().asFormUrlEncoded().get("istatus")[0];
		String points = request().body().asFormUrlEncoded().get("points")[0];
		String commentid = request().body().asFormUrlEncoded().get("commentid")[0];
		if(iid!=null && !"".equals(iid)&& istatus!=null&& !"".equals(istatus)){
			entity.manager.AdminUser au = userService.getCuerrentUser();
			String verifyUser = au!=null ? au.getCusername() : "";
			Integer st = Integer.parseInt(istatus);
			boolean flag = productReviewsService.verifyVideo(Integer.parseInt(iid), st,
					verifyUser);
			if(flag && st==1 && points!=null && !"".equals(points) && commentid.length()==0){
				int p = Integer.parseInt(points);
				int sid = siteid!=null&&!"".equals(siteid)?Integer.parseInt(siteid):1;
				pointService.grantPoints(email, sid,p ,
						"upload video","upload video",1,"upload video");
			}
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}
	
	public Result batchverify(){
		Map<String,Object> mjson = new HashMap<String,Object>();
		mjson.put("result", "error");
		String batchstring = request().body().asFormUrlEncoded().get("batchstring")[0];
		String points = request().body().asFormUrlEncoded().get("points")[0];
		String status = request().body().asFormUrlEncoded().get("status")[0];
		
		if(!StringUtils.isEmpty(batchstring) && !StringUtils.isEmpty(points) && 
				!StringUtils.isEmpty(status) ){
			entity.manager.AdminUser au = userService.getCuerrentUser();
			String verifyUser = au!=null ? au.getCusername() : "";
			Integer p = Integer.parseInt(points);
			Integer st = Integer.parseInt(status);
			String s[] = batchstring.split(";");
			for(String ss : s){
				String s1[] = ss.split(",");
				Integer iid = Integer.parseInt(s1[0]);
				String email = s1[1];
				Integer sid = Integer.parseInt(s1[2]);
				Integer commentid = Integer.parseInt(s1[3]);
				productReviewsService.verifyVideo(iid, st, verifyUser);
				if(st==1 && commentid==0){
					pointService.grantPoints(email, sid,p ,
							"upload video","upload video",1,"upload video");
				}
			}
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}
}
