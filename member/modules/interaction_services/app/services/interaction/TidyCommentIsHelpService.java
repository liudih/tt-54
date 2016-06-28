/**
 * 
 */
package services.interaction;

import java.util.Date;
import java.util.List;

import org.mybatis.guice.transactional.Transactional;

import play.Logger;
import scala.util.Random;

import com.google.inject.Inject;

import dto.interaction.InteractionComment;
import dto.interaction.InteractionCommentHelpEvaluate;
import dto.interaction.InteractionCommentHelpQty;

/**
 * @author wujirui
 *
 */
public class TidyCommentIsHelpService {

	@Inject
	InteractionCommentService interactionCommentService;

	public void processCommentHelpCount() {
		Logger.debug("ProcessCommentHelpCount begin!");

		// 查询出符合条件的评论数
		List<InteractionComment> commentList = interactionCommentService
				.getCommentHelpCount();

		int count = 0;// 用于统计某个商品评论的数量
		String clistingid = "";
		for (InteractionComment interactionComment : commentList) {
			clistingid = interactionComment.getClistingid();
			count = interactionComment.getCount();
			tidyData(clistingid, count);
		}
		Logger.debug("ProcessCommentHelpCount end!");
	}

	/**
	 * 根据规则整理数据。
	 */
	private void tidyData(String clistingid, int count) {

		Logger.debug("ProcessCommentHelpCount tidy data begin!");

		List<InteractionComment> interaCommentList = interactionCommentService
				.getCommentHelpCountByCliId(clistingid);

		if (count > 5 && count <= 20) {
			Logger.debug("Tidy data count 5 > count < 20!");
			// 随机取1个有用插入到数据库
			insertData(interaCommentList, true);
		} else if (count > 21 && count <= 50) {
			Logger.debug("Tidy data count 21 > count < 50!");
			// 随机取3个有用插入到数据库
			for (int k = 0; k < 3; k++) {
				insertData(interaCommentList, true);
			}
		} else if (count > 51 && count <= 100) {
			Logger.debug("Tidy data count 51 > count < 100!");
			// 随机取5个有用插入到数据库
			for (int k = 0; k < 5; k++) {
				insertData(interaCommentList, true);
			}
			// 随机取1个无用插入到数据库
			insertData(interaCommentList, false);
		} else if (count > 101) {
			Logger.debug("Tidy data count 101 > count!");
			// 随机取5个有用插入到数据库
			for (int k = 0; k < 5; k++) {
				insertData(interaCommentList, true);
			}
			// 随机取2个无用插入到数据库
			for (int k = 0; k < 2; k++) {
				insertData(interaCommentList, false);
			}
		}
	}

	/**
	 * 插入数据
	 * 
	 * @return
	 */
	@Transactional
	private void insertData(List<InteractionComment> interaCommentList,
			boolean isHelpFul) {

		InteractionComment comment = new InteractionComment();
		int index = getRandom(interaCommentList.size());
		comment = interaCommentList.get(index); // 从list中随机取出一条数据；
		interaCommentList.remove(index); // 将取出的数据从list中移除，避免后面取数据时取到重复的；

		InteractionCommentHelpEvaluate evaluate = new InteractionCommentHelpEvaluate();
		evaluate.setIcommentid(comment.getIid());
		evaluate.setHelpfulcode(1);
		evaluate.setCmemberemail(comment.getCmemberemail());
		int resultId = interactionCommentService.insertEvaluate(evaluate);

		if (resultId > 0) {
			InteractionCommentHelpQty helpQty = interactionCommentService
					.selectByCommentId(comment.getIid());
			if (null != helpQty && null != helpQty.getIid()) {// 如果存在对应的评论信息则更新有用数或无用数
				Logger.info("InteractionCommentHelpQty-->iid:"
						+ helpQty.getIid());
				Logger.info("InteractionCommentHelpQty-->helpfulqty:"
						+ helpQty.getHelpfulqty());
				Logger.info("InteractionCommentHelpQty-->nothelpfulqty:"
						+ helpQty.getNothelpfulqty());
				Logger.info("InteractionCommentHelpQty-->commentid:"
						+ helpQty.getCommentid());
				if (isHelpFul) {
					// 有用数加1
					helpQty.setHelpfulqty(helpQty.getHelpfulqty() + 1);
				} else {
					// 无用数加1
					helpQty.setNothelpfulqty(helpQty.getNothelpfulqty() + 1);
				}
				interactionCommentService.updateHelpQtyByPK(helpQty);
			} else {// 不存在则插入一条
				helpQty = new InteractionCommentHelpQty();
				helpQty.setCommentid(comment.getIid());
				helpQty.setDcreatedate(new Date());
				if (isHelpFul) {
					// 有用数加1
					helpQty.setHelpfulqty(1);
				} else {
					// 无用数加1
					helpQty.setNothelpfulqty(1);
				}
				interactionCommentService.insertHelpQty(helpQty);
			}

		}

	}

	/**
	 * 获取一个小于index的随机数
	 * 
	 * @param index
	 * @return
	 */
	private int getRandom(int index) {
		Random random = new Random();
		int resultInt = random.nextInt(index);
		return resultInt;
	}

}
