package services.base;

import java.util.List;

import javax.inject.Inject;

import services.IPlatformService;
import mapper.base.PlatformMapper;
import dto.Platform;

public class PlatformService implements IPlatformService {

	@Inject
	PlatformMapper mapper;
	
	/* (non-Javadoc)
	 * @see services.IPlatformService#getAllPlatform()
	 */
	@Override
	public List<Platform> getAllPlatform() {
		return mapper.getAllPlatform();
	}
}
