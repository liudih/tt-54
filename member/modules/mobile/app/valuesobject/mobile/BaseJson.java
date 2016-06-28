package valuesobject.mobile;

import java.io.Serializable;

/**
 * @author HW
 * @date 2015年4月30日
 * @descr 基本的Json返回结果
 * @tags json base
 */
public class BaseJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 返回结果状态 **/
	private int re = 0;

	/** 状态提示消息 **/
	private String msg = "";

	public BaseJson() {

	}

	public BaseJson(int re, String msg) {
		super();
		this.re = re;
		this.msg = msg;
	}

	public int getRe() {
		return re;
	}

	public void setRe(int re) {
		this.re = re;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
