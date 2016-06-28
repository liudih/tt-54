package controllers.manager;

import dto.Website;
import dto.Language;
import java.util.Map;
import java.util.List;
import java.util.Date;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import java.util.HashMap;
import play.mvc.Controller;
import java.util.ArrayList;
import java.util.stream.Collectors;
import play.twirl.api.Html;
import javax.inject.Inject;
import services.ISeoService;
import home.basic.model.SeoType;
import services.base.WebsiteService;
import services.base.lang.LanguageService;
import org.springframework.beans.BeanUtils;

import forms.base.homebasicSearch.SeoBaseForm;

/**
 * 首页管理控制器
 * 
 * @author guozy
 */
public class Seo extends Controller {

	@Inject
	private ISeoService seoService;

	@Inject
	private LanguageService languageService;

	@Inject
	private WebsiteService websiteService;

	private final Integer ADD_SEO_SUCCESS = 1;
	private final Integer ADD_SEO_ERROR = 2;
	private final Integer DELETE_SEO_SUCCESS = 3;
	private final Integer DELETE_SEO_ERROR = 4;
	private final Integer UPDATE_SEO_SUCCESS = 5;
	private final Integer UPDATE_SEO_ERROR = 6;

	/**
	 * 获取首页管理数据信息
	 * 
	 * @return
	 */
	@controllers.AdminRole(menuName = "HomeBasicMessage")
	public Result getSeoList(int p) {
		SeoBaseForm seoForm = new SeoBaseForm();
		seoForm.setPageNum(p);
		return ok(getSeoList(seoForm));
	}

	/**
	 * 根据相应的条件搜索首页基本信息数据
	 * 
	 * @return
	 */
	public Result search() {
		// 创建一个新的HomeBasicMessage实例，用来接受HTTP数据
		Form<dto.Seo> seoForm = Form.form(dto.Seo.class).bindFromRequest();
		// 将从表单得到的数据赋给HomeMessage实例
		dto.Seo seo = seoForm.get();
		SeoBaseForm seoBaseForm = new SeoBaseForm();
		BeanUtils.copyProperties(seo, seoBaseForm);
		return ok(getSeoList(seoBaseForm));
	};

	/**
	 * 获取首页基本信息的集合对象数据
	 * 
	 * @param seoForm
	 * @return
	 */
	private Html getSeoList(SeoBaseForm seoBaseForm) {
		List<Object> seoTypeList = new ArrayList<Object>();
		// 获取页面管理的说有数据信息集合
		List<dto.Seo> seos = seoService.getLists(seoBaseForm);
		SeoType[] seoTypes = SeoType.values();
		if (seoTypes.length > 0 || seoTypes != null) {
			for (SeoType homeBasicType : seoTypes) {
				seoTypeList.add(homeBasicType.getIndex());
			}
		}
		// 获取站点集合对象信息
		List<Website> iwebsiteList = websiteService.getAll();
		// 获取语言集合对象信息
		List<Language> languagesList = languageService.getAllLanguage();
		Map<Integer, String> siteAndNameMap = iwebsiteList.stream().collect(
				Collectors.toMap(a -> a.getIid(), a -> a.getCcode()));
		Map<Integer, String> languageAndNameMap = languagesList.stream()
				.collect(Collectors.toMap(a -> a.getIid(), a -> a.getCname()));
		// 获取首页的基本信息数据的条数
		Integer count = seoService.getCount(seoBaseForm);
		// 获取首页基本信息页面数量
		Integer pageTotal = count / seoBaseForm.getPageSize()
				+ ((count % seoBaseForm.getPageSize() > 0) ? 1 : 0);
		return views.html.manager.seo.seo.render(
				languagesList, iwebsiteList, seos, siteAndNameMap,
				languageAndNameMap, seoTypeList, count, seoBaseForm.getPageNum(),
				pageTotal);
	};

	/**
	 * 添加首页管理信息
	 * 
	 * @return
	 */
	public Result addSeo() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 创建一个新的HomeBasicMessage实例，用来接受HTTP数据
		Form<dto.Seo> seoForm = Form.form(dto.Seo.class)
				.bindFromRequest();
		// 将从表单得到的数据赋给HomeMessage实例
		dto.Seo fetchSeo = seoForm.get();
		if (fetchSeo == null) {
			return badRequest();
		}
		dto.Seo fetchSeoData=seoService.getSeoBylanguageIdAndSiteIdAndType(fetchSeo.getIwebsiteid(), fetchSeo.getIlanguageid(), fetchSeo.getCtype());
		if(fetchSeoData!=null){
			map.put("dataMessages", ADD_SEO_ERROR);
		}else {
			
			// 创建SEO实例对象
			dto.Seo seo = new dto.Seo();
			seo.setIwebsiteid(fetchSeo.getIwebsiteid());
			seo.setCcreatename(fetchSeo.getCcreatename());
			seo.setIlanguageid(fetchSeo.getIlanguageid());
			seo.setCtitle(fetchSeo.getCtitle());
			seo.setCkeywords(fetchSeo.getCkeywords());
			seo.setCtype(fetchSeo.getCtype());
			seo.setCdescription(fetchSeo.getCdescription());
			seo.setDcreatedate(new Date());
			
			if (seoService.insertSeo(seo)) {
				map.put("dataMessages", ADD_SEO_SUCCESS);
			} else {
				map.put("dataMessages", ADD_SEO_ERROR);
			}
		}
		return ok(Json.toJson(map));
	}

	/**
	 * 删除首页管理的数据信息
	 * 
	 * @return
	 */
	public Result deleteSeo() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 创建一个新的HomeBasicMessage实例，用来接受HTTP数据
		Form<dto.Seo> seoForm = Form.form(dto.Seo.class).bindFromRequest();
		// 将从表单得到的数据赋给HomeMessage实例
		dto.Seo seo = seoForm.get();
		if (seo == null) {
			return badRequest();
		}
		boolean result = seoService.deleteSeo(seo.getIid());
		if (result) {
			map.put("dataMessages", DELETE_SEO_SUCCESS);
		} else {
			map.put("dataMessages", DELETE_SEO_ERROR);
		}
		return ok(Json.toJson(map));
	}

	/**
	 * 修改首页管理的数据信息
	 * 
	 * @return
	 */
	public Result updateSeo() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 创建一个新的HomeBasicMessage实例，用来接受HTTP数据
		Form<dto.Seo> seoForm = Form.form(dto.Seo.class)
				.bindFromRequest();
		// 将从表单得到的数据赋给HomeMessage实例
		dto.Seo fetchSeo = seoForm.get();

		if (fetchSeo == null) {
			return badRequest();
		}
		// 创建SEO实例对象
		dto.Seo seo = new dto.Seo();
		;
		seo.setIwebsiteid(fetchSeo.getIwebsiteid());
		seo.setIlanguageid(fetchSeo.getIlanguageid());
		seo.setCmodifiedname(fetchSeo.getCmodifiedname());
		seo.setIid(fetchSeo.getIid());
		seo.setCtitle(fetchSeo.getCtitle());
		seo.setCkeywords(fetchSeo.getCkeywords());
		seo.setCtype(fetchSeo.getCtype());
		seo.setCdescription(fetchSeo.getCdescription());
		seo.setDmodifieddate(new Date());

		boolean result = seoService.updateSeo(seo);
		if (result) {
			map.put("dataMessages", UPDATE_SEO_SUCCESS);
		} else {
			map.put("dataMessages", UPDATE_SEO_ERROR);
		}
		return ok(Json.toJson(map));
	};

}
