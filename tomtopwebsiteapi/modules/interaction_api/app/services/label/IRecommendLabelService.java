package services.label;

import java.util.List;

import dto.label.RecommendLabelAndName;
import dto.label.RecommendLabelBase;
import dto.label.RecommendLabelName;

public interface IRecommendLabelService {

	public List<RecommendLabelAndName> getRecommendLabelAndName(
			Integer languageid, Integer websiteid, String cdevice,
			Boolean bshow, Integer pageSize, Integer pageNum);

	public List<RecommendLabelAndName> getRecommendLabelAndName(
			Integer languageid, Integer websiteid, String cdevice, Boolean bshow);

	public RecommendLabelAndName getRecommendLabelAndNameByLabelIdAndLanguageId(
			Integer labelid, Integer languageid, Integer websiteid,
			String cdevice);

	public Integer getRecommendLabelAndNameCount(Integer languageid,
			Integer websiteid, String cdevice, Boolean bshow);

	public int addRecommendLabel(RecommendLabelBase recommendLabel);

	public int addRecommendLabelName(RecommendLabelName recommendLabelName);

	public int updateRecommendLabel(RecommendLabelBase recommendLabel);

	public int updateRecommendLabelName(RecommendLabelName recommendLabelName);

	public Integer getRecommendLabelMaxIid();

	public Integer deleteRecommendLabelById(Integer iid);

	public RecommendLabelName getRecommendLabelNameByIdAndLangId(
			Integer labelid, Integer languageid);
}
