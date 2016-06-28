package mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import entity.mobile.MobileVisitLog;

public interface VisitLogMapper {

	@Insert({ "<script>insert into  t_mobile_visit_log (iplatform, csysversion, cimei, cphonename, iappid, ccid, ilanguageid, cnetwork, "
			+ "icurrentversion, cclientid, iconsumeTime, crequestUri, cremoteAddress) "
			+ "values (#{iplatform}, #{csysversion}, #{cimei},  #{cphonename}, #{iappid},  #{ccid}, #{ilanguageid}, "
			+ "#{cnetwork}, #{icurrentversion}, #{cclientid}, #{iconsumetime}, #{crequesturi}, "
			+ "#{cremoteaddress})</script>" })
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(MobileVisitLog log);

}
