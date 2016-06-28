package com.tomtop.website.migration.comment;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.migration.member.CustomerEntity;
import com.tomtop.website.migration.product.ProductEntity;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;

public class CommentExport implements ICommand, IMyBatisConfig {

	@Inject
	CommentMapper mapper;

	@Override
	public String commandName() {
		return "comment-export";
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
			while (true) {
				System.out.println("===== Working Page: " + page + " =====");
				List<CommentEntity> comments = mapper.getAllComment(psize, page
						* psize);
				if (comments.size() == 0) {
					break;
				}
				page++;

				comments.parallelStream().forEach(
						comment -> {
							File output;
							String filename = comment.getReview_id() + ".json";
							if (null != path) {
								output = new File(path, filename);
							} else {
								output = new File(filename);
							}
							try {
								jsonMapper.writeValue(output, comment);
							} catch (Exception e1) {
								throw new RuntimeException("Error Processing: "
										+ comment.getReview_id(), e1);
							}
						});
				n += comments.size();
			}
			System.out.println("Processed Total " + n + " Records");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void config(MyBatisService myBatisService) {
		myBatisService.addMapperClass("magento", CommentMapper.class);
	}

}
