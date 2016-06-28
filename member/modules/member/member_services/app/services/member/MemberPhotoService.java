package services.member;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.inject.Inject;

import mapper.member.MemberPhotoMapper;

import org.apache.commons.codec.binary.Hex;

import play.Logger;
import services.base.FoundationService;
import services.base.utils.FileUtils;
import services.member.login.ILoginService;
import session.ISessionService;
import valueobjects.member.MemberInSession;
import context.WebContext;
import dto.member.MemberPhoto;

public class MemberPhotoService implements IMemberPhotoService {

	@Inject
	MemberPhotoMapper photoMapper;

	@Inject
	ISessionService sessionService;

	@Inject
	ILoginService loginService;

	final String guid = "TYPEIID";

	@Inject
	FoundationService foundationService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.IMemberPhotoService#pushWithSession(java.io.File,
	 * java.lang.String)
	 */
	@Override
	public void pushWithSession(File file, String contentType) {

		String sessionid = loginService.getLoginData().getSessionId();
		String fileid = sessionid;
		String contenttypeid = sessionid + guid;
		sessionService.set(fileid, file);
		sessionService.set(contenttypeid, contentType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.IMemberPhotoService#getWithSession()
	 */
	@Override
	public byte[] getWithSession() {
		String sessionid = loginService.getLoginData().getSessionId();
		File file = (File) sessionService.get(sessionid);

		return FileUtils.toByteArray(file);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.IMemberPhotoService#getContentTypeWithSession()
	 */
	@Override
	public String getContentTypeWithSession() {
		String sessionid = loginService.getLoginData().getSessionId();
		return (String) sessionService.get(sessionid + guid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.IMemberPhotoService#getPhoto(java.lang.String)
	 */
	@Override
	public MemberPhoto getPhoto(String email, WebContext webContext) {
		Integer websiteId = foundationService.getSiteID(webContext);
		return photoMapper.getMemberPhotoByEamil(email, websiteId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.IMemberPhotoService#updatePhoto()
	 */
	@Override
	public boolean updatePhoto() {

		MemberInSession session = loginService.getLoginData();
		String sessionid = session.getSessionId();
		String eamil = session.getEmail();
		String fileid = sessionid;
		String contenttypeid = sessionid + guid;
		File file = (File) sessionService.get(fileid);
		String contenttype = (String) sessionService.get(contenttypeid);
		int siteID = foundationService.getSiteID();
		if (file == null) {
			return false;
		}

		byte[] buff = FileUtils.toByteArray(file);
		if (buff == null) {
			return false;
		}
		MemberPhoto photo = null;
		photo = photoMapper.getMemberPhotoByEamil(eamil, siteID);

		int result = 0;
		String md5;
		try {
			md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(
					buff));
		} catch (NoSuchAlgorithmException e) {
			Logger.error("member upload photo error:{}", e.getMessage());
			return false;
		}
		if (photo == null) {
			photo = new MemberPhoto();
			photo.setBfile(buff);
			photo.setCcontenttype(contenttype);
			photo.setCemail(eamil);
			photo.setCmd5(md5);
			photo.setIwebsiteid(siteID);
			result = photoMapper.insert(photo);
		} else {
			photo.setBfile(buff);
			photo.setCmd5(md5);
			photo.setCcontenttype(contenttype);
			result = photoMapper.updateByPrimaryKey(photo);
		}
		return result > 0 ? true : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.member.IMemberPhotoService#pushRemoteWithSession(java.io.InputStream
	 * )
	 */
	@Override
	public void pushRemoteWithSession(String filename, InputStream is) {
		String sessionid = loginService.getLoginData().getSessionId();

		byte[] bytes = this.inputstreamToBytes(is);
		sessionService.set(sessionid, bytes);

	}

	private byte[] inputstreamToBytes(InputStream is) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		try {
			while ((n = is.read(buffer)) != -1) {
				out.write(buffer, 0, n);
			}
		} catch (IOException e) {
			Logger.debug("会员上传图片数据流转换失败====================");
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.member.IMemberPhotoService#getRemoteWithSession()
	 */
	@Override
	public byte[] getRemoteWithSession(String sessionId) {
		byte[] bytes = (byte[]) sessionService.get(sessionId);

		return bytes;
	}

	@Override
	public boolean updateMemberPhoto(MemberPhoto memberPhoto) {
		Integer result = 0;
		MemberPhoto memberPhotoByEamil = photoMapper.getMemberPhotoByEamil(
				memberPhoto.getCemail(), memberPhoto.getIwebsiteid());
		if (null != memberPhotoByEamil) {
			memberPhoto.setIid(memberPhotoByEamil.getIid());
			result = photoMapper.updateByPrimaryKey(memberPhoto);
		} else {
			result = photoMapper.insert(memberPhoto);
		}
		return result > 0 ? true : false;
	}

	@Override
	public byte[] getMemberPhotoPart(String email, WebContext webContext,
			int start, int len) {
		Integer websiteId = foundationService.getSiteID(webContext);
		MemberPhoto memberPhoto = photoMapper.getMemberPhotoByEamil(email,
				websiteId);
		if (null == memberPhoto || (start >= memberPhoto.getBfile().length)) {
			return null;
		}
		return getImageByteByLength(memberPhoto.getBfile(), start, len);
	}

	private byte[] getImageByteByLength(byte[] bytes, int start, int len) {
		if (null == bytes || (start >= bytes.length)) {
			return null;
		}
		int tlen = start + len;
		if (tlen > bytes.length) {
			tlen = bytes.length;
		}
		Logger.debug("start {} len  {}   {}", start, tlen, bytes.length);
		return Arrays.copyOfRange(bytes, start, tlen);
	}
}
