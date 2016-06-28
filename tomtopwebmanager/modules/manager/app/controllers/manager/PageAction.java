package controllers.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Logger.ALogger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import services.activity.page.IPageService;
import services.activity.page.IPageTitleService;
import services.base.FoundationService;
import services.base.WebsiteService;
import services.image.ImageEnquiryService;
import services.manager.AdminUserService;
import valueobject.activity.page.Page;
import valueobject.activity.page.PageTitle;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import dto.SimpleLanguage;
import dto.Website;
import dto.image.Img;
import dto.image.ImgUseMapping;
import forms.activity.page.PageForm;
import forms.img.ImgPageForm;

/**
 * 页面管理页面
 * 
 * @author liu
 *
 */
public class PageAction extends Controller {

	@Inject
	private ImageEnquiryService imageEnquiryService;

	/**
	 * 日志
	 */
	private ALogger logger = Logger.of(this.getClass());

	/**
	 * 基础服务
	 */
	@Inject
	FoundationService fService;

	/**
	 * 页面服务接口
	 */
	@Inject
	IPageService pageService;

	/**
	 * 站点服务
	 */
	@Inject
	WebsiteService websiteService;

	/**
	 * 语言服务接口
	 */
	@Inject
	ILanguageService languageService;

	/**
	 * 主题的标题服务接口
	 */
	@Inject
	IPageTitleService pageTitleService;

	private final String LOTTERY_LABEL = "lottery";

	/**
	 * 管理方法，分页显示数据
	 * 
	 * @param p
	 * @return
	 */
	public Result manage(int p) {
		try {
			PageForm page = new PageForm();
			String itype = request().getQueryString("itype");
			String curl = request().getQueryString("curl");
			String ienable = request().getQueryString("ienable");
			page.setItype(StringUtils.isNotBlank(itype) ? Integer
					.valueOf(itype) : null);

			page.setIenable(StringUtils.isNotBlank(ienable) ? Integer
					.valueOf(ienable) : null);
			page.setCurl(curl);
			ImgUseMapping aImgUseMapping = new ImgUseMapping();
			valueobjects.base.Page<PageForm> pages = pageService.getPage(p, 15,
					page);
			List<SimpleLanguage> languageList = languageService
					.getAllSimpleLanguages();
			List<Website> websites = websiteService.getAll();
			ImgPageForm form = new ImgPageForm();
			List<ImgUseMapping> ImgUseMappingList = imageEnquiryService
					.getImgUseMappingByClabel(LOTTERY_LABEL);
			return ok(views.html.manager.page.page_manage.render(
					ImgUseMappingList, pages, page,
					fService.getLanguage(), languageList, websites));
		} catch (Exception e) {
			logger.error("页面错误" + e.getMessage());
			return badRequest("页面错误");
		}
	}

	/**
	 * 删除
	 * 
	 * @param id
	 *            id
	 * @param p
	 *            需要跑转的页面索引
	 * @return
	 */
	public Result delete(int id, int p) {
		try {
			pageService.deleteByID(id);
			return redirect(controllers.manager.routes.PageAction.manage(p));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("delete page sql error,errormsg:" + e.getMessage());
			return badRequest("delete data error:,id= " + id);
		}
	}

	/**
	 * 获取数据，并跳转到编辑页面
	 * 
	 * @param id
	 *            主题id
	 * @return
	 */
	public Result get(int id) {
		Page page = pageService.getById(id);
		Map<Integer, PageTitle> pageTitleMap = null;
		if (page != null) {
			List<PageTitle> themeTitles = pageTitleService.getListByPageid(page
					.getIid());
			if (themeTitles != null && themeTitles.size() > 0) {
				pageTitleMap = Maps.uniqueIndex(themeTitles,
						new Function<PageTitle, Integer>() {
							@Override
							public Integer apply(PageTitle paramF) {
								// TODO Auto-generated method stub
								return paramF.getIlanguageid();
							}
						});
			}
		}
		return ok("{\"page\":" + Json.toJson(page) + ",\"langs\":"
				+ Json.toJson(pageTitleMap) + "}");
	}

	/**
	 * 保存修改后的数据
	 * 
	 * @return
	 */
	public Result save() {
		Form<PageForm> form = Form.form(PageForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PageForm page = form.get();
		if (page.getIid() == null) {
			return badRequest("id can't be empty");
		}
		if (page.getDenableenddate().before(page.getDenablestartdate())) {
			return badRequest("end time can not be less than start time");
		}
		page.setCupdateuser(AdminUserService.getInstance().getCuerrentUser()
				.getCusername());
		page.setDupdatedate(new Date());
		page.setCurl(page.getCurl().toLowerCase().trim());
		if (page.getDenablestartdate().equals(page.getDenableenddate())) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(page.getDenableenddate());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			page.setDenableenddate(calendar.getTime());
		}
		try {
			pageService.updateInfo(page);

		} catch (Exception e) {
			logger.error("update page sql error,errormsg:" + e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.routes.PageAction.manage(p));
	}

	/**
	 * 新增
	 * 
	 * @return
	 */
	public Result add() {
		Form<PageForm> form = Form.form(PageForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		PageForm pageForm = form.get();
		if (pageForm.getDenableenddate().before(pageForm.getDenablestartdate())) {
			return badRequest("end time Can not be less than start time");
		}
		String admin = AdminUserService.getInstance().getCuerrentUser()
				.getCusername();
		pageForm.setCcreateuser(admin);
		Date currDate = new Date();
		pageForm.setDcreatedate(currDate);
		pageForm.setDupdatedate(currDate);
		pageForm.setCupdateuser(admin);
		pageForm.setCurl(pageForm.getCurl().toLowerCase().trim());
		if (pageForm.getDenablestartdate().equals(pageForm.getDenableenddate())) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(pageForm.getDenableenddate());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			pageForm.setDenableenddate(calendar.getTime());
		}
		try {
			pageService.insertInfo(pageForm);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("insert page error,URL can not repeat or Other mistakes,errormsg:"
					+ e.getMessage());
			return badRequest("form data error: " + form.errorsAsJson());
		}
		String sp = form.data().get("p");
		int p = StringUtils.isNotBlank(sp) ? Integer.valueOf(sp) : 1;
		return redirect(controllers.manager.routes.PageAction.manage(p));
	}

	/**
	 * 验证
	 * 
	 * @return
	 */
	public Result validate() {
		return pageService.validateUrl(request().getQueryString("url").trim()
				.toLowerCase()) > 0 ? ok("1") : ok("0");
	}
}
