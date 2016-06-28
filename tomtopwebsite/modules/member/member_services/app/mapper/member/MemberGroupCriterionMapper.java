package mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import dto.member.MemberGroupCriterion;



public interface MemberGroupCriterionMapper {    
    @Select("select iid, iwebsiteid, igroupid, dconsumptionprice,ccreateuser, "
    		+ "dcreatedate from t_member_group_criterion where iwebsiteid=#{0} order by dconsumptionprice asc")
    List<MemberGroupCriterion> getMgcWebsiteIdOderByAsc(int websiteid);
}
