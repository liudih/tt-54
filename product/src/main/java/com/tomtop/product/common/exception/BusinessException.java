package com.tomtop.product.common.exception;

/** 
 *************************************************************** 
 * <p>
 * @DESCRIPTION :   业务异常供商品系统统一调用 
 * @AUTHOR : 文龙       13715116671
 * @DATE :2015-11-7 上午11:15:33
 * </p>
 **************************************************************** 
 */
public class BusinessException extends RuntimeException{

	private String code;
	
    public BusinessException(String msg){
        super(msg);
    }
    
    public BusinessException(String code, String msg){
        super(msg);
        this.code = code;
    }
    
    public BusinessException(String msg, Throwable t){
        super(msg, t);
        setStackTrace(t.getStackTrace());
    }

    private static final long serialVersionUID = -3023207770899967891L;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	} 
}