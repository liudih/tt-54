package mapper.base;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import dto.Banner;


public interface BannerMapper {

	int deleteByPrimaryKey(Integer iid);

	int insertSelective(Banner record);

	Banner selectByPrimaryKey(Integer iid);

	int updateByPrimaryKeySelective(Banner record);

	@Select("select iid,iwebsiteid,ilanguageid,cbgcolor,ctitle,bstatus,curl,iindex,bbgimgtile,bhasbgimage,dcreatedate "
			+ " from t_banner where ilanguageid=#{0} and iwebsiteid =#{1} and bstatus=#{2} order by iindex desc ")
	List<Banner> getBanner(Integer ilanguageid, Integer iwebsiteid,
			Boolean bstatus);

	List<Banner> getBannerPageByParamMap(Map<String, Object> paramMap);

	int getBannerCountByParamMap(Map<String, Object> paramMap);

	@Select("select count(iindex) from t_banner ")
	int getMaxIndex();

	@Select("select iid,iwebsiteid,ilanguageid,cbgcolor,ctitle,bstatus,curl,iindex,dcreatedate "
			+ " from t_banner where iindex=#{0}")
	Banner getBannerByIndex(int index);

	int batchUpdateIndex(List<Banner> list);

	@Select("select iid,iwebsiteid,ilanguageid,cbgcolor,ctitle,bstatus,curl,iindex,dcreatedate "
			+ " from t_banner where iindex > #{0}")
	List<Banner> getBannerLgIndex(int index);
}
