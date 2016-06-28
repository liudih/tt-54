package services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import play.mvc.Http.Context;
import context.WebContext;

/**
 * 验证码
 * 
 * @author lijun
 *
 */
public interface ICaptchaService {
	// 验证码保存在缓存里面的key
	public static final String KAPTCHA_VARNAME = "kaptcha-cache";
	// 默认生成验证码的长度
	public static final int DEFAULT_KAPTCHA_LEN = 4;

	/**
	 * 验证用户输入的验证码是否正确
	 * 
	 * @param captcha
	 * @return
	 */
	public boolean verify(String captcha);

	/**
	 * 创建一个新的验证码 ,该方法专门为服务端用
	 * 
	 * @return ByteArrayOutputStream
	 * @throws IOException
	 */
	public ByteArrayOutputStream createCaptcha(Context ctx) throws IOException;

	/**
	 * 创建一个新的验证码, 一般是为远程调用服务
	 * 
	 * @param webCtx
	 * @return ByteArrayOutputStream
	 * @throws IOException
	 */
	public List<Byte> createCaptcha(WebContext webCtx) throws IOException;

	/**
	 * 验证验证码是否正确,该方法是为远程调用
	 * 
	 * @param captcha
	 * @param webCtx
	 * @return
	 */
	public boolean verify(String captcha, WebContext webCtx);
}
