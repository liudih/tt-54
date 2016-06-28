package com.tomtop.website.migration.relation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class CategoryRelationExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	ERPCategory eRPCategory;

	@Override
	public void config(MyBatisService myBatisService) {
	}

	@Override
	public String commandName() {
		return "category-relation";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("CP", true,
				"erpcategory relation category excel file");
		options.addOption("CE", true, "erp category json file");
		options.addOption("CS", true, "sku cateogy json file");
		options.addOption(
				"D",
				true,
				"Directory that category records being extracted to.  Default: current directory");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		try {
			if (args.hasOption("CP") && args.hasOption("CE")
					&& args.hasOption("CS")) {
				String categoryrelationpath = args.getOptionValue("CP");
				String erpCategoryPath = args.getOptionValue("CE");
				String skuCategoryPaht = args.getOptionValue("CS");
				eRPCategory.getSkuRelation(erpCategoryPath,
						categoryrelationpath, skuCategoryPaht, path);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
