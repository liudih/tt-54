package com.tomtop.website.migration.product;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class ProductGroupPriceExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	ProductMapper mapper;

	@Override
	public void config(MyBatisService myBatisService) {
		myBatisService.addMapperClass("magento", ProductMapper.class);
	}

	@Override
	public String commandName() {
		return "product-groupprice";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption(
				"D",
				true,
				"Directory that product records being extracted to.  Default: current directory");
		options.addOption("S", true,
				"page size product records being extracted to.  Default: 100  ");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		System.out.println("====begin group price====");
		// TODO Auto-generated method stub
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		int psize = args.hasOption("S") ? Integer.valueOf(args
				.getOptionValue("S")) : 100;
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = null;
		try {
			beginDate = time.parse("1900-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int page = 0;
		// int psize = 100;
		long line = 0;
		ObjectMapper om = new ObjectMapper();
		while (true) {
			List<ProductEntity> skulist = mapper.getProductSkus(false, psize,
					page * psize, beginDate);
			if (null == skulist || skulist.size() == 0) {
				break;
			}
			page++;
			line += skulist.size();
			List<ProductGroupPrice> list = mapper.getGroupPrices(Lists
					.transform(skulist, obj -> obj.getSku()));
			for (ProductGroupPrice obj : list) {
				try {
					om.writeValue(new File(path, obj.getSku() + ".json"), obj);
				} catch (JsonGenerationException e) {
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
			System.out.println("export product count : " + line);
		}
	}

}
