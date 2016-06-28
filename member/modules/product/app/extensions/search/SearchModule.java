package extensions.search;

import handlers.search.ProductIndexingHandler;
import handlers.search.SaleIndexHandler;
import handlers.search.SearchIndexingHandler;
import handlers.search.SearchKeywordSuggestHandler;
import handlers.search.SearchLogHandler;

import java.util.Arrays;
import java.util.List;

import mapper.search.KeywordSearchLogMapper;
import mapper.search.KeywordSuggestMapper;
import mybatis.MyBatisExtension;
import mybatis.MyBatisService;

import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import play.Logger;
import services.search.IAsyncSearchService;
import services.search.ISearchService;
import services.search.SearchService;
import valueobjects.search.ISearchQueryParser;
import valueobjects.search.ISearchUIProvider;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import extensions.IModule;
import extensions.ModuleSupport;
import extensions.event.IEventExtension;
import extensions.hessian.HessianRegistrar;
import extensions.hessian.HessianServiceExtension;
import extensions.runtime.IApplication;
import extensions.runtime.IConfiguration;

public class SearchModule extends ModuleSupport implements MyBatisExtension,
		IEventExtension, HessianServiceExtension {

	Node node;
	Client client;

	@Override
	public Module getModule(IApplication application) {
		IConfiguration esconfig = application.getConfiguration().getConfig(
				"elasticsearch");
		if (esconfig == null) {
			Logger.info("Starting local ElasticSearch Node");
			node = NodeBuilder.nodeBuilder().local(true).build();
			node.start();
			client = node.client();
		} else {
			String clusterName = esconfig.getString("cluster_name");
			List<String> addresses = null;
			try {
				addresses = esconfig.getStringList("server_address");
			} catch (Exception e) {
				if (esconfig.getString("server_address") != null) {
					addresses = Arrays.asList(esconfig.getString(
							"server_address").split(","));
				}
			}
			Logger.info("ElasticSearch Server Addresses: {}", addresses);

			TransportClient tclient = null;
			node = null;
			if (clusterName != null) {
				Settings settings = ImmutableSettings.settingsBuilder()
						.put("cluster.name", clusterName)
						.put("client.transport.sniff", true).build();
				tclient = new TransportClient(settings);
				Logger.info("Connecting to Elasticsearch Node Cluster: {}",
						clusterName);
			} else {
				tclient = new TransportClient();
			}

			for (String address : addresses) {
				String[] part = address.split(":");
				if (part.length == 2) {
					tclient.addTransportAddress(new InetSocketTransportAddress(
							part[0], Integer.parseInt(part[1])));
				} else if (part.length == 1) {
					tclient.addTransportAddress(new InetSocketTransportAddress(
							part[0], 9300));
				}
			}
			client = tclient;
		}

		return new AbstractModule() {
			@Override
			protected void configure() {
				bind(Client.class).toInstance(client);
				bind(ISearchService.class).to(SearchService.class);
				bind(IAsyncSearchService.class).to(SearchService.class);
			}
		};
	}

	@Override
	public void configBinderExtras(List<? extends IModule> modules,
			Binder binder) {
		Multibinder<ISearchIndexProvider> indexProviders = Multibinder
				.newSetBinder(binder, ISearchIndexProvider.class);
		Multibinder<ISearchQueryParser> parsers = Multibinder.newSetBinder(
				binder, ISearchQueryParser.class);
		Multibinder<ISearchUIProvider> ui = Multibinder.newSetBinder(binder,
				ISearchUIProvider.class);
		for (ISearchExtension se : filterModules(modules,
				ISearchExtension.class)) {
			se.registerSearchBehaviours(indexProviders, parsers, ui);
		}
	}

	@Override
	public void onStart(IApplication app, Injector injector) {
		ClusterStatsResponse stat = client.admin().cluster()
				.prepareClusterStats().execute().actionGet();
		Logger.info("ElasticSearch Cluster Information:");
		Logger.info("   Name: {}", stat.getClusterNameAsString());
		Logger.info(" Status: {}", stat.getStatus());
	}

	@Override
	public void onStop(IApplication app, Injector injector) {
		if (client != null) {
			client.close();
			client = null;
		}
		if (node != null) {
			node.close();
			node = null;
		}
	}

	@Override
	public void registerListener(EventBus eventBus, Injector injector) {
		eventBus.register(injector.getInstance(SearchIndexingHandler.class));
		eventBus.register(injector.getInstance(SearchLogHandler.class));
		eventBus.register(injector
				.getInstance(SearchKeywordSuggestHandler.class));
		eventBus.register(injector.getInstance(SaleIndexHandler.class));
		eventBus.register(injector.getInstance(ProductIndexingHandler.class));
	}

	@Override
	public void processConfiguration(MyBatisService service) {
		service.addMapperClass("search", KeywordSuggestMapper.class);
		service.addMapperClass("search", KeywordSearchLogMapper.class);
	}

	@Override
	public void registerRemoteService(HessianRegistrar reg) {
		reg.publishService("search", ISearchService.class, SearchService.class);
	}

}
