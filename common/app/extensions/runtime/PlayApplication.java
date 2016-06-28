package extensions.runtime;

import play.Application;

public class PlayApplication implements IApplication {
	final Application application;

	public PlayApplication(Application application) {
		super();
		this.application = application;
	}

	@Override
	public IConfiguration getConfiguration() {
		return new PlayConfiguration(application.configuration());
	}

	@Override
	public boolean isDev() {
		return application.isDev();
	}

	@Override
	public boolean isProd() {
		return application.isProd();
	}

	@Override
	public boolean isTest() {
		return application.isTest();
	}

}
