package controllers.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.server.HessianSkeleton;
import com.google.common.collect.Maps;

import extensions.hessian.HessianServiceDefinition;

@Singleton
public class HessianController extends Controller {

	Map<String, HessianServiceDefinition> serviceMap;
	Map<String, HessianSkeleton> skeletonMap;
	SerializerFactory serFactory;

	@Inject
	public HessianController(Set<HessianServiceDefinition> serviceDefs) {
		this.serviceMap = Maps.uniqueIndex(serviceDefs, sd -> sd.getPath());
		this.serFactory = new SerializerFactory();
		this.skeletonMap = Maps.transformValues(
				serviceMap,
				sd -> new HessianSkeleton(sd.getServiceObject(), sd
						.getServiceClass()));
	}

	@BodyParser.Of(value  = BodyParser.Raw.class,maxLength = 10 * 1024 * 1024)
	public Result serve(String path) throws Exception {
		if (!skeletonMap.containsKey(path)) {
			return notFound("Service not found: " + path);
		}

		HessianSkeleton skel = skeletonMap.get(path);

		InputStream is = new ByteArrayInputStream(request().body().asRaw()
				.asBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		invoke(is, os, skel, serFactory);

		return ok(os.toByteArray()).as("application/x-hessian");
	}

	protected void invoke(InputStream is, OutputStream os,
			HessianSkeleton skel, SerializerFactory serializerFactory)
			throws Exception {
		skel.invoke(is, os, serializerFactory);
	}
}
