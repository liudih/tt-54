package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.LabelDto;

public interface IProductLabelDao {

	/**
	 * 查询所有标签
	 * 
	 * @return
	 */
	public List<LabelDto> getLabels();

	/**
	 * 查询标签码下的标签
	 * 
	 * @return
	 */
	public List<LabelDto> getLabels(String labelCode);

}