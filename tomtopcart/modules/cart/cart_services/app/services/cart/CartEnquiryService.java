package services.cart;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import mapper.cart.CartHistoryMapper;
import play.Logger;
import play.mvc.Http.Context;
import services.base.FoundationService;
import services.price.IPriceService;
import services.product.IEntityMapService;
import services.product.IProductEnquiryService;
import services.product.inventory.IInventoryEnquiryService;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartHistoryType;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;
import valueobjects.price.BundlePrice;
import valueobjects.price.Price;
import valueobjects.product.ProductLite;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.ProductSpecBuilder;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import dto.cart.CartHistory;

public class CartEnquiryService implements ICartEnquiryService {
	
	@Inject
	IPriceService priceService;
	
	@Inject
	IProductEnquiryService productEnquiryService;
	
	@Inject
	IEntityMapService entityMapService;
	
	@Inject
	CartHistoryMapper cartHistoryMapper;
	
	@Inject
	FoundationService foundationService;
	
	@Inject
	IInventoryEnquiryService inventoryEnquiryService;
	
	
	@Override
	public List<CartItem> getCartItems(List<CartItem> items, int siteID,
			int languageID, String ccy) {
		List<String> listingid  = Lists.newArrayList();
		Boolean hasWrong = false;
		//取得listingid的list
		List<List<String>> listingids = Lists.transform(items, it -> {
			if (it instanceof SingleCartItem) {
				return Lists.newArrayList(it.getClistingid());
			} else if (it instanceof BundleCartItem) {
				return Lists.transform(((BundleCartItem) it).getChildList(),
						c -> c.getClistingid());
			}
			throw new RuntimeException("Not Single/Bundle Cart Item Found!");
		});
		Set<String> ids = Sets.newHashSet();
		for (Collection<String> i : listingids) {
			ids.addAll(i);
		}
		List<String> distinctIds = Lists.newLinkedList(ids);
		if(distinctIds==null || distinctIds.size()==0){
			return Lists.newArrayList();
		}
		Logger.debug("distinctIds==={}",distinctIds);
		//取得产品信息
		List<ProductLite> products = productEnquiryService
				.getProductLiteByListingIDs(distinctIds, siteID, languageID);
		products = ImmutableSet.copyOf(products).asList();
		Map<String, ProductLite> productMap = Maps.uniqueIndex(products,
				p -> p.getListingId());
		
		for(CartItem ci : items){
			if(ci instanceof SingleCartItem){
				IProductSpec spec = ProductSpecBuilder.build(ci.getClistingid())
						.setQty(ci.getIqty()).get();
				ci.setPrice(copyPrice(priceService.getPrice(spec, ccy)));
				ProductLite p = productMap.get(ci.getClistingid());
				if (p != null) {
					ci.setCtitle(p.getTitle());
					ci.setCurl(p.getUrl());
					ci.setCimageurl(p.getImageUrl());
					ci.setSku(p.getSku());
					ci.setIstatus(p.getIstatus());
					Map<String, String> attributeMap = entityMapService
							.getAttributeMap(ci.getClistingid(), languageID);
					ci.setAttributeMap(attributeMap);
				}
			}else if(ci instanceof BundleCartItem){
				SingleCartItem main = ((BundleCartItem) ci).getChildList().get(0);
				Iterable<String> childIDs = Lists.transform(((BundleCartItem) ci).getChildList(),
						li -> li.getClistingid());
				IProductSpec spec = ProductSpecBuilder
						.build(main.getClistingid()).bundleWith(childIDs)
						.setQty(main.getIqty()).get();
				//从产品的price赋值到cart的price
				Price bprice = priceService.getPrice(spec, ccy);
				ci.setPrice(copyPrice(bprice));
				BundlePrice p = (BundlePrice) bprice;
				Map<String, Price> mapprice1 = null;
				if (p == null) {
					hasWrong = true;
				} else {
					mapprice1 = Maps.uniqueIndex(p.getBreakdown(), objprice -> {
						return objprice.getListingId();
					});
				}
				for (SingleCartItem sci : ((BundleCartItem) ci).getChildList()) {
					ProductLite cp = productMap.get(sci .getClistingid());
					if(cp!=null){
						sci.setCtitle(cp.getTitle());
						sci.setCurl(cp.getUrl());
						sci.setCimageurl(cp.getImageUrl());
						sci.setSku(cp.getSku());
						sci.setIstatus(cp.getIstatus());
						Map<String, String> attributeMap2 = entityMapService.getAttributeMap(
								sci.getClistingid(),
								languageID);
						sci.setAttributeMap(attributeMap2);
					}
					if (null != mapprice1){
						sci.setPrice(copyPrice(mapprice1.get(sci.getClistingid())));
					}
				}
				
			}
		}
		return items;
	}
	
	public static valueobjects.cart.Price copyPrice(Price oprice){
		if(oprice==null){
			return null;
		}
		valueobjects.cart.Price nprice = new valueobjects.cart.Price();
		nprice.setCurrency(oprice.getCurrency());
		nprice.setDiscount(oprice.getDiscount());
		nprice.setDiscounted(oprice.isDiscounted());
		nprice.setSymbol(oprice.getSymbol());
		nprice.setUnitBasePrice(oprice.getUnitBasePrice());
		nprice.setUnitPrice(oprice.getUnitPrice());
		nprice.setValidFrom(oprice.getValidFrom());
		nprice.setValidTo(oprice.getValidTo());
		nprice.setPrice(oprice.getPrice());
		return nprice;
	}
	
	public static Price copyToPrice(valueobjects.cart.Price oprice,IProductSpec spec){
		if(oprice==null){
			return null;
		}
		Price nprice = new Price(spec);
		nprice.setCurrency(oprice.getCurrency());
		nprice.setDiscount(oprice.getDiscount());
		nprice.setSymbol(oprice.getSymbol());
		nprice.setUnitBasePrice(oprice.getUnitBasePrice());
		nprice.setUnitPrice(oprice.getUnitPrice());
		nprice.setValidFrom(oprice.getValidFrom());
		nprice.setValidTo(oprice.getValidTo());
		return nprice;
	}
	
	public void addCartHistory(CartItem ci, Integer type){
		CartHistory ch = new CartHistory();
		String email = null;
		if(foundationService.getLoginContext().isLogin()){
			email = foundationService.getLoginContext().getMemberID();
		}
		ch.setCip(Context.current().request().remoteAddress());
		ch.setDcreatedate(new Date());
		ch.setItype(1);
		ch.setCmemberemail(email);
		ch.setItype(type);
		if(ci instanceof SingleCartItem){
			ch.setClistingid(ci.getClistingid());
			cartHistoryMapper.insertSelective(ch);
		}else if(ci instanceof BundleCartItem){
			List<SingleCartItem> slist = ((BundleCartItem) ci).getChildList();
			for(SingleCartItem s : slist){
				ch.setClistingid(s.getClistingid());
				cartHistoryMapper.insertSelective(ch);
			}
		}
	}
	
	/**
	 * 添加删除记录
	 */
	public void addCartDelHistory(List<String> listingids){
		CartHistory ch = new CartHistory();
		String email = null;
		if(foundationService.getLoginContext().isLogin()){
			email = foundationService.getLoginContext().getMemberID();
		}
		ch.setCip(Context.current().request().remoteAddress());
		ch.setDcreatedate(new Date());
		ch.setItype(1);
		ch.setCmemberemail(email);
		ch.setItype(CartHistoryType.delete.value());
		for(String lis : listingids){
			String[] arr = lis.split("\\+");
			for(String ar : arr){
				if(ar!=null && !"".equals(ar)){
					ch.setClistingid(ar);
					cartHistoryMapper.insertSelective(ch);
				}
			}
		}
	}
	
	/**
	 * 判断是否够库存
	 * @param itemID
	 * @param qty
	 * @param listingids
	 * @return
	 */
	@Override
	public boolean isEnoughQty(CartItem cartItem) {
		if (cartItem instanceof SingleCartItem) {
			if (inventoryEnquiryService.checkInventory(cartItem.getClistingid(), cartItem.getIqty()) && cartItem.getIqty()<=999) {
				return true;
			}
		} else if (cartItem instanceof BundleCartItem) {
			List<SingleCartItem> blist = ((BundleCartItem) cartItem).getChildList();
			for(int i=0;i<blist.size();i++){
				if (!inventoryEnquiryService.checkInventory(blist.get(i).getClistingid(), blist.get(i).getIqty()) && blist.get(i).getIqty()>999) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	

	public boolean checkStatus(CartItem cartItem, int siteID, int languageID){
		List<String> listingids = Lists.newArrayList();
		if (cartItem instanceof SingleCartItem) {
			listingids.add(cartItem.getClistingid());
		} else if (cartItem instanceof BundleCartItem) {
			List<SingleCartItem> blist = ((BundleCartItem) cartItem).getChildList();
			for(int i=0;i<blist.size();i++){
				listingids.add(blist.get(i).getClistingid());
			}
		}
		List<ProductLite> products = productEnquiryService
				.getProductLiteByListingIDs(listingids, siteID, languageID);
		boolean status = true;
		for(ProductLite p : products){
			if(p.getIstatus()!=null && p.getIstatus()!=1){
				status = false;
				break;
			}
		}
		return status;
	}
	
	public boolean checkBundle(CartItem cartItem){
		if(cartItem instanceof BundleCartItem){
			List<SingleCartItem> blist = ((BundleCartItem) cartItem).getChildList();
			SingleCartItem first = blist.get(0);
			for(int i=1;i<blist.size();i++){
				int count = productEnquiryService.getCountBundleProduct(first.getClistingid(), blist.get(i).getClistingid());
				if(count<=0){
					return false;
				}
			}
			return true;
		}else{
			return true;
		}
	}
}
