package com.rabbit.services.iservice.product;

import java.util.List;

public interface IImageUpdateService {

	public abstract boolean deleteImByPaths(List<String> paths);
}