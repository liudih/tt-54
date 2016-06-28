package com.tomtop.website.translation;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.google.common.collect.Lists;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class TranslatorCommand implements ICommand, IMyBatisConfig {

	@Inject
	ProductTranslator productTranslator;

	@Inject
	CategoryTranslator categoryTranslator;

	@Override
	public void config(MyBatisService myBatisService) {
		// TODO Auto-generated method stub

	}

	@Override
	public String commandName() {
		// TODO Auto-generated method stub
		return "trans";
	}

	@Override
	public Options extraOptions() {
		// TODO Auto-generated method stub
		Options options = new Options();
		options.addOption("L", true, "translation to language code ; cn,de ");
		options.addOption("S", true, "need translation json path");
		options.addOption("D", true, "translationed save path");
		options.addOption("SP", true,
				"source sell points path;translation product will be using");
		options.addOption("T", true,
				"tran type  [category,product] default product");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		// TODO Auto-generated method stub
		String sourcePath = args.getOptionValue("S", "");
		String targetPath = args.getOptionValue("D", "");
		String targetlanguage = args.getOptionValue("L", "en");
		String sellpath = args.getOptionValue("SP", "");
		String typestr = args.getOptionValue("T", "product");
		List<String> llist = Lists.newArrayList(targetlanguage.split(","));
		try {
			System.out.println("----------begin trans");
			if ("product".equals(typestr)) {
				productTranslator.Translation(sourcePath, targetPath, llist,
						sellpath);
			} else if ("category".equals(typestr)) {
				categoryTranslator.Translation(sourcePath, targetPath, llist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
