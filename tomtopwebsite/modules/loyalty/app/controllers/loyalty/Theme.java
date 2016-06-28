package controllers.loyalty;

import javax.inject.Inject;

import controllers.base.Home;
import entity.loyalty.ThemeCss;
import entity.loyalty.ThemeTitle;
import play.mvc.Result;
import services.base.FoundationService;
import services.loyalty.theme.IThemeCssService;
import services.loyalty.theme.IThemeService;
import services.loyalty.theme.IThemeTitleService;
import services.loyalty.theme.ThemeCompositeEnquiry;
import services.loyalty.theme.ThemeCompositeRenderer;
import valueobjects.loyalty.ThemeComposite;
import valueobjects.loyalty.ThemeContext;

public class Theme {
	@Inject
	ThemeCompositeRenderer compositeRenderer;
	
	@Inject
	ThemeCompositeEnquiry tce;
	
	@Inject
	IThemeService themeService;
	
	@Inject
	IThemeCssService themeCssService;
	
	@Inject
	FoundationService foundationService;
	
	@Inject
	IThemeTitleService themeTitleService;
	
	/**
	 * 
	 * @Title: view
	 * @Description: TODO(主题页面)
	 * @param @param title
	 * @param @return
	 * @return Result
	 * @throws 
	 * @author yinfei
	 */
	public Result view(String title){
		ThemeContext context = new ThemeContext();
		context.setLanguageId(foundationService.getLanguage());
		context.setWebsiteId(foundationService.getSiteID());
		context.setCurrency(foundationService.getCurrency());
		entity.loyalty.Theme theme = themeService.getThemeByName(title,context.getWebsiteId());
		if(null != theme){
			ThemeCss tc = themeCssService.getThemeCssById(theme.getIcssid());
			ThemeTitle tt = themeTitleService.getTTByThemeIdAndLanguageId(theme.getIid(),context.getLanguageId());
			ThemeComposite vo = tce.getThemeComposite(context,theme);
			return play.mvc.Results.ok(views.html.loyalty.theme.theme.render(vo, compositeRenderer,tc,tt,theme));
		}else{
			return Home.notFoundResult();
		}
	}

}
