package mapper.label;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dto.label.RecommendLabelAndName;
import dto.label.RecommendLabelBase;
import dto.label.RecommendLabelName;

public interface RecommendLabelMapper {

	@Select("select rl.iid,rl.icategoryid,rl.itype,rl.iwebsiteid,rl.cdevice,rl.ipriority,rl.bshow,rl.ccreateuser,rl.dcreatedate,"
			+ "rln.ilanguageid,rln.clabelname,rln.cvalue,rln.cimageurl from t_recommend_label rl left join t_recommend_label_name rln "
			+ "on rl.iid=rln.ilabelid where rln.ilanguageid=#{0} and rl.iwebsiteid=#{1} and rl.cdevice=#{2} and rl.bshow=#{3} "
			+ "limit #{4} offset (#{4} * (#{5} - 1))")
	List<RecommendLabelAndName> getRecommendLabelAndNameForPage(
			Integer languageid, Integer websiteid, String cdevice,
			Boolean bshow, Integer pageSize, Integer pageNum);

	@Select("select rl.iid,rl.icategoryid,rl.itype,rl.iwebsiteid,rl.cdevice,rl.ipriority,rl.bshow,rl.ccreateuser,rl.dcreatedate,"
			+ "rln.ilanguageid,rln.clabelname,rln.cvalue,rln.cimageurl from t_recommend_label rl left join t_recommend_label_name rln "
			+ "on rl.iid=rln.ilabelid where rln.ilanguageid=#{0} and rl.iwebsiteid=#{1} and rl.cdevice=#{2} and rl.bshow=#{3} ")
	List<RecommendLabelAndName> getRecommendLabelAndName(Integer languageid,
			Integer websiteid, String cdevice, Boolean bshow);

	@Select("select rl.iid,rl.icategoryid,rl.itype,rl.iwebsiteid,rl.cdevice,rl.ipriority,rl.bshow,rl.ccreateuser,rl.dcreatedate,"
			+ "rln.ilanguageid,rln.clabelname,rln.cvalue,rln.cimageurl from t_recommend_label rl left join t_recommend_label_name rln "
			+ "on rl.iid=rln.ilabelid and  rln.ilabelid=#{0} where rln.ilanguageid=#{1} and rl.iwebsiteid=#{2} and rl.cdevice=#{3}")
	RecommendLabelAndName getRecommendLabelAndNameByLabelIdAndLanguageId(
			Integer labelid, Integer languageid, Integer websiteid,
			String cdevice);

	@Select("select ilabelid,ilanguageid,clabelname,cvalue,cimageurl,cimages from t_recommend_label_name "
			+ "where ilabelid=#{0} and ilanguageid=#{1}")
	RecommendLabelName getRecommendLabelNameImage(Integer labelid,
			Integer languageid);

	@Select("select count(*) from t_recommend_label rl left join t_recommend_label_name rln "
			+ "on rl.iid=rln.ilabelid where rln.ilanguageid=#{0} and rl.iwebsiteid=#{1} and rl.cdevice=#{2} and rl.bshow=#{3}")
	Integer getCountRecommendLabelAndName(Integer languageid,
			Integer websiteid, String cdevice, Boolean bshow);

	int insertRecommendLabel(RecommendLabelBase record);

	int insertRecommendLabelName(RecommendLabelName record);

	int updateRecommendLabelByPrimaryKeySelective(RecommendLabelBase record);

	int updateRecommendLabelNameByPrimaryKeySelective(RecommendLabelName record);

	@Select("select max(iid) from t_recommend_label")
	Integer getMaxIid();

	@Update("update t_recommend_label set bshow=#{0} where iid=#{1}")
	Integer updateRecommendLableBshowById(Boolean bshow, Integer iid);
}