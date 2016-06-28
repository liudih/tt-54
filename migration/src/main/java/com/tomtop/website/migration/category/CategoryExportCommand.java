package com.tomtop.website.migration.category;

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
import com.tomtop.website.migration.relation.ERPCategory;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class CategoryExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	CategoryConverter excelUnit;
	@Inject
	ERPCategory eRPCategory;

	@Override
	public void config(MyBatisService myBatisService) {
	}

	@Override
	public String commandName() {
		return "category-export";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption("T", true, "Category excel filepath.");
		options.addOption("TV", true, "Category visible excel filepath.");
		options.addOption("TA", true, "Category Attribute excel filepath.");
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
			System.out.println("----lack of Category file path--- (T) ");

			return;
		}
		String spath = args.getOptionValue("T");
		String vpath = args.getOptionValue("TV", null);
		String atpath = args.getOptionValue("TA", null);
		try {
			List<com.website.dto.category.Category> list = excelUnit
					.getCategorys(spath, path,atpath);
			ObjectMapper om = new ObjectMapper();
			File output;
			String filename = "categorys.json";
			if (path != null) {
				output = new File(path, filename);
			} else {
				output = new File(filename);
			}
			om.writeValue(output, list);
			excelUnit.convertToWebSiteCategory(list, path, vpath);

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
