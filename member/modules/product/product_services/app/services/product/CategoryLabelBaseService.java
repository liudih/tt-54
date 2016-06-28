package services.product;

import java.util.List;

import javax.inject.Inject;

import mapper.product.CategoryLabelMapper;
import mapper.product.CategoryLabelNameMapper;

import org.mybatis.guice.transactional.Transactional;

import valueobjects.product.CategoryLabelBase;
import dto.product.CategoryLabelName;
import dto.product.CategoryLabel;

public class CategoryLabelBaseService {
	@Inject
	CategoryLabelMapper categoryLabelMapper;

	@Inject
	CategoryLabelNameMapper categoryLabelNameMapper;

	public List<Integer> getCategoryIdByWebsiteIdAndType(Integer siteId,
			String type) {

		return categoryLabelMapper.getCategoryIdByWebsiteIdAndType(siteId,
				type);
	}

	public List<CategoryLabel> getCategoryLabels(Integer siteId, String type) {
		return categoryLabelMapper.getCategoryLabelByWebsiteIdAndType(siteId,
				type);
	}

	public boolean batchInsertCategoryLabel(List<CategoryLabel> categoryLabels) {
		return categoryLabelMapper.batchInsert(categoryLabels) > 0;
	}

	public CategoryLabelName getCategoryLabelNameByLabelIdAndLanguageId(
			Integer labelId, Integer languageId) {
		return categoryLabelNameMapper
				.getCategoryLabelNameByLabelIdAndLanguageId(labelId, languageId);
	}

	public boolean saveOrUpdateCategoryLabelName(
			CategoryLabelName categoryLabelName) {
		if (categoryLabelName.getIid() != null) {
			return categoryLabelNameMapper
					.updateByPrimaryKeySelective(categoryLabelName) > 0;
		}
		return categoryLabelNameMapper.insert(categoryLabelName) > 0;
	}

	public CategoryLabelName getCategoryLabelName(Integer iid) {
		return categoryLabelNameMapper.selectByPrimaryKey(iid);
	}

	public List<CategoryLabelBase> getCategoryLabelBases(Integer siteId,
			Integer languageid, String type, Integer pageSize,
			Integer pageNum) {
		return categoryLabelNameMapper.getCategoryLabelBases(siteId,
				languageid, type, pageSize, pageNum);
	}
	
	public List<CategoryLabelBase> getCategoryLabelBasesByCategoryIds(Integer websiteId,
			Integer languageid, String type,List<Integer> categoryIds) {
		return categoryLabelNameMapper.getCategoryLabelBasesByCategoryIds(websiteId,
				languageid, type, categoryIds);
	}

	public Integer getCategoryBaseCount(Integer siteId, String type) {
		return categoryLabelNameMapper.getCategoryBaseCount(siteId, type);
	}

	@Transactional
	public boolean deleteCategoryLabel(Integer labelId) {
		Integer count = categoryLabelNameMapper
				.selectCategoryLabelNameCount(labelId);
		boolean deleteLabelName = false;
		if (count > 0) {
			deleteLabelName = (categoryLabelNameMapper
					.deleteCategoryLabelNameByCategorylabelid(labelId) > 0) ? true
					: false;
		} else {
			deleteLabelName = true;
		}

		if (deleteLabelName) {
			return categoryLabelMapper.deleteByPrimaryKey(labelId) > 0;
		}

		return false;
	}
}
