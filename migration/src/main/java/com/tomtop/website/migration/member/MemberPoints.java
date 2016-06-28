package com.tomtop.website.migration.member;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class MemberPoints {

	@Inject
	MemberMapper memberMapper;

	public void getPoints(String savePath, List<Integer> customerids) {

		List<CustomerPoint> points = memberMapper
				.getCustomerPoints(customerids);
		if (null == points || points.size() == 0)
			return;

		/*
		 * Multimap<String, CustomerPoint> maps = Multimaps.index(points, obj ->
		 * obj.getEmail());
		 */
		ObjectMapper om = new ObjectMapper();
		File f = new File(savePath, "/points");
		if (f.exists() == false) {
			f.mkdirs();
		}
		List<com.website.dto.member.MemberPoint> ls = Lists
				.transform(
						points,
						obj -> {
							com.website.dto.member.MemberPoint mpoint = new com.website.dto.member.MemberPoint();
							mpoint.setDotype(getType(obj.getPoints_type()));
							mpoint.setEmail(obj.getEmail().toLowerCase());
							mpoint.setIntegral(new Double(obj.getPoints()));
							mpoint.setRemark(obj.getName());
							mpoint.setWebsiteid(1);
							mpoint.setId(obj.getId());
							return mpoint;
						});
		for (com.website.dto.member.MemberPoint point : ls) {
			File f1 = new File(f.getPath(), point.getId() + "point.json");
			try {
				om.writeValue(f1, point);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getType(int typeid) {
		switch (typeid) {
		case 1:
			return "signsup";
		case 3:
			return "joiningnewsletter";
		case 4:
			return "writeproductreview";
		case 5:
			return "votinginpoll";
		case 6:
			return "recommendaproducttofriend";
		case 7:
			return "addanewtag";
		case 9:
			return "recommendafriendtomakesapurchase";
		case 10:
			return "recommendafriendtosignsup";
		case 11:
			return "creatingavaluablepost";
		case 12:
			return "makingavaluablepostreply";
		case 13:
			return "reportinganorderreceivedate";
		case 14:
			return "postingavideo";
		case 15:
			return "uploadingproductphoto";
		default:
			return "";
		}
	}

}