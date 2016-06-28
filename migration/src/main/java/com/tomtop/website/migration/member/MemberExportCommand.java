package com.tomtop.website.migration.member;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class MemberExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	MemberMapper mapper;
	
	@Inject
	MemberPoints memberPoints;

	@Override
	public String commandName() {
		return "member-export";
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
		while (true) {
			System.out.println("===== Working Page: " + page + " =====");
			List<CustomerEntity> customer = mapper.getPagedCustomerEntity(
					psize, page * psize);

			if (customer.size() == 0) {
				break;
			}
			page++;
			List<Integer> entityIds = Lists.transform(customer,
					c -> c.getEntity_id());

			// attributes
			List<CustomerAttribute> attrs = mapper
					.getCustomerAttributes(entityIds);
			Multimap<Integer, CustomerAttribute> attrsMap = Multimaps.index(
					attrs, ca -> ca.getEntity_id());

			// addresses
			List<CustomerAttribute> addresses = mapper
					.getCustomerAddressAttributes(entityIds);
			Multimap<Integer, CustomerAttribute> addressMap = Multimaps.index(
					addresses, ca -> ca.getParent_id());

			ObjectMapper om = new ObjectMapper();
			customer.parallelStream()
					.forEach(e -> {

						int entityId = e.getEntity_id();

						System.out.println("Email: " + e.getEmail());

						// attributes
							Map<String, CustomerAttribute> attrMap = Maps
									.uniqueIndex(attrsMap.get(entityId),
											a -> a.getAttribute_code());
							Map<String, String> attrMapValue = Maps
									.transformValues(attrMap, a -> a.getValue());
							ObjectNode on = JsonNodeFactory.instance
									.objectNode();
							Iterator<Entry<String, String>> j = attrMapValue
									.entrySet().iterator();
							while (j.hasNext()) {
								Entry<String, String> en = j.next();
								on.put(en.getKey(), en.getValue());
							}

							// addresses
							Multimap<Integer, CustomerAttribute> customerAddresses = Multimaps
									.index(addressMap.get(entityId),
											ca -> ca.getEntity_id());
							final ArrayNode array = on.withArray("addresses");

							Map<Integer, ObjectNode> addressNodes = Maps.transformValues(
									customerAddresses.asMap(),
									ca -> {
										ObjectNode o = array.addObject();
										Map<String, CustomerAttribute> oca = Maps
												.uniqueIndex(ca, cai -> cai
														.getAttribute_code());
										Map<String, String> oa = Maps
												.transformValues(oca,
														cai -> cai.getValue());
										Iterator<Entry<String, String>> kvi = oa
												.entrySet().iterator();
										while (kvi.hasNext()) {
											Entry<String, String> a = kvi
													.next();
											o.put(a.getKey(), a.getValue());
										}
										return o;
									});

							for (Iterator<Entry<Integer, ObjectNode>> k = addressNodes
									.entrySet().iterator(); k.hasNext();) {
								Entry<Integer, ObjectNode> o = k.next();
								o.getValue().put("entity_id", o.getKey());
							}

							on.put("email", e.getEmail());
							on.put("entity_id", e.getEntity_id());
							on.put("entity_type_id", e.getEntity_type_id());
							on.put("attribute_set_id", e.getAttribute_set_id());
							on.put("increment_id", e.getIncrement_id());
							on.put("group_id", e.getGroup_id());
							on.put("store_id", e.getStore_id());
							on.put("website_id", e.getWebsite_id());
							on.put("is_active", e.isIs_active());
							on.put("disable_auto_group_change",
									e.isDisable_auto_group_change());
							// on.put("created_at", e.getCreated_at());
							// on.put("updated_at", e.getUpdated_at());
							// om.writeValue(System.out, on);
							File output;
							String filename = e.getEntity_id() + ".json";
							if (path != null) {
								output = new File(path, filename);
							} else {
								output = new File(filename);
							}
							try {
								om.writeValue(output, on);
							} catch (Exception e1) {
								throw new RuntimeException("Error Processing: "
										+ e.getEmail(), e1);
							}
						});
			System.out.println("--------customer points");
			memberPoints.getPoints(path, entityIds);
			n += customer.size();
		}
		System.out.println("Processed Total " + n + " Records");
	}

	@Override
	public void config(MyBatisService myBatisService) {
		myBatisService.addMapperClass("magento", MemberMapper.class);
	}

}
