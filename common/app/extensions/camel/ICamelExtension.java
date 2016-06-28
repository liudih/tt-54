package extensions.camel;

import java.util.List;

import org.apache.camel.builder.RouteBuilder;

import extensions.IExtensionPoint;

public interface ICamelExtension extends IExtensionPoint {

	List<RouteBuilder> getRouteBuilders();

}
