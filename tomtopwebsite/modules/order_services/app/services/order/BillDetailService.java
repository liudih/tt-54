package services.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.order.BillDetailMapper;
import services.base.utils.DoubleCalculateUtils;

import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.order.BillDetail;

public class BillDetailService implements IBillDetailService {
	@Inject
	BillDetailMapper billDetailMapper;

	@Override
	public boolean insert(BillDetail bill) {
		if (null != bill) {
			int i = billDetailMapper.insert(bill);
			if (1 == i) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean batchInsert(List<BillDetail> list) {
		if (list != null && !list.isEmpty()) {
			int i = billDetailMapper.batchInsert(list);
			if (i == list.size()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Double saveTotal(Integer orderId) {
		List<BillDetail> temp = getBillByOrderAndType(orderId, TYPE_PRODUCT);
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(0);
		for (BillDetail billDetail : temp) {
			dcu = dcu.add((billDetail.getForiginalprice() - billDetail
					.getFpresentprice()) * billDetail.getIqty());
		}
		return dcu.doubleValue();
	}

	@Override
	public List<BillDetail> getExtraBill(Integer orderId) {
		List<BillDetail> list = billDetailMapper.getByOrderId(orderId);
		List<BillDetail> temp = new ArrayList<BillDetail>();
		for (BillDetail billDetail : list) {
			if (!(billDetail.getCtype().equals(TYPE_PRODUCT) || billDetail
					.getCtype().equals(TYPE_SHIPPING_METHOD))) {
				temp.add(billDetail);
			}
		}
		return temp;
	}

	@Override
	public Map<String, BillDetail> getMapExceptProduct(Integer orderId) {
		List<BillDetail> list = billDetailMapper.getByOrderId(orderId);
		Collection<BillDetail> collection = Collections2.filter(list, e -> !e
				.getCtype().equals(TYPE_PRODUCT));
		Map<String, BillDetail> map = Maps.uniqueIndex(collection,
				e -> e.getCtype());
		return map;
	}

	@Override
	public List<BillDetail> getBillByOrderAndType(Integer orderId, String type) {
		List<BillDetail> list = billDetailMapper.getByOrderId(orderId);
		return Lists.newArrayList(Iterables.filter(list, e -> e.getCtype()
				.equals(type)));
	}
	
}
