package services.interaction;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.inject.Inject;

import mapper.interaction.InteractionProductMemberPhotosMapper;
import mapper.order.DetailMapper;

import org.apache.commons.codec.binary.Base64;

import play.Logger;
import services.base.FoundationService;
import services.base.utils.FileUtils;
import services.order.IOrderStatusService;
import session.ISessionService;

import com.google.api.client.util.Lists;

import dto.interaction.InteractionProductMemberPhotos;
import dto.order.OrderDetail;

public class ProductPhotosService implements IProductPhotosService {

	@Inject
	ISessionService sessionService;

	@Inject
	FoundationService foundationService;

	@Inject
	InteractionProductMemberPhotosMapper mapper;

	@Inject
	IOrderStatusService orderStatusService;

	@Inject
	DetailMapper detailMapper;

	final String guid = "TYPEIID";

	final String countid = "countid";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.interaction.IProductPhotosService#pushPreveiwImageWithSession
	 * (java.io.File, int, java.lang.String)
	 */
	@Override
	public void pushPreveiwImageWithSession(File file, int index,
			String contentType) {

		String sessionid = foundationService.getSessionID();
		String fileid = sessionid + index;
		String contenttypeid = sessionid + guid + index;
		String countID = sessionid + countid;
		sessionService.set(countID, index);
		sessionService.set(fileid, file);
		sessionService.set(contenttypeid, contentType);
		this.clearPreveiwImageCount();
	}

	@Override
	public void pushPreveiwImageWithSession(String file, int index,
			String contentType) {
		Logger.debug("+++" + file);
		String sessionid = foundationService.getSessionID();
		Logger.debug("session111=" + sessionid);
		String fileid = sessionid + index;
		String contenttypeid = sessionid + guid + index;
		String countID = sessionid + countid;
		sessionService.set(countID, index);
		sessionService.set(fileid, file);
		sessionService.set(contenttypeid, contentType);
		this.clearPreveiwImageCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.interaction.IProductPhotosService#clearPreveiwImageCount()
	 */
	@Override
	public void clearPreveiwImageCount() {
		String sessionid = foundationService.getSessionID();
		String countID = sessionid + countid;
		sessionService.remove(countID);
		sessionService.set(countID, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.interaction.IProductPhotosService#getPreveiwImageCount()
	 */
	@Override
	public int getPreveiwImageCount() {
		String sessionid = foundationService.getSessionID();
		String countID = sessionid + countid;
		Integer count = (Integer) sessionService.get(countID);
		return count == null ? 0 : count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.interaction.IProductPhotosService#getPreveiwImageWithSession
	 * (int)
	 */
	@Override
	public byte[] getPreveiwImageWithSession(int index) {
		String sessionid = foundationService.getSessionID();
		String fileid = sessionid + index;
		File file = (File) sessionService.get(fileid);
		return FileUtils.toByteArray(file);

	}

	@Override
	public byte[] getPreveiwImageWithSessionForWeb(int index) {
		String sessionid = foundationService.getSessionID();
		String fileid = sessionid + index;
		String img = (String) sessionService.get(fileid);
		byte[] b = img.getBytes();
		Base64 base64 = new Base64();
		b = base64.decode(b);
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.interaction.IProductPhotosService#delPreveiwImageWithSession
	 * (int)
	 */
	@Override
	public void delPreveiwImageWithSession(int index) {
		String sessionid = foundationService.getSessionID();
		String fileid = sessionid + index;
		String contenttypeid = sessionid + guid + index;
		sessionService.remove(fileid);
		sessionService.remove(contenttypeid);
		String countID = sessionid + countid;
		Integer count = (Integer) sessionService.get(countID);
		count -= 1;
		sessionService.set(countID, count);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.interaction.IProductPhotosService#getContentTypeWithSession(int)
	 */
	@Override
	public String getContentTypeWithSession(int index) {
		String sessionid = foundationService.getSessionID();
		return (String) sessionService.get(sessionid + guid + index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.interaction.IProductPhotosService#addphotos(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, int,
	 * java.lang.Integer)
	 */
	@Override
	public boolean addphotos(String comment, String previewIndexs,
			String listingid, String csku, int siteid, Integer commentid,
			String email) {
		String sessionid = foundationService.getSessionID();
		Logger.debug("session2222=" + sessionid);
		StringTokenizer tokenizer = new StringTokenizer(previewIndexs, "|");
		List<InteractionProductMemberPhotos> list = Lists.newArrayList();

		while (tokenizer.hasMoreElements()) {
			String index = tokenizer.nextToken();
			String fileid = sessionid + index;
			String contenttypeid = sessionid + guid + index;

			File file = (File) sessionService.get(fileid);
			String type = (String) sessionService.get(contenttypeid);

			InteractionProductMemberPhotos p = new InteractionProductMemberPhotos();
			p.setCmemberemail(email);
			p.setClistingid(listingid);
			p.setBfile(FileUtils.toByteArray(file));
			p.setCcontenttype(type);
			p.setClabel(comment);
			p.setIcommentid(commentid);
			p.setCsku(csku);
			p.setIwebsiteid(siteid);
			p.setDcreatedate(new Date());
			int flag = mapper.insertSelective(p);
			p.setCimageurl("/interaction/product-photos/at?iid=" + p.getIid());
			mapper.updateByPrimaryKeySelective(p);
			if (flag > 0) {
				list.add(p);
			}
			sessionService.remove(fileid);
			sessionService.remove(contenttypeid);
		}
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addphotosForWeb(String comment, String previewIndexs,
			String listingid, String csku, int siteid, Integer commentid,
			String email) {
		String sessionid = foundationService.getSessionID();
		StringTokenizer tokenizer = new StringTokenizer(previewIndexs, "|");
		List<InteractionProductMemberPhotos> list = Lists.newArrayList();
		while (tokenizer.hasMoreElements()) {
			String index = tokenizer.nextToken();
			String fileid = sessionid + index;
			Logger.debug("mobile-fileid=" + fileid);
			String contenttypeid = sessionid + guid + index;
			String img = (String) sessionService.get(fileid);
			byte[] b = img.getBytes();
			Base64 base64 = new Base64();
			b = base64.decode(b);
			String type = (String) sessionService.get(contenttypeid);
			InteractionProductMemberPhotos p = new InteractionProductMemberPhotos();
			p.setCmemberemail(email);
			p.setClistingid(listingid);
			p.setBfile(b);
			p.setCcontenttype(type);
			p.setClabel(comment);
			p.setIcommentid(commentid);
			p.setCsku(csku);
			p.setIwebsiteid(siteid);
			p.setDcreatedate(new Date());
			int flag = mapper.insertSelective(p);
			p.setCimageurl("/interaction/product-photos/at?iid=" + p.getIid());
			mapper.updateByPrimaryKeySelective(p);
			if (flag > 0) {
				list.add(p);
			}
			sessionService.remove(fileid);
			sessionService.remove(contenttypeid);
		}
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.interaction.IProductPhotosService#getPhotoById(int)
	 */
	@Override
	public InteractionProductMemberPhotos getPhotoById(int id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public byte[] getPhotoPartById(int id, int start, int len) {
		InteractionProductMemberPhotos pm = mapper.selectByPrimaryKey(id);
		if (pm != null) {
			byte[] bytes = pm.getBfile();
			if (null == bytes || (start >= bytes.length)) {
				return null;
			}
			int tlen = start + len;
			if (tlen > bytes.length) {
				tlen = bytes.length;
			}
			Logger.debug("start {} len  {}   {}", start, len, bytes.length);
			return Arrays.copyOfRange(bytes, start, tlen);
		} else {
			return new byte[] {};
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.interaction.IProductPhotosService#checkPhotoNum(java.lang.String
	 * , java.lang.String, java.lang.Integer)
	 */
	@Override
	public boolean checkPhotoNum(String email, String listingid,
			Integer updateNum) {
		int existNum = mapper.getPhotoCheckNum(email, listingid);
		Integer status = orderStatusService
				.getIdByName(orderStatusService.COMPLETED);
		List<OrderDetail> detail = detailMapper.getOrderDetailsForPhotoByEmail(
				email, listingid, status);
		if ((detail.size() * 5 - existNum) >= updateNum
				&& (detail.size() * 5 - existNum) > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addPhotoForApp(InteractionProductMemberPhotos photo,
			String imgUrl) {
		String sessionid = foundationService.getSessionID();
		String fileid = sessionid + "1";
		Logger.debug("=app-sessionid=" + sessionid);
		String contenttypeid = sessionid + guid + "1";
		byte[] file = (byte[]) sessionService.get(fileid);
		String type = (String) sessionService.get(contenttypeid);
		Logger.debug("=app-file-length=" + file.length);
		Logger.debug("=app-file-type=" + type);
		int flag = mapper.insertSelective(photo);
		photo.setBfile(file);
		photo.setCcontenttype(type);
		photo.setCimageurl(imgUrl + photo.getIid());
		mapper.updateByPrimaryKeySelective(photo);
		sessionService.remove(fileid);
		sessionService.remove(contenttypeid);
		if (flag > 0) {
			return true;
		}
		return false;
	}
}
