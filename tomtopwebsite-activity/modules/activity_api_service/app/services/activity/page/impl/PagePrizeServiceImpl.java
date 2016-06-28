package services.activity.page.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.Logger.ALogger;
import services.activity.page.IPagePrizeService;
import valueobject.activity.page.PagePrize;
import valueobjects.base.Page;
import values.activity.page.PagePrizeQuery;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dao.activitydb.page.IPagePrizeDao;
import forms.activity.page.PagePrizeForm;

public class PagePrizeServiceImpl implements IPagePrizeService {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	private IPagePrizeDao dao;

	@Override
	public PagePrize getById(Integer id) {
		// TODO Auto-generated method stub
		entity.activity.page.PagePrize prize = dao.getById(id);
		PagePrize dest = new valueobject.activity.page.PagePrize();
		try {
			BeanUtils.copyProperties(dest, prize);
		} catch (Exception e) {
			logger.error("化对象失败" + e.getMessage());
		}
		return dest;
	}

	@Override
	public int add(PagePrize prize) {
		// TODO Auto-generated method stub
		entity.activity.page.PagePrize dest = new entity.activity.page.PagePrize();
		try {
			BeanUtils.copyProperties(dest, prize);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error("化对象失败" + e.getMessage());
			return 0;
		}
		return dao.add(dest);
	}

	@Override
	public int deleteById(int iid) {
		// TODO Auto-generated method stub
		return dao.deleteByid(iid);
	}

	@Override
	public int updateById(PagePrize prize) {
		// TODO Auto-generated method stub
		entity.activity.page.PagePrize dest = new entity.activity.page.PagePrize();
		try {
			BeanUtils.copyProperties(dest, prize);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			logger.error("化对象失败" + e.getMessage());
			return 0;
		}
		return dao.updateById(dest);
	}

	@Override
	public Page<PagePrizeForm> getPage(int page, int pagesize,
			PagePrizeForm quForm) {
		// TODO Auto-generated method stub
		List<PagePrizeQuery> pages = dao.getPage(page, pagesize,
				quForm.getCurl());
		List<PagePrizeForm> despages = Lists.transform(pages,
				new Function<PagePrizeQuery, PagePrizeForm>() {
					@Override
					public PagePrizeForm apply(PagePrizeQuery arg0) {
						// TODO Auto-generated method stub
						PagePrizeForm dest = new PagePrizeForm();
						try {
							BeanUtils.copyProperties(dest, arg0);
						} catch (Exception e) {
							logger.error("化对象失败" + e.getMessage());
						}
						return dest;
					}
				});
		int total = dao.getCount(quForm.getCurl());
		return new valueobjects.base.Page<PagePrizeForm>(despages, total, page,
				pagesize);
	}

	public List<PagePrize> getPagePrizes() {
		List<entity.activity.page.PagePrize> pagePrizes = dao.getPagePrizes();

		List<PagePrize> pageprizeList = Lists.transform(pagePrizes,
				new Function<entity.activity.page.PagePrize, PagePrize>() {

					@Override
					public PagePrize apply(
							entity.activity.page.PagePrize pagePrize) {
						PagePrize prize = new PagePrize();
						try {
							BeanUtils.copyProperties(prize, pagePrize);
						} catch (Exception e) {
							logger.error("转化对象失败" + e.getMessage());
						}
						return prize;
					}

				});
		return pageprizeList;
	}
}
