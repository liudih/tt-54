package dao.loyalty.theme.impl;

import java.util.List;

import mapper.loyalty.ThemeSkuRelationMapper;

import com.google.inject.Inject;

import dao.loyalty.theme.IThemeSkuRelationDao;
import entity.loyalty.ThemeSkuRelation;
import forms.loyalty.theme.template.ThemeSkuRelationForm;

/**
 * 主题和sku的关联dao实现
 * 
 * @author liulj
 *
 */
public class ThemeSkuRelationDaoImpl implements IThemeSkuRelationDao {

	@Inject
	private ThemeSkuRelationMapper mapper;

	@Override
	public ThemeSkuRelation getById(int id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int insert(ThemeSkuRelation themeSku) {
		// TODO Auto-generated method stub
		return mapper.insert(themeSku);
	}

	@Override
	public int update(ThemeSkuRelation themeSku) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(themeSku);
	}

	@Override
	public int deleteByID(int id) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int getCount(String sku) {
		// TODO Auto-generated method stub
		return mapper.getCount(sku);
	}

	@Override
	public List<ThemeSkuRelationForm> getPage(int page, int pagesize, String sku) {
		// TODO Auto-generated method stub
		return mapper.getPage(page, pagesize, sku);
	}

	public ThemeSkuRelationMapper getMapper() {
		return mapper;
	}

	public void setMapper(ThemeSkuRelationMapper mapper) {
		this.mapper = mapper;
	}
}
