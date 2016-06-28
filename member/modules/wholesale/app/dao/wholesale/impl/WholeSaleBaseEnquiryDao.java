package dao.wholesale.impl;

import java.util.List;

import mapper.wholesale.WholeSaleBaseMapper;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleBaseEnquiryDao;
import entity.wholesale.WholeSaleBase;
import form.wholesale.WholeSaleSearchForm;

public class WholeSaleBaseEnquiryDao implements IWholeSaleBaseEnquiryDao {
	@Inject
	WholeSaleBaseMapper wholeSaleBaseMapper;

	@Override
	public WholeSaleBase getWholeSaleBaseByEmail(String email,
			Integer iwebsiteId) {
		return wholeSaleBaseMapper.getWholeSaleBaseByEmail(email, iwebsiteId);
	}

	@Override
	public List<WholeSaleBase> getWholeSaleBasesBySearchForm(
			WholeSaleSearchForm form) {
		return wholeSaleBaseMapper.getWholeSaleBasesBySearch(form.getStatus(),
				form.getPageSize(), form.getPageNum(), form.getEmail(),
				form.getWebsiteId());
	}

	@Override
	public Integer getWholeSaleBaseCount(WholeSaleSearchForm form) {
		return wholeSaleBaseMapper.getWholeSaleBasesCount(form.getStatus(),
				form.getEmail(), form.getWebsiteId());
	}

	@Override
	public WholeSaleBase getWholeSaleBaseByIid(Integer iid) {
		return wholeSaleBaseMapper.getWholeSaleBaseByIid(iid);
	}
}
