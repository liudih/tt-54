package services.loyalty.award;

import javax.inject.Inject;

import play.Logger;
import services.loyalty.IPointsService;
import entity.loyalty.MemberIntegralHistory;

public class PointsAwardProvider implements IAwardProvider {

	@Inject
	IPointsService service;

	public static final String NAME = "points";

	@Override
	public String awardtype() {
		return NAME;
	}

	@Override
	public void runAward(Integer siteid, String email, Double value,
			String Cawardtype) {
		// TODO Auto-generated method stub
		MemberIntegralHistory mhistory = new MemberIntegralHistory();
		mhistory.setCemail(email);
		// mhistory.setCremark("");
		mhistory.setCdotype(Cawardtype);
		Logger.debug("----------value----------:"+value);
		mhistory.setIintegral(value);
		mhistory.setIwebsiteid(siteid);
		service.insertIntegralHistory(mhistory);
	}
}
