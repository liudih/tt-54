package controllers.manager.storage;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Request;
import services.base.CountryService;
import services.base.StorageArrivalService;
import services.base.StorageDefaultService;
import services.base.StorageParentService;
import services.base.StorageService;
import session.ISessionService;
import constant.Const;
import dto.Country;
import dto.Storage;
import dto.StorageArrival;
import dto.StorageDefault;
import dto.StorageParent;

public class StorageManagerController extends Controller {

	@Inject
	StorageParentService storageParentService;
	@Inject
	StorageArrivalService storageArrivalService;
	@Inject
	StorageDefaultService storageDefaultService;
	@Inject
	StorageService storageService;
	@Inject
	CountryService countryService;
	@Inject
	ISessionService sessionService;
	public Result storageParentSubList(String type, String cstorageName,
			Integer page, Integer pageSize) {
		if (StringUtils.isEmpty(type) || StringUtils.isEmpty(cstorageName)
				|| page < 0 || pageSize < 1) {
			Logger.info("storageParentSubList params null");
			return null;
		}
		List<StorageParent> storageInfoList = storageParentService
				.getStorageInfo(cstorageName, type, page, pageSize);
		if (CollectionUtils.isEmpty(storageInfoList)) {

			return null;
		}
		// 参数主要用与下拉列表
		List<StorageParent> storageParentList = storageParentService
				.getAllStorageParentList();
		StorageParent storageParent = storageInfoList.get(0);

		int pageTotal = 0;
		if (Const.STORAGE_TYPE_ARRIVAL.equals(type)) {
			List<StorageArrival> storageArrivalInfo = storageInfoList.get(0)
					.getStorageArrivalInfo();
			pageTotal = storageArrivalService
					.getCountStorageArrivalList(storageParent.getIid());
			List<Country> allCountries = countryService.getAllCountries();
			return ok(views.html.manager.storage.storage_arrival_table_list
					.render(storageParentList, storageParent,
							storageArrivalInfo, pageTotal, page,
							pageCount(pageTotal, pageSize),allCountries));

		} else if (Const.STORAGE_TYPE_DEFAULT.equals(type)) {
			List<StorageDefault> storageDefaultInfo = storageInfoList.get(0)
					.getStorageDefaultInfo();
			pageTotal = storageDefaultService
					.getCountStorageDefault(storageParent.getIid());
			List<Country> allCountries = countryService.getAllCountries();
			return ok(views.html.manager.storage.storage_default_table_list
					.render(storageParentList, storageParent,
							storageDefaultInfo, pageTotal, page,
							pageCount(pageTotal, pageSize),allCountries));

		} else if (Const.STORAGE_TYPE_SUB.equals(type)) {
			List<Storage> storageSubInfo = storageInfoList.get(0)
					.getStorageSubInfo();
			pageTotal = storageService.getCountStorages(storageParent.getIid());
			return ok(views.html.manager.storage.storage_parent_table_list
					.render(storageParentList, storageParent, storageSubInfo,
							pageTotal, page, pageCount(pageTotal, pageSize)));

		} else {

			List<Storage> storageSubInfo = storageInfoList.get(0)
					.getStorageSubInfo();
			pageTotal = storageService.getCountStorages(storageParent.getIid());
			return ok(views.html.manager.storage.storage_parent_table_list
					.render(storageParentList, storageParent, storageSubInfo,
							pageTotal, page, pageCount(pageSize, pageSize)));
		}

	}

	private int pageCount(int pageTotal, int pageSize) {
		return pageTotal % pageSize == 0 ? (pageTotal / pageSize)
				: ((pageTotal / pageSize) + 1);
	}

	@controllers.AdminRole(menuName = "StorageMgr")
	public Result storageParentInfo() {
		List<StorageParent> storageInfoList = storageParentService
				.getAllStorageParentList();
		List<Storage> allStorages = storageService.getAllStorages();
		return ok(views.html.manager.storage.storage_parent
				.render(storageInfoList,allStorages));
	}

	@controllers.AdminRole(menuName = "StorageArrival")
	public Result storageArrivalInfo() {

		List<StorageParent> storageInfoList = storageParentService
				.getAllStorageParentList();
		List<Country> allCountries = countryService.getAllCountries();
		return ok(views.html.manager.storage.storage_arrival
				.render(storageInfoList,allCountries));
	}

	@controllers.AdminRole(menuName = "StorageDefault")
	public Result storageDefaultInfo() {
		List<StorageParent> storageInfoList = storageParentService
				.getAllStorageParentList();
		List<Country> allCountries = countryService.getAllCountries();
		return ok(views.html.manager.storage.storage_default
				.render(storageInfoList,allCountries));
	}

	@controllers.AdminRole(menuName = "StorageArrival")
	public Result updateStorageArrival() {
		Form<StorageArrival> form = Form.form(StorageArrival.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		StorageArrival storageArrival = form.get();

		storageArrivalService.update(storageArrival);
		return redirect(controllers.manager.storage.routes.StorageManagerController
				.storageArrivalInfo());
	}

	@controllers.AdminRole(menuName = "StorageDefault")
	public Result updateStorageDefault() {
		Form<StorageDefault> form = Form.form(StorageDefault.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		StorageDefault storageDefault = form.get();
		storageDefaultService.update(storageDefault);

		return redirect(controllers.manager.storage.routes.StorageManagerController
				.storageDefaultInfo());
	}

	@controllers.AdminRole(menuName = "StorageMgr")
	public Result updateStorageSub() {
		Form<Storage> form = Form.form(Storage.class).bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		Storage storage = form.get();
		storageService.updateStorageParentById(storage);

		return redirect(controllers.manager.storage.routes.StorageManagerController
				.storageParentInfo());
	}

	public Result deleteStorageRecord(String type, Integer iid) {
		if (StringUtils.isEmpty(type) || iid < 0) {
			return ok("params error");
		}
		if (Const.STORAGE_TYPE_ARRIVAL.equals(type)) {
			storageArrivalService.delete(iid);
		} else if (Const.STORAGE_TYPE_DEFAULT.equals(type)) {
			storageDefaultService.delete(iid);

		} else if (Const.STORAGE_TYPE_SUB.equals(type)) {
			Storage storage = new Storage();
			storage.setIid(iid);
			storageService.deleteStorageParent(storage);
		}
		return ok();
	}

	public Result deleteStorageParentRecord(String name) {
		storageParentService.delete(name);
		return ok();
	}

	public Result addStorageArrival() {
		Form<StorageArrival> form = Form.form(StorageArrival.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}

		try {
			entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			StorageArrival storageArrival = form.get();
			if(user!=null){
				storageArrival.setCcreateuser(user.getCcreateuser());
			}
			storageArrivalService.insert(storageArrival);
		} catch (Exception e) {
			Logger.error("addStorageArrival error", e);
			return ok(views.html.manager.user.error.render());
		}
		return redirect(controllers.manager.storage.routes.StorageManagerController
				.storageArrivalInfo());
	}

	public Result addStorageDefault() {
		Form<StorageDefault> form = Form.form(StorageDefault.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}

		try {
			StorageDefault storageDefault = form.get();
			entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			if(user!=null){
				storageDefault.setCcreateuser(user.getCcreateuser());
			}
			storageDefaultService.insert(storageDefault);
		} catch (Exception e) {
			Logger.error("addStorageDefault error", e);
			return ok(views.html.manager.user.error.render());
		}
		return redirect(controllers.manager.storage.routes.StorageManagerController
				.storageDefaultInfo());
	}

	public Result addStorageParent() {
		Form<StorageParent> form = Form.form(StorageParent.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}

		try {
			StorageParent storageParent = form.get();
			entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			if(user!=null){
				storageParent.setCcreateuser(user.getCcreateuser());
			}
			storageParentService.insert(storageParent);
		} catch (Exception e) {
			Logger.error("addStorageParent error", e);
			return ok(views.html.manager.user.error.render());
		}
		return redirect(controllers.manager.storage.routes.StorageManagerController
				.storageParentInfo());
	}

}
