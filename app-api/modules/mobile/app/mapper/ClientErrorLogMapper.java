package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import entity.mobile.MobileClientErrorLog;

public interface ClientErrorLogMapper {

	@Insert({ "<script>insert into  t_mobile_client_error_log ( icode, cerrormsg, cremoteaddress,csysversion,cphonename,iappid,icurrentversion,cnetwork) "
			+ "values ( #{icode}, #{cerrormsg}, #{cremoteaddress},#{csysversion},#{cphonename},#{iappid},#{icurrentversion},#{cnetwork})</script>" })
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(MobileClientErrorLog log);

	@Insert({ "<script>insert into  t_mobile_client_error_log ( icode, cerrormsg, cremoteaddress,csysversion,cphonename,iappid,icurrentversion,cnetwork) "
			+ "values <foreach collection=\"list\" item=\"item\" index=\"index\"  separator=\",\">"
			+ "(#{item.icode}, #{item.cerrormsg}, #{item.cremoteaddress},#{item.csysversion},#{item.cphonename},#{item.iappid},#{item.icurrentversion},#{item.cnetwork})"
			+ " </foreach></script>" })
	int batchInsert(@Param("list") List<MobileClientErrorLog> logs);
}
