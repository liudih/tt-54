package services.loyalty.bulk;

import interceptors.CacheResult;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import mapper.loyalty.BulkMapper;
import mapper.loyalty.BulkRateMapper;
import valueobjects.base.Page;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import entity.loyalty.Bulk;
import entity.loyalty.BulkRate;
import forms.loyalty.BulkForm;
import forms.loyalty.BulkRateForm;

public class BulkRateService {

	@Inject
	BulkRateMapper mapper;
	@Inject
	BulkMapper bulkMapper;

	@CacheResult
	public List<BulkRate> getBulkRates(int groupId) {
		return mapper.getBulkRates(groupId);
	}

	/**
	 * Return Bulk Discount for the given qty's
	 * 
	 * @param qty
	 * @param transformer
	 * @param iid
	 * 
	 * @return
	 */
	public <T> Map<Integer, T> getBulkRates(int groupId, Set<Integer> qty,
			Function<BulkRate, T> transformer) {
		List<BulkRate> rates = mapper.getBulkRates(groupId);
		RangeMap<Integer, BulkRate> rateMap = TreeRangeMap.create();
		BulkRate lastRate = null;
		for (BulkRate br : rates) {
			if (lastRate == null) {
				lastRate = br;
				continue;
			}
			rateMap.put(Range.closedOpen(lastRate.getIqty(), br.getIqty()),
					lastRate);
			lastRate = br;
		}
		if (lastRate != null) {
			rateMap.put(Range.atLeast(lastRate.getIqty()), lastRate);
		}
		return Maps.asMap(qty, (Integer q) -> {
			BulkRate r = rateMap.get(q);
			return transformer.apply(r);
		});
	}

	public boolean addBulk(BulkForm form) {
		Bulk bulk = new Bulk();
		bulk.setCremark(form.getCremark());
		bulk.setDcreatedate(new Date());
		bulk.setDenddate(form.getDenddate());
		bulk.setIgroupid(form.getIgroupid());
		bulk.setIstatus(form.getIstatus());
		bulk.setIwebsiteid(form.getIwebsiteid());
		bulkMapper.insertSelective(bulk);
		return bulk.getIid()!=null;
	}
	public boolean addBulkRate(BulkRateForm form) {
		BulkRate bulkRate = new BulkRate();
		bulkRate.setDcreatedate(new Date());
		bulkRate.setIbulkid(form.getIbulkid());
		bulkRate.setFdiscount(form.getFdiscount());
		bulkRate.setFgrossprofit(form.getFgrossprofit());
		bulkRate.setIqty(form.getIqty());
		mapper.insertSelective(bulkRate);
		return bulkRate.getIid()!=null;
	}
	
	public Page<Bulk> getBulksPage(Integer page,Integer pageSize){
		int pageIndex = (page-1)*pageSize;
		List<Bulk> list = bulkMapper.getBulksPage(pageIndex, 
				pageSize);  
		int count = bulkMapper.getBulksCount();
		return new Page<Bulk>(list,count,page,pageSize);
	}
	
	public Bulk getBulkById(Integer id){
		return bulkMapper.selectByPrimaryKey(id);
	}
	public boolean editBulk(BulkForm b){
		Bulk bulk =  bulkMapper.selectByPrimaryKey(b.getIid());
		bulk.setCremark(b.getCremark());
		bulk.setDenddate(b.getDenddate());
		bulk.setIgroupid(b.getIgroupid());
		bulk.setIstatus(b.getIstatus());
		bulk.setIwebsiteid(b.getIwebsiteid());
		int flag = bulkMapper.updateByPrimaryKey(bulk);
		return flag>0;
	}
	public boolean delBulk(Integer id){
		int flag0 = mapper.delByBulkId(id);
		int flag = bulkMapper.deleteByPrimaryKey(id);
		return flag>0;
	}
	public Page<BulkRate> getBulkRatesPage(Integer bulkid,Integer page,Integer pageSize){
		int pageIndex = (page-1)*pageSize;
		List<BulkRate> list = mapper.getBulkRatesPage(bulkid,pageIndex, 
				pageSize);  
		int count = mapper.getBulkRatesCount(bulkid);
		return new Page<BulkRate>(list,count,page,pageSize);
	}
	public BulkRate getBulkRateById(Integer id){
		return mapper.selectByPrimaryKey(id);
	}
	public boolean editBulkRate(BulkRateForm b){
		BulkRate bulkrate =  mapper.selectByPrimaryKey(b.getIid());
		bulkrate.setFdiscount(b.getFdiscount());
		bulkrate.setFgrossprofit(b.getFgrossprofit());
		bulkrate.setIbulkid(b.getIbulkid());
		bulkrate.setIqty(b.getIqty());
		int flag = mapper.updateByPrimaryKey(bulkrate);
		return flag>0;
	}
	public boolean delBulkRate(Integer id){
		int flag = mapper.deleteByPrimaryKey(id);
		return flag>0;
	}
}
