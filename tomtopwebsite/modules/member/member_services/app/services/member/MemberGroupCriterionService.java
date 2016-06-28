package services.member;

import java.util.List;

import javax.inject.Inject;




import dto.member.MemberGroupCriterion;
import mapper.member.MemberGroupCriterionMapper;

public class MemberGroupCriterionService {

	@Inject
	MemberGroupCriterionMapper mapper;

	public List<MemberGroupCriterion> getMgcWebsiteIdOderByAsc(int websiteid) {
		List<MemberGroupCriterion> mgc = mapper
				.getMgcWebsiteIdOderByAsc(websiteid);
		return mgc;
	}
}
