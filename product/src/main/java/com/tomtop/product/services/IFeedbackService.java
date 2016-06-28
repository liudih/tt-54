package com.tomtop.product.services;

import com.tomtop.product.models.bo.BaseBo;
import com.tomtop.product.models.vo.ReportErrorVo;
import com.tomtop.product.models.vo.WholesaleInquiryVo;

public interface IFeedbackService {

	public BaseBo addWholesaleInquiry(WholesaleInquiryVo wivo);
	
	public BaseBo addReportError(ReportErrorVo revo);
}
