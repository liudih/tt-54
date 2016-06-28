package com.tomtop.website.migration.filter;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class ProductSkuFilterCommand implements ICommand, IMyBatisConfig {

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("P1", true, " need fix files ");
		options.addOption("P2", true, " path filter exists files ");
		options.addOption(
				"D",
				true,
				"Directory that category records being extracted to.  Default: current directory");
		return options;
	}

	public void execute(CommandLine args) {
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		String sourcepath = args.hasOption("P1") ? args.getOptionValue("P1")
				: null;
		String filterpath = args.hasOption("P2") ? args.getOptionValue("P2")
				: null;
		try {
			ObjectMapper om = new ObjectMapper();
			JsonNode sourcenode = om.readValue(new File(sourcepath),
					JsonNode.class);
			List<String> filternode = Arrays.asList(om.readValue(new File(
					filterpath), String[].class));
			Iterator<JsonNode> snodeList = sourcenode.iterator();
			System.out.println("exist size" + filternode.size());
			Iterator<JsonNode> existList = Iterators.filter(snodeList,
					obj -> filternode.contains(obj.get("sku").asText()));
			while (existList.hasNext()) {
				JsonNode jn = existList.next();
				//System.out.println(jn.get("sku").asText());
				om.writeValue(new File(path, jn.get("sku").asText() + ".json"), jn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void config(MyBatisService myBatisService) {
	}

	@Override
	public String commandName() {
		return "product-filter";
	}

}
