package services.product.fragment.renderer;

import java.util.List;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.member.login.ILoginService;
import services.product.IProductFragmentRenderer;
import valueobjects.interaction.Comment;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductRenderContext;

public class InteractionCommentFragmentRenderer implements
		IProductFragmentRenderer {
	
	@Inject
	ILoginService loginService;

	@Override
	public Html render(IProductFragment fragment, ProductRenderContext context) {
		List<Html> loginButtons = loginService.getOtherLoginButtons();
		return views.html.interaction.fragment.interaction_comments.render((Comment) fragment,loginButtons);
	}

}
