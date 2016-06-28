package controllers.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.databind.JsonNode;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.product.IProductExplainService;
import controllers.InterceptActon;
import forms.product.ProductExplainForm;

@With(InterceptActon.class)
public class ProductExplain extends Controller {
	@Inject
	IProductExplainService productExplainService;

	public Result productExplainManager() {
		return ok(views.html.manager.productExplain.productExplain_manager
				.render());
	}

	public Result productExplainEdit(Integer websiteId, Integer languageId) {
		List<dto.product.ProductExplain> productExplains = productExplainService
				.getProductExplainsBySiteAndLan(websiteId, languageId);
		List<ProductExplainForm> productExplainForms = new ArrayList<ProductExplainForm>();
		if (!productExplains.isEmpty()) {
			productExplainForms = Lists
					.transform(
							productExplains,
							productExplain -> {
								ProductExplainForm productExplainForm = new ProductExplainForm();
								productExplainForm.setIid(productExplain
										.getIid());
								productExplainForm
										.setIlanguageid(productExplain
												.getIlanguageid());
								productExplainForm.setIwebsiteid(productExplain
										.getIwebsiteid());
								productExplainForm.setCtype(productExplain
										.getCtype());
								productExplainForm.setCcontent(productExplain
										.getCcontent());
								return productExplainForm;
							});
		}

		return ok(views.html.manager.productExplain.product_explain_table_list
				.render(productExplainForms));
	}

	public Result productExplainUpdate() {
		Form<ProductExplainForm> productExplainUpdateForm = Form.form(
				ProductExplainForm.class).bindFromRequest();
		ProductExplainForm productExplainForm = productExplainUpdateForm.get();
		dto.product.ProductExplain productExplain = new dto.product.ProductExplain();
		BeanUtils.copyProperties(productExplainForm, productExplain);
		boolean productExplainUpdate = false;
		Integer websiteId = productExplain.getIwebsiteid();
		Integer languageId = productExplain.getIlanguageid();
		String type = productExplain.getCtype();
		dto.product.ProductExplain productE = productExplainService
				.getProductExplainBySiteIdAndLanIdAndType(websiteId,
						languageId, type);
		if (productE != null) {
			productExplain.setIid(productE.getIid());
		}
		if (productExplain.getIid() != null) {
			productExplainUpdate = productExplainService
					.updateProductExplain(productExplain);
		} else {
			productExplainUpdate = productExplainService
					.addProductExplain(productExplain);
		}
		if (productExplainUpdate) {
			return productExplainEdit(productExplainForm.getIwebsiteid(),
					productExplainForm.getIlanguageid());
		} else {
			return badRequest();
		}
	}

	public Result productExplainDelete() {
		JsonNode jsonNode = request().body().asJson();
		Integer iid = jsonNode.get("id").asInt();
		boolean deleteFlag = productExplainService
				.deleteProductExplainById(iid);
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", deleteFlag);

		return ok(Json.toJson(result));
	}
}
