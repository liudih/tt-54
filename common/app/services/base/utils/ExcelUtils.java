package services.base.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import play.Logger;

public class ExcelUtils {
	private SimpleDateFormat sdf;

	/**
	 * excel 2003 行数最大为65536
	 * 
	 * @param data
	 * @return
	 */
	public byte[] arrayToXSL(ArrayList<ArrayList<Object>> data) {
		return arrayToXLS(data, null);
	}

	/**
	 * excel 2003 行数最大为65536
	 * 
	 * @param data
	 * @return
	 */
	public byte[] arrayToXLS(ArrayList<ArrayList<Object>> data, String sheetName) {
		try {
			ByteArrayOutputStream file = new ByteArrayOutputStream();
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = null;
			if (null != sheetName) {
				sheet = workbook.createSheet(sheetName);
			} else {
				sheet = workbook.createSheet();
			}
			int i = 0;
			for (ArrayList<Object> rowdata : data) {
				HSSFRow row = sheet.createRow(i++);
				int j = 0;
				for (Object column : rowdata) {
					HSSFCell cell = row.createCell(j++);
					if (null == column) {
						cell.setCellValue("");
					} else {
						cell.setCellValue(column.toString());
					}
				}
			}
			workbook.write(file);
			file.close();
			return file.toByteArray();
		} catch (Exception e) {
			Logger.error("arrayToXLS is error", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * excel 2007以上
	 * 
	 * @param data
	 * @return
	 */
	public byte[] arrayToXLSX(ArrayList<ArrayList<Object>> data) {
		return arrayToXLSX(data, null);
	}

	/**
	 * excel 2007以上
	 * 
	 * @param data
	 * @return
	 */
	public byte[] arrayToXLSX(ArrayList<ArrayList<Object>> data,
			String sheetName) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			ByteArrayOutputStream file = new ByteArrayOutputStream();
			XSSFSheet sheet = null;
			if (null != sheetName) {
				sheet = workbook.createSheet(sheetName);
			} else {
				sheet = workbook.createSheet();
			}
			int i = 0;
			for (ArrayList<Object> rowdata : data) {
				XSSFRow row = sheet.createRow(i++);
				int j = 0;
				for (Object column : rowdata) {
					XSSFCell cell = row.createCell(j++);
					if (null == column) {
						cell.setCellValue("");
					} else {
						cell.setCellValue(column.toString());
					}
				}
			}
			workbook.write(file);
			file.close();
			return file.toByteArray();
		} catch (Exception e) {
			Logger.error("arrayToXLSX is error", e);
			throw new RuntimeException(e);
		}
	}

	public static boolean isExcel2003(String fileName) {
		if (null == fileName) {
			return false;
		}
		return fileName.matches("^.+\\.(?i)(xls)$");
	}

	public static boolean isExcel2007(String fileName) {
		if (null == fileName) {
			return false;
		}
		return fileName.matches("^.+\\.(?i)(xlsx)$");
	}

	public List<List<String>> read(File file, boolean isExcel2003) {
		List<List<String>> dataList = null;
		/** 根据版本选择创建Workbook的方式 */
		try {
			FileInputStream fis = new FileInputStream(file);
			Workbook wb = null;
			if (isExcel2003) {
				wb = new HSSFWorkbook(fis);
			} else {
				wb = new XSSFWorkbook(fis);
			}
			dataList = read(wb);
		} catch (IOException e) {
			Logger.error("read xls error: ", e);
			return null;
		}
		return dataList;

	}

	private List<List<String>> read(Workbook wb) {
		List<List<String>> dataList = new ArrayList<List<String>>();
		/** 得到第一个shell */
		Sheet sheet = wb.getSheetAt(0);
		/** 得到Excel的行数 */
		int totalRows = sheet.getPhysicalNumberOfRows();
		/** 得到Excel的列数 */
		int totalCells = 0;
		if (totalRows >= 1 && sheet.getRow(0) != null) {
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}
		/** 循环Excel的行 */
		for (int r = 0; r < totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;
			}
			List<String> rowList = new ArrayList<String>();
			/** 循环Excel的列 */
			for (int c = 0; c < totalCells; c++) {
				Cell cell = row.getCell(c);
				String cellValue = "";
				if (null != cell) {
					// 以下是判断数据的类型
					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_NUMERIC: // 数字
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							cellValue = getSdf().format(
									HSSFDateUtil.getJavaDate(
											cell.getNumericCellValue())
											.getTime());
						} else {
							cellValue = cell.getNumericCellValue() + "";
						}
						break;
					case HSSFCell.CELL_TYPE_STRING: // 字符串
						cellValue = cell.getStringCellValue();
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
						cellValue = cell.getBooleanCellValue() + "";
						break;
					case HSSFCell.CELL_TYPE_FORMULA: // 公式
						cellValue = cell.getCellFormula() + "";
						break;
					case HSSFCell.CELL_TYPE_BLANK: // 空值
						cellValue = "";
						break;
					case HSSFCell.CELL_TYPE_ERROR: // 故障
						cellValue = "Illegal Character";
						break;
					default:
						cellValue = "Unknown Type";
						break;
					}
				}
				rowList.add(cellValue);
			}
			/** 保存第r行的第c列 */
			dataList.add(rowList);
		}
		return dataList;
	}

	public SimpleDateFormat getSdf() {
		if (sdf == null) {
			sdf = DateFormatUtils.getSimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

}
