package services.interaction.collect;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.interaction.ProductCollectMapper;
import mapper.product.ProductBaseMapper;
import services.interaction.ICollectService;
import valueobjects.base.Page;
import valueobjects.interaction.CollectCount;

import com.google.common.collect.Lists;

import dto.interaction.ProductCollect;

public class CollectService implements ICollectService{
	
	@Inject
	ProductCollectMapper productCollectMapper;
	
	@Inject
	ProductBaseMapper productBaseMapper;
	
	
	/* (non-Javadoc)
	 * @see services.interaction.collect.ICollectService#getCollectByMember(java.lang.String, java.lang.String)
	 */
	@Override
	public List<ProductCollect> getCollectByMember(String lid,String email){
		List<ProductCollect> cList = productCollectMapper.getCollectByMember(lid, email);
		return cList;
	}
	/* (non-Javadoc)
	 * @see services.interaction.collect.ICollectService#addCollect(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addCollect(String lid,String email){
		ProductCollect p = new ProductCollect();
		p.setClistingid(lid);
		p.setCemail(email);
		p.setDcreatedate(new Date());
		int flag = productCollectMapper.insertSelective(p);
		return flag>0;
	}
	
	/* (non-Javadoc)
	 * @see services.interaction.collect.ICollectService#delCollect(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean delCollect(String lid,String email){
		int flag = productCollectMapper.delCollect(lid, email);
		return flag>0;
	}
	
	/* (non-Javadoc)
	 * @see services.interaction.collect.ICollectService#delCollectByListingids(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean delCollectByListingids(String lids,String email){
		String[] arr = lids.split(",");
		int flag = productCollectMapper.delCollectByListingids(Arrays.asList(arr), email);
		return flag>0;
	}
	
	/* (non-Javadoc)
	 * @see services.interaction.collect.ICollectService#getCollectListingIDByEmail(java.lang.String)
	 */
	@Override
	public List<String> getCollectListingIDByEmail(String email){
		List<String> list = productCollectMapper.getCollectListingIDByEmail(email);
		return list;
	}
	
	/* (non-Javadoc)
	 * @see services.interaction.collect.ICollectService#getCountByListingID(java.lang.String)
	 */
	@Override
	public int getCountByListingID(String listingID){
		return productCollectMapper.getCountByListingID(listingID);
	}
	
	/* (non-Javadoc)
	 * @see services.interaction.collect.ICollectService#getCollectsPageByEmail(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@Override
	public Page<ProductCollect> getCollectsPageByEmail(String email,Integer page,
			Integer pageSize,Integer language,String sort,String searchname,Integer categoryId){
		List<ProductCollect> pclist = productCollectMapper.getCollectsPageByEmail(email,null,null,null,null,null);
		if(pclist.size()==0){
			return new Page<ProductCollect>(Lists.newArrayList(),0,page,pageSize);
		}
		List<String> pcListingIds = Lists.transform(pclist, p->p.getClistingid());
		List<String> listingids = productBaseMapper.selectListingidBySearchNameAndSort(language, searchname, sort, categoryId,
				pcListingIds);
		int pageIndex = (page-1)*pageSize;
		List<ProductCollect> list = productCollectMapper.getCollectsPageByEmail(email, pageIndex, 
				pageSize,sort,listingids,String.join(",", listingids));  
		int count = productCollectMapper.getCollectsCountByEmail(email);
		return new Page<ProductCollect>(list,count,page,pageSize);
	}
	
	/* (non-Javadoc)
	 * @see services.interaction.collect.ICollectService#getCollectCountByListingIds(java.util.List)
	 */
	@Override
	public List<CollectCount> getCollectCountByListingIds(List<String> listingIds){
		return productCollectMapper.getCollectCountByListingIds(listingIds);
	}
	
	/* (non-Javadoc)
	 * @see services.interaction.collect.ICollectService#getCollectByListingIds(java.util.List)
	 */
	@Override
	public List<ProductCollect> getCollectByListingIds(List<String> listingIds){
		return productCollectMapper.getCollectByListingIds(listingIds);
	}
}
