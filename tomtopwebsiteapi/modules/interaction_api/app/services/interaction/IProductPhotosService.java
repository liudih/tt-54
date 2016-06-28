package services.interaction;

import java.io.File;

import dto.interaction.InteractionProductMemberPhotos;

public interface IProductPhotosService {

	public abstract void pushPreveiwImageWithSession(File file, int index,
			String contentType);

	public abstract void pushPreveiwImageWithSession(String file, int index,
			String contentType);

	public abstract void clearPreveiwImageCount();

	public abstract int getPreveiwImageCount();

	public abstract byte[] getPreveiwImageWithSession(int index);

	public abstract byte[] getPreveiwImageWithSessionForWeb(int index);

	public abstract void delPreveiwImageWithSession(int index);

	public abstract String getContentTypeWithSession(int index);

	public abstract boolean addphotos(String comment, String previewIndexs,
			String listingid, String csku, int siteid, Integer commentid,
			String email);

	public abstract boolean addphotosForWeb(String comment,
			String previewIndexs, String listingid, String csku, int siteid,
			Integer commentid, String email);

	public abstract InteractionProductMemberPhotos getPhotoById(int id);

	public abstract byte[] getPhotoPartById(int id, int start, int len);

	public abstract boolean checkPhotoNum(String email, String listingid,
			Integer updateNum);

	public abstract boolean addPhotoForApp(
			InteractionProductMemberPhotos photo, String imgurl);
}