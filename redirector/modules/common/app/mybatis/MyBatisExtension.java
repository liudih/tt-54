package mybatis;

import extensions.IExtensionPoint;

public interface MyBatisExtension extends IExtensionPoint {

	/**
	 * Should call <code>MyBatisService.addMapperClass()</code> method here.
	 * 
	 * @param service
	 * @see MyBatisService#addMapperClass(String, Class)
	 */
	void processConfiguration(MyBatisService service);

}
