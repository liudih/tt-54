package services.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.manager.AdminUserMapper;
import mappers.tracking.AffiliateInfoMapper;
import mappers.tracking.VisitLogMapper;
import play.Logger;
import play.mvc.Security.Authenticated;
import services.base.utils.DateFormatUtils;
import services.base.utils.StringUtils;
import valueobjects.base.Page;
import authenticators.member.MemberLoginAuthenticator;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import dto.api.VisitLogApiVo;
import entity.manager.AdminUser;
import entity.tracking.AffiliateInfo;
import entity.tracking.VisitLog;

@Authenticated(MemberLoginAuthenticator.class)
public class TrafficService {
	@Inject
	VisitLogMapper visitLogMapper;
	@Inject
	AffiliateInfoMapper affiliateInfoMapper;
	@Inject
	AdminUserMapper adminUserMapper;

	public Page<VisitLog> getVisitLogPage(int page, int pageSize,
			String startDate, String endDate, String aid, String source,
			String landing, String aidarr, Integer website) {
		Date sd = null, ed = null;
		if (StringUtils.notEmpty(startDate)) {
			sd = DateFormatUtils.getFormatDateYmdhmsByStr(startDate
					+ " 00:00:00");
		}
		if (StringUtils.notEmpty(endDate)) {
			ed = DateFormatUtils
					.getFormatDateYmdhmsByStr(endDate + " 23:59:59");
		}

		int pageIndex = (page - 1) * pageSize;

		// aid分割
		List<String> aidarr2 = Lists.newArrayList();
		if (StringUtils.notEmpty(aidarr)) {
			aidarr2.addAll(Lists.newArrayList(aidarr.split(",")));
		}
		if (StringUtils.notEmpty(aid)) {
			aidarr2.add(aid);
		}
		if(aidarr2.size()==0){
			Logger.debug("getVisitLogPage aid must required");
			return new Page<VisitLog>(new ArrayList<VisitLog>(), 0, page, pageSize);
		}
		List<VisitLog> list = visitLogMapper.getvisitLogPage(pageIndex,
				pageSize, sd, ed, source, landing, aidarr2, website);
		int count = visitLogMapper.getvisitLogCount(sd, ed, source, landing,
				aidarr2, website);

		// 存入账号名称
		List<AffiliateInfo> infolist = affiliateInfoMapper
				.getAffiliateInfoForAdmin();
		if (list.size() > 0) {
			List<Integer> saleids = Lists.transform(infolist,
					i -> i.getIsalerid());
			List<AdminUser> users = adminUserMapper.getAdminUserList(saleids);
			Map<Integer, AdminUser> usermap = Maps.uniqueIndex(users,
					i -> i.getIid());
			for (AffiliateInfo ai : infolist) {
				if (usermap.get(ai.getIsalerid()) != null) {
					ai.setSalerName(usermap.get(ai.getIsalerid())
							.getCusername());
				}
			}
			Map<String, AffiliateInfo> infomap = Maps.uniqueIndex(infolist,
					i -> i.getCaid());
			for (VisitLog v : list) {
				if (v.getCaid() != null && infomap.get(v.getCaid()) != null) {
					v.setSaler(infomap.get(v.getCaid()).getSalerName());
				}
			}
		}
		return new Page<VisitLog>(list, count, page, pageSize);
	}

	public int[] statisticalData(String startDate, String endDate, String aid,
			String source, String landing, String aidarr, Integer website) {
		int[] arr = new int[] { 0, 0 };
		Date sd = null, ed = null;
		if (StringUtils.notEmpty(startDate)) {
			sd = DateFormatUtils.getFormatDateYmdhmsByStr(startDate
					+ " 00:00:00");
		}
		if (StringUtils.notEmpty(endDate)) {
			ed = DateFormatUtils
					.getFormatDateYmdhmsByStr(endDate + " 23:59:59");
		}
		// aid分割
		List<String> aidarr2 = Lists.newArrayList();
		if (StringUtils.notEmpty(aidarr)) {
			aidarr2.addAll(Lists.newArrayList(aidarr.split(",")));
		}
		if (StringUtils.notEmpty(aid)) {
			aidarr2.add(aid);
		}
		if (aidarr2.size() == 0) {
			Logger.debug("statisticalData aid must required");
			return arr;
		}
		List<VisitLog> list = visitLogMapper.getvisitLogPage(-1, -1, sd, ed,
				source, landing, aidarr2, website);
		if (list.size() == 0) {
			return arr;
		}
		// 获取aid数
		ImmutableSet<String> aids = Multimaps.index(list, l -> l.getCaid())
				.keySet();
		// 每天ip数相加的独立ip数
		// 日期去掉时分秒
		list = Lists.transform(list,
				c -> {
					if (c.getDcreateDate() != null) {
						c.setDcreateDate(DateFormatUtils.delHHmmss(c
								.getDcreateDate()));
					}
					return c;
				});
		int ips = 0;
		int uniqueips = 0;
		Multimap<Date, VisitLog> vmap1 = Multimaps.index(list,
				l -> l.getDcreateDate());
		for (Date d : vmap1.keySet()) {
			List<VisitLog> vlist = Lists.newArrayList(vmap1.get(d));
			Multimap<String, VisitLog> vmap2 = Multimaps.index(vlist,
					v -> v.getCaid());
			for (String aa : aids) {
				List<VisitLog> vlist2 = Lists.newArrayList(vmap2.get(aa));
				ips += vlist2.size();
				Multimap<String, VisitLog> vmap3 = Multimaps.index(vlist2,
						v -> v.getCip());
				uniqueips += vmap3.keySet().size();
				// System.out.println("data="+d+",ips="+vmap2.keySet().size());
			}

		}
		arr[0] = ips;
		arr[1] = uniqueips;
		return arr;
	}

	public Page<VisitLogApiVo> getVisitLogPageForApi(Integer page,
			Integer pageSize, String startDate, String endDate) {
		Date sd = null, ed = null;
		if (StringUtils.notEmpty(startDate)) {
			sd = new Date(Long.parseLong(startDate));
		}
		if (StringUtils.notEmpty(endDate)) {
			ed = new Date(Long.parseLong(endDate));
		}
		int pageIndex = (page - 1) * pageSize;
		List<VisitLogApiVo> list = visitLogMapper.getVisitLogPageForApi(
				pageIndex, pageSize, sd, ed);
		int count = visitLogMapper.getVisitLogCountForApi(sd, ed);
		return new Page<VisitLogApiVo>(list, count, page, pageSize);
	}
}
