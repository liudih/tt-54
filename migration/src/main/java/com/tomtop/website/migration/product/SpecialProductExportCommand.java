package com.tomtop.website.migration.product;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.migration.category.StringUtils;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;
import com.website.dto.product.AttributeItem;
import com.website.dto.product.ImageItem;
import com.website.dto.product.TranslateItem;

public class SpecialProductExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	StringUtils stringUtils;
	@Inject
	SellingPointService sellingPointService;

	@Override
	public void config(MyBatisService myBatisService) {

	}

	@Override
	public String commandName() {
		return "product-special";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("D", true,
				"Directory that records being extracted to.  Default: current directory");
		options.addOption("P", true,
				"Directory that erp special product Source.");
		options.addOption("P1", true,
				"Directory that tomtop Active product Source.");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		String specialpath = args.hasOption("P") ? args.getOptionValue("P")
				: null;
/*		String ttActiveSkupath = args.hasOption("P1") ? args
				.getOptionValue("P1") : null;*/
		ObjectMapper om = new ObjectMapper();
		try {
/*			List<String> activeskus = Arrays.asList(om.readValue(new File(
					ttActiveSkupath), String[].class));*/

			JsonNode specialNode = om.readValue(new File(specialpath),
					JsonNode.class);

			Iterator<JsonNode> specialNodes = specialNode.iterator();
			while (specialNodes.hasNext()) {
				JsonNode obj = specialNodes.next();
/*				if (activeskus.contains(obj.get("sku").asText())) {
					continue;
				}*/
				ObjectNode on = om.createObjectNode();
				on.put("websiteId", 1);
				on.put("sku", obj.get("sku").asText());
				ArrayNode an = on.putArray("labelTypes");
				an.add("special");
				om.writeValue(
						new File(path, obj.get("sku").asText() + ".json"), on);
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
