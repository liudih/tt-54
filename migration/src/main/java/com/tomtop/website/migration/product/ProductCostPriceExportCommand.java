package com.tomtop.website.migration.product;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.migration.category.StringUtils;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;
import com.website.dto.product.Product;

public class ProductCostPriceExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	StringUtils stringUtils;

	@Override
	public void config(MyBatisService myBatisService) {
	}

	@Override
	public String commandName() {
		return "costprice-export";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("D", true,
				"Directory that records being extracted to.  Default: current directory");
		options.addOption("P", true,
				"Directory that product costprice  records Source.");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		String sourcepath = args.hasOption("P") ? args.getOptionValue("P")
				: null;
		this.getPrice(path, sourcepath);
	}

	public void getPrice(String spath, String path) {
		ObjectMapper om = new ObjectMapper();
		JsonNode jnode;
		try {
			System.out.println(path);
			jnode = om.readValue(new File(path), JsonNode.class);

			Iterator<JsonNode> it = jnode.iterator();
			DecimalFormat df = new DecimalFormat("0.00");
			while (it.hasNext()) {
				JsonNode tnode = it.next();
				ObjectNode onode = om.createObjectNode();
				String sku = tnode.get("sku").asText();
				sku = stringUtils.generationKey(sku, "");
				onode.put("sku", tnode.get("sku").asText().replace("\t", ""));
				double price = tnode.get("price").asDouble();
				price = Double.parseDouble(df.format(price / 6.19));
				onode.put("costPrice", price);
				onode.put("websiteId", 1);
				om.writeValue(new File(spath, sku + ".json"), onode);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
