package services.interaction.product.post;

import mapper.interaction.ProductPostEvaluateMapper;
import mapper.interaction.ProductPostHelpQtyMapper;

import com.google.inject.Inject;

import dto.interaction.ProductPostEvaluate;
import dto.interaction.ProductPostHelpQty;

public class ProductPostEvaluateService {

	@Inject
	ProductPostEvaluateMapper evaluateMapper;

	@Inject
	ProductPostHelpQtyMapper helpQtyMapper;

	public boolean addEvalute(ProductPostEvaluate evalute) {
		boolean result = evaluateMapper.insertSelective(evalute) > 0 ? true
				: false;
		if (!result) {
			return false;
		}
		int ipostid = evalute.getIpostid();

		ProductPostHelpQty helpQty = helpQtyMapper
				.selectByProductPostId(ipostid);

		if (helpQty == null) {
			helpQty = new ProductPostHelpQty();
			helpQty.setIpostid(ipostid);
			if (evalute.getIhelpfulcode() == 1) {
				helpQty.setIhelpfulqty(1);
				helpQty.setInothelpfulqty(0);
			} else if (evalute.getIhelpfulcode() == 0) {
				helpQty.setInothelpfulqty(1);
				helpQty.setIhelpfulqty(0);
			}

			return helpQtyMapper.insert(helpQty) > 0 ? true : false;
		} else {
			if (evalute.getIhelpfulcode() == 1) {
				int qty = helpQty.getIhelpfulqty();
				helpQty.setIhelpfulqty(qty + 1);
			} else if (evalute.getIhelpfulcode() == 0) {
				int qty = helpQty.getInothelpfulqty();
				helpQty.setInothelpfulqty(qty);
			}

		}

		return helpQtyMapper.updateByPrimaryKey(helpQty) > 0 ? true : false;
	}

	public boolean checkExtist(Integer ipostid, String cmemberemail) {
		ProductPostEvaluate evalute = evaluateMapper.getProductEvaluteOne(
				ipostid, cmemberemail);

		return evalute == null ? false : true;
	}
}
