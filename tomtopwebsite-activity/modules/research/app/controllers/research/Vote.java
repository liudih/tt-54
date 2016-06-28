package controllers.research;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;

import controllers.base.Home;
import controllers.base.Product;
import entity.activity.page.Page;
import entity.activity.page.PageTitle;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import service.activity.IPageService;
import service.activity.IVoteRecordService;
import services.base.FoundationService;
import services.research.vote.VoteCompositeEnquiry;
import services.research.vote.VoteCompositeRenderer;
import valueobjects.research.vote.VoteComposite;
import valueobjects.research.vote.VoteContext;
import values.activity.page.PageItemCount;

public class Vote extends Controller{
	@Inject
	VoteCompositeRenderer compositeRenderer;

	@Inject
	VoteCompositeEnquiry vce;

	@Inject
	FoundationService foundationService;

	@Inject
	IVoteRecordService iVoteRecordService;

	@Inject
	IPageService pageService;

	@Inject
	Product product;

	public Result view(String title) {
		VoteContext context = new VoteContext();
		//~现在先取站点1的数据
		context.setLanguageId(1);
		context.setWebsiteId(foundationService.getSiteID());
		context.setCurrency(foundationService.getCurrency());
		Logger.debug("get title -->" + title);
		Logger.info("lang {},site {},currency {}",
				foundationService.getLanguage(), foundationService.getSiteID(),
				foundationService.getCurrency());
		Page page = pageService.getPageByUrl(title, context.getWebsiteId());
		if (null != page) {
			PageTitle pageTitle = pageService.getPTByPageIdAndLId(
					page.getIid(), context.getLanguageId());
			VoteComposite vo = vce.getVoteComposite(context, page);
			return play.mvc.Results.ok(views.html.research.vote.votenew.render(
					vo, compositeRenderer, pageTitle, page));
		} else {
			return product.view(title);
		}
	}

}
