package services.activity.page.impl;

import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import play.Logger;
import play.Logger.ALogger;
import services.activity.page.IPageItemNameService;
import valueobject.activity.page.PageItemName;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import dao.activitydb.page.IPageItemNameDao;

public class PageItemNameServiceImpl implements IPageItemNameService{

	ALogger logger = Logger.of(this.getClass());
	
	@Inject
	IPageItemNameDao dao;

	@Override
	public PageItemName getById(int id){
		// TODO Auto-generated method stub
		entity.activity.page.PageItemName itemName = dao.getById(id);
		PageItemName dest = new PageItemName();
		try{
			BeanUtils.copyProperties(dest, itemName);
		}
		catch(Exception e){
			logger.error("页面项转化对象失败"+e.getMessage());
		}
		return dest;
	}

	@Override
	public List<PageItemName> getListByPageItemid(Integer pageid) {
		// TODO Auto-generated method stub
		List<entity.activity.page.PageItemName> items = dao.getListByPageItemid(pageid);
		List<PageItemName> dests = Lists.transform(items, new Function<entity.activity.page.PageItemName,PageItemName>(){

			@Override
			public PageItemName apply(entity.activity.page.PageItemName arg0) {
				// TODO Auto-generated method stub
				PageItemName dest = new PageItemName();
				try{
					BeanUtils.copyProperties(dest, arg0);
				}
				catch(Exception e){
					logger.error("页面项转化对象失败"+e.getMessage());
				}
				return dest;
			}			
		});
		return dests;
	}

	@Override
	public int insert(PageItemName pageItemName) {
		// TODO Auto-generated method stub
		entity.activity.page.PageItemName itemName = new entity.activity.page.PageItemName();
		try{
			BeanUtils.copyProperties(itemName, pageItemName);
		}
		catch(Exception e){
			logger.error("页面项转化对象失败"+e.getMessage());
			return 0;
		}
		return dao.insert(itemName);
	}

	@Override
	public int update(PageItemName pageItemName) {
		// TODO Auto-generated method stub
		entity.activity.page.PageItemName itemName = new entity.activity.page.PageItemName();
		try{
			BeanUtils.copyProperties(itemName, pageItemName);
		}
		catch(Exception e){
			logger.error("页面项转化对象失败"+e.getMessage());
			return 0;
		}
		return dao.update(itemName);
	}

	@Override
	public int deleteByID(int id) {
		// TODO Auto-generated method stub
		return dao.deleteByID(id);
	}
}