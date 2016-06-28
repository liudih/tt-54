package services.image;

import javax.inject.Inject;

import mapper.image.UploadFilePathMapper;
import dto.image.UploadFilePath;

public class UploadFilePathUpdateService {

	@Inject
	UploadFilePathMapper mapper;

	public boolean insertUploadFilePath(UploadFilePath uploadFilePath) {
		return mapper.createPath(uploadFilePath) > 0;
	}
	
	public boolean deleteUploadFilePath(Integer iid) {
		return mapper.deletePathById(iid) > 0;
	}
}
