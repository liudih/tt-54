package controllers.manager;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.libs.ws.WS;
import play.Play;

import com.alibaba.fastjson.JSON;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F.Either;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.image.IImageUpdateService;
import services.image.ImageEnquiryService;
import session.ISessionService;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.InterceptActon;
import dto.image.FileLabelType;
import dto.image.Img;
import dto.image.ImgUseMapping;
import entity.manager.AdminUser;
import forms.img.ImgPageForm;

@With(InterceptActon.class)
public class UploadFile extends Controller {
	@Inject
	IImageUpdateService update;

	@Inject
	ImageEnquiryService imageEnquiryService;

	@Inject
	ISessionService sessionService;
	
	@Inject
	private FoundationService foundation;

	public Result uploadFileManager() {
		ImgPageForm imgPageForm = new ImgPageForm();
		return ok(views.html.manager.uploadfile.uploadfile_manager
				.render(getFileList(imgPageForm)));
	}

	public Result uploadFile() {
		return ok(views.html.manager.uploadfile.uploadfile.render());
	}

	public Result saveFile() throws IOException{
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		NetHttpTransport transport = new NetHttpTransport();
		HttpRequestFactory httpRequestFactory = transport.createRequestFactory();
		play.mvc.Http.MultipartFormData body = request().body()
				.asMultipartFormData();
		DynamicForm form = Form.form().bindFromRequest();
		String path = form.get("path");
		String clabel = form.get("clabel");
		int siteId = foundation.getSiteID();
		if (!clabel.equals(FileLabelType.LOTTERY.getFileLabelType())
				&& !clabel.equals(FileLabelType.EDM.getFileLabelType())
				&& !clabel.equals(FileLabelType.ACTIVITY.getFileLabelType())) {
			clabel = FileLabelType.DEFAULT.getFileLabelType();
		}
		List<FilePart> files = body.getFiles();
		Map<String, List<ImgUseMapping>> result = Maps.newHashMap();
		Map<String, Object> result2 = new HashMap<String, Object>();
		List<ImgUseMapping> uploadSuccess = Lists.newArrayList();
		List<ImgUseMapping> uploadFail = Lists.newArrayList();
		for (FilePart filePart : files) {
			ImgUseMapping imgUseMapping2 = new ImgUseMapping();
			String contentType = filePart.getContentType();
			String filename = filePart.getFilename();
			String fullPath = "";
			Img image = new Img();
			image.setIwebsiteid(siteId);
			if ("".equals(path)) {
				fullPath = filename;
			} else {
				fullPath = path + "/" + filename;
			}
			Map<String, String> parameters = Maps.newHashMap();
			parameters.put("token", Play.application().configuration()
					.getString("cdn.token"));
			// Add parameters
			MultipartContent content = new MultipartContent()
					.setMediaType(new HttpMediaType("multipart/form-data").setParameter("boundary", "__END_OF_PART__"));
			for (String name : parameters.keySet()) {
				MultipartContent.Part part = new MultipartContent.Part(
						new ByteArrayContent(null, parameters.get(name).getBytes()));
				part.setHeaders(
						new HttpHeaders().set("Content-Disposition", String.format("form-data; name=\"%s\"", name)));
				content.addPart(part);
			}
			FileInputStream fis = new FileInputStream(filePart.getFile());  
            ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            byte[] b = new byte[1024];  
            int n;  
            while ((n = fis.read(b)) != -1)  
            {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            byte[] buffer = bos.toByteArray();
			// Add file
			HttpContent fileContent = new ByteArrayContent("image/jpeg", buffer);

			MultipartContent.Part part = new MultipartContent.Part(fileContent);
			part.setHeaders(new HttpHeaders().set("Content-Disposition",
					String.format("form-data; name=\"file\"; filename=\"%s\"", fullPath)));
			content.addPart(part);
			GenericUrl url = new GenericUrl(Play.application().configuration()
					.getString("cdn.url"));
			String response = httpRequestFactory.buildPostRequest(url, content).execute().parseAsString();
			ObjectMapper om = new ObjectMapper();
			result2 = om.readValue(response, Map.class);
			String newPath = "";
			if(null!=result2){
				String r = result2.get("succeed").toString();
				if("true".equals(r)){
					newPath = result2.get("path").toString();
					image.setCpath(fullPath);
					image.setCdnpath(newPath);
				}
			}
			
			Either<Exception, Long> t;
			try {
				image.setCcontenttype(contentType);
				t = update.createCdnImage(image);
				ImgUseMapping imgUseMapping = imageEnquiryService
						.getImgUseMappingByPathAndLabel(fullPath, clabel);
				if (null != imgUseMapping) {
					imgUseMapping2 = imgUseMapping;
				}
				if (t.right.isDefined()) {
					Img img2 = new Img();
					if (null == imgUseMapping) {
						if (0 == image.getIid()) {
							img2 = imageEnquiryService.getImageByPath(fullPath,
									false);
						} else {
							img2 = image;
						}
						imgUseMapping2.setIimgid(img2.getIid());
						imgUseMapping2.setCpath(fullPath);
						imgUseMapping2.setClabel(clabel);
						imgUseMapping2.setCcreateuser(user.getCcreateuser());
						update.addNewImgUseMapping(imgUseMapping2);
					}
					uploadSuccess.add(imgUseMapping2);
				} else {
					uploadFail.add(imgUseMapping2);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error("upload file error " ,  e);
				uploadFail.add(imgUseMapping2);
			}
		}
		result.put("success", uploadSuccess);
		result.put("fail", uploadFail);
		return ok(views.html.manager.uploadfile.uploadfile_result
				.render(result));
	}

	public Html getFileList(ImgPageForm imgPageForm) {
		int siteId = foundation.getSiteID();
		List<Img> files = imageEnquiryService.getAllImageByPage(imgPageForm,siteId);
		Logger.info("siteId-------------------------------------:{}",siteId);
		Integer count = imageEnquiryService.getImgCount(imgPageForm,siteId);
		Integer pageTotal = count / imgPageForm.getPageSize()
				+ ((count % imgPageForm.getPageSize() > 0) ? 1 : 0);
		Map<Long, List<String>> fileIdAndLabelMap = Maps.newHashMap();
		if (null != files && files.size() > 0) {
			List<Long> imgids = Lists.transform(files, i -> i.getIid());
			List<ImgUseMapping> imgUseMappings = imageEnquiryService
					.getImgUseMappingByImgIds(imgids);
			if (null != imgUseMappings && imgUseMappings.size() > 0) {
				for (ImgUseMapping imgUseMapping : imgUseMappings) {
					Long imgid = imgUseMapping.getIimgid();
					List<ImgUseMapping> imgUseList = Lists
							.newArrayList(Collections2.filter(imgUseMappings,
									i -> (imgid == i.getIimgid())));
					fileIdAndLabelMap.put(imgid,
							Lists.transform(imgUseList, k -> k.getClabel()));
				}
			}
		}
		return views.html.manager.uploadfile.uploadfile_table_list.render(
				files, count, imgPageForm.getPageNum(), pageTotal,
				fileIdAndLabelMap);
	}

	public Result search() {
		Form<ImgPageForm> imgPageForm = Form.form(ImgPageForm.class)
				.bindFromRequest();

		return ok(getFileList(imgPageForm.get()));
	}

	public Result deleteUploafile(Integer iid) {
		boolean deleteImage = update.deleteImage(iid);
		update.deleteImgUserMapping(iid);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", deleteImage);
		return ok(Json.toJson(result));
	}
}
