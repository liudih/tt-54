package services.activity.page.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import play.Logger;
import play.Logger.ALogger;
import dao.activitydb.page.IVoteRecordDao;
import forms.activity.page.VoteRecordForm;
import services.activity.page.IVoteRecordService;
import valueobject.activity.page.VoteRecord;
import values.activity.page.VoteRecordQuery;

public class VoteRecordServiceImpl implements IVoteRecordService {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	private IVoteRecordDao iVoteRecordDao;

	@Override
	public List<VoteRecordForm> getVoteRecordByPageItemNameAndDate(
			VoteRecordForm voteRecordForm) {
		VoteRecordQuery voteRecordQuery = new VoteRecordQuery();
		List<VoteRecordForm> voteRecordForms = new ArrayList<VoteRecordForm>();
		try {
			BeanUtils.copyProperties(voteRecordQuery, voteRecordForm);
			List<VoteRecordQuery> voteRecordQueries = iVoteRecordDao
					.getVoteRecordByPageItemNameAndDate(voteRecordQuery);

			voteRecordForms = Lists.transform(voteRecordQueries,
					new Function<VoteRecordQuery, VoteRecordForm>() {

						@Override
						public VoteRecordForm apply(VoteRecordQuery query) {
							VoteRecordForm voteRecordForm = new VoteRecordForm();
							try {
								BeanUtils.copyProperties(voteRecordForm, query);
							} catch (Exception e) {
								logger.error("转化对象失败" + e.getMessage());
							}
							return voteRecordForm;
						}
					});
		} catch (Exception e) {
			logger.error("转化对象失败" + e.getMessage());
		}
		return voteRecordForms;
	}

	@Override
	public int getVoteRecordCount(VoteRecordForm voteRecordForm) {
		VoteRecordQuery voteRecordQuery = new VoteRecordQuery();
		int result = 0;
		try {
			BeanUtils.copyProperties(voteRecordQuery, voteRecordForm);
			result = iVoteRecordDao.getVoteRecordCount(voteRecordQuery);
		} catch (Exception e) {
			logger.error("转换对象失败" + e.getMessage());
		}
		return result;
	}

	@Override
	public List<VoteRecordForm> getVoteRecordUserByPageItemNameAndDate(
			VoteRecordForm voteRecordForm) {
		VoteRecordQuery voteRecordQuery = new VoteRecordQuery();
		List<VoteRecordForm> voteRecordForms = new ArrayList<VoteRecordForm>();
		try {
			BeanUtils.copyProperties(voteRecordQuery, voteRecordForm);
			List<VoteRecordQuery> voteRecordQueries = iVoteRecordDao
					.getVoteRecordUserByPageItemNameAndDate(voteRecordQuery);
			voteRecordForms = Lists.transform(voteRecordQueries,
					new Function<VoteRecordQuery, VoteRecordForm>() {

						@Override
						public VoteRecordForm apply(VoteRecordQuery query) {
							VoteRecordForm voteRecordForm = new VoteRecordForm();
							try {
								BeanUtils.copyProperties(voteRecordForm, query);
							} catch (Exception e) {
								logger.error("转化对象失败" + e.getMessage());
							}
							return voteRecordForm;
						}
					});
		} catch (Exception e) {
			logger.error("转化对象失败" + e.getMessage());
		}
		return voteRecordForms;
	}

	@Override
	public int getVoteRecordUserCount(VoteRecordForm voteRecordForm) {
		VoteRecordQuery voteRecordQuery = new VoteRecordQuery();
		int result = 0;
		try {
			BeanUtils.copyProperties(voteRecordQuery, voteRecordForm);
			result = iVoteRecordDao.getVoteRecordUserCount(voteRecordQuery);
		} catch (Exception e) {
			logger.error("转换对象失败" + e.getMessage());
		}
		return result;
	}

	@Override
	public List<VoteRecord> getVoteRecords() {
		List<entity.activity.page.VoteRecord> voteRecords = iVoteRecordDao
				.getVoteRecords();

		List<VoteRecord> voteRecordList = Lists.transform(voteRecords,
				new Function<entity.activity.page.VoteRecord, VoteRecord>() {

					@Override
					public VoteRecord apply(
							entity.activity.page.VoteRecord voteRecord) {
						VoteRecord record = new VoteRecord();
						try {
							BeanUtils.copyProperties(record, voteRecord);
						} catch (Exception e) {
							logger.error("转化对象失败" + e.getMessage());
						}
						return record;
					}
				});
		return voteRecordList;
	}

}
