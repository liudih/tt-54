package services.member;


import mapper.member.MemberByStatisticsMapper;

import com.google.inject.Inject;

import dto.member.MemberByStatistics;



public class MemberBuyStatisticsService {

	
	@Inject 
	MemberByStatisticsMapper statisticsMapper;
	
	public MemberByStatistics getStatisticsByEmail(String email){
		MemberByStatistics statistcs=statisticsMapper.select(email);
		return statistcs;
	}
}
