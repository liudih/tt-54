package mappers.tracking;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import entity.tracking.AffiliateBanner;

public interface ManagerAffiliateBannerMapper {
	List<AffiliateBanner> getAffiliateBanners(@Param("ab") AffiliateBanner ab,
			@Param("page") Integer page, @Param("pageSize") Integer pageSize);

	int edit(AffiliateBanner affiliateBanner);

	int delete(List<Integer> list);

	int add(AffiliateBanner affiliateBanner);

	int count(AffiliateBanner affiliateBanner);

	@Select("select * from t_affiliate_banner where iid = #{0}")
	AffiliateBanner get(int id);
}
