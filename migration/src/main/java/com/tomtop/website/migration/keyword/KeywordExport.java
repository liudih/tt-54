package com.tomtop.website.migration.keyword;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class KeywordExport implements ICommand, IMyBatisConfig {
	
	@Inject
	KeywordMapper mapper;
	
	@Override
	public String commandName() {
		return "keyword-export";
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
		try {			
			String path = args.hasOption("D") ? args.getOptionValue("D") : null;
			int page = args.hasOption("P") ? Integer.parseInt(args
					.getOptionValue("P")) : 0;
			int psize = args.hasOption("S") ? Integer.parseInt(args
					.getOptionValue("S")) : 100;
			long n = 0;

			ObjectMapper jsonMapper = new ObjectMapper();

			while(true) {
				System.out.println("===== Working Page: " + page + " =====");
				List<KeywordEntity> keywords = mapper.getKeywords(psize, page * psize);	
				if (CollectionUtils.isEmpty(keywords)) {
					break;
				}
				page++;
												
				Stream<ObjectNode> rootNodes = keywords.parallelStream().map(
						keyword -> {
							ObjectNode obj = jsonMapper.convertValue(keyword, ObjectNode.class);		
							return obj;
						});
				rootNodes.forEach(on -> {
					File output;
					String filename = on.get("query_id") + ".json";
					if (null != path) {
						output = new File(path, filename);
					} else {
						output = new File(filename);
					}
					try {
						jsonMapper.writeValue(output, on);
					} catch (Exception e1) {
						throw new RuntimeException("Error Processing: "
								+ on.get("query_id"), e1);
					}
				});		
				n += keywords.size();
			}
			System.out.println("Processed Total " + n + " Records");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void config(MyBatisService myBatisService) {
		myBatisService.addMapperClass("magento", KeywordMapper.class);
	}

}
