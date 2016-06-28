package mapper.member;

import org.apache.ibatis.annotations.Select;

import dto.member.MemberPhoto;

public interface MemberPhotoMapper {

	int deleteByPrimaryKey(Integer iid);

	int insert(MemberPhoto record);

	int insertSelective(MemberPhoto record);

	MemberPhoto selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(MemberPhoto record);

	int updateByPrimaryKey(MemberPhoto record);

	@Select("select iid, cemail, ccontenttype, bfile, cmd5 "
			+ "from t_member_photo where cemail=#{0} and iwebsiteid = #{1} limit 1")
	MemberPhoto getMemberPhotoByEamil(String eamil, Integer websiteId);
}
