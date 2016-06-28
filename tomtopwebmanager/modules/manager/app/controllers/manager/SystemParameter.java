package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.SimpleLanguage;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import services.base.SystemParameterService;
import services.base.WebsiteService;
import dto.Language;
import dto.Website;
import forms.SysParameterForm;

@controllers.AdminRole(menuName = "SystemParameterMgr")
public class SystemParameter extends Controller {

	@Inject
	SystemParameterService parameterService;

	@Inject
	ILanguageService languageService;

	@Inject
	WebsiteService websiteService;

	public Result list() {

		List<dto.SystemParameter> paramList = parameterService
				.getAllSysParameter();
		
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		
		Map<Integer, SimpleLanguage> languageMap = Maps.uniqueIndex(
				languageList, new Function<SimpleLanguage, Integer>() {
					public Integer apply(SimpleLanguage lang) {
						return lang.getIid();
					}
				});
		
		
		List<dto.SystemParameter> sysParamList = Lists
				.transform(paramList, ab -> {
					Integer langId = ab.getIlanguageid();
					SimpleLanguage lang = languageMap.get(langId);
					if(null != lang)
					ab.setLanguagename(lang.getCname());
					return ab;
				});
		
		
		List<Website> websiteList = this.websiteService.getAll();
		Map<Integer, Website> websiteMap = Maps.uniqueIndex(websiteList,
				new Function<Website, Integer>() {
					public Integer apply(Website website) {
						return website.getIid();
					}
				});
		
		sysParamList = Lists
				.transform(sysParamList, ab -> {
					Integer websiteId = ab.getIwebsiteid();
					Website website = websiteMap.get(websiteId);
					
					if(null != website)
					ab.setWebsitename(website.getCcode());

					return ab;
				});

		return ok(views.html.manager.sysparameter.sysparameter_manager
				.render(sysParamList));
	}

	public Result addSysParameter() {
		Form<SysParameterForm> form = Form.form(SysParameterForm.class)
				.bindFromRequest();

		dto.SystemParameter systemParameter = new dto.SystemParameter();
		SysParameterForm uform = form.get();
		BeanUtils.copyProperties(uform, systemParameter);

		dto.SystemParameter param = parameterService
				.addSysParameter(systemParameter);
		return ok(Json.toJson(param));
	}

	public Result alterSysParameter() {

		Form<SysParameterForm> form = Form.form(SysParameterForm.class)
				.bindFromRequest();

		dto.SystemParameter systemParameter = new dto.SystemParameter();
		SysParameterForm uform = form.get();
		BeanUtils.copyProperties(uform, systemParameter);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (parameterService.alterSysParameter(systemParameter)) {
			resultMap.put("errorCode", 0);
			return ok(Json.toJson(resultMap));
		}
		Logger.debug("系统参数更新失败");
		resultMap.put("errorCode", 1);
		return ok(Json.toJson(resultMap));
	}

	public Result deleteSysParameter(Integer iid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (this.parameterService.deleteSysParameter(iid)) {
			resultMap.put("errorCode", 0);
			resultMap.put("msg", "系统参数删除成功");
		} else {
			resultMap.put("errorCode", 1);
			resultMap.put("msg", "删除失败");
		}
		return ok(Json.toJson(resultMap));
	}

	public Result validateKey(String key, Integer iwebsiteId) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (this.parameterService.validateKey(key, iwebsiteId)) {
			resultMap.put("errorCode", 0);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);
		return ok(Json.toJson(resultMap));
	}

	public Result getAlllanguage() {
		List<Language> languageList = this.languageService.getAllLanguage();

		return ok(Json.toJson(languageList));
	}

	public Result getAllWetsite() {
		List<Website> websiteList = this.websiteService.getAll();

		return ok(Json.toJson(websiteList));
	}
}
