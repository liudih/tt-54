package com.tomtop.advert.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tomtop.advert.common.enums.ExceptionEnum;
import com.tomtop.framework.core.utils.Result;

/**
 *************************************************************** 
 * <p>
 * 
 * @DESCRIPTION : 异常封装接口供商品系统统一调用
 * @AUTHOR : 文龙 13715116671
 * @DATE :2015-11-7 上午11:15:33
 *       </p>
 **************************************************************** 
 */
public class ExceptionProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(ExceptionProcessor.class);

	/**
	 * 参数异常处理
	 * 
	 * @Title: getParameterExceptionResult
	 * @param
	 * @return Result 返回类型
	 * @throws
	 */
	public static Result getParameterExceptionResult(ParameterException e) {
		Result result = new Result();
		result.setRet(Result.FAIL);
		result.setErrCode(ExceptionEnum.ERROE_CODE_PARAMETER);
		result.setErrMsg(e.getMessage());

		logger.error("ParameterException: errCode={}, errMsg={}", new Object[] {
				result.getErrCode(), result.getErrMsg() });
		return result;
	}

	/**
	 * 业务异常处理
	 * 
	 * @Title: getBusinessExceptionResult
	 * @param
	 * @return Result 返回类型
	 * @throws
	 */
	public static Result getBusinessExceptionResult(BusinessException e) {
		String code = e.getCode();
		String msg = e.getMessage();

		Result result = new Result();
		result.setRet(Result.FAIL);
		if (StringUtils.isNotBlank(code)) {
			result.setErrCode(code);
			if (StringUtils.isNotBlank(msg)) {
				result.setErrMsg(msg);
			} else {
				result.setErrMsg(ExceptionEnum.BusExcep.getList().get(code));
			}
		} else {
			result.setErrCode(ExceptionEnum.ERROE_CODE_BUSINESS);
			result.setErrMsg(msg);
		}

		logger.info("BusinessException: errCode={}, errMsg={}", new Object[] {
				result.getErrCode(), result.getErrMsg() });
		return result;
	}

	/**
	 * 系统异常处理
	 * 
	 * @Title: getExceptionResult
	 * @param
	 * @return Result 返回类型
	 * @throws
	 */
	public static Result getExceptionResult(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));

		Result result = new Result();
		result.setRet(Result.FAIL);
		result.setErrCode(ExceptionEnum.ERROE_CODE_SYSTEM);
		result.setErrMsg("system exception,please contact administrator!");

		logger.error("Exception: errCode={}, errMsg={}",
				new Object[] { result.getErrCode(), sw.toString() });
		return result;
	}

}
