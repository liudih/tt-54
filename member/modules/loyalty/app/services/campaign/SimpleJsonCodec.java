package services.campaign;

import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SimpleJsonCodec<T> implements ICodec<T, JsonNode> {

	ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Class<? extends JsonNode> getTargetClass() {
		return JsonNode.class;
	}

	@Override
	public JsonNode encode(T fromObj) {
		if (fromObj == null) {
			return null;
		}
		return objectMapper.convertValue(fromObj, getTargetClass());
	}

	@Override
	public T decode(JsonNode fromObj) {
		if (fromObj == null) {
			return null;
		}
		return objectMapper.convertValue(fromObj, getSourceClass());
	}

}
