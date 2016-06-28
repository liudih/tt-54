package com.tomtop.advert.common.exception;

/** 
 *************************************************************** 
 * <p>
 * @DESCRIPTION :   参数异常供商品系统统一调用 
 * @AUTHOR : 文龙       13715116671
 * @DATE :2015-11-7 上午11:15:33
 * </p>
 **************************************************************** 
 */
public class ParameterException extends RuntimeException{

	private static final long serialVersionUID = -5016362827189051832L;

	public ParameterException(String msg){
        super(msg);
    }

    public ParameterException(String msg, Throwable t){
        super(msg, t);
        setStackTrace(t.getStackTrace());
    }

    
}