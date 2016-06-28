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

public class RelationExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	CategoryProductRelation categoryProductRelation;

	@Inject
	ProductCategoryUpdater productCategoryUpdater;

	@Override
	public void config(MyBatisService myBatisService) {
	}

	@Override
	public String commandName() {
		return "relation";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("T", true, "relation excel filepath.");
		options.addOption("S", true, "category source path.");
		options.addOption("P", true, "product filepath");
		options.addOption(
				"D",
				true,
				"Directory that category records being extracted to.  Default: current directory");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		if (args.hasOption("T") == false) {
			System.out.println("----lack of relation file --- (T) ");
			return;
		}
		if (args.hasOption("S") == false) {
			System.out.println("----lack of Category file --- (S) ");
			return;
		}
		String relationpath = args.getOptionValue("T");
		String categoryPath = args.getOptionValue("S");
		String productPath = args.getOptionValue("P", "");
		try {
			// File file = new File(path, "skucategory.json");
			// String caPath = file.getPath();
			// if (file.exists() == false) {
			String caPath = categoryProductRelation.getProductCategory(path,
					relationpath, categoryPath);
			// }
			if (productPath.length() == 0) {
				System.out.println("product file path invalid skip ");
				return;
			}
			productCategoryUpdater.update(productPath, caPath, path);
			/*
			 * ObjectMapper om = new ObjectMapper(); File output; String
			 * filename = "categorys.json"; if (path != null) { output = new
			 * File(path, filename); } else { output = new File(filename); }
			 * om.writeValue(output, list);
			 */
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
