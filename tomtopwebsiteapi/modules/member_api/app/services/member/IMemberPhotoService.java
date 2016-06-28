package services.member;

import java.io.File;
import java.io.InputStream;

import context.WebContext;
import dto.member.MemberPhoto;

public interface IMemberPhotoService {

	public abstract void pushWithSession(File file, String contentType);

	public abstract byte[] getWithSession();

	public abstract String getContentTypeWithSession();

	public abstract MemberPhoto getPhoto(String email, WebContext webContext);

	public abstract boolean updatePhoto();

	/**
	 * 远程上传文件，示例
	 * 
	 * HessianProxyFactory factory = new HessianProxyFactory();  
     *   Uploader uploader = (Uploader) factory.create(Uploader.class, url);  
     *  InputStream data = new BufferedInputStream(new FileInputStream("D:\\test.7z"));  
     *  uploader.upload("test.7z", data);  
	 * @param is
	 */
	public abstract void pushRemoteWithSession(String filename, InputStream is);

	public abstract byte[] getRemoteWithSession(String sessionId);
	
	public abstract boolean updateMemberPhoto(MemberPhoto memberPhoto);
	
	public abstract byte[] getMemberPhotoPart(String email, WebContext webContext, int start, int len);
}