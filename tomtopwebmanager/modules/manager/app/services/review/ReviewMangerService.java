package services.review;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import play.Logger;
import services.base.utils.DateFormatUtils;
import services.interaciton.review.ProductReviewsService;
import util.AppsUtil;
import dto.interaction.InteractionComment;
import forms.review.ReviewForm;

public class ReviewMangerService {

	@Inject
	ProductBaseMapper productBaseMapper;

	/**
	 * Like搜索sku
	 * 
	 * @param sku
	 * @return
	 */
	public List<String> getSku(String sku) {
		List<String> skuList = productBaseMapper.getSkuLike(sku);
		if (skuList == null) {
			skuList = new ArrayList<String>();
		}

		return skuList;
	}

	public int addReview(ReviewForm rform, Integer siteid, String user,
			ProductReviewsService reviewService) {
		Logger.debug("siteId-------------------------------------------------:{}",siteid);
		InteractionComment review = new InteractionComment();
		Integer iid = reviewService.getMaxId();
		review.setIid(iid);
		review.setIwebsiteid(siteid);
		review.setCplatform(user);
		review.setCcomment(rform.getCdtl());
		review.setCsku(rform.getCsku());
		review.setCmemberemail(rform.getCemail());
		String ddate = rform.getDdate();
		if (!"".equals(ddate)) {
			ddate = ddate + " " + AppsUtil.randomTime(24) + ":"
					+ AppsUtil.randomTime(60) + ":" + AppsUtil.randomTime(60);
			Date sdate = DateFormatUtils.getFormatDateYmdhmsByStr(ddate);
			review.setDcreatedate(sdate);
		}
		review.setIusefulness(rform.getUsefulness());
		review.setIshipping(rform.getShipping());
		review.setIquality(rform.getQuality());
		review.setIprice(rform.getPrice());
		review.setFoverallrating(rform.getFoverallrating());
		String listingid = this.getListingIdBySku(rform.getCsku(), siteid);
		if (listingid == null || "".equals(listingid)) {
			Logger.error("Review addReview listingid is null error");
		} else {
			review.setClistingid(listingid);
		}
		Integer res = reviewService.doAddProductReview(review);
		if (res == null) {
			Logger.error("Review saveReviewManger error");
		}
		return res;
	}

	public int updateReview(ReviewForm rform,
			ProductReviewsService reviewService) {
		InteractionComment review = new InteractionComment();
		review.setIid(rform.getIid());
		review.setCcomment(rform.getCdtl());
		review.setCmemberemail(rform.getCemail());
		String ddate = rform.getDdate();
		if (!"".equals(ddate)) {
			ddate = ddate + " " + AppsUtil.randomTime(24) + ":"
					+ AppsUtil.randomTime(60) + ":" + AppsUtil.randomTime(60);
			Date sdate = DateFormatUtils.getFormatDateYmdhmsByStr(ddate);
			review.setDcreatedate(sdate);
		}
		review.setIusefulness(rform.getUsefulness());
		review.setIshipping(rform.getShipping());
		review.setIquality(rform.getQuality());
		review.setIprice(rform.getPrice());
		review.setFoverallrating(rform.getFoverallrating());
		Integer res = reviewService.updateProductReview(review);
		if (res == null) {
			res = 0;
			Logger.error("Review updateReview error");
		}
		return res;
	}

	public int uploadReview(List<ReviewForm> rformlist, Integer siteid,
			String user, ProductReviewsService reviewService) {
		InteractionComment review = null;
		Integer iid = reviewService.getMaxId();
		for (ReviewForm rform : rformlist) {
			review = new InteractionComment();
			review.setIid(iid);
			review.setIwebsiteid(siteid);
			review.setCplatform(user);
			review.setCcomment(rform.getCdtl());
			review.setCsku(rform.getCsku());
			review.setCmemberemail(rform.getCemail());
			String ddate = rform.getDdate();
			if (!"".equals(ddate)) {
				ddate = ddate + " " + AppsUtil.randomTime(24) + ":"
						+ AppsUtil.randomTime(60) + ":"
						+ AppsUtil.randomTime(60);
				Date sdate = DateFormatUtils.getFormatDateYmdhmsByStr(ddate);
				review.setDcreatedate(sdate);
			}
			review.setIusefulness(rform.getUsefulness());
			review.setIshipping(rform.getShipping());
			review.setIquality(rform.getQuality());
			review.setIprice(rform.getPrice());
			review.setFoverallrating(rform.getFoverallrating());
			String listingid = getListingIdBySku(rform.getCsku(), siteid);
			if (listingid == null) {
				Logger.error("Review addReview listingid is null error");
			} else {
				review.setClistingid(listingid);
			}
			Integer res = reviewService.doAddProductReview(review);
			if (res == null) {
				Logger.error("Review saveReviewManger error");
			}
			iid++;
		}
		return rformlist.size();
	}

	public String getListingIdBySku(String sku, Integer siteId) {
		return productBaseMapper.getListingIdBySkuAndWebsiteId(sku, siteId);
	}

	/**
	 * 获取Excel数据
	 * 
	 * @param file
	 * @param ignoreRows
	 *            从第几行开始取
	 * 
	 * @return
	 */
	public String[][] getData(File file, int ignoreRows) {
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		BufferedInputStream in = null;
		String[][] returnArray = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));

			// 打开HSSFWorkbook
			POIFSFileSystem fs = new POIFSFileSystem(in);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFCell cell = null;
			for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
				HSSFSheet st = wb.getSheetAt(sheetIndex);
				// 第一行为标题，不取
				for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
					HSSFRow row = st.getRow(rowIndex);
					if (row == null) {
						continue;
					}
					int tempRowSize = row.getLastCellNum() + 1;
					if (tempRowSize > rowSize) {
						rowSize = tempRowSize;
					}
					String[] values = new String[rowSize];
					Arrays.fill(values, "");
					boolean hasValue = false;
					for (short columnIndex = 0; columnIndex <= row
							.getLastCellNum(); columnIndex++) {
						String value = "";
						cell = row.getCell(columnIndex);
						if (cell != null) {
							switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_STRING:
								value = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_NUMERIC:
								if (HSSFDateUtil.isCellDateFormatted(cell)) {
									Date date = cell.getDateCellValue();
									if (date != null) {
										value = new SimpleDateFormat(
												"yyyy-MM-dd").format(date);
									} else {
										value = "";
									}
								} else {
									value = new DecimalFormat("0").format(cell
											.getNumericCellValue());
								}
								break;
							case HSSFCell.CELL_TYPE_FORMULA:
								// 导入时如果为公式生成的数据则无值
								if (!cell.getStringCellValue().equals("")) {
									value = cell.getStringCellValue();
								} else {
									value = cell.getNumericCellValue() + "";
								}
								break;
							case HSSFCell.CELL_TYPE_BLANK:
								break;
							case HSSFCell.CELL_TYPE_ERROR:
								value = "";
								break;
							case HSSFCell.CELL_TYPE_BOOLEAN:
								value = (cell.getBooleanCellValue() == true ? "Y"
										: "N");
								break;
							default:
								value = "";
							}
						}
						if (columnIndex == 0 && value.trim().equals("")) {
							break;
						}
						values[columnIndex] = rightTrim(value);
						hasValue = true;
					}
					if (hasValue) {
						result.add(values);
					}
				}
			}
			returnArray = new String[result.size()][rowSize];
			for (int i = 0; i < returnArray.length; i++) {
				returnArray[i] = (String[]) result.get(i);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return returnArray;
	}

	/**
	 * 去掉字符串右边的空格
	 * 
	 * @param str
	 *            要处理的字符串
	 * @return 处理后的字符串
	 */
	public static String rightTrim(String str) {

		if (str == null) {
			return "";
		}
		int length = str.length();
		for (int i = length - 1; i >= 0; i--) {
			if (str.charAt(i) != 0x20) {
				break;
			}
			length--;
		}
		return str.substring(0, length);
	}

}