package services.label;

import java.util.List;

import javax.inject.Inject;

import mapper.label.RecommendLabelMapper;
import dto.label.RecommendLabelAndName;
import dto.label.RecommendLabelBase;
import dto.label.RecommendLabelName;

public class RecommendLabelService implements IRecommendLabelService {

	@Inject
	RecommendLabelMapper recommendLabelMapper;

	private static final boolean noShow = false;

	/**
	 * 获取客户喜爱推荐标签list(有分页)
	 * 
	 * @param languageid
	 *            语言ID
	 * @param websiteid
	 *            站点ID
	 * @param cdevice
	 *            设备
	 * @param bshow
	 *            是否显示 (显示：true,不显示:false)
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	@Override
	public List<RecommendLabelAndName> getRecommendLabelAndName(
			Integer languageid, Integer websiteid, String cdevice,
			Boolean bshow, Integer pageSize, Integer pageNum) {
		return recommendLabelMapper.getRecommendLabelAndNameForPage(languageid,
				websiteid, cdevice, bshow, pageSize, pageNum);
	}

	/**
	 * 获取客户喜爱推荐标签list
	 * 
	 * @param languageid
	 *            语言ID
	 * @param websiteid
	 *            站点ID
	 * @param cdevice
	 *            设备
	 * @param bshow
	 *            是否显示 (显示：true,不显示:false)
	 * @return
	 */
	@Override
	public List<RecommendLabelAndName> getRecommendLabelAndName(
			Integer languageid, Integer websiteid, String cdevice, Boolean bshow) {
		return recommendLabelMapper.getRecommendLabelAndName(languageid,
				websiteid, cdevice, bshow);
	}

	/**
	 * 根据条件获取记录数
	 * 
	 * @param languageid
	 *            语言ID
	 * @param websiteid
	 *            站点ID
	 * @param cdevice
	 *            设备
	 * @param bshow
	 *            是否显示 (显示：true,不显示:false)
	 * @return
	 */
	@Override
	public Integer getRecommendLabelAndNameCount(Integer languageid,
			Integer websiteid, String cdevice, Boolean bshow) {
		return recommendLabelMapper.getCountRecommendLabelAndName(languageid,
				websiteid, cdevice, bshow);
	}

	/**
	 * 添加 RecommendLabelBase
	 * 
	 * @param RecommendLabelBase
	 * @return
	 */
	@Override
	public int addRecommendLabel(RecommendLabelBase recommendLabel) {
		return recommendLabelMapper.insertRecommendLabel(recommendLabel);
	}

	/**
	 * 添加 RecommendLabelName
	 * 
	 * @param RecommendLabelName
	 * @return
	 */
	@Override
	public int addRecommendLabelName(RecommendLabelName recommendLabelName) {
		return recommendLabelMapper
				.insertRecommendLabelName(recommendLabelName);
	}

	/**
	 * 更新 RecommendLabel
	 * 
	 * @param RecommendLabel
	 * @return
	 */
	@Override
	public int updateRecommendLabel(RecommendLabelBase recommendLabel) {
		return recommendLabelMapper
				.updateRecommendLabelByPrimaryKeySelective(recommendLabel);
	}

	/**
	 * 更新 RecommendLabelName
	 * 
	 * @param RecommendLabelName
	 * @return
	 */
	@Override
	public int updateRecommendLabelName(RecommendLabelName recommendLabelName) {
		return recommendLabelMapper
				.updateRecommendLabelNameByPrimaryKeySelective(recommendLabelName);
	}

	/**
	 * 获取RecommendLabelAndName
	 * 
	 * @param labelid
	 *            标签ID
	 * @param languageid
	 *            语言ID
	 * @param websiteid
	 *            站点ID
	 * @param cdevice
	 *            设备
	 * @return
	 */
	@Override
	public RecommendLabelAndName getRecommendLabelAndNameByLabelIdAndLanguageId(
			Integer labelid, Integer languageid, Integer websiteid,
			String cdevice) {
		return recommendLabelMapper
				.getRecommendLabelAndNameByLabelIdAndLanguageId(labelid,
						languageid, websiteid, cdevice);
	}

	/**
	 * 获取recommend_label最大的iid值
	 * 
	 * @return
	 */
	@Override
	public Integer getRecommendLabelMaxIid() {
		return recommendLabelMapper.getMaxIid();
	}

	/**
	 * 设置recommend_label为不显示
	 * 
	 * @param iid
	 *            标签ID
	 * @return
	 */
	@Override
	public Integer deleteRecommendLabelById(Integer iid) {
		return recommendLabelMapper.updateRecommendLableBshowById(noShow, iid);
	}

	/**
	 * 获取RecommendLabelName
	 * 
	 * @param labelid
	 *            标签ID
	 * @param languageid
	 *            语言ID
	 * @return
	 */
	public RecommendLabelName getRecommendLabelNameByIdAndLangId(
			Integer labelid, Integer languageid) {
		return recommendLabelMapper.getRecommendLabelNameImage(labelid,
				languageid);
	}

}
