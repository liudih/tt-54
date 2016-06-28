package controllers.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import dto.interaction.WholesaleInquiry;
import forms.interaction.WholesaleInquiryBaseForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.utils.ExcelUtils;
import services.base.utils.StringUtils;
import services.interaction.WholesaleService;

/**
 * 客户批发记录管理类
 * 
 * @author Administrator
 *
 */
public class WholeSaleInquiry extends Controller {

	@Inject
	private WholesaleService wholesaleService;

	/**
	 * 初始化查询WholeSaleInquiry表中的全部数据信息
	 * @return
	 */
	public Result getList(int p) {
		WholesaleInquiryBaseForm wholesaleInquiries = new WholesaleInquiryBaseForm();
		wholesaleInquiries.setPageNum(p);
		return ok(getWholesaleInquiry(wholesaleInquiries));

	};

	/**
	 * 查询客户批发记录信息
	 * 
	 * @return
	 */
	public Result search() {
		// 创建一个新的WholesaleInquiry实例，用来接受HTTP数据
		Form<WholesaleInquiryBaseForm> form = Form.form(WholesaleInquiryBaseForm.class)
				.bindFromRequest();
		WholesaleInquiryBaseForm wholesaleInquiryForm = form.get();		
		// 定义时间格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (wholesaleInquiryForm.getEndDate()!=null && wholesaleInquiryForm.getStartDate()!=null) {
			String startStr=sdf.format(wholesaleInquiryForm.getStartDate());
			String endStr=sdf.format(wholesaleInquiryForm.getEndDate());
			try {	
				Date startDate=sdf.parse(startStr);
				Date endDate=sdf.parse(endStr);
				if(endDate.getTime()<startDate.getTime()){
					return badRequest("時間格式不正確");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ok(getWholesaleInquiry(wholesaleInquiryForm));
	}

	/**
	 * 获取WholeSaleInquiry表中的全部数据信息
	 * 
	 * @param wholesaleInquiryForm
	 * @return
	 */
	public  Html getWholesaleInquiry(WholesaleInquiryBaseForm wholesaleInquiryForm) {
	
		// 定义时间格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取WholesaleInquiry的所有数据信息
		List<WholesaleInquiry> wholesaleInquiries = wholesaleService
				.getWholesaleInquiries(wholesaleInquiryForm);
	
		//获取WholesaleInquiry所有数据的总量
		Integer count = wholesaleService.getCount(wholesaleInquiryForm);
		//获取页面的总数
		Integer pageTotal = count / wholesaleInquiryForm.getPageSize()
				+ ((count % wholesaleInquiryForm.getPageSize() > 0) ? 1 : 0);
		Map<Integer, String> dateMap = new HashMap<Integer, String>();
		
		for (WholesaleInquiry wholesaleInquiry : wholesaleInquiries) {
			String dateStr = sdf.format(wholesaleInquiry.getDcreatedate());
			dateMap.put(wholesaleInquiry.getIid(), dateStr);
		}
		return views.html.manager.member.wholesale.wholesale_inquiry_list
				.render(wholesaleInquiryForm,dateMap, wholesaleInquiries,count,pageTotal,wholesaleInquiryForm.getPageNum());
	};

	/**
	 * 导出WholeSaleInquirys的所有数据信息
	 * 
	 * @return
	 */
	public Result downloadWholeSaleInquirys(String startDate,String endDate) {
		WholesaleInquiry inquiry=new WholesaleInquiry();
		WholesaleInquiryBaseForm form=new WholesaleInquiryBaseForm();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (StringUtils.notEmpty(endDate) && StringUtils.notEmpty(endDate)) {
				form.setStartDate(sdf.parse(startDate));
				form.setEndDate(sdf.parse(endDate));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		BeanUtils.copyProperties(inquiry, form);
		// 获取WholesaleInquiry的所有数据信息
		List<WholesaleInquiry> wholesaleInquiries = wholesaleService
				.getWholesaleInquiries(form);

		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> title = new ArrayList<Object>();

		title.add("Advertising code");
		title.add("SKU");
		title.add("Full name");
		title.add("Contact phone");
		title.add("E_mail");
		title.add("Target price");
		title.add("Quantity");
		title.add("Country/State");
		title.add("Company");
		title.add("Inquiry");
		title.add("Create time");
		data.add(title);
		for (WholesaleInquiry wholesaleInquiry : wholesaleInquiries) {
			String dateStr = sdf.format(wholesaleInquiry
					.getDcreatedate());

			ArrayList<Object> row = new ArrayList<Object>();
			row.add(wholesaleInquiry.getClistingid());
			row.add(wholesaleInquiry.getCsku());
			row.add(wholesaleInquiry.getCname());
			row.add(wholesaleInquiry.getCphone());
			row.add(wholesaleInquiry.getCemail());
			row.add(wholesaleInquiry.getFtargetprice());
			row.add(wholesaleInquiry.getIquantity());
			row.add(wholesaleInquiry.getCcountrystate());
			row.add(wholesaleInquiry.getCcompany());
			row.add(wholesaleInquiry.getCinquiry());
			row.add(dateStr);
			data.add(row);
		}
		String filename = "WholeSale_Inquiry-list-" + sdf.format(new Date())
				+ ".xlsx";
		ExcelUtils excel = new ExcelUtils();
		byte[] tmpFile = excel.arrayToXLSX(data);
		response().setHeader("Content-disposition",
				"attachment; filename=" + filename);
		return ok(tmpFile).as("application/vnd.ms-excel");
	};

}