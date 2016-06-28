package controllers.mobile.personal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import services.mobile.MobileService;
import services.mobile.product.InterestTagService;
import utils.MsgUtils;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseListJson;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.member.MobileContext;
import controllers.mobile.TokenController;
import entity.mobile.InterestTag;

public class InterestTagController extends TokenController {

	@Inject
	MobileService serveice;

	@Inject
	InterestTagService interestTagService;

	/**
	 * 用户保存 感兴趣的标签
	 * 
	 * @return
	 */
	public Result addInterestTag() {
		try {
			Map<String, String[]> queryStrings = request().queryString();
			String[] tagStr = queryStrings.get("tagid");
			String[] tagids = null;
			if (StringUtils.isNotBlank(tagStr[0])) {
				tagids = tagStr[0].split("\\,");
			}
			if (tagids != null && tagids.length <= 6 && tagids.length > 0) {
				List<String> tagidList = Arrays.asList(tagids);
				List<InterestTag> icList = new ArrayList<InterestTag>();
				MobileContext mcontext = serveice.getMobileContext();
				for (String tagid : tagidList) {
					if (mcontext != null) {
						InterestTag ic = new InterestTag();
						ic.setImei(mcontext.getCimei());
						ic.setAppid(mcontext.getIappid());
						ic.setTagid(Integer.parseInt(tagid));
						ic.setCremoteaddress(mcontext.getIp());
						ic.setPhonename(mcontext.getCphonename());
						ic.setSysversion(mcontext.getCsysversion());
						ic.setStatus(1);
						icList.add(ic);
					}
				}
				boolean flag = interestTagService.addInterestTags(icList,
						mcontext.getCimei());
				if (flag) {
					BaseJson result = new BaseJson();
					result.setRe(BaseResultType.SUCCESS);
					result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
					return ok(Json.toJson(result));
				}
			} else if (tagids != null && tagids.length > 6) {
				BaseJson result = new BaseJson();
				result.setRe(BaseResultType.ADD_EXCEPTION);
				result.setMsg(MsgUtils.msg(BaseResultType.ADDORDERERROR));
				return ok(Json.toJson(result));
			}
		} catch (NumberFormatException e) {
			Logger.error("Exception", e);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.FAILURE);
		result.setMsg(MsgUtils.msg(BaseResultType.OPERATE_FAIL));
		return ok(Json.toJson(result));
	}

	/**
	 * 获取后台配置的标签
	 * 
	 * @return
	 */
	public Result getTagTypes() {
		int lang = serveice.getLanguageID();
		List<Map<String, Object>> tagTypes = interestTagService
				.getTagTypes(lang);
		if ((tagTypes == null || tagTypes.size() < 1) && lang != 1) {
			tagTypes = interestTagService.getTagTypes(1);
		}
		if (tagTypes != null && tagTypes.size() > 0) {
			BaseListJson<Map<String, Object>> result = new BaseListJson<Map<String, Object>>();
			result.setRe(BaseResultType.SUCCESS);
			result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
			result.setList(tagTypes);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(MsgUtils.msg(BaseResultType.NODATA));
		return ok(Json.toJson(result));
	}

}
