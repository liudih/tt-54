package controllers.manager.google.category;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.google.category.AttributeKeyMapper;
import mapper.google.category.AttributeValueMapper;
import mapper.google.category.GoogleAttributeMapper;
import mapper.google.category.GoogleCategoryDetailMapper;
import mapper.google.category.GoogleCategoryMapMapper;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.With;
import service.google.category.GoogleCategoryService;
import services.ILanguageService;
import services.attribute.AttributeService;
import services.base.FoundationService;
import services.base.WebsiteService;
import services.manager.AdminUserService;
import services.product.google.feeds.IGoogleCategoryBaseService;
import valueobjects.base.Page;

import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;

import controllers.InterceptActon;
import dao.product.IProductTranslateEnquiryDao;
import dto.AttributeKeyItem;
import dto.SimpleLanguage;
import dto.Website;
import dto.product.ProductTranslate;
import dto.product.google.category.AddSkuForm;
import dto.product.google.category.AttributeKey;
import dto.product.google.category.GoogleAttributForm;
import dto.product.google.category.GoogleAttribute;
import dto.product.google.category.GoogleCategoryDetail;
import dto.product.google.category.GoogleCategoryMapper;
import dto.product.google.category.SkuDetail;

@With(InterceptActon.class)
public class GoogleCategory extends Controller {

	@Inject
	ILanguageService languageService;

	@Inject
	IGoogleCategoryBaseService googleCategoryBaseService;

	@Inject
	GoogleCategoryDetailMapper categoryDetialMapper;

	@Inject
	GoogleCategoryMapMapper categoryMapMapper;

	@Inject
	WebsiteService websiteService;

	@Inject
	FoundationService fService;

	@Inject
	IProductTranslateEnquiryDao productTranslate;

	@Inject
	GoogleCategoryService categoryService;

	@Inject
	GoogleAttributeMapper attributeMapper;

	@Inject
	AttributeKeyMapper keyMapper;

	@Inject
	AttributeValueMapper valueMapper;

	@Inject
	AttributeService attributeService;

	public Result googleCategoryManager(int p, String cname) {

		List<dto.product.google.category.GoogleCategory> categories = googleCategoryBaseService
				.getFirstCategory();

		AddSkuForm addSkuForm = new AddSkuForm();
		if (null == cname||cname.equals("")) {
			String name = request().getQueryString("cname");
			addSkuForm.setCname(name);
		} else {
			addSkuForm.setCname(cname);
		}
		List<AttributeKeyItem> attributeKeys = attributeService
				.getAttributeKeyByLanguageId(1);

		Page<GoogleAttributForm> attList = categoryService.getAttr(p, 15,
				addSkuForm);

		return ok(views.html.manager.google.category.google_categoryedit
				.render(categories, attList, cname, attributeKeys));

	}

	public Result selectChildren(int cid) {
		List<dto.product.google.category.GoogleCategory> categories = googleCategoryBaseService
				.getChildsByParentId(cid);
		return ok(Json.toJson(categories));
	}

	public Result categoryDetail(int cid, String name, int p) {
		AddSkuForm addSkuForm = new AddSkuForm();
		String sku = request().getQueryString("csku");
		addSkuForm.setIcategory(cid);
		addSkuForm.setCsku(sku);
		List<dto.product.google.category.GoogleCategory> categories = googleCategoryBaseService
				.getFirstCategory();
		Page<GoogleCategoryMapper> cmapper = categoryService.getCategory(p, 15,
				addSkuForm);
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		List<Website> websites = websiteService.getAll();
		return ok(views.html.manager.google.category.google_categoryMessage
				.render(categories, cmapper, name, fService.getLanguage(),
						languageList, websites, sku, cid));
	}

	public Result addSku() {
		Form<AddSkuForm> form = Form.form(AddSkuForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest();
		}
		AddSkuForm detail = form.get();
		String skus = detail.getCsku();
		String[] csku = skus.split(",");
		for (int i = 0; i < csku.length; i++) {
			categoryMapMapper.add(csku[i], detail.getWebsiteid(),
					detail.getIcategory(), AdminUserService.getInstance()
							.getCuerrentUser().getCusername());
		}
		return redirect(routes.GoogleCategory.categoryDetail(
				detail.getIcategory(), detail.getCname(), 1));
	}

	public Result selectDetail() {
		Form<AddSkuForm> form = Form.form(AddSkuForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest();
		}
		AddSkuForm detail = form.get();
		return redirect(routes.GoogleCategory.categoryDetail(
				detail.getIcategory(), detail.getCname(), 1));
	}

	public Result addDetail() {
		Form<AddSkuForm> form = Form.form(AddSkuForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest();
		}
		AddSkuForm detail = form.get();
		for (SkuDetail skuDetail : detail.getSkuDetails()) {
			List<GoogleCategoryDetail> cate = categoryDetialMapper
					.selectDetailBySkuAndLanguage(detail.getCsku(),
							skuDetail.getIlanguageid());
			if (cate.size() != 0 && cate != null) {
				for (GoogleCategoryDetail de : cate) {
					if (!skuDetail.getCtitle().equals(de.getCtitle())
							|| !skuDetail.getCdescription().equals(
									de.getCdescription())) {
						Date date = new Date();
						categoryDetialMapper.updateDetail(detail.getCsku(),
								skuDetail.getIlanguageid(),
								skuDetail.getCtitle(),
								skuDetail.getCdescription(), AdminUserService
										.getInstance().getCuerrentUser()
										.getCusername(), date);
					}
				}
			} else if (!skuDetail.getCtitle().isEmpty()
					|| !skuDetail.getCdescription().isEmpty()) {
				categoryDetialMapper
						.addDetail(detail.getCsku(),
								skuDetail.getIlanguageid(),
								skuDetail.getCtitle(),
								skuDetail.getCdescription(), AdminUserService
										.getInstance().getCuerrentUser()
										.getCusername());
			}
		}

		return redirect(routes.GoogleCategory.categoryDetail(
				detail.getIcategory(), detail.getCname(), 1));
	}

	public Result selectSkuDetail(int cid, String sku) {
		/**
		 * select title、description group by languager
		 * 
		 */

		List<GoogleCategoryDetail> getDetails = categoryDetialMapper
				.getDetails(sku);
		Map<Integer, Object> categoryDetialMapper = Maps.newHashMap();
		if (getDetails.size() != 0 && getDetails != null) {
			for (GoogleCategoryDetail detail : getDetails) {
				categoryDetialMapper.put(detail.getIlanguageid(), detail);
			}
		} else {
			List<ProductTranslate> translates = productTranslate
					.getProductTranslateBySku(sku);
			if (translates.size() != 0 && translates != null) {
				for (ProductTranslate translate : translates) {
					categoryDetialMapper.put(translate.getIlanguageid(),
							translate);
				}
			}
		}
		return ok(Json.toJson(categoryDetialMapper));
	}

	public Result delSku() {
		Form<AddSkuForm> form = Form.form(AddSkuForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest();
		}
		AddSkuForm detail = form.get();
		categoryMapMapper.deleteByIid(detail.getIid());
		categoryDetialMapper.deleteBySkuAndCid(detail.getCsku());
		return redirect(routes.GoogleCategory.categoryDetail(
				detail.getIcategory(), detail.getCname(), 1));
	}

	public Result importGoogleCategorys() {
		MultipartFormData multidata = request().body().asMultipartFormData();
		int importcount = 0;
		List<String> uploadSuccess = Lists.newArrayList();
		List<String> uploadfail = Lists.newArrayList();
		if (multidata != null) {
			List<FilePart> flist = request().body().asMultipartFormData()
					.getFiles();
			String encoding = "UTF-8";
			for (FilePart f : flist) {
				InputStreamReader read = null;
				try {
					read = new InputStreamReader(new FileInputStream(
							f.getFile()), encoding);// 考虑到编码格式
					BufferedReader bufferedReader = new BufferedReader(read);
					String lineTxt = null;
					dto.product.google.category.GoogleCategory googleca = new dto.product.google.category.GoogleCategory();
					while ((lineTxt = bufferedReader.readLine()) != null) {
						if (lineTxt.contains("-")) {
							String[] values = lineTxt.split("-");
							int cid = 0;
							try {
								cid = Integer.valueOf(values[0].trim());
							} catch (Exception ex) {
								continue;
							}
							googleca.setIcategory(cid);
							googleca.setCpath(values[1].trim());
							importcount += googleCategoryBaseService
									.saveOrUpdate(googleca);
						}
					}
					uploadSuccess.add("import category count " + importcount);
				} catch (IOException e) {
					Logger.error("import google category error: ", e);
					uploadfail.add(e.getMessage());
					return play.mvc.Results.internalServerError(e.getMessage());
				} finally {
					if (read != null)
						try {
							read.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}
		Map<String, List<String>> resultmap = Maps.newHashMap();
		resultmap.put("fail", uploadfail);
		resultmap.put("success", uploadSuccess);
		return ok(views.html.manager.attachment.upload_attachment_result
				.render(resultmap));
	}

	public Result selectAttr() {
		Form<AddSkuForm> form = Form.form(AddSkuForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest();
		}
		AddSkuForm detail = form.get();
		return redirect(routes.GoogleCategory.googleCategoryManager(1,
				detail.getCname()));
	}

	public Result addAttr() {
		Form<GoogleAttributForm> form = Form.form(GoogleAttributForm.class)
				.bindFromRequest();
		GoogleAttributForm detail = form.get();
		AttributeKey attkey = keyMapper.getAttrByName(detail.getCkeyname());
		if (attkey != null) {
			AttributeKey attr = keyMapper.selectIidByName(detail.getCkeyname());
			attributeMapper.insertAttr(detail.getIcategoryid(), attr.getIid(),
					detail.getWkeyId());
		} else {
			int result = keyMapper.insertKey(detail.getCkeyname());
			if (result > 0) {
				AttributeKey attr = keyMapper.selectIidByName(detail
						.getCkeyname());
				attributeMapper.insertAttr(detail.getIcategoryid(),
						attr.getIid(), detail.getWkeyId());
			}
		}
		return redirect(routes.GoogleCategory.googleCategoryManager(1, null));

	}

	public Result delAttr(int id, int cid, String cname, String keyname) {
		attributeMapper.delAttrByIid(id);
		return redirect(routes.GoogleCategory.googleCategoryManager(1, cname));
	}

	public Result checkSku(String skus, int cid) {
		List<String> mappers = categoryMapMapper.getSkusByCid(cid);
		String r = "";
		String[] csku = skus.split(",");
		for (int i = 0; i < csku.length; i++) {
			if (mappers.contains(csku[i])) {
				r = csku[i] + "已存在！";
				break;
			}
		}
		return ok(Json.toJson(r));
	}

	public Result checkAttr(String attName, int cid, int watt) {
		String r = "";
		AttributeKey aKey = keyMapper.selectIidByName(attName);
		if (aKey != null) {
			GoogleAttribute attribute = attributeMapper.getAttrByKeyId(
					aKey.getIid(), cid);
			if (attribute != null) {
				r = "该属性已存在！";
			} else {
				GoogleAttribute att = attributeMapper.getWebAttrByKeyId(watt,
						cid);
				if (att != null) {
					r = " 网站品类属性已存在！";
				}
			}
		}else{
			GoogleAttribute att = attributeMapper.getWebAttrByKeyId(watt,
					cid);
			if (att != null) {
				r = " 网站品类属性已存在！";
			}
		}

		return ok(Json.toJson(r));
	}
}
