package services.activity.page;

import java.util.List;

import valueobject.activity.page.VoteRecord;
import forms.activity.page.VoteRecordForm;

/**
 * 投票统计服务接口
 * 
 * @author Guozy
 *
 */
public interface IVoteRecordService {

	/**
	 * 根据相应条件,获取投票统计的所有信息
	 * 
	 * @return
	 */
	public List<VoteRecordForm> getVoteRecordByPageItemNameAndDate(
			VoteRecordForm voteRecordForm);

	/**
	 * 根据相应条件,获取投票统计的所有数量
	 * 
	 * @return
	 */
	public int getVoteRecordCount(VoteRecordForm voteRecordForm);

	/**
	 * 根据相应条件,获取投票统计的所有信息
	 * 
	 * @return
	 */
	public List<VoteRecordForm> getVoteRecordUserByPageItemNameAndDate(
			VoteRecordForm voteRecordForm);

	/**
	 * 根据相应条件,获取投票统计的所有数量
	 * 
	 * @return
	 */
	public int getVoteRecordUserCount(VoteRecordForm voteRecordForm);

	/**
	 * 获取投票的所有数据信息
	 * 
	 * @return
	 */
	public List<VoteRecord> getVoteRecords();

}
