package services.image;

import java.util.List;

import javax.inject.Inject;

import mapper.image.UploadFilePathMapper;
import dto.image.UploadFilePath;

public class FilePathEnquiryService {

	@Inject
	UploadFilePathMapper filePathMapper;
	
	public List<UploadFilePath> getAllFilePath() {
		return filePathMapper.getAllFilePath();
	}
	
	public UploadFilePath getUploadFilePathByPath(String path) {
		return filePathMapper.getUploadFilePathByPath(path);
	}
}
