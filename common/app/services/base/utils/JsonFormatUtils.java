package services.base.utils;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFormatUtils {
	private static ObjectMapper mapper = new ObjectMapper();

	public static String beanToJson(Object obj) {
		StringWriter writer = new StringWriter();
		JsonGenerator gen = null;
		String json = "";
		try {
			gen = new JsonFactory().createJsonGenerator(writer);
			mapper.writeValue(gen, obj);
			gen.close();
			json = writer.toString();
			writer.close();
		} catch (IOException e) {
			play.Logger.error(e.toString());
		} catch (Exception e1) {
			play.Logger.error(e1.toString());
		}
		return json;
	}

	public static <T> T jsonToBean(String json, Class<T> cls) {
		T vo = null;
		try {
			vo = mapper.readValue(json, cls);
		} catch (JsonParseException e) {
			play.Logger.error(e.toString());
		} catch (JsonMappingException e) {
			play.Logger.error(e.toString());
		} catch (IOException e) {
			play.Logger.error(e.toString());
		}
		return vo;
	}
}