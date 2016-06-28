package controllers.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import services.common.UUIDGenerator;
import session.ISessionService;

import com.google.inject.Inject;

public class Image extends Controller {

	@Inject
	ISessionService sessionService;

	public Result push() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String uuid = UUIDGenerator.createAsString().replace("-", "");
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart filePart = body.getFile("file");
		if (filePart != null) {
			File file = filePart.getFile();
			byte[] buff;
			try {
				buff = IOUtils.toByteArray(new FileInputStream(file));
				sessionService.set(uuid, buff);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultMap.put("errorCode", 0);
			resultMap.put("uuid", uuid);
		} else {
			resultMap.put("errorCode", 1);
		}

		return ok(Json.toJson(resultMap));
	}

	public Result get(String uuid) {
		Serializable s = sessionService.get(uuid);
		if (s != null) {
			byte[] buff = (byte[]) s;
			return ok(buff).as("image/png");
		}
		return notFound();

	}

}
