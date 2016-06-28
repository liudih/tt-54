package com.tomtop.product.models.vo;

import java.io.Serializable;
import java.util.List;

import com.tomtop.product.models.bo.BaseBo;
import com.tomtop.product.models.bo.CollectCountBo;
import com.tomtop.product.models.bo.PrdouctDescBo;
import com.tomtop.product.models.bo.ProductDetailsBo;
import com.tomtop.product.models.bo.ProductSeoBo;
import com.tomtop.product.models.bo.ReviewStartNumBo;

public class ProductBaseDtlVo extends BaseBo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ProductDetailsBo> pdbList;//详情基本信息
	private PrdouctDescBo pdb;//desc
	private ProductSeoBo psb;//seo
	private ReviewStartNumBo rsnbo;//评论星级
	private CollectCountBo ccb;//收藏数
	
	public List<ProductDetailsBo> getPdbList() {
		return pdbList;
	}
	public void setPdbList(List<ProductDetailsBo> pdbList) {
		this.pdbList = pdbList;
	}
	public PrdouctDescBo getPdb() {
		return pdb;
	}
	public void setPdb(PrdouctDescBo pdb) {
		this.pdb = pdb;
	}
	public ProductSeoBo getPsb() {
		return psb;
	}
	public void setPsb(ProductSeoBo psb) {
		this.psb = psb;
	}
	public ReviewStartNumBo getRsnbo() {
		return rsnbo;
	}
	public void setRsnbo(ReviewStartNumBo rsnbo) {
		this.rsnbo = rsnbo;
	}
	public CollectCountBo getCcb() {
		return ccb;
	}
	public void setCcb(CollectCountBo ccb) {
		this.ccb = ccb;
	}
}
