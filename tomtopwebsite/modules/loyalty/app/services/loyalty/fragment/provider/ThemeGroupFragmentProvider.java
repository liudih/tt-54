package services.loyalty.fragment.provider;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.common.collect.FluentIterable;

import dto.product.ProductBase;
import play.Logger;
import mapper.loyalty.ThemeGroupMapper;
import mapper.loyalty.ThemeGroupNameMapper;
import mapper.loyalty.ThemeSkuRelationMapper;
import mapper.product.ProductBaseMapper;
import entity.loyalty.Theme;
import entity.loyalty.ThemeGroup;
import entity.loyalty.ThemeGroupName;
import entity.loyalty.ThemeSkuRelation;
import services.loyalty.theme.IThemeFragmentProvider;
import services.loyalty.theme.IThemeGroupService;
import services.product.IProductBadgeService;
import services.product.IProductEnquiryService;
import services.product.ProductBadgeService;
import valueobjects.base.Page;
import valueobjects.loyalty.IThemeFragment;
import valueobjects.loyalty.ThemeContext;
import valueobjects.loyalty.ThemeGroupFragment;
import valueobjects.product.IProductFragment;
import valueobjects.product.ProductBadge;

public class ThemeGroupFragmentProvider implements IThemeFragmentProvider {
	@Inject
	ThemeGroupMapper themeGroupMapper;
	
	@Inject
	ThemeGroupNameMapper themeGroupNameMapper;
	
	@Inject
	ThemeSkuRelationMapper themeSkuRelationMapper;
	
	@Inject
	IProductEnquiryService productEnquiryService;
	
	@Inject
	IProductBadgeService productBadgeService;
	

	
	public static final String NAME = "theme-group";

	public String getName() {
		return NAME;
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: getFragment</p>
	 * <p>Description: 获取专题片段</p>
	 * @return
	 * @see services.loyalty.theme.IThemeFragmentProvider#getFragment()
	 */
	public IThemeFragment getFragment(ThemeContext context,Theme theme){
		ThemeGroupFragment tgf = new ThemeGroupFragment();
		//设置专题组名称
		tgf.setThemeGroupName(NAME);
		//获取专题组列表
		List<ThemeGroup> tgList = themeGroupMapper.selectTGByThemeId(theme.getIid());
		//SKU列表
		List<String> skuList = null;
		//展示产品列表
		List<ProductBadge> pbList = Lists.newArrayList();
		//专题SKU列表
		List<ThemeSkuRelation> tsrList = null;
		//产品列表
		List<ProductBase> pBaseList = null;
		//广告编号id列表
		List<String> listingIds = null;
		//专题组名称
		ThemeGroupName groupName = null;
		//SKU与顺序的map
		HashMap<String,Integer> skuSortMap = Maps.newHashMap();
		for(ThemeGroup tg : tgList){
			tsrList = themeSkuRelationMapper.selectByGroupId(tg.getIid());
			skuList = FluentIterable.from(tsrList).transform(tsr -> tsr.getCsku()).toList();
			for(ThemeSkuRelation tsr : tsrList){
				skuSortMap.put(tsr.getCsku(), tsr.getIsort());
			}
			pBaseList = productEnquiryService.getProductBaseBySkus(skuList, context.getWebsiteId());
			//按照专题SKU的排序来排序
			pBaseList = FluentIterable.from(pBaseList).toSortedList((p1, p2) -> skuSortMap.get(p1.getCsku()).compareTo(skuSortMap.get(p2.getCsku())));
			listingIds = FluentIterable.from(pBaseList).transform(pb -> pb.getClistingid()).toList();
			pbList = productBadgeService.getNewProductBadgesByListingIds(listingIds, context.getLanguageId(), context.getWebsiteId(), context.getCurrency(), null);
			tg.setPbList(pbList);
			groupName = themeGroupNameMapper.selectByGroupIdAndLanguageId(tg.getIid(),context.getLanguageId());
			if(null != groupName){
				tg.setThemeGroupName(groupName.getCname());
			}else{
				tg.setThemeGroupName("");
			}
		}
		tgf.setTgList(tgList);
		return tgf;
	}

}
