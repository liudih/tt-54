package mapper.member;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import dto.member.MemberOtherId;

public interface MemberOtherIdMapper {

	@Insert("insert into t_member_other_id (cemail, csource, csourceid, bvalidated) "
			+ "values (#{cemail},#{csource},#{csourceid},#{bvalidated})")
	int insert(MemberOtherId otherId);

	@Select("select * from t_member_other_id where cemail=#{0}")
	List<MemberOtherId> getAllByEmail(String email);

	@Select("select * from t_member_other_id where csource=#{0} AND csourceid=#{1} limit 1")
	MemberOtherId getBySource(String source, String sourceId);

}