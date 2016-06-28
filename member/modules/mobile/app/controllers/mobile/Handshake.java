package controllers.mobile;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import services.common.UUIDGenerator;
import services.mobile.MobileService;
import utils.NumberUtils;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.HandshakeJson;
import valuesobject.mobile.member.MobileContext;

public class Handshake extends Controller {

	@Inject
	MobileService serveice;

	final static int DEF_LAN_ID = 1;
	final static int DEF_VERSION = 1;

	public Result doHandshake() {
		Request request = request();
		String token = UUIDGenerator.createAsString().replace("-", "");
		String strfalt = request.getQueryString("flat");
		String sys_vs = request.getQueryString("sys_vs");
		String imei = request.getQueryString("imei");
		String phonename = request.getQueryString("phonename");
		String strappid = request.getQueryString("appid");
		String cid = request.getQueryString("cid");
		String strlanid = request.getQueryString("lid");
		String network = request.getQueryString("network");
		String str_cur_vs = request.getQueryString("cur_vs");
		String uuid = request.getQueryString("uuid");
		String ip = request.remoteAddress();
		String host = request.host();
		MobileContext ctx = new MobileContext();
		int appid = DEF_VERSION, lanid = DEF_LAN_ID, cur_vs = DEF_VERSION, falt = 3;

		if (strfalt != null && NumberUtils.isNumeric(strfalt)) {
			falt = Integer.parseInt(strfalt);
		}
		if (strappid != null && NumberUtils.isNumeric(strappid)) {
			appid = Integer.parseInt(strappid);
		}
		if (strlanid != null && NumberUtils.isNumeric(strlanid)) {
			lanid = Integer.parseInt(strlanid);
		}
		if (str_cur_vs != null && NumberUtils.isNumeric(str_cur_vs)) {
			cur_vs = Integer.parseInt(str_cur_vs);
		}
		ctx.setIplatform(falt);
		ctx.setCsysversion(sys_vs);
		ctx.setCimei(imei);
		ctx.setCphonename(phonename);
		ctx.setIappid(appid);
		ctx.setCcid(cid);
		ctx.setIappid(lanid);
		ctx.setCnetwork(network);
		ctx.setCurrentversion(cur_vs);
		ctx.setIp(ip);
		ctx.setHost(host);
		ctx.setUuid(uuid);
		ctx.setToken(token);
		serveice.setMobileContext(ctx);
		HandshakeJson resultJson = new HandshakeJson();
		resultJson.setRe(BaseResultType.SUCCESS);
		resultJson.setMsg("success");
		resultJson.setToken(token);
		return ok(Json.toJson(resultJson));
	}
}
