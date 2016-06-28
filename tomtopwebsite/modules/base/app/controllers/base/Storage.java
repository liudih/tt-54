package controllers.base;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.StorageService;
import com.google.common.collect.Collections2;

public class Storage extends Controller {

	@Inject
	StorageService storageEnquiryService;

	public Result getAllStorages() {

		List<dto.Storage> storages = storageEnquiryService
				.getAllStorages();
		Collection<com.website.dto.Storage> dtoStorages = null;
		if (null != storages && storages.size() > 0) {
			dtoStorages = Collections2.transform(storages, obj -> {
				com.website.dto.Storage storage = new com.website.dto.Storage();
				storage.setId(obj.getIid());
				storage.setName(obj.getCstoragename());
				return storage;
			});
		}

		if (null == dtoStorages) {
			return notFound();
		} else {
			return ok(Json.toJson(dtoStorages));
		}
	}

}
