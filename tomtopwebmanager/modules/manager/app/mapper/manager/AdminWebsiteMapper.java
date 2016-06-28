package mapper.manager;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import entity.manager.AdminWebsite;

public interface AdminWebsiteMapper {
	
	@Select("select * from t_website")
	List<AdminWebsite> getWebsite();
}
