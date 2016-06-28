package services.loyalty.theme.impl;

import java.util.List;

import services.loyalty.theme.IThemeSkuRelationService;
import valueobjects.base.Page;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeSkuRelationDao;
import entity.loyalty.ThemeSkuRelation;
import forms.loyalty.theme.template.ThemeSkuRelationForm;

public class ThemeSkuRelationServiceImpl implements IThemeSkuRelationService {

	@Inject
	private IThemeSkuRelationDao dao;

	@Override
	public ThemeSkuRelation getById(int id) {
		// TODO Auto-generated method stub
		return dao.getById(id);
	}

	@Override
	public Page<ThemeSkuRelationForm> getPage(int page, int pageSize,
			ThemeSkuRelationForm queryForm) {
		// TODO Auto-generated method stub
		List<ThemeSkuRelationForm> themes = dao.getPage(page, pageSize,
				queryForm.getCsku());
		int total = dao.getCount(queryForm.getCsku());
		return new Page<ThemeSkuRelationForm>(themes, total, page, pageSize);
	}

	@Override
	public int insert(ThemeSkuRelation form) {
		// TODO Auto-generated method stub
		return dao.insert(form);
	}

	@Override
	public int update(ThemeSkuRelation form) {
		// TODO Auto-generated method stub
		return dao.update(form);
	}

	@Override
	public int deleteByID(int id) {
		// TODO Auto-generated method stub
		return dao.deleteByID(id);
	}

	public IThemeSkuRelationDao getDao() {
		return dao;
	}

	public void setDao(IThemeSkuRelationDao dao) {
		this.dao = dao;
	}
}
