package com.rabbit.services.serviceImp.image;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.conf.imagemapper.ImageMapper;
import com.rabbit.services.iservice.product.IImageUpdateService;
@Service
public class ImageUpdateService implements IImageUpdateService {

	@Autowired
	ImageMapper mapper;

	@Override
	public boolean deleteImByPaths(List<String> paths) {
		return mapper.deleteImageByPaths(paths) > 0 ? true : false;
	}
}
