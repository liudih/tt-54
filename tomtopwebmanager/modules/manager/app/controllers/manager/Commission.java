package controllers.manager;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.manager.CommissionService;
import valueobjects.base.Page;
import valueobjects.tracking.CommissionOrderVo;

import com.google.inject.Inject;

import controllers.InterceptActon;
import entity.tracking.CommissionHistory;
import entity.tracking.CommissionOrder;

@With(InterceptActon.class)
public class Commission extends Controller {

	@Inject
	CommissionService commissionService;

	@Inject
	service.tracking.CommissionService commissionService2;

	public Result index(int page, int pageSize, String startdate,
			String enddate, String aid, String transactionid, Integer status) {
		Page<CommissionHistory> list = commissionService
				.getCommissionHistoryPage(page, pageSize, startdate, enddate,
						aid, transactionid, status);
		
		//合计
		double arr[] = commissionService.getTotal(startdate, enddate,
						aid, transactionid, status);

		return ok(views.html.manager.commission.list.render(list, startdate,
				enddate, aid, transactionid, status,arr[0], arr[1]));
	}

	/**
	 * 
	 * @param iid
	 * @param originalStatus
	 *            Pending(10)待处理 Processing(20)已通过 Success(30)交易成功 Fail(0)交易失败
	 * @param status
	 *            20,30,0
	 * @return
	 */
	public Result changeStatus(int iid, int originalStatus, int status) {
		// 10 可变更为20， 0
		if (originalStatus == 10 && (status == 0 || status == 20)) {
			if (commissionService.changeStatus(iid, originalStatus, status)) {
				return redirect(controllers.manager.routes.Commission.index(1,
						10, null, null, null, null, null));
			}
		}
		// 20可变更为30
		if (originalStatus == 20 && status == 30) {
			if (commissionService.changeStatus(iid, originalStatus, status)) {
				return redirect(controllers.manager.routes.Commission.index(1,
						10, null, null, null, null, null));
			}
		}
		flash().put("error", "error");
		return redirect(controllers.manager.routes.Commission.index(1, 10,
				null, null, null, null, null));
	}

	public Result getOrder(Integer cid, int page, int pageSize, int hstatus) {
		Page<CommissionOrder> list = commissionService2.getCommissionOrderPage(
				cid, page, pageSize, hstatus);
		Page<CommissionOrderVo> olist = list
				.batchMap(list1 -> commissionService2.transformOrderVo(
						list.getList(), null, null));
		return ok(views.html.manager.commission.order_list.render(olist, cid));
	}

	public Result editCommission(Integer id, Integer cid) {
		CommissionOrderVo info = commissionService.editCommission(id, cid);
		return ok(views.html.manager.commission.order_edit.render(info));
	}

	public Result doEdit() {

		Form<CommissionOrder> form = Form.form(CommissionOrder.class)
				.bindFromRequest();

		CommissionOrder info = form.get();

		int commissionId = info.getIcommissionid();

		if (commissionService.doEdit(info)) {
			return redirect(controllers.manager.routes.Commission.getOrder(
					commissionId, 1, 10, -1));
		}

		flash().put("error", "commission is too heigh");
		return redirect(controllers.manager.routes.Commission.getOrder(
				commissionId, 1, 10, -1));
	}

	public Result changeOrderStatus(Integer id, Integer commissionId,
			Integer status) {

		if (commissionService.changeOrderStatus(id, status)) {
			return redirect(controllers.manager.routes.Commission.getOrder(
					commissionId, 1, 10, -1));
		}
		flash().put("error", "error");
		return redirect(controllers.manager.routes.Commission.getOrder(
				commissionId, 1, 10, -1));
	}

	public Result editTransaction(int id) {
		dto.Commission info = commissionService.editTransaction(id);
		return ok(views.html.manager.commission.edit.render(info));
	}

	public Result doEditTransaction() {
		Form<CommissionHistory> form = Form.form(CommissionHistory.class)
				.bindFromRequest();

		CommissionHistory info = form.get();

		if (commissionService.doTransaction(info)) {
			return redirect(controllers.manager.routes.Commission.index(1, 10,
					null, null, null, null, null));
		}

		flash().put("error", "error");
		return redirect(controllers.manager.routes.Commission.index(1, 10,
				null, null, null, null, null));
	}
}
