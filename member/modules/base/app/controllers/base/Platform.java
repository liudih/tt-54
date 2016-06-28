package controllers.base;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.PlatformService;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * 平台控制器
 * @author xcf
 *
 */
public class Platform extends Controller {
	
	@Inject
	PlatformService platformService;
	
	public Result getAllPlatform() {
		List<dto.Platform> platforms = platformService.getAllPlatform();
		
        Collection<dto.Platform> dtoPlatforms = null;

        if (null != platforms && platforms.size() > 0) {
//        	dtoPlatforms = Collections2.transform(platforms, new Function<entity.base.Platform, dto.Platform>() {
//                @Override
//                public dto.Platform apply(entity.base.Platform p) {
//                    return new dto.Platform(p.getIid(), p.getCcode());
//                }
//            });
        	
          dtoPlatforms = Collections2.transform(platforms, e-> {
        	  return  new dto.Platform(e.getIid(), e.getCcode());
        	  
      });
        	
        }

        if (null == dtoPlatforms) {
            return notFound();
        } else {
            return ok(Json.toJson(dtoPlatforms));
        }
	}
}
