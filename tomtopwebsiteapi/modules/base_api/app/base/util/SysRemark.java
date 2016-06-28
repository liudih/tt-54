package base.util;

import org.apache.commons.lang3.StringUtils;

public class SysRemark {
	public static final String startFix = "【";
	public static final String endFix = "】";
	public static final String newLine = "<br/>";
	
	private static String _fix(String remark) {
		return startFix + remark + endFix;
	}
	
	/**
	 * exitsRemark 表里面已经存在的备注
	 * newRemark 新备注
	 */
	public static String append(String exitsRemark, String newRemark) {
		int rows = StringUtils.countMatches(exitsRemark, startFix);
		String newIndex = new Integer(rows + 1).toString() + ": ";
		if (StringUtils.isEmpty(exitsRemark)) {
			exitsRemark = "";
		} else {
			exitsRemark += newLine;
		}
		
		return exitsRemark + newIndex + _fix(newRemark);
	}
	
}
