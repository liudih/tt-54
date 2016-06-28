package mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import dto.mobile.ContactDto;
import entity.mobile.Contact;

public interface ContactMapper {

	@Insert({ "<script>insert into  t_contact (memberemail, uuid, title, content, createdate, device, sysversion, imei, "
			+ "phonename, languageid, currentversion, status,operationuser,operationdate,remark) "
			+ "values (#{memberemail}, #{uuid}, #{title},  #{content}, #{createdate},  #{device}, #{sysversion},#{imei}, "
			+ "#{phonename}, #{languageid}, #{currentversion}, #{status}, #{operationuser}, #{operationdate}, #{remark})</script>" })
	@Options(useGeneratedKeys = true, keyProperty = "iid", keyColumn = "iid")
	int insert(Contact contact);

	@Select("select * from t_contact order by iid desc ")
	List<ContactDto> getAllContact();

}
