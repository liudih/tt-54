package com.tomtop.website.migration.product;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.tomtop.website.migration.ICommand;
import com.tomtop.website.mybatis.IMyBatisConfig;
import com.tomtop.website.mybatis.MyBatisService;
import com.website.dto.product.Product;

public class ProductExportCommand implements ICommand, IMyBatisConfig {

	@Inject
	ProductMapper mapper;
	@Inject
	ProductService productService;

	@Override
	public void config(MyBatisService myBatisService) {
		myBatisService.addMapperClass("magento", ProductMapper.class);
		myBatisService.addMapperClass("magento", CategoryEntityMapper.class);
	}

	@Override
	public String commandName() {
		return "product-export";
	}

	@Override
	public Options extraOptions() {
		Options options = new Options();
		options.addOption(
				"D",
				true,
				"Directory that product records being extracted to.  Default: current directory");
		options.addOption(
				"SP",
				true,
				"Directory that product Selling Point records Source.  Default: current directory");
		options.addOption("T", true,
				" product that after this time records will be get.  Default: 1900-01-01");
		options.addOption("S", true,
				"page size product records being extracted to.  Default: 100  ");
		options.addOption("M", false,
				" product type being extracted to.  Default: single  ");
		return options;
	}

	@Override
	public void execute(CommandLine args) {
		String path = args.hasOption("D") ? args.getOptionValue("D") : null;
		String sellpointpath = args.hasOption("SP") ? args.getOptionValue("SP")
				: null;

		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = null;
		try {
			beginDate = time.parse("1900-01-01");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (args.hasOption("T")) {
			try {
				String beginDateStr = args.getOptionValue("T");
				beginDate = time.parse(beginDateStr);
			} catch (Exception ex) {
				System.out
						.println(" input time format invalid! format must be: yyyy-MM-dd");
				return;
			}
		}
		System.out.println("begintime->" + time.format(beginDate));
		Boolean mutil = args.hasOption("M") ? true : false;
		int psize = args.hasOption("S") ? Integer.valueOf(args
				.getOptionValue("S")) : 100;

		int page = 0;
		// int psize = 100;
		long line = 0;
		ObjectMapper om = new ObjectMapper();
		while (true) {
			List<ProductEntity> skulist = mapper.getProductSkus(mutil, psize,
					page * psize, beginDate);
			if (null == skulist || skulist.size() == 0) {
				break;
			}
			page++;

			List<String> skus = this.getExtractSkus(skulist, false, path);
			if (null == skus || skus.size() == 0) {
				continue;
			}
			if (mutil) {
				productService.getMutiProduct(skus, path);
				line += skus.size();
				System.out.println("export product count : " + line);
				continue;
			}
			List<Product> productList = productService.getProduct(path, skus);
			line += productList.size();

			productList.forEach(pentity -> {
				String sku = pentity.getSku();
				System.out.println("sku: " + sku);
				//if (sku.matches("[a-zA-Z0-9-]+")) {
					File output;
					String filename = sku + ".json";
					if (path != null) {
						output = new File(path, filename);
					} else {
						output = new File(filename);
					}

					pentity = productService.setSellPoint(pentity,
							sellpointpath);
					try {
						om.writeValue(output, pentity);
					} catch (Exception e1) {
						throw new RuntimeException("Error Processing: " + sku,
								e1);
					}
				//}
			});
			System.out.println("export product count : " + line);
		}
	}

	private List<String> getExtractSkus(List<ProductEntity> skulist,
			Boolean update, String path) {
		List<String> extractskus = null;
		if (update) {
			Collection<ProductEntity> skus = Collections2.filter(
					skulist,
					obj -> {
						File output;
						String filename = obj.getSku() + ".json";
						if (path != null) {
							output = new File(path, filename);
						} else {
							output = new File(filename);
						}
						if (output.exists()) {
							Calendar cal = Calendar.getInstance();
							cal.setTimeInMillis(output.lastModified());
							cal.add(Calendar.HOUR, -16);
							if (obj.getUpdatedDate().before(cal.getTime())) {
								System.out.println("not be to update sku: "
										+ obj.getSku());
								return false;
							}
						}
						return true;
					});
			if (skus != null && skus.size() > 0) {
				extractskus = Lists.transform(Lists.newArrayList(skus),
						obj -> obj.getSku());
			}
		} else {
			extractskus = Lists.transform(skulist, obj -> obj.getSku());
		}
		return extractskus;
	}
}
