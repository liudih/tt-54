package com.tomtop.website.migration.order;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class OrderExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	OrderMapper mapper;

	@Override
	public String commandName() {
		return "order-export";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("D", true,
				"Directory that member records being extracted to.  Default: current directory");
		options.addOption("S", true, "Page Size. Default 100");
		options.addOption("P", true, "Starting from Page. Default 0");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		int page = args.hasOption("P") ? Integer.parseInt(args
				.getOptionValue("P")) : 0;
		int psize = args.hasOption("S") ? Integer.parseInt(args
				.getOptionValue("S")) : 100;
		long n = 0;
		ObjectMapper jsonMapper = new ObjectMapper();
		while (true) {
			System.out.println("===== Working Page: " + page + " =====");
			List<OrderEntity> batch = mapper.getPagedOrderEntity(psize, page
					* psize);

			List<Integer> entityIds = Lists.transform(batch,
					o -> o.getEntity_id());

			if (batch.size() == 0) {
				break;
			}
			page++;

			// get items
			List<OrderItem> batchItems = mapper.getOrderItems(entityIds);
			Multimap<Integer, OrderItem> batchItemMap = Multimaps.index(
					batchItems, i -> i.getOrder_id());

			// get addresses
			List<OrderAddress> batchAddresses = mapper
					.getOrderAddresses(entityIds);
			Multimap<Integer, OrderAddress> batchAddressMap = Multimaps.index(
					batchAddresses, i -> i.getParent_id());

			// get payment
			List<OrderPayment> batchPayments = mapper
					.getOrderPayments(entityIds);
			Map<Integer, OrderPayment> batchPaymentMap = Maps.uniqueIndex(
					batchPayments, i -> i.getParent_id());

			// get status history
			List<OrderStatusHistory> batchStatuses = mapper
					.getOrderStatusHistories(entityIds);
			Multimap<Integer, OrderStatusHistory> batchStatusMap = Multimaps
					.index(batchStatuses, i -> i.getParent_id());

			Stream<ObjectNode> rootNodes = batch
					.parallelStream()
					.map(order -> {
						ObjectNode obj = jsonMapper.convertValue(order,
								ObjectNode.class);

						// -- items
						Collection<OrderItem> items = batchItemMap.get(order
								.getEntity_id());
						ArrayNode itemArray = obj.putArray("items");
						items.forEach(oi -> {
							itemArray.add(jsonMapper.convertValue(oi,
									ObjectNode.class));
						});

						// -- addresses
						Collection<OrderAddress> addresses = batchAddressMap
								.get(order.getEntity_id());
						ArrayNode addressArray = obj.putArray("addresses");
						addresses.forEach(oi -> {
							addressArray.add(jsonMapper.convertValue(oi,
									ObjectNode.class));
						});

						// -- payment
						OrderPayment payment = batchPaymentMap.get(order
								.getEntity_id());
						obj.set("payment", jsonMapper.convertValue(payment,
								ObjectNode.class));

						// -- status history
						Collection<OrderStatusHistory> statuses = batchStatusMap
								.get(order.getEntity_id());
						ArrayNode statusArray = obj.putArray("status_history");
						statuses.forEach(oi -> {
							statusArray.add(jsonMapper.convertValue(oi,
									ObjectNode.class));
						});
						return obj;
					});

			rootNodes.forEach(on -> {
				File output;
				String filename = on.get("entity_id") + ".json";
				if (path != null) {
					output = new File(path, filename);
				} else {
					output = new File(filename);
				}
				try {
					jsonMapper.writeValue(output, on);
				} catch (Exception e1) {
					throw new RuntimeException("Error Processing: "
							+ on.get("entity_id"), e1);
				}
			});

			n += batch.size();
		}
		System.out.println("Processed Total " + n + " Records");
	}

	@Override
	public void config(MyBatisService myBatisService) {
		myBatisService.addMapperClass("magento", OrderMapper.class);
	}

}
