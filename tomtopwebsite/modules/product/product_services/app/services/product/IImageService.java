package services.product;

import valueobjects.product.ImageBo;
import dto.image.Img;

/**
 * 图片服务接口
 * 
 * @author lijun
 *
 */
public interface IImageService {

	/**
	 * 获取优先级
	 * 
	 * @return
	 */
	public int getPriority();

	/**
	 * 获取dir路径的图片数据
	 * 
	 * @param dir
	 * @return
	 */
	public ImageBo getImage(String dir);

	/**
	 * 保存图片
	 * 
	 * @param img
	 */
	public void save(Img img);
	
	/**
	 * 保存图片
	 * 
	 * @param img
	 */
	public void save(Img img,Integer width, Integer height);

}
