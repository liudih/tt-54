package services.member;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import dto.MemberLevel;
import dto.member.MemberByStatistics;
import dto.member.MemberGroupCriterion;

public class MemberLevelService {

	@Inject
	MemberBuyStatisticsService statisticsService;

	@Inject
	MemberGroupCriterionService groupCriterionService;

	@Inject
	MemberGroupService groupService;

	final double defluatAmount = 00.00;

	public MemberLevel getMemberLevel(int websiteid, String email) {

		MemberByStatistics statistics = statisticsService
				.getStatisticsByEmail(email);

		double amount = defluatAmount;
		if (statistics != null) {
			amount = statistics.getFamount();
		}

		List<MemberGroupCriterion> mgcList = groupCriterionService
				.getMgcWebsiteIdOderByAsc(websiteid);

		Integer levelgourpid = null;
		Integer nextlevlgroupid = null;
		Double nextlevldp = null;

		for (int i = 0, j = i + 1; i < mgcList.size(); i++,j++) {
			MemberGroupCriterion mgc = mgcList.get(i);
			double dp = mgc.getDconsumptionprice();
			if (amount >= dp) {
				levelgourpid = mgc.getIgroupid();
				if (i < mgcList.size() - 1) {
					MemberGroupCriterion nextmgc = mgcList.get(j);
					nextlevlgroupid = nextmgc.getIgroupid();
					nextlevldp = nextmgc.getDconsumptionprice();
				} else {
					nextlevlgroupid = null;
				}
			}
		}

		MemberLevel memberLevel = new MemberLevel();
		memberLevel.setAmount(amount);
		String groupName = groupService.getMemberGroupNameById(levelgourpid);
		memberLevel.setGroupname(groupName);

		if (nextlevlgroupid != null) {
			String nextGroupName = groupService
					.getMemberGroupNameById(nextlevlgroupid);
			memberLevel.setNextgoupname(nextGroupName);
			BigDecimal bg = new BigDecimal(nextlevldp - amount);
			double d = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			memberLevel.setWithnextamout(d);
		}
		return memberLevel;
	}

}
