package controllers.mobile;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.mobile.ClientErrorLogService;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.CilentErrorMsgDetailJson;
import valuesobject.mobile.CilentErrorMsgJson;

import com.fasterxml.jackson.databind.ObjectMapper;

import entity.mobile.MobileClientErrorLog;
import forms.mobile.ClientErrorLogForm;

public class ClientErrorLog extends Controller {

	@Inject
	ClientErrorLogService logService;

	public Result pushErrorLog() {
		BaseJson json = new BaseJson();
		Form<ClientErrorLogForm> form = Form.form(ClientErrorLogForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			json.setMsg("faild");
			json.setRe(BaseResultType.ERROR);
			return ok(Json.toJson(json));
		}
		ClientErrorLogForm errorForm = form.get();

		CilentErrorMsgJson error;
		try {
			ObjectMapper mapper = new ObjectMapper();
			error = mapper.readValue(errorForm.getError(),
					CilentErrorMsgJson.class);

		} catch (IOException e) {
			Logger.debug("analyze json source faild.msg:{}", e.getMessage());
			json.setMsg("parser json source faild");
			json.setRe(BaseResultType.ERROR);
			return ok(Json.toJson(json));
		}
		List<CilentErrorMsgDetailJson> errorDetails = error.getList();

		String remoteAddress = request().remoteAddress();
		List<MobileClientErrorLog> logs = Lists.transform(errorDetails, d -> {
			MobileClientErrorLog log = new MobileClientErrorLog();
			log.setIcode(d.getCode());
			log.setCerrormsg(d.getMsg());
			log.setCnetwork(d.getNetwork());
			log.setCphonename(error.getPhonename());
			log.setCsysversion(error.getSysvs());
			log.setCremoteaddress(remoteAddress);
			log.setIappid(error.getAppid());
			log.setIcurrentversion(error.getCurvs());
			return log;
		});

		if (logService.batchAdd(logs)) {
			json.setMsg("success");
			json.setRe(BaseResultType.SUCCESS);
		} else {
			json.setMsg("faild");
			json.setRe(BaseResultType.ERROR);
		}
		return ok(Json.toJson(json));
	}

}
