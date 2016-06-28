package validates.messaging;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 数据验证接口
 * @author lijun
 *
 */
public interface IValidate {

	/**
	 * 验证
	 * @param paras
	 * @return null-数据正常,JSONObject-错误信息,格式为{succeed：false,exception:}
	 */
	public JSONObject valid(JsonNode paras);
}
