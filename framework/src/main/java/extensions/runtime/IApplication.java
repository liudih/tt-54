package extensions.runtime;

public interface IApplication {

	IConfiguration getConfiguration();

	boolean isDev();

	boolean isProd();

	boolean isTest();
}
