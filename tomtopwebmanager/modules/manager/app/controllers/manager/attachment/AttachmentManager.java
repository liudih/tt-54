package controllers.manager.attachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.ISystemParameterService;
import services.base.FoundationService;
import services.product.IAttachmentDescService;
import services.product.IAttachmentService;
import session.ISessionService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import controllers.InterceptActon;
import dto.product.Attachment;
import entity.manager.AdminUser;
import forms.product.AttachmentSearchForm;

@With(InterceptActon.class)
public class AttachmentManager extends Controller {
	@Inject
	ISessionService sessionService;

	@Inject
	IAttachmentService attachmentService;

	@Inject
	ISystemParameterService parameterService;

	@Inject
	FoundationService foundationService;

	@Inject
	IAttachmentDescService attachmentDescService;

	public Result index() {
		return ok(views.html.manager.attachment.attachment_manager.render());
	}

	public Result updateAttachment() {
		return ok(views.html.manager.attachment.upload_attachment.render());
	}

	public Boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + destDirName + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + destDirName + "失败！");
			return false;
		}
	}

	public boolean createFile(String destFileName, FilePart filePart,
			String override) {
		File file = new File(destFileName);
		if (file.exists()) {
			System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");

			if (!"yes".equals(override)) {
				return false;
			}
		} else {
			if (destFileName.endsWith(File.separator)) {
				System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
				return false;
			}
			// 判断目标文件所在的目录是否存在
			if (!file.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建父目录
				Logger.debug("目标文件所在目录不存在，准备创建它！");
				if (!file.getParentFile().mkdirs()) {
					Logger.debug("创建目标文件所在目录失败！");
					return false;
				}
			} else {
				Logger.debug("目录已创建！");
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
				System.out.println("创建单个文件" + destFileName + "成功！");
			} else {
				System.out.println("创建单个文件" + destFileName + "失败！");
			}
			try {
				FileInputStream fis = new FileInputStream(filePart.getFile());
				OutputStream out = new FileOutputStream(file);
				int length = 0;
				byte[] buf = new byte[1024 * 10];
				while ((length = fis.read(buf)) != -1) {
					out.write(buf, 0, length);
				}
				out.close();
				fis.close();

				return true;
			} catch (FileNotFoundException e) {
				Logger.error("upload file error ", e);

				return false;
			} catch (IOException e) {
				Logger.error("upload file error ", e);

				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out
					.println("创建单个文件" + destFileName + "失败！" + e.getMessage());

			return false;
		}
	}

	public Result saveAttachment() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		String username = user.getCcreateuser();
		Map<String, List<String>> result = Maps.newHashMap();
		play.mvc.Http.MultipartFormData body = request().body()
				.asMultipartFormData();
		DynamicForm form = Form.form().bindFromRequest();
		String msg = "\\" + java.io.File.separator;
		String path = form.get("cpath").replaceAll("/", msg);
		String ctype = form.get("ctype");
		String override = form.get("override");
		List<String> uploadSuccess = Lists.newArrayList();
		List<String> uploadFail = Lists.newArrayList();
		String rootPath = Play.application().configuration()
				.getConfig("attachment").getString("path");
		String filepath = rootPath + path;
		filepath = filepath.replaceAll("/", msg);
		String cdescribe = form.get("cdescribe");
		createDir(filepath);

		List<FilePart> files = body.getFiles();
		for (FilePart filePart : files) {
			String filename = filePart.getFilename();
			String attachmentpath = path + File.separator
					+ filePart.getFilename();
			String fileRootPath = filepath + File.separator
					+ filePart.getFilename();
			boolean createFile = createFile(fileRootPath, filePart, override);
			if (!createFile) {
				uploadFail.add(filename);
				continue;
			}
			Attachment attachment = new Attachment();
			attachment.setCdescribe(cdescribe);
			attachment.setCfilename(filename);
			attachment.setCpath(attachmentpath);
			attachment.setCtype(ctype);
			attachment.setDcreatedate(new Date());
			attachment.setCcreateuser(username);
			boolean addDownloadPath = attachmentService
					.addAttachment(attachment);
			if (addDownloadPath) {
				uploadSuccess.add(filename);
			} else {
				uploadFail.add(filename);
			}
		}
		result.put("success", uploadSuccess);
		result.put("fail", uploadFail);
		return ok(views.html.manager.attachment.upload_attachment_result
				.render(result));
	}

	public Html getList(AttachmentSearchForm downloadPathSearchForm) {
		List<Attachment> attachments = attachmentService
				.getAttachmentBySearch(downloadPathSearchForm);
		Integer count = attachmentService
				.getCountBySearch(downloadPathSearchForm);
		Integer pageTotal = count / downloadPathSearchForm.getPageSize()
				+ ((count % downloadPathSearchForm.getPageSize() > 0) ? 1 : 0);

		return views.html.manager.attachment.upload_attachment_table_list
				.render(attachments, count,
						downloadPathSearchForm.getPageNum(), pageTotal);
	}

	public Result search() {
		Form<AttachmentSearchForm> imgPageForm = Form.form(
				AttachmentSearchForm.class).bindFromRequest();

		return ok(getList(imgPageForm.get()));
	}

	public Result deleteAttachment(Integer iid) {
		Integer count = attachmentService.getMapperCountByAttachmentIid(iid);
		if (count > 0) {
			Map<String, String> result = new HashMap<String, String>();
			result.put("fair",
					"The file has been associated goods, first remove the mapping relationship!");

			return ok(Json.toJson(result));
		}
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		Attachment attachmentByIid = attachmentService.getAttachmentByIid(iid);
		if (null == attachmentByIid) {
			result.put("result", false);

			return ok(Json.toJson(result));
		}

		boolean deleteFile = deleteFile(attachmentByIid.getCpath());
		if (!deleteFile) {
			result.put("result", false);

			return ok(Json.toJson(result));
		}
		attachmentDescService.deleteAttachmentDescByIattachmentid(iid);
		boolean delete = attachmentService.deleteAttachmentByIid(iid);
		result.put("result", delete);

		return ok(Json.toJson(result));
	}

	public boolean deleteFile(String sPath) {
		String rootPath = Play.application().configuration()
				.getConfig("attachment").getString("path");
		String fileRootPath = rootPath + File.separator + sPath;
		String msg = "\\" + java.io.File.separator;
		fileRootPath = fileRootPath.replaceAll("/", msg);
		Logger.debug("fileRootPath:{}", fileRootPath);
		boolean flag = false;
		File file = new File(fileRootPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return true;
		} else {
			// 路径为文件且不为空则进行删除
			if (file.isFile() && file.exists()) {
				Logger.debug("删除成功! 文件名:{}", fileRootPath);
				file.delete();
				deleteDir(file.getParent());

				flag = true;
			}
		}
		return flag;
	}

	public void deleteDir(String sPath) {
		Logger.debug(sPath);
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		String msg = "\\" + java.io.File.separator;
		sPath = sPath.replaceAll("/", msg);
		String rootPath = Play.application().configuration()
				.getConfig("attachment").getString("path");
		rootPath = rootPath.replaceAll("/", msg);
		if (rootPath.endsWith(sPath)) {
			Logger.debug("删除完成!");
			return;
		}

		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			Logger.debug("没有找到文件!");

			return;
		}
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		//Logger.debug("files:{}", files);
		if (null == files || 0 >= files.length) {
			dirFile.delete();
			Logger.debug("文件夹被删除:{}", sPath);
			String parentPath = dirFile.getParent();
			deleteDir(parentPath);
		}
		return;
	}
}
