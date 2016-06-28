package validates.messaging;

import org.json.simple.JSONObject;

import services.base.utils.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;

import enums.messaging.Type;

public class DefaultValidate implements IValidate {

	@Override
	public JSONObject valid(JsonNode paras) {
		if(paras == null){
			return null;
		}
		JSONObject feedback = new JSONObject();
		 JsonNode websiteId = paras.get("websiteId");
		 if(!(websiteId != null && !StringUtils.isEmpty(websiteId.asText()))){
			feedback.put("succeed", false);
			feedback.put("exception", "websiteId not find");
			return feedback;
		 }
		 
		 JsonNode subject = paras.get("subject");
		 if(!(subject != null && !StringUtils.isEmpty(subject.asText()))){
			 feedback.put("succeed", false);
			 feedback.put("exception", "subject not find");
			 return feedback;
		 }
		 
		 JsonNode content = paras.get("content");
		 if(!(content != null && !StringUtils.isEmpty(content.asText()))){
			 feedback.put("succeed", false);
			 feedback.put("exception", "content not find");
			 return feedback;
		 }
		 
		 JsonNode userEmail = paras.get("email");
		 if(!(userEmail != null && !StringUtils.isEmpty(userEmail.asText()))){
			 feedback.put("succeed", false);
			 feedback.put("exception", "userEmail not find");
			 return feedback;
		 }
		 
		 JsonNode type = paras.get("type");
		 if(!(type != null && !StringUtils.isEmpty(type.asText()))){
			 feedback.put("succeed", false);
			 feedback.put("exception", "type not find");
			 return feedback;
		 }else{
			 int typeInt = type.asInt();
			 Type te = enums.messaging.Type.getType(typeInt);
			 if(te == null){
				 feedback.put("succeed", false);
				 feedback.put("exception", "type:" + typeInt + " not exist");
				 return feedback;
			 }
		 }
		return null;
	}

}
